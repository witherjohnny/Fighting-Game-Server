package Data;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CharactersData {

// List to hold Character objects
    public static List<Character> characters = new ArrayList<>();

    // Load characters manually (can replace with JSON parsing if using Maven/Gradle)
    public static void loadCharacters(/* String filePath*/) {
        // Manually creating Character objects
        characters.add(new Character(
                "FireWizard",
                Arrays.asList("Idle", "Run", "Jump", "Dead", "Hurt", "Rest", "Roll", "Walk"),
                Arrays.asList("Fireball", "Flame_Jet", "AreaAttack", "Attack_1", "Attack_2"),
                Arrays.asList("Charge")
        ));

        characters.add(new Character(
                "Warrior_2",
                Arrays.asList("Idle", "Run", "Jump", "Dead", "Hurt", "Rest", "Roll", "Walk"),
                Arrays.asList("AreaAttack", "Attack_1", "Attack_2", "Run+Attack", "Protect"),
                Arrays.asList()
        ));
    }

    // Check if any animation exists for a character
    public static boolean animationExists(String personaggio, String animationName) {
        return baseActionExists(personaggio, animationName)
                || attackExists(personaggio, animationName)
                || projectileExists(personaggio, animationName);
    }

    // Check if an animation exists in BaseActions
    public static boolean baseActionExists(String characterName, String actionName) {
        Character character = findCharacterByName(characterName);
        return character != null && character.getBaseActions().contains(actionName);
    }

    // Check if an animation exists in Attacks
    public static boolean attackExists(String characterName, String attackName) {
        Character character = findCharacterByName(characterName);
        return character != null && character.getAttacks().contains(attackName);
    }

    // Check if an animation exists in Projectiles
    public static boolean projectileExists(String characterName, String projectileName) {
        Character character = findCharacterByName(characterName);
        return character != null && character.getProjectiles().contains(projectileName);
    }

    // Helper method to find a character by name
    private static Character findCharacterByName(String name) {
        return characters.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }    
}
