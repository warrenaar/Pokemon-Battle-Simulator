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

    public String getName() {
        return name;
    }

    public void add(Pokemon pokemon) {
        if (members.size() >= 3) {
            throw new IllegalStateException("A team cannot have more than 3 Pokemon.");
        }
        members.add(pokemon);
    }

    public List<Pokemon> getMembers() {
        return members;
    }

    public Pokemon getActive() {
        if (members.isEmpty()) {
            return null;
        }
        if (activeIndex < 0 || activeIndex >= members.size()) {
            activeIndex = 0;
        }
        return members.get(activeIndex);
    }

    public void switchTo(int index) {
        if (index < 0 || index >= members.size()) {
            throw new IllegalArgumentException("Invalid index for team.");
        }
        if (index == activeIndex) {
            throw new IllegalArgumentException("You are already using that Pokemon.");
        }
        Pokemon target = members.get(index);
        if (target.isFainted()) {
            throw new IllegalArgumentException("That Pokemon has fainted and cannot be switched to.");
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
            System.out.printf("%d) %s%n", i, p.toString());
        }
    }
}
