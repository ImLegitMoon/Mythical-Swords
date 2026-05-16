package com.mythical.mythicalswords.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<String, Long> cooldowns = new HashMap<>();

    public boolean isOnCooldown(UUID playerId, String key) {
        String composite = playerId + ":" + key;
        long now = System.currentTimeMillis();
        return cooldowns.getOrDefault(composite, 0L) > now;
    }

    public long getRemaining(UUID playerId, String key) {
        String composite = playerId + ":" + key;
        long now = System.currentTimeMillis();
        return Math.max(0L, cooldowns.getOrDefault(composite, 0L) - now);
    }

    public void setCooldown(UUID playerId, String key, long millis) {
        String composite = playerId + ":" + key;
        long now = System.currentTimeMillis();
        cooldowns.put(composite, now + millis);
    }
}
