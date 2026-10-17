package net.example.teleportcrystals.client.animation;

// GENERATED from spacial_lizard.bbmodel by gen/bb2java.py - do not hand-edit.
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class SpacialLizardAnimations {
	public static final AnimationDefinition LICK = AnimationDefinition.Builder.withLength(0.5F)
			.addAnimation("tung", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0F, KeyframeAnimations.posVec(0F, 0F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.04167F, KeyframeAnimations.posVec(-1F, 0F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.08333F, KeyframeAnimations.posVec(1F, 0F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.5F, KeyframeAnimations.posVec(1F, 0F, 0F), AnimationChannel.Interpolations.LINEAR)))
			.build();

	public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(0.25F)
			.looping()
			.addAnimation("h-head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.08333F, KeyframeAnimations.degreeVec(-0F, -10F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, 17.5F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.08333F, KeyframeAnimations.degreeVec(-0F, -15F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, 20F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0.08333F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, -60F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.08333F, KeyframeAnimations.degreeVec(-0F, -35F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, -5F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0.08333F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, 17.5F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.08333F, KeyframeAnimations.degreeVec(-0F, 27.5F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, -7.5F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.build();

	public static final AnimationDefinition DIVE = AnimationDefinition.Builder.withLength(0.625F)
			.addAnimation("spacial_lizard", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, 0F, 10F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.375F, KeyframeAnimations.degreeVec(-0F, 0F, -70F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.54167F, KeyframeAnimations.degreeVec(-0F, 0F, 2.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("spacial_lizard", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0F, KeyframeAnimations.posVec(0F, 0F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.posVec(0F, 26F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.54167F, KeyframeAnimations.posVec(0F, -27F, 0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("h-head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.04167F, KeyframeAnimations.degreeVec(7.5F, 0F, 15F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-22.3159F, -2.1539F, 5.232F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.29167F, KeyframeAnimations.degreeVec(-35.4787F, -12.5919F, -8.9527F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("h-head", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0F, KeyframeAnimations.posVec(0F, 0F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.04167F, KeyframeAnimations.posVec(0F, 2F, 0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, 0F, 17.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.54167F, KeyframeAnimations.degreeVec(-0F, 0F, 2.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("nek", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, 0F, -15F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.29167F, KeyframeAnimations.degreeVec(14.8774F, -1.936F, -7.7528F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("tung", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0F, KeyframeAnimations.posVec(0F, 0F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.posVec(1F, 0F, -1F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("tung", new AnimationChannel(AnimationChannel.Targets.SCALE,
				new Keyframe(0F, KeyframeAnimations.scaleVec(1F, 1F, 0.7F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("sholder1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-4.9811F, -0.4352F, -4.9811F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, 0F, 25F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.54167F, KeyframeAnimations.degreeVec(-0F, 0F, -7.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(-62.1501F, -3.486F, -1.2579F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(-34.867F, 29.0084F, -14.8152F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(66.0516F, -34.9536F, -48.6452F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.125F, KeyframeAnimations.degreeVec(33.7031F, 6.1391F, -39.4001F), AnimationChannel.Interpolations.LINEAR)))
			.build();

	public static final AnimationDefinition SURFACE = AnimationDefinition.Builder.withLength(0.41667F)
			.addAnimation("spacial_lizard", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, 0F, 22.5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, 0F, 5F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.33333F, KeyframeAnimations.degreeVec(-0F, 0F, 2.5F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("spacial_lizard", new AnimationChannel(AnimationChannel.Targets.POSITION,
				new Keyframe(0F, KeyframeAnimations.posVec(0F, -13F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.posVec(0F, 2F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.posVec(0F, 4F, 0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.33333F, KeyframeAnimations.posVec(0F, 2F, 0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("h-head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, 12.5F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, -15F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("Tail", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, 12.5F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, -20F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, -37.5F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, -7.5F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("right2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, -45F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, -42.5F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.addAnimation("left1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
				new Keyframe(0F, KeyframeAnimations.degreeVec(-0F, 0F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.16667F, KeyframeAnimations.degreeVec(-0F, -15F, -0F), AnimationChannel.Interpolations.LINEAR),
				new Keyframe(0.25F, KeyframeAnimations.degreeVec(-0F, 20F, -0F), AnimationChannel.Interpolations.LINEAR)))
			.build();
}
