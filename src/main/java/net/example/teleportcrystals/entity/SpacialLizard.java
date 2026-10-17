package net.example.teleportcrystals.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

/**
 * The Spacial Lizard. Walks on the surface in short bursts, licks to "smell"
 * for players, and when it has a target far away it dives into end stone,
 * tunnels underneath to the target and surfaces next to them.
 *
 * States (synced to the client so animations can follow):
 *   SURFACE   - ordinary walking mob
 *   DIVING    - "dive" animation plays on the spot
 *   BURROWED  - noPhysics tunnelling through end stone, dive pose held
 *   SURFACING - rises to the ground, "surface" animation plays
 */
public class SpacialLizard extends Monster {
    public static final int STATE_SURFACE = 0;
    public static final int STATE_DIVING = 1;
    public static final int STATE_BURROWED = 2;
    public static final int STATE_SURFACING = 3;

    private static final EntityDataAccessor<Integer> STATE =
            SynchedEntityData.defineId(SpacialLizard.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LICK_COUNT =
            SynchedEntityData.defineId(SpacialLizard.class, EntityDataSerializers.INT);

    // Tuning knobs
    private static final int DIVE_TICKS = 13;            // dive animation is 0.625 s (12.5 ticks); keep in sync with the .bbmodel
    private static final double UNDERGROUND_DEPTH = 1.5; // how far below the surface it ends up after diving
    private static final int SURFACING_MAX_TICKS = 14;
    private static final double RISE_SPEED = 0.2;       // blocks per tick while surfacing
    private static final double BURROW_SPEED = 0.32;     // blocks per tick while tunnelling
    private static final double SMELL_RANGE = 24.0;
    private static final int DIVE_COOLDOWN = 0;             // ticks between dives; 0 = no limit in a fight
    private static final double MIN_DIVE_DISTANCE_SQ = 0.0; // only dive if the target is farther than this (squared); 0 = always
    private static final double APPROACH_DISTANCE = 0.9;    // surfaces when this close (horizontally) to the target
    private static final double EMERGE_REACH_SQ = 9.0;      // emerging hits a target within 3 blocks
    private static final int IDLE_LICK_MIN = 100;        // 5 seconds
    private static final int IDLE_LICK_RANGE = 61;       // up to 8 seconds
    private static final int FIGHT_LICK_MIN = 30;        // licks more often while it has a target
    private static final int FIGHT_LICK_RANGE = 21;

    public final AnimationState diveAnimationState = new AnimationState();
    public final AnimationState surfaceAnimationState = new AnimationState();
    public final AnimationState lickAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    private int stateTicks;
    private int diveCooldown = 0;
    private int lickTimer;
    private int outsideStoneTicks;
    private double surfaceY;
    private boolean emergeHit;

    public SpacialLizard(EntityType<? extends SpacialLizard> type, Level level) {
        super(type, level);
        this.lickTimer = IDLE_LICK_MIN + this.random.nextInt(IDLE_LICK_RANGE);
    }

    public static AttributeSupplier.Builder createLizardAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                // Guess at "as fast as a sprinting player" (~5.6 blocks/s) - tune after testing.
                .add(Attributes.MOVEMENT_SPEED, 0.28);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STATE, STATE_SURFACE);
        builder.define(LICK_COUNT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false) {
            @Override
            public boolean canUse() {
                return SpacialLizard.this.isOnSurface() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return SpacialLizard.this.isOnSurface() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(3, new BurstWanderGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public int getLizardState() {
        return this.entityData.get(STATE);
    }

    public boolean isOnSurface() {
        return getLizardState() == STATE_SURFACE;
    }

    private void setLizardState(int state) {
        this.entityData.set(STATE, state);
        this.stateTicks = 0;
    }

    private boolean isPhasing() {
        int s = getLizardState();
        return s == STATE_BURROWED || s == STATE_SURFACING;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        // Lizards live inside solid blocks; never take suffocation damage.
        if (source.is(DamageTypes.IN_WALL)) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // structure lizards stay put instead of despawning
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (data == STATE) {
            int s = this.entityData.get(STATE);
            this.diveAnimationState.animateWhen(s == STATE_DIVING, this.tickCount);
            this.surfaceAnimationState.animateWhen(s == STATE_SURFACING, this.tickCount);
        } else if (data == LICK_COUNT) {
            this.lickAnimationState.start(this.tickCount);
        }
    }

    @Override
    public void tick() {
        if (isPhasing()) {
            this.noPhysics = true; // like a Vex: pass through blocks while tunnelling
        }
        super.tick();
        this.noPhysics = false;

        if (this.level().isClientSide()) {
            double mx = this.getX() - this.xo;
            double mz = this.getZ() - this.zo;
            boolean moving = (isOnSurface() || getLizardState() == STATE_BURROWED) && (mx * mx + mz * mz) > 1.0E-5;
            this.walkAnimationState.animateWhen(moving, this.tickCount);
            return;
        }
        serverTick();
    }

    private void serverTick() {
        this.stateTicks++;
        LivingEntity target = this.getTarget();
        switch (getLizardState()) {
            case STATE_SURFACE -> {
                if (this.diveCooldown > 0) this.diveCooldown--;
                tickLick(target);
                if (target != null && target.isAlive() && this.diveCooldown <= 0 && this.onGround()
                        && this.distanceToSqr(target) > MIN_DIVE_DISTANCE_SQ
                        && this.level().getBlockState(this.blockPosition().below()).is(Blocks.END_STONE)
                        && this.level().getBlockState(this.blockPosition().below(2)).is(Blocks.END_STONE)) {
                    this.getNavigation().stop();
                    setLizardState(STATE_DIVING);
                }
            }
            case STATE_DIVING -> {
                // The dive animation moves the model up and then down into the ground while the
                // lizard itself stays put. When it finishes, the lizard is really underground.
                holdStill();
                if (this.stateTicks >= DIVE_TICKS) {
                    this.setPos(this.getX(), this.getY() - UNDERGROUND_DEPTH, this.getZ());
                    this.setNoGravity(true);
                    this.outsideStoneTicks = 0;
                    setLizardState(STATE_BURROWED);
                }
            }
            case STATE_BURROWED -> burrowedTick(target);
            case STATE_SURFACING -> surfacingTick();
            default -> { }
        }
    }

    private void holdStill() {
        this.getNavigation().stop();
        this.setZza(0.0F);
        this.setXxa(0.0F);
    }

    private void burrowedTick(LivingEntity target) {
        holdStill(); // no ground-style movement input while underground
        if (target == null || !target.isAlive() || this.stateTicks > 400) {
            beginSurfacing();
            return;
        }
        // Tunnel toward the target, staying about 1.5 blocks under their feet. The only way back
        // out is beginSurfacing() -> the surface animation.
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        if (horizontal < APPROACH_DISTANCE) {
            beginSurfacing();
            return;
        }
        boolean inStone = this.level().getBlockState(this.blockPosition()).is(Blocks.END_STONE)
                || this.level().getBlockState(this.blockPosition().above()).is(Blocks.END_STONE);
        this.outsideStoneTicks = inStone ? 0 : this.outsideStoneTicks + 1;
        if (this.outsideStoneTicks > 8) {
            beginSurfacing();
            return;
        }
        double desiredY = target.getY() - UNDERGROUND_DEPTH;
        double vy = Mth.clamp((desiredY - this.getY()) * 0.3, -0.25, 0.25);
        double step = Math.min(BURROW_SPEED, horizontal); // don't overshoot the target
        double len = Math.max(horizontal, 1.0E-4);
        this.setDeltaMovement(dx / len * step, vy, dz / len * step);
        float yaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        this.setYRot(yaw);
        this.yBodyRot = yaw;
        this.yHeadRot = yaw;
    }

    private void beginSurfacing() {
        // Find the first open block (with solid below it) above the current position.
        BlockPos start = this.blockPosition();
        double found = this.getY() + 1.0;
        for (int i = 0; i < 16; i++) {
            BlockPos p = start.above(i);
            if (this.level().getBlockState(p).isAir() && !this.level().getBlockState(p.below()).isAir()) {
                found = p.getY();
                break;
            }
        }
        this.surfaceY = found;
        this.emergeHit = false;
        setLizardState(STATE_SURFACING);
    }

    private void surfacingTick() {
        // Bursting out of the ground hurts whoever is standing there (once per emergence).
        if (!this.emergeHit) {
            this.emergeHit = true;
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive() && this.distanceToSqr(target) < EMERGE_REACH_SQ
                    && this.level() instanceof ServerLevel serverLevel) {
                target.hurtServer(serverLevel, this.damageSources().mobAttack(this),
                        (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                target.push(0.0, 0.4, 0.0);
            }
        }
        double dy = this.surfaceY - this.getY();
        if (dy > 0.05 && this.stateTicks < SURFACING_MAX_TICKS) {
            this.setDeltaMovement(0.0, Math.min(RISE_SPEED, dy), 0.0); // ~7-8 ticks to match the 0.417 s surface animation
        } else {
            this.setPos(this.getX(), Math.max(this.getY(), this.surfaceY), this.getZ());
            this.setDeltaMovement(Vec3.ZERO);
            this.setNoGravity(false);
            this.diveCooldown = DIVE_COOLDOWN;
            setLizardState(STATE_SURFACE);
        }
    }

    private void tickLick(LivingEntity target) {
        if (--this.lickTimer > 0) return;
        this.entityData.set(LICK_COUNT, this.entityData.get(LICK_COUNT) + 1);
        if (target != null) {
            this.lickTimer = FIGHT_LICK_MIN + this.random.nextInt(FIGHT_LICK_RANGE);
        } else {
            this.lickTimer = IDLE_LICK_MIN + this.random.nextInt(IDLE_LICK_RANGE);
            smellForPlayers();
        }
    }

    /** Warden-style smelling: a lick detects a nearby player even through walls. */
    private void smellForPlayers() {
        Player player = this.level().getNearestPlayer(this, SMELL_RANGE);
        if (player != null && !player.isCreative() && !player.isSpectator()) {
            this.setTarget(player);
        }
    }

    /** Idle movement: short bursts toward a random nearby spot, then a pause. */
    private static class BurstWanderGoal extends Goal {
        private final SpacialLizard lizard;
        private Vec3 wanted;
        private int pause = 20;
        private int burstTicks;

        BurstWanderGoal(SpacialLizard lizard) {
            this.lizard = lizard;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!lizard.isOnSurface() || lizard.getTarget() != null) return false;
            if (pause > 0) {
                pause--;
                return false;
            }
            this.wanted = DefaultRandomPos.getPos(lizard, 10, 4);
            return this.wanted != null;
        }

        @Override
        public boolean canContinueToUse() {
            return lizard.isOnSurface() && lizard.getTarget() == null
                    && burstTicks > 0 && !lizard.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.burstTicks = 15 + lizard.getRandom().nextInt(16);
            lizard.getNavigation().moveTo(wanted.x, wanted.y, wanted.z, 1.0);
        }

        @Override
        public void tick() {
            this.burstTicks--;
        }

        @Override
        public void stop() {
            lizard.getNavigation().stop();
            this.pause = 20 + lizard.getRandom().nextInt(50);
        }
    }
}
