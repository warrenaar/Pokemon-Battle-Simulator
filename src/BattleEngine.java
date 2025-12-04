import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class BattleEngine {
    private static final Random RNG = new Random();
    private static final int SWITCH_SPEED_BONUS = 50;

    public static void runBattle(Scanner in, Team player, Team enemy) {

        int playerSpeedBonus = 0;
        int enemySpeedBonus = 0;

        System.out.printf("Battle begins between %s and %s.", player.getName(), enemy.getName());
        System.out.println(player.getActive());
        System.out.println(enemy.getActive());

        while (!player.allFainted() && !enemy.allFainted()) {

            boolean playerForced = ensurePlayerActiveNotFainted(in, player);
            boolean enemyForced = ensureEnemyActiveNotFainted(enemy);

            if (player.allFainted() || enemy.allFainted()) {
                break;
            }

            if (playerForced) {
                playerSpeedBonus = SWITCH_SPEED_BONUS;
            }
            if (enemyForced) {
                enemySpeedBonus = SWITCH_SPEED_BONUS;
            }

            Pokemon playerActive = player.getActive();
            Pokemon enemyActive = enemy.getActive();

            System.out.println();
            System.out.printf("Your active Pokémon: ", playerActive);
            System.out.printf("Enemy active Pokémon: ", enemyActive);

            int playerEffectiveSpeed = playerActive.getSpeed() + playerSpeedBonus;
            int enemyEffectiveSpeed = enemyActive.getSpeed() + enemySpeedBonus;

            playerSpeedBonus = 0;
            enemySpeedBonus = 0;

            boolean playerFirst = playerEffectiveSpeed >= enemyEffectiveSpeed;

            if (playerFirst) {
                playerSpeedBonus = playerTurn(in, player, enemy);

                // If enemy Pokémon fainted, skip their move this round
                if (enemy.allFainted() || enemy.getActive().isFainted()) {
                    continue;
                }

                enemySpeedBonus = enemyTurn(enemy, player);
            } else {
                enemySpeedBonus = enemyTurn(enemy, player);

                // If player Pokémon fainted, skip their move this round
                if (player.allFainted() || player.getActive().isFainted()) {
                    continue;
                }

                playerSpeedBonus = playerTurn(in, player, enemy);
            }
        }

        if (player.allFainted()) {
            System.out.println("All your Pokémon fainted. You lose.");
        } else {
            System.out.println("All enemy Pokémon fainted. You win.");
        }

        // After the battle, print damage logs for each side
        printBattleLog(player, "Player");
        printBattleLog(enemy, "Enemy");
    }

    // show how much damage each Pokemon on a team dealt
    private static void printBattleLog(Team team, String teamName) {
        System.out.println();
        System.out.printf("%s battle log (total damage dealt):%n", teamName);

        boolean any = false;
        for (Pokemon p : team.getMembers()) {
            if (p instanceof BattlePokemon) {
                BattlePokemon bp = (BattlePokemon) p;
                double total = bp.getTotalDamageDealt();
                if (total > 0) {
                    any = true;
                    System.out.printf("- %s dealt %.1f total damage.%n",
                            bp.getName(), total);
                }
            }
        }

        if (!any) {
            System.out.println("- No damage dealt.");
        }
    }

    // reset damage counters for all BattlePokemon on a team
    private static void resetDamageDealt(Team team) {
        for (Pokemon p : team.getMembers()) {
            if (p instanceof BattlePokemon) {
                ((BattlePokemon) p).resetDamageDealt();
            }
        }
    }

    // returns speed bonus for next round if switched
    private static int playerTurn(Scanner in, Team player, Team enemy) {
        while (true) {
            System.out.println();
            System.out.println("Your turn. Choose an action.");
            System.out.println("1) Standard Attack");
            System.out.println("2) Coin Flip Attack");
            System.out.println("3) Switch");

            String choice = in.nextLine().trim();
            BattlePokemon attacker = (BattlePokemon) player.getActive();
            BattlePokemon defender = (BattlePokemon) enemy.getActive();

            if (choice.equals("1")) {
                attacker.standardAttack(defender);
                return 0;
            } else if (choice.equals("2")) {
                attacker.coinFlipAttack(defender);
                return 0;
            } else if (choice.equals("3")) {
                if (handlePlayerSwitch(in, player)) {
                    return SWITCH_SPEED_BONUS;
                } else {
                    continue;
                }
            } else {
                System.out.println("Please choose 1, 2, or 3.");
            }
        }
    }

    // smarter AI: switch below 30 percent HP if possible, otherwise attack
    // returns speed bonus for next round if switched
    private static int enemyTurn(Team enemy, Team player) {
        System.out.println();
        System.out.println("Enemy turn.");

        BattlePokemon attacker = (BattlePokemon) enemy.getActive();
        BattlePokemon defender = (BattlePokemon) player.getActive();

        double healthRatio = attacker.getCurrentHealth() / attacker.getMaxHealth();
        List<Pokemon> healthyBench = enemy.getHealthyBench();

        if (healthRatio <= 0.30 && !healthyBench.isEmpty()) {
            Pokemon bestSwitchTarget = healthyBench.stream()
                    .max(Comparator.comparingDouble(Pokemon::getCurrentHealth))
                    .orElse(null);

            if (bestSwitchTarget != null) {
                int index = enemy.getMembers().indexOf(bestSwitchTarget);
                enemy.switchTo(index);
                System.out.println("Enemy switches to " + enemy.getActive().getName() + ".");
                return SWITCH_SPEED_BONUS;
            }
        }

        if (RNG.nextBoolean()) {
            attacker.standardAttack(defender);
        } else {
            attacker.coinFlipAttack(defender);
        }

        return 0;
    }

    // ensure the player has a non fainted active Pokémon
    // returns true if a forced switch happened
    private static boolean ensurePlayerActiveNotFainted(Scanner in, Team team) {

        Pokemon active = team.getActive();
        if (active != null && !active.isFainted()) {
            return false;
        }

        if (team.allFainted()) {
            return false;
        }

        System.out.println();
        System.out.println("Your active Pokémon has fainted. You must switch.");

        while (true) {
            if (handlePlayerSwitch(in, team)) {
                return true;
            }
        }
    }

    // ensure the enemy has a non fainted active Pokémon
    // returns true if a forced switch happened
    private static boolean ensureEnemyActiveNotFainted(Team enemy) {

        Pokemon active = enemy.getActive();
        if (active != null && !active.isFainted()) {
            return false;
        }

        if (enemy.allFainted()) {
            return false;
        }

        List<Pokemon> healthy = enemy.getHealthyMembers();
        if (!healthy.isEmpty()) {
            Pokemon best = healthy.stream()
                    .max(Comparator.comparingDouble(Pokemon::getCurrentHealth))
                    .orElse(null);
            if (best != null) {
                int index = enemy.getMembers().indexOf(best);
                enemy.switchTo(index);
                System.out.println("Enemy switches to " + enemy.getActive().getName() + ".");
                return true;
            }
        }
        return false;
    }

    private static boolean handlePlayerSwitch(Scanner in, Team team) {
        System.out.println("Choose a Pokémon to switch to.");
        team.printTeam();
        System.out.print("Enter index: ");
        String line = in.nextLine().trim();
        try {
            int index = Integer.parseInt(line);
            team.switchTo(index-1);
            System.out.printf("You switched to %s.", team.getActive().getName());
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
