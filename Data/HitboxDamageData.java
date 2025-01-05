package Data;

import java.util.HashMap;
import java.util.Map;

public class HitboxDamageData {
    private static Map<String, Integer> hitboxDamageMap = new HashMap<>();

    public static void loadHitboxDamageData() {
        // Load damage values for each hitbox name
        hitboxDamageMap.put("Fireball", 0);
        hitboxDamageMap.put("Flame_Jet", 10);
        hitboxDamageMap.put("AreaAttack", 10);
        hitboxDamageMap.put("Attack_1", 3);
        hitboxDamageMap.put("Attack_2", 5);
        hitboxDamageMap.put("Charge", 10);
        hitboxDamageMap.put("Run+Attack", 5);
        hitboxDamageMap.put("Protect", 0);
    }

    public static int getDamage(String hitboxName) {
        return hitboxDamageMap.getOrDefault(hitboxName, 0);
    }
}
