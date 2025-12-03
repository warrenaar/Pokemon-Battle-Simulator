import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BattleSimulatorDriver {

    private static final String CSV_PATH = "pokemon.csv"; // adjust path if needed
    private static final Random RNG = new Random();

    public static void main(String[] args) {

        try (Scanner in = new Scanner(System.in)) {

            List<Pokemon> pokedex = ReadCSV.loadPokemonFromFile(CSV_PATH);
            if (pokedex.isEmpty()) {
                System.out.println("No Pokémon loaded from CSV.");
                return;
            }

            // anonymous class Comparator to satisfy that requirement
            Collections.sort(pokedex, new Comparator<Pokemon>() {
                @Override
                public int compare(Pokemon a, Pokemon b) {
                    return a.getName().compareToIgnoreCase(b.getName());
                }
            });

            System.out.println("Loaded " + pokedex.size() + " Pokémon from CSV.");

            Team player = new Team("Player");
            Team enemy = new Team("Enemy");

            choosePlayerTeam(in, pokedex, player);
            chooseEnemyTeam(pokedex, player, enemy);

            System.out.println();
            System.out.println("Your team:");
            player.printTeam();
            System.out.println();
            System.out.println("Enemy team:");
            enemy.printTeam();

            BattleEngine.runBattle(in, player, enemy);

        } catch (IOException e) {
            System.out.println("Failed to load pokemon.csv. " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void choosePlayerTeam(Scanner in, List<Pokemon> pokedex, Team player) {
        System.out.println("Choose your team of three Pokémon.");
        int limit = pokedex.size();

        System.out.println();
        System.out.println("Available Pokémon:");
        for (int i = 0; i < limit; i++) {
            System.out.printf("%d) %s%n", i, pokedex.get(i).toString());
        }

        while (player.getMembers().size() < 3) {
            int memberNumber = player.getMembers().size() + 1;
            System.out.printf("Enter index for Pokémon %d of 3: ", memberNumber);

            String line = in.nextLine().trim();
            try {
                int index = Integer.parseInt(line);
                if (index < 0 || index >= limit) {
                    System.out.println("Index out of range.");
                    continue;
                }
                Pokemon chosen = pokedex.get(index);
                if (player.getMembers().contains(chosen)) {
                    System.out.println("You already chose that Pokémon.");
                    continue;
                }
                player.add(chosen);
                System.out.println("Added " + chosen.getName() + " to your team.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void chooseEnemyTeam(List<Pokemon> pokedex, Team player, Team enemy) {

        Collections.shuffle(pokedex, RNG);

        for (Pokemon p : pokedex) {
            if (!player.getMembers().contains(p)) {
                enemy.add(p);
            }
            if (enemy.getMembers().size() == 3) {
                break;
            }
        }
    }
}
