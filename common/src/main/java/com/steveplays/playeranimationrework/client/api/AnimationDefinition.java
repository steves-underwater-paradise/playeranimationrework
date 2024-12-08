package com.steveplays.playeranimationrework.client.api;

import static com.steveplays.playeranimationrework.PlayerAnimationRework.MOD_ID;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.util.Identifier;

public class AnimationDefinition {
	public static final AnimationDefinition DEFAULT = new AnimationDefinition(new Identifier(MOD_ID, "none"),
			new AnimationTriggerDefinition(Either.right(Map.of("when", new Identifier(MOD_ID, "idle"))), false), Optional.empty(), Optional.empty());
	public static final Codec<AnimationDefinition> CODEC =
			RecordCodecBuilder
					.create(instance -> instance
							.group(Identifier.CODEC.fieldOf("identifier").forGetter(AnimationDefinition::getIdentifier),
									AnimationTriggerDefinition.CODEC.fieldOf("trigger").forGetter(AnimationDefinition::getAnimationTriggerDefinition),
									AnimationInterpolationDefinition.CODEC.optionalFieldOf("interpolation").forGetter(AnimationDefinition::getAnimationInterpolationDefinition),
									AnimationPriorityDefinition.CODEC.optionalFieldOf("priority").forGetter(AnimationDefinition::getAnimationPriorityDefinition))
							.apply(instance, AnimationDefinition::new));

	private final @NotNull Identifier identifier;
	private final @NotNull AnimationTriggerDefinition trigger;
	private final @NotNull Optional<AnimationInterpolationDefinition> interpolation;
	private final @NotNull Optional<AnimationPriorityDefinition> priority;

	AnimationDefinition(@NotNull Identifier identifier, @NotNull AnimationTriggerDefinition trigger, @NotNull Optional<AnimationInterpolationDefinition> interpolation,
			@NotNull Optional<AnimationPriorityDefinition> priority) {
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
	public @NotNull Optional<AnimationInterpolationDefinition> getAnimationInterpolationDefinition() {
		return interpolation;
	}

	/**
	 * @return The animation's priority.
	 */
	public @NotNull Optional<AnimationPriorityDefinition> getAnimationPriorityDefinition() {
		return priority;
	}

	private static class AnimationTriggerDefinition {
		public static final Codec<AnimationTriggerDefinition> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(createTypeCodec().forGetter(animationTriggerDefinition -> Either.left(Map.of())), Codec.BOOL.fieldOf("loop").forGetter(AnimationTriggerDefinition::getLoop))
						.apply(instance, AnimationTriggerDefinition::new));

		private final boolean loop;

		// These 2 fields cannot be final because of the use of Either
		private @NotNull String type = "";
		private @NotNull List<Identifier> identifiers = List.of();

		AnimationTriggerDefinition(@NotNull Either<Map<String, List<Identifier>>, Map<String, Identifier>> triggerOrTriggers, boolean loop) {
			triggerOrTriggers.ifLeft(triggersMap -> {
				@NotNull var triggerOptional = triggersMap.entrySet().stream().findFirst();
				if (triggerOptional.isEmpty()) {
					throw new JsonSyntaxException("Invalid animation trigger definition: trigger key is empty");
				}

				@NotNull var trigger = triggerOptional.get();
				type = trigger.getKey();

				@NotNull var triggerIdentifiers = trigger.getValue();
				if (triggerIdentifiers.isEmpty()) {
					throw new JsonSyntaxException("Invalid animation trigger definition: trigger value is empty");
				}
				identifiers = triggerIdentifiers;
			});
			triggerOrTriggers.ifRight(triggerMap -> {
				@NotNull var triggerOptional = triggerMap.entrySet().stream().findFirst();
				if (triggerOptional.isEmpty()) {
					throw new JsonSyntaxException("Invalid animation trigger definition: trigger key is empty");
				}

				@NotNull var trigger = triggerOptional.get();
				type = trigger.getKey();
				identifiers = List.of(trigger.getValue());
			});

			this.loop = loop;
		}

		/**
		 * @return The animation trigger's type.
		 */
		public @NotNull String getType() {
			return type;
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

		private static MapCodec<Either<Map<String, List<Identifier>>, Map<String, Identifier>>> createTypeCodec() {
			return Codec.mapEither(Codec.simpleMap(Codec.STRING, Identifier.CODEC.listOf(), Keyable.forStrings(() -> List.of("when", "while", "after").stream())),
					Codec.simpleMap(Codec.STRING, Identifier.CODEC, Keyable.forStrings(() -> List.of("when", "while", "after").stream())));
		}
	}

	private static class AnimationInterpolationDefinition {
		public static final Codec<AnimationInterpolationDefinition> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.optionalFieldOf("type").forGetter(AnimationInterpolationDefinition::getType),
						Codec.FLOAT.optionalFieldOf("length_in").forGetter(AnimationInterpolationDefinition::getLengthIn),
						Codec.FLOAT.optionalFieldOf("length_out").forGetter(AnimationInterpolationDefinition::getLengthOut)).apply(instance, AnimationInterpolationDefinition::new));

		private final @NotNull Optional<String> type;
		private final @NotNull Optional<Ease> convertedType;
		private final @NotNull Optional<Float> lengthIn;
		private final @NotNull Optional<Float> lengthOut;

		AnimationInterpolationDefinition(@NotNull Optional<String> type, @NotNull Optional<Float> lengthIn, @NotNull Optional<Float> lengthOut) {
			this.type = type;
			this.convertedType = type.isEmpty() ? Optional.empty() : Optional.of(Ease.valueOf(type.get()));
			this.lengthIn = lengthIn;
			this.lengthOut = lengthOut;
		}

		/**
		 * @return The animation interpolation type.
		 */
		public @NotNull Optional<String> getType() {
			return type;
		}

		/**
		 * @return The animation interpolation type, converted to an instance of {@link Ease}.
		 */
		public @NotNull Optional<Ease> getConvertedType() {
			return convertedType;
		}

		/**
		 * @return The animation interpolation's length in.
		 */
		public @NotNull Optional<Float> getLengthIn() {
			return lengthIn;
		}

		/**
		 * @return The animation interpolation's length out.
		 */
		public @NotNull Optional<Float> getLengthOut() {
			return lengthOut;
		}
	}

	private static class AnimationPriorityDefinition {
		public static final Codec<AnimationPriorityDefinition> CODEC = RecordCodecBuilder.create(instance -> instance
				.group(Codec.INT.optionalFieldOf("head").forGetter(AnimationPriorityDefinition::getHead), Codec.INT.optionalFieldOf("torso").forGetter(AnimationPriorityDefinition::getTorso),
						AnimationTwoPartPriorityDefinition.CODEC.optionalFieldOf("arms").forGetter(AnimationPriorityDefinition::getArms),
						AnimationTwoPartPriorityDefinition.CODEC.optionalFieldOf("legs").forGetter(AnimationPriorityDefinition::getLegs))
				.apply(instance, AnimationPriorityDefinition::new));

		private final @NotNull Optional<Integer> head;
		private final @NotNull Optional<Integer> torso;
		private final @NotNull Optional<AnimationTwoPartPriorityDefinition> arms;
		private final @NotNull Optional<AnimationTwoPartPriorityDefinition> legs;

		public AnimationPriorityDefinition(@NotNull Optional<Integer> head, @NotNull Optional<Integer> torso, @NotNull Optional<AnimationTwoPartPriorityDefinition> arms,
				@NotNull Optional<AnimationTwoPartPriorityDefinition> legs) {
			this.head = head;
			this.torso = torso;
			this.arms = arms;
			this.legs = legs;
		}

		/**
		 * @return The animation priority of the head.
		 */
		public @NotNull Optional<Integer> getHead() {
			return head;
		}

		/**
		 * @return The animation priority of the torso.
		 */
		public @NotNull Optional<Integer> getTorso() {
			return torso;
		}

		/**
		 * @return The animation priority of the arms.
		 */
		public @NotNull Optional<AnimationTwoPartPriorityDefinition> getArms() {
			return arms;
		}

		/**
		 * @return The animation priority of the legs.
		 */
		public @NotNull Optional<AnimationTwoPartPriorityDefinition> getLegs() {
			return legs;
		}

		private static class AnimationTwoPartPriorityDefinition {
			public static final Codec<AnimationTwoPartPriorityDefinition> CODEC =
					RecordCodecBuilder.create(instance -> instance.group(Codec.INT.optionalFieldOf("right").forGetter(AnimationTwoPartPriorityDefinition::getRight),
							Codec.INT.optionalFieldOf("left").forGetter(AnimationTwoPartPriorityDefinition::getLeft)).apply(instance, AnimationTwoPartPriorityDefinition::new));

			private final @NotNull Optional<Integer> right;
			private final @NotNull Optional<Integer> left;

			public AnimationTwoPartPriorityDefinition(@NotNull Optional<Integer> right, @NotNull Optional<Integer> left) {
				this.right = right;
				this.left = left;
			}

			/**
			 * @return The animation priority of the right part.
			 */
			public @NotNull Optional<Integer> getRight() {
				return right;
			}

			/**
			 * @return The animation priority of the left part.
			 */
			public @NotNull Optional<Integer> getLeft() {
				return left;
			}
		}
	}
}
