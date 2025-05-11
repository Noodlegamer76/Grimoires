package com.noodlegamer76.grimoires.spellcrafting.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManaParticle {
    private final Map<Class<?>, Object> particleData = new HashMap<>();
    private final int startPressure;
    private int currentPressure;

    public ManaParticle(int startPressure) {
        this.startPressure = startPressure;
        this.currentPressure = startPressure;
    }

    public int getStartPressure() {
        return startPressure;
    }

    public int getCurrentPressure() {
        return currentPressure;
    }

    public void setCurrentPressure(int currentPressure) {
        this.currentPressure = currentPressure;
    }

    public <T> void addData(Class<T> type, T data) {
        particleData.put(type, data);
    }

    @SuppressWarnings("unchecked")
    public <T> T getDataOfType(Class<T> type) {
        return (T) particleData.get(type);
    }

    public boolean hasDataOfType(Class<?> type) {
        return particleData.containsKey(type);
    }

    public static List<ManaParticle> createManaParticles(int amount, int startPressure) {
        List<ManaParticle> particles = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            particles.add(new ManaParticle(startPressure));
        }
        return particles;
    }
}
