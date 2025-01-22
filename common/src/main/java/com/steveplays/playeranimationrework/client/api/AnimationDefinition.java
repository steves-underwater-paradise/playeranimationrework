package com.steveplays.playeranimationrework.client.api;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.steveplays.playeranimationrework.client.api.AnimationDefinition.AnimationPriorityDefinition.AnimationTwoPartPriorityDefinition;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.util.Identifier;

public class AnimationDefinition {
	private static final int DEFAULT_PRIORITY = 1000;

	public static final AnimationDefinition DEFAULT = new AnimationDefinition(Identifier.of(MOD_ID, "none"), new AnimationTriggerDefinition("when", Either.left(Identifier.of(MOD_ID, "idle")), false),
			new AnimationInterpolationDefinition("INOUTSINE", 0.5f, 0.5f), new AnimationPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY, DEFAULT_PRIORITY,
					new AnimationTwoPartPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY), new AnimationTwoPartPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY)));
	public static final Codec<AnimationDefinition> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Identifier.CODEC.fieldOf("identifier").forGetter(AnimationDefinition::getIdentifier),
					AnimationTriggerDefinition.CODEC.fieldOf("trigger").forGetter(AnimationDefinition::getAnimationTriggerDefinition),
					AnimationInterpolationDefinition.CODEC.optionalFieldOf("interpolation", new AnimationInterpolationDefinition("INOUTSINE", 0.5f, 0.5f))
							.forGetter(AnimationDefinition::getAnimationInterpolationDefinition),
					AnimationPriorityDefinition.CODEC.optionalFieldOf("priority",
							new AnimationPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY, DEFAULT_PRIORITY, new AnimationTwoPartPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY),
									new AnimationTwoPartPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY)))
							.forGetter(AnimationDefinition::getAnimationPriorityDefinition))
			.apply(instance, AnimationDefinition::new));


	private final @NotNull Identifier identifier;
	private final @NotNull AnimationTriggerDefinition trigger;
	private final @NotNull AnimationInterpolationDefinition interpolation;
	private final @NotNull AnimationPriorityDefinition priority;

	AnimationDefinition(@NotNull Identifier identifier, @NotNull AnimationTriggerDefinition trigger, @NotNull AnimationInterpolationDefinition interpolation,
			@NotNull AnimationPriorityDefinition priority) {
		this.identifier = identifier;
		this.trigger = trigger;
		this.interpolation = interpolation;
		this.priority = priority;
	}

	/**
	 * @return The animation's identifier.
	 */
	public @NotNull Identifier getIdentifier() {
		return identifier;
	}

	/**
	 * @return The animation's trigger.
	 */
	public @NotNull AnimationTriggerDefinition getAnimationTriggerDefinition() {
		return trigger;
	}

	/**
	 * @return The animation's interpolation.
	 */
	public @NotNull AnimationInterpolationDefinition getAnimationInterpolationDefinition() {
		return interpolation;
	}

	/**
	 * @return The animation's priority.
	 */
	public @NotNull AnimationPriorityDefinition getAnimationPriorityDefinition() {
		return priority;
	}

	public static class AnimationTriggerDefinition {
		public static final Codec<AnimationTriggerDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("type").forGetter(AnimationTriggerDefinition::getType),
				Codec.mapEither(Identifier.CODEC.fieldOf("identifier"), Identifier.CODEC.listOf().fieldOf("identifier")).forGetter(t -> {
					return Either.right(t.getIdentifiers());
				}), Codec.BOOL.optionalFieldOf("loop", false).forGetter(AnimationTriggerDefinition::getLoop)).apply(instance, AnimationTriggerDefinition::new));

		public enum Type {
			WHEN, WHILE, AFTER
		}

		private final @NotNull String type;
		private final @NotNull Type convertedType;
		private final @NotNull List<Identifier> identifiers;
		private final boolean loop;

		AnimationTriggerDefinition(@NotNull String type, @NotNull Either<Identifier, List<Identifier>> identifierOrIdentifiers, boolean loop) {
			this.type = type;
			this.convertedType = Type.valueOf(type.toUpperCase());
			identifiers = new ArrayList<>();
			identifierOrIdentifiers.ifLeft(identifier -> this.identifiers.add(identifier));
			identifierOrIdentifiers.ifRight(identifiers -> this.identifiers.addAll(identifiers));
			this.loop = loop;
		}

		/**
		 * @return The animation trigger's type.
		 */
		public @NotNull String getType() {
			return type;
		}

		/**
		 * @return The animation trigger's converted type.
		 */
		public @NotNull Type getConvertedType() {
			return convertedType;
		}

		/**
		 * @return The animation triggers' identifiers.
		 */
		public @NotNull List<Identifier> getIdentifiers() {
			return identifiers;
		}

		/**
		 * @return Whether or not the animation should loop.
		 */
		public boolean getLoop() {
			return loop;
		}
	}

	public static class AnimationInterpolationDefinition {
		public static final Codec<AnimationInterpolationDefinition> CODEC =
				RecordCodecBuilder
						.create(instance -> instance
								.group(Codec.STRING.optionalFieldOf("type", "INOUTSINE").forGetter(AnimationInterpolationDefinition::getType),
										Codec.FLOAT.optionalFieldOf("length_in", 0.5f).forGetter(AnimationInterpolationDefinition::getLengthIn),
										Codec.FLOAT.optionalFieldOf("length_out", 0.5f).forGetter(AnimationInterpolationDefinition::getLengthOut))
								.apply(instance, AnimationInterpolationDefinition::new));

		private final @NotNull String type;
		private final @NotNull Ease convertedType;
		private final @NotNull Float lengthIn;
		private final @NotNull Float lengthOut;

		AnimationInterpolationDefinition(@NotNull String type, @NotNull Float lengthIn, @NotNull Float lengthOut) {
			this.type = type;
			this.convertedType = Ease.valueOf(type);
			this.lengthIn = lengthIn;
			this.lengthOut = lengthOut;
		}

		/**
		 * @return The animation interpolation type.
		 */
		public @NotNull String getType() {
			return type;
		}

		/**
		 * @return The animation interpolation type, converted to an instance of {@link Ease}.
		 */
		public @NotNull Ease getConvertedType() {
			return convertedType;
		}

		/**
		 * @return The animation interpolation's length in.
		 */
		public @NotNull Float getLengthIn() {
			return lengthIn;
		}

		/**
		 * @return The animation interpolation's length out.
		 */
		public @NotNull Float getLengthOut() {
			return lengthOut;
		}
	}

	public static class AnimationPriorityDefinition {
		public static final Codec<AnimationPriorityDefinition> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(Codec.INT.optionalFieldOf("layer", DEFAULT_PRIORITY).forGetter(AnimationPriorityDefinition::getLayer),
						Codec.INT.optionalFieldOf("head", DEFAULT_PRIORITY).forGetter(AnimationPriorityDefinition::getHead),
						Codec.INT.optionalFieldOf("torso", DEFAULT_PRIORITY).forGetter(AnimationPriorityDefinition::getTorso),
						AnimationTwoPartPriorityDefinition.CODEC.optionalFieldOf("arms", new AnimationTwoPartPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY))
								.forGetter(AnimationPriorityDefinition::getArms),
						AnimationTwoPartPriorityDefinition.CODEC.optionalFieldOf("legs", new AnimationTwoPartPriorityDefinition(DEFAULT_PRIORITY, DEFAULT_PRIORITY))
								.forGetter(AnimationPriorityDefinition::getLegs))
						.apply(instance, AnimationPriorityDefinition::new));

		private final @NotNull Integer layer;
		private final @NotNull Integer head;
		private final @NotNull Integer torso;
		private final @NotNull AnimationTwoPartPriorityDefinition arms;
		private final @NotNull AnimationTwoPartPriorityDefinition legs;

		public AnimationPriorityDefinition(@NotNull Integer layer, @NotNull Integer head, @NotNull Integer torso, @NotNull AnimationTwoPartPriorityDefinition arms,
				@NotNull AnimationTwoPartPriorityDefinition legs) {
			this.layer = layer;
			this.head = head;
			this.torso = torso;
			this.arms = arms;
			this.legs = legs;
		}

		/**
		 * @return The animation priority of the animation layer.
		 */
		public @NotNull Integer getLayer() {
			return layer;
		}

		/**
		 * @return The animation priority of the head.
		 */
		public @NotNull Integer getHead() {
			return head;
		}

		/**
		 * @return The animation priority of the torso.
		 */
		public @NotNull Integer getTorso() {
			return torso;
		}

		/**
		 * @return The animation priority of the arms.
		 */
		public @NotNull AnimationTwoPartPriorityDefinition getArms() {
			return arms;
		}

		/**
		 * @return The animation priority of the legs.
		 */
		public @NotNull AnimationTwoPartPriorityDefinition getLegs() {
			return legs;
		}

		public static class AnimationTwoPartPriorityDefinition {
			public static final Codec<AnimationTwoPartPriorityDefinition> CODEC =
					RecordCodecBuilder.create(instance -> instance
							.group(Codec.INT.optionalFieldOf("right", DEFAULT_PRIORITY).forGetter(AnimationTwoPartPriorityDefinition::getRight),
									Codec.INT.optionalFieldOf("left", DEFAULT_PRIORITY).forGetter(AnimationTwoPartPriorityDefinition::getLeft))
							.apply(instance, AnimationTwoPartPriorityDefinition::new));

			private final @NotNull Integer right;
			private final @NotNull Integer left;

			public AnimationTwoPartPriorityDefinition(@NotNull Integer right, @NotNull Integer left) {
				this.right = right;
				this.left = left;
			}

			/**
			 * @return The animation priority of the right part.
			 */
			public @NotNull Integer getRight() {
				return right;
			}

			/**
			 * @return The animation priority of the left part.
			 */
			public @NotNull Integer getLeft() {
				return left;
			}
		}
	}
}
