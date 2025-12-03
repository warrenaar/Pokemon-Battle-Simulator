import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class BattleEngine {

    private static final Random RNG = new Random();

    private BattleEngine() {
    }

    public static void runBattle(Scanner in, Team player, Team enemy) {

        System.out.println("Battle begins between " + player.getName() + " and " + enemy.getName() + ".");
        System.out.println(player.getActive().battleCry());
        System.out.println(enemy.getActive().battleCry());

        while (!player.allFainted() && !enemy.allFainted()) {

            System.out.println();
            System.out.println("Your active Pokémon: " + player.getActive());
            System.out.println("Enemy active Pokémon: " + enemy.getActive());

            // decide order by speed
            BattlePokemon p1 = (BattlePokemon) player.getActive();
            BattlePokemon p2 = (BattlePokemon) enemy.getActive();

            boolean playerFirst = p1.getSpeed() >= p2.getSpeed();

            if (playerFirst) {
                playerTurn(in, player, enemy);
                if (!enemy.allFainted()) {
                    enemyTurn(enemy, player);
                }
            } else {
                enemyTurn(enemy, player);
                if (!player.allFainted()) {
                    playerTurn(in, player, enemy);
                }
            }
        }

        if (player.allFainted()) {
            System.out.println("All your Pokémon fainted. You lose.");
        } else {
            System.out.println("All enemy Pokémon fainted. You win.");
        }
    }

    private static void playerTurn(Scanner in, Team player, Team enemy) {
        while (true) {
            System.out.println();
            System.out.println("Your turn. Choose an action.");
            System.out.println("1) Attack");
            System.out.println("2) Switch");

            String choice = in.nextLine().trim();
            if (choice.equals("1")) {
                BattlePokemon attacker = (BattlePokemon) player.getActive();
                BattlePokemon defender = (BattlePokemon) enemy.getActive();
                attacker.standardAttack(defender);
                break;
            } else if (choice.equals("2")) {
                if (handleSwitch(in, player)) {
                    break;
                }
            } else {
                System.out.println("Invalid choice, please enter 1 or 2.");
            }
        }
    }

    private static boolean handleSwitch(Scanner in, Team team) {
        System.out.println("Choose a Pokémon to switch to.");
        team.printTeam();
        System.out.print("Enter index: ");
        String line = in.nextLine().trim();
        try {
            int index = Integer.parseInt(line);
            team.switchTo(index);
            System.out.println("You switched to " + team.getActive().getName() + ".");
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static void enemyTurn(Team enemy, Team player) {
        System.out.println();
        System.out.println("Enemy turn.");

        if (enemy.getActive().isFainted()) {
            // auto switch to first non fainted
            List<Integer> choices = enemy.getAvailableIndexes();
            if (!choices.isEmpty()) {
                int chosenIndex = choices.get(0);
                enemy.switchTo(chosenIndex);
                System.out.println("Enemy switched to " + enemy.getActive().getName() + ".");
            }
        }

        BattlePokemon attacker = (BattlePokemon) enemy.getActive();
        BattlePokemon defender = (BattlePokemon) player.getActive();

        // very simple AI, mostly attacks
        if (RNG.nextDouble() < 0.9 || enemy.getAvailableIndexes().size() <= 1) {
            attacker.standardAttack(defender);
        } else {
            List<Integer> choices = enemy.getAvailableIndexes();
            int idx = choices.get(RNG.nextInt(choices.size()));
            enemy.switchTo(idx);
            System.out.println("Enemy switched to " + enemy.getActive().getName() + ".");
        }
    }
}
