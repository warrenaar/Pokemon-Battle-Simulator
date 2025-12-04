import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/* Stretch goals attempted:
 * 
 * 2. coinflip : RNG to deal massive damage or lose a turn trying 
 * 
 * 4. Attack Multipliers : based on enum type matchups
 * 
 * 5. battleLog : differentiated by team, keeping track of 
 * 	  damage dealt by each pokemon
 * 
 * 6. "ai" enemy that selects from pool
 */


public class BattleSimulatorDriver {

	private static final String CSV_PATH = "pokemon.csv";
	private static final Random RNG = new Random();

	public static void main(String[] args) {

		try (Scanner in = new Scanner(System.in)) {

			List<Pokemon> pokedex = ReadCSV.loadPokemonFromFile(CSV_PATH);
			if (pokedex.isEmpty()) {
				System.out.println("No Pokémon loaded from CSV.");
				return;
			}

			//anonymous comparator sorts alphabetically
			Collections.sort(pokedex, new Comparator<Pokemon>() {
				@Override
				public int compare(Pokemon a, Pokemon b) {
					return a.getName().compareToIgnoreCase(b.getName());
				}
			});
			System.out.println("Loaded " + pokedex.size() + " Pokémon from CSV.");

			sortingMenu(in, pokedex);

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

	private static void sortingMenu(Scanner in, List<Pokemon> pokedex) {
		boolean done = false;

		while (!done) {
			System.out.println();
			System.out.println("Current Pokémon list:");
			printPokedex(pokedex);

			System.out.println();
			System.out.println("How would you like to sort the list?");
			System.out.println("1) Name ");
			System.out.println("2) Type ");
			System.out.println("3) Max HP ");
			System.out.println("4) Attack (descending)");
			System.out.println("5) Defense (descending)");
			System.out.println("6) Speed (descending)");
			System.out.println("7) Done sorting, choose my team");
			System.out.println("Enter choice: ");
			String choice = in.nextLine().trim();

			switch (choice) {
			case "1":
				pokedex.sort(
						(a, b) -> a.getName().compareToIgnoreCase(b.getName())
						);
				break;
			case "2":
				pokedex.sort((a, b) -> {
					int typeCmp = a.getType().name()
							.compareToIgnoreCase(b.getType().name());
					if (typeCmp != 0) {
						return typeCmp;
					}
					return a.getName().compareToIgnoreCase(b.getName());
				});
				break;
			case "3":
				pokedex.sort(
						(a, b) -> Double.compare(b.getMaxHealth(), a.getMaxHealth())
						);
				break;
			case "4":
				pokedex.sort(
						(a, b) -> Double.compare(b.getAttack(), a.getAttack())
						);
				break;
			case "5":
				pokedex.sort(
						(a, b) -> Double.compare(b.getDefense(), a.getDefense())
						);
				break;
			case "6":
				pokedex.sort(
						(a, b) -> Integer.compare(b.getSpeed(), a.getSpeed())
						);
				break;
			case "7":
				done = true;
				break;
			default:
				System.out.println("Please enter a number between 1 and 7.");
			}
		}
	}

	private static void printPokedex(List<Pokemon> pokedex) {
		for (int i = 0; i < pokedex.size(); i++) {
			System.out.printf("%d) %s%n", i+1, pokedex.get(i));
		}
	}

	private static void choosePlayerTeam(Scanner in, List<Pokemon> pokedex, Team player) {
		System.out.println("Choose your team of three Pokemon.");

		int limit = pokedex.size();

		System.out.println();
		System.out.println("Available Pokémon:");
		printPokedex(pokedex);

		while (player.getMembers().size() < 3) {
			int memberNumber = player.getMembers().size() + 1;
			System.out.printf("Enter index for Pokémon %d of 3: ", memberNumber);

			String line = in.nextLine().trim();
			try {
				int index =  Integer.parseInt(line) - 1;
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
