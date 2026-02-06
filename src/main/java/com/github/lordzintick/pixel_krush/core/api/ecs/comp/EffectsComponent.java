package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Effect;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.LivingEntity;
import com.github.lordzintick.pixel_krush.core.util.Nullable;
import com.github.lordzintick.pixel_krush.core.util.registry.LazyList;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A somewhat specific implementation of {@link AbstractComponent} designed specifically for use as the {@link Effect} tracker for {@link Entity Entities}.<br>
 * Most of its core functionality is just a <code>private</code> {@link LazyList} with a bunch of helper methods to get and modify certain attributes of it.
 */
public class EffectsComponent extends AbstractComponent {
    /**
     * A {@link LazyList} used to track the actual effects and safely add and remove ones on the fly.<br>
     * See {@link LazyList#flush()} for more information on how the lazy list works.
     */
    private final LazyList<Effect> effects = new LazyList<>();

    /**
     * Constructs a new {@link EffectsComponent} linked to the specified {@link Entity}.
     * @param parent The parent {@link Entity} of the component.
     */
    public EffectsComponent(Entity parent) {
        super(parent);
    }

    /**
     * Ticks all effects in the list using the provided <code>deltaTime</code>.
     * @param deltaTime The time since the last frame was rendered; used for various purposes depending on the specific effect.
     */
    public void tickAll(float deltaTime) {
        effects.forEach(effect -> {
            effect.tick((LivingEntity) parent, deltaTime);
            effect.timeLeft -= deltaTime;

            if (effect.timeLeft <= 0) {
                removeEffect(effect);
            }
        });
    }

    /**
     * Flushes the effects list.<br>
     * See {@link LazyList#flush()} for more information on what exactly this does.
     */
    public void flush() {
        effects.flush();
    }

    /**
     * Gets the size of the effects list.
     * @return The size of the effects list, as an integer.
     */
    public int size() {
        return effects.size();
    }

    /**
     * Gets whether the specified <code>effect</code> exists within the list.
     * @param effect The {@link Effect} to check the contents of the effects list against.
     * @return Whether the specified <code>effect</code> was found in the stored list.
     */
    public boolean hasEffect(Effect effect) {
        return effects.contains(effect);
    }

    /**
     * Gets the effect from the list at the specified index, or <code>null</code> if none was found.
     * @param index The index of the effect to attempt to retrieve.
     * @param <T> The requested effect type.
     * @return The {@link Effect} found at the specified <code>index</code> in the list, or <code>null</code> if no such effect was found.
     */
    @Nullable
    public <T extends Effect> T getEffectOrNull(int index) {
        return effects.get(index);
    }

    /**
     * Gets the first effect from the list with the specified class.
     * @param type The type of effect to attempt to retrieve.
     * @param <T> The requested effect type.
     * @return The first {@link Effect} found in the list of the specified <code>type</code>, or <code>null</code> if no such effect was found.
     */
    @Nullable
    public <T extends Effect> T getEffectOrNull(Class<T> type) {
        AtomicReference<T> returnEffect = new AtomicReference<>();
        effects.forEach(effect -> {
            if (effect.getClass().isAssignableFrom(type)) {
                returnEffect.set((T) effect);
            }
        });

        return returnEffect.get();
    }

    /**
     * Applies the specified effect to the parent {@link Entity} by adding it to the effects list.
     * @param effect The {@link Effect} to add to the stored list.
     */
    public void applyEffect(Effect effect) {
        effect.apply((LivingEntity) parent);
        effects.add(effect);
    }

    /**
     * Removes the specified effect from the parent {@link Entity} by removing it to the effects list.
     * @param effect The {@link Effect} to remove from the stored list.
     */
    public void removeEffect(Effect effect) {
        effects.remove(effect);
        effect.end((LivingEntity) parent);
    }
}
