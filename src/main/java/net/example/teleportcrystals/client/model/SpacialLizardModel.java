package net.example.teleportcrystals.client.model;

// GENERATED from spacial_lizard.bbmodel by gen/bb2java.py - do not hand-edit.
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.example.teleportcrystals.client.animation.SpacialLizardAnimations;
import net.example.teleportcrystals.client.state.SpacialLizardRenderState;

public class SpacialLizardModel extends EntityModel<SpacialLizardRenderState> {
	private final KeyframeAnimation lick;
	private final KeyframeAnimation walk;
	private final KeyframeAnimation dive;
	private final KeyframeAnimation surface;

	public SpacialLizardModel(ModelPart root) {
		super(root);
		this.lick = SpacialLizardAnimations.LICK.bake(root);
		this.walk = SpacialLizardAnimations.WALK.bake(root);
		this.dive = SpacialLizardAnimations.DIVE.bake(root);
		this.surface = SpacialLizardAnimations.SURFACE.bake(root);
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition rootDef = mesh.getRoot();
		// The Blockbench model faces -X; Minecraft models face -Z, so everything hangs off a rotated container.
		// If the lizard walks backwards in-game, change FACING_YAW's sign (or add PI).
		PartDefinition container = rootDef.addOrReplaceChild("container", CubeListBuilder.create(),
			PartPose.offsetAndRotation(0F, 24F, 0F, 0F, -1.5708F, 0F));
		PartDefinition p0_spacial_lizard = container.addOrReplaceChild("spacial_lizard", CubeListBuilder.create(),
			PartPose.offsetAndRotation(-21F, -3F, 8F, 0F, 0F, 0F));
		PartDefinition p1_h_head = p0_spacial_lizard.addOrReplaceChild("h-head", CubeListBuilder.create()
			.texOffs(21, 6).addBox(-11F, -3F, -4.58579F, 6F, 5F, 9F),
			PartPose.offsetAndRotation(5F, 1F, -15F, 0F, 0F, 0F));
		PartDefinition p1_h_head_c0 = p1_h_head.addOrReplaceChild("h-head_cube0",
			CubeListBuilder.create().texOffs(11, 36).addBox(-7F, -2F, 1F, 6F, 3F, 6F),
			PartPose.offsetAndRotation(-5F, 0F, 0F, 0F, -0.7854F, 0F));
		PartDefinition p3_nek = p1_h_head.addOrReplaceChild("nek", CubeListBuilder.create()
			.texOffs(25, 11).addBox(-9.07107F, -2.005F, -2.58579F, 9F, 4F, 5F),
			PartPose.offsetAndRotation(2F, 0F, 0F, 0F, 0F, 0F));
		PartDefinition p4_tung = p1_h_head.addOrReplaceChild("tung", CubeListBuilder.create(),
			PartPose.offsetAndRotation(-13F, 0F, 0F, 0F, 0F, 0F));
		PartDefinition p4_tung_c0 = p4_tung.addOrReplaceChild("tung_cube0",
			CubeListBuilder.create().texOffs(8, 27).addBox(-1.29289F, 0F, -1.12132F, 1F, 0F, 1F),
			PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0.7854F, 0F));
		PartDefinition p6_body = p0_spacial_lizard.addOrReplaceChild("body", CubeListBuilder.create(),
			PartPose.offsetAndRotation(8F, -1F, -14F, 0F, 0F, 0F));
		PartDefinition p7_sholder1 = p6_body.addOrReplaceChild("sholder1", CubeListBuilder.create()
			.texOffs(1, 0).addBox(-2.62231F, -3F, -6.51151F, 7F, 6F, 11F),
			PartPose.offsetAndRotation(0F, 1F, 0F, 0F, 0F, 0F));
		PartDefinition p8_left1 = p6_body.addOrReplaceChild("left1", CubeListBuilder.create(),
			PartPose.offsetAndRotation(0F, 1F, -6F, 0F, -0.04363F, 0F));
		PartDefinition p8_left1_c0 = p8_left1.addOrReplaceChild("left1_cube0",
			CubeListBuilder.create().texOffs(44, 14).addBox(-0.66289F, -1.30401F, -2.40166F, 8F, 3F, 3F),
			PartPose.offsetAndRotation(0.34896F, -1F, -0.99239F, 0.78556F, 0.94138F, 0.59532F));
		PartDefinition p8_left1_c1 = p8_left1.addOrReplaceChild("left1_cube1",
			CubeListBuilder.create().texOffs(14, 52).addBox(-7F, -2F, -1F, 8F, 2F, 2F),
			PartPose.offsetAndRotation(1.34896F, 3F, -5.99239F, 0.43528F, 0.11782F, -0.34395F));
		PartDefinition p11_right1 = p6_body.addOrReplaceChild("right1", CubeListBuilder.create(),
			PartPose.offsetAndRotation(0F, 1F, 4F, 0F, -0.61087F, 0F));
		PartDefinition p11_right1_c0 = p11_right1.addOrReplaceChild("right1_cube0",
			CubeListBuilder.create().texOffs(36, 45).addBox(-1.31567F, -1.74577F, -1.33958F, 8F, 3F, 3F),
			PartPose.offsetAndRotation(0F, 0F, 0F, -0.3068F, -0.12296F, 0.35184F));
		PartDefinition p11_right1_c1 = p11_right1.addOrReplaceChild("right1_cube1",
			CubeListBuilder.create().texOffs(0, 50).addBox(-7F, -2F, -1F, 8F, 2F, 2F),
			PartPose.offsetAndRotation(5F, 3F, 1F, -0.79513F, 0.89947F, -0.46621F));
		PartDefinition p14_midle = p6_body.addOrReplaceChild("midle", CubeListBuilder.create()
			.texOffs(0, 0).addBox(-3.30237F, -2.005F, -3.28516F, 12F, 5F, 7F),
			PartPose.offsetAndRotation(4F, 1F, -1F, 0F, 0F, 0F));
		PartDefinition p15_sholder2 = p6_body.addOrReplaceChild("sholder2", CubeListBuilder.create()
			.texOffs(0, 14).addBox(-2.62231F, -2F, -5.51151F, 8F, 6F, 9F),
			PartPose.offsetAndRotation(11F, 0F, 0F, 0F, 0F, 0F));
		PartDefinition p16_right2 = p6_body.addOrReplaceChild("right2", CubeListBuilder.create(),
			PartPose.offsetAndRotation(12F, 1F, 3F, 0F, -0.43633F, 0F));
		PartDefinition p16_right2_c0 = p16_right2.addOrReplaceChild("right2_cube0",
			CubeListBuilder.create().texOffs(35, 15).addBox(-0.042F, -2.13185F, -2.31021F, 8F, 4F, 4F),
			PartPose.offsetAndRotation(-0.51631F, -1F, -0.32893F, -0.3068F, -0.12296F, 0.35184F));
		PartDefinition p16_right2_c1 = p16_right2.addOrReplaceChild("right2_cube1",
			CubeListBuilder.create().texOffs(43, 29).addBox(-7F, -3F, -1F, 8F, 3F, 3F),
			PartPose.offsetAndRotation(3.48369F, 2F, -0.32893F, -0.88199F, 0.95789F, -0.57459F));
		PartDefinition p19_left2 = p6_body.addOrReplaceChild("left2", CubeListBuilder.create(),
			PartPose.offsetAndRotation(12F, 1F, -6F, 0F, -0.2618F, 0F));
		PartDefinition p19_left2_c0 = p19_left2.addOrReplaceChild("left2_cube0",
			CubeListBuilder.create().texOffs(27, 42).addBox(-0.66289F, -2.30401F, -2.40166F, 8F, 4F, 4F),
			PartPose.offsetAndRotation(0.37894F, -1F, 0.68681F, 0.82919F, 0.89774F, 0.59532F));
		PartDefinition p19_left2_c1 = p19_left2.addOrReplaceChild("left2_cube1",
			CubeListBuilder.create().texOffs(44, 23).addBox(-7F, -3F, -1F, 8F, 3F, 3F),
			PartPose.offsetAndRotation(1.37894F, 3F, -4.31319F, 0.43528F, 0.11782F, -0.34395F));
		PartDefinition p22_Tail = p6_body.addOrReplaceChild("Tail", CubeListBuilder.create()
			.texOffs(0, 21).addBox(-2F, -2.005F, -3F, 10F, 4F, 6F)
			.texOffs(33, 21).addBox(5F, -1.01F, -2F, 10F, 3F, 4F)
			.texOffs(47, 41).addBox(11F, -0.015F, -1F, 10F, 2F, 2F),
			PartPose.offsetAndRotation(16F, 2F, -1F, 0F, 0F, 0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(SpacialLizardRenderState state) {
		super.setupAnim(state);
		if (state.diveAnimationState.isStarted()) {
			this.dive.apply(state.diveAnimationState, state.ageInTicks);
		}
		if (state.surfaceAnimationState.isStarted()) {
			this.surface.apply(state.surfaceAnimationState, state.ageInTicks);
		}
		if (state.walkAnimationState.isStarted()) {
			this.walk.apply(state.walkAnimationState, state.ageInTicks);
		}
		if (state.lickAnimationState.isStarted()) {
			this.lick.apply(state.lickAnimationState, state.ageInTicks);
		}
	}
}
