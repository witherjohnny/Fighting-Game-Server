package Data;

import java.util.HashMap;
import java.util.Map;

public class HitboxDamageData {
    private static Map<String, Integer> hitboxDamageMap = new HashMap<>();

    public static void loadHitboxDamageData() {
        // Load damage values for each hitbox name
        hitboxDamageMap.put("Fireball", 10);
        hitboxDamageMap.put("Flame_Jet", 15);
        hitboxDamageMap.put("AreaAttack", 20);
        hitboxDamageMap.put("Attack_1", 5);
        hitboxDamageMap.put("Attack_2", 7);
        hitboxDamageMap.put("Charge", 12);
        hitboxDamageMap.put("Run+Attack", 8);
        hitboxDamageMap.put("Protect", 0);
    }

    public static int getDamage(String hitboxName) {
        return hitboxDamageMap.getOrDefault(hitboxName, 0);
    }
}
