package Data;

import java.io.StringReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
public class CharactersData {

    // List to hold Character objects
    public static List<Character> characters = null;

    // Method to load characters from a JSON file
    public static void loadCharacters(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            characters = objectMapper.readValue(new File(filePath), new TypeReference<List<Character>>() {});
        } catch (IOException e) {
            System.out.println("Error loading JSON: " + e.getMessage());
        }
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
