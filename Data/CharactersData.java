package Data;

import java.util.Arrays;
import java.util.List;

public class CharactersData {

    // List to hold Character objects
    public static List<Character> characters = null;

    public static void loadCharacters(String filePath) {//JSON non funziona, serve un progetto maven o gradle
        // Manually create Character objects and add to the list
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


    // Method to check if an animation exists for a character
    public static boolean animationExists(String personaggio, String animationName) {
        // Find the character by name
        Character character = characters.stream()
                .filter(c -> c.getName().equals(personaggio))
                .findFirst()
                .orElse(null);  // Return null if not found

        if (character != null) {
            // Check base actions
            for (String action : character.getBaseActions()) {
                if (action.equals(animationName)) {
                    return true;
                }
            }

            // Check attacks
            for (String action : character.getAttacks()) {
                if (action.equals(animationName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
