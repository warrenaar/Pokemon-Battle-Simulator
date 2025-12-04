import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Team {

    private final String name;
    private final List<Pokemon> members = new ArrayList<>();
    private int activeIndex = 0;

    public Team(String name) {
        this.name = name;
    }

    public void add(Pokemon pokemon) {
        if (members.size() >= 3) {
            throw new IllegalStateException("A team can only have three Pokémon.");
        }
        members.add(pokemon);
    }

    public String getName() {
        return name;
    }

    public List<Pokemon> getMembers() {
        return members;
    }

    public Pokemon getActive() {
        if (members.isEmpty()) {
            return null;
        }
        return members.get(activeIndex);
    }

    public void switchTo(int index) {
        if (index < 0 || index >= members.size()) {
            throw new IllegalArgumentException("Invalid team slot.");
        }
        if (members.get(index).isFainted()) {
            throw new IllegalArgumentException("Cannot switch to a fainted Pokémon.");
        }
        activeIndex = index;
    }

    public boolean allFainted() {
        return members.stream().allMatch(Pokemon::isFainted);
    }

    public List<Pokemon> getHealthyMembers() {
        return members.stream()
                .filter(p -> !p.isFainted())
                .collect(Collectors.toList());
    }

    public List<Pokemon> getHealthyBench() {
        Pokemon active = getActive();
        return members.stream()
                .filter(p -> !p.isFainted() && p != active)
                .collect(Collectors.toList());
    }

    public void printTeam() {
        for (int i = 0; i < members.size(); i++) {
            Pokemon p = members.get(i);
            System.out.printf("%d) %s%n", i+1, p);
        }
    }
}
