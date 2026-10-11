package net.example.teleportcrystals.client.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class SpacialLizardRenderState extends LivingEntityRenderState {
    public final AnimationState diveAnimationState = new AnimationState();
    public final AnimationState surfaceAnimationState = new AnimationState();
    public final AnimationState lickAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
}
