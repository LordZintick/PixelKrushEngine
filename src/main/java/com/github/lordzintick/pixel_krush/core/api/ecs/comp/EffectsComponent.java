package com.github.lordzintick.pixel_krush.core.api.ecs.comp;

import com.github.lordzintick.pixel_krush.core.api.ecs.Effect;
import com.github.lordzintick.pixel_krush.core.api.ecs.Entity;
import com.github.lordzintick.pixel_krush.core.api.ecs.LivingEntity;
import com.github.lordzintick.pixel_krush.core.util.registry.LazyList;

import java.util.concurrent.atomic.AtomicReference;

public class EffectsComponent extends AbstractComponent {
    private final LazyList<Effect> effects = new LazyList<>();

    public EffectsComponent(Entity parent) {
        super(parent);
    }

    public void tickAll(float deltaTime) {
        effects.forEach(effect -> {
            effect.tick((LivingEntity) parent, deltaTime);
            effect.timeLeft -= deltaTime;

            if (effect.timeLeft <= 0) {
                removeEffect(effect);
            }
        });
    }

    public void flush() {
        effects.flush();
    }

    public int size() {
        return effects.size();
    }

    public boolean hasEffect(Effect effect) {
        return effects.contains(effect);
    }

    public <T extends Effect> T getEffectOrNull(int index) {
        return effects.get(index);
    }

    public <T extends Effect> T getEffectOrNull(Class<T> type) {
        AtomicReference<T> returnEffect = new AtomicReference<>();
        effects.forEach(effect -> {
            if (effect.getClass().isAssignableFrom(type)) {
                returnEffect.set((T) effect);
            }
        });

        return returnEffect.get();
    }

    public <T extends Effect> T applyEffect(Effect effect) {
        effect.apply((LivingEntity) parent);
        return (T) effects.add(effect);
    }

    public void removeEffect(Effect effect) {
        effects.remove(effect);
        effect.end((LivingEntity) parent);
    }
}
