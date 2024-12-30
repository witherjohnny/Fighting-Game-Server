package Data;

import java.util.List;

public class Character {
    private String name;
    private List<String> baseActions;
    private List<String> attacks;

    // Constructor
    public Character(String name, List<String> baseActions, List<String> attacks) {
        this.name = name;
        this.baseActions = baseActions;
        this.attacks = attacks;
    }

    // Getter and Setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter methods for baseActions
    public List<String> getBaseActions() {
        return baseActions;
    }

    public void setBaseActions(List<String> baseActions) {
        this.baseActions = baseActions;
    }

    // Getter and Setter methods for attacks
    public List<String> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<String> attacks) {
        this.attacks = attacks;
    }
}
