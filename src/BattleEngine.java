import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public final class BattleEngine {
  
    private static final int SWITCH_SPEED_BONUS = 50;
    private BattleEngine() {}

    public static void runBattle(Scanner in, Team player, Team enemy) {

        int playerSpeedBonus = 0;
        int enemySpeedBonus = 0;

        System.out.println("Battle begins between " + player.getName() + " and " + enemy.getName() + ".");
        System.out.println(player.getActive().battleCry());
        System.out.println(enemy.getActive().battleCry());

        while (!player.allFainted() && !enemy.allFainted()) {

            // handle fainted actives and forced switches before the round
            boolean playerForced = ensureActiveNotFainted(in, player, true);
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
            System.out.println("Your active Pokémon: " + playerActive);
            System.out.println("Enemy active Pokémon: " + enemyActive);

            // calculate effective speeds with bonuses
            int playerEffectiveSpeed = playerActive.getSpeed() + playerSpeedBonus;
            int enemyEffectiveSpeed = enemyActive.getSpeed() + enemySpeedBonus;

            // bonuses are single use per round
            playerSpeedBonus = 0;
            enemySpeedBonus = 0;

            boolean playerFirst = playerEffectiveSpeed >= enemyEffectiveSpeed;

            if (playerFirst) {
                playerSpeedBonus = playerTurn(in, player, enemy);
                if (!enemy.allFainted()) {
                    enemySpeedBonus = enemyTurn(enemy, player);
                }
            } else {
                enemySpeedBonus = enemyTurn(enemy, player);
                if (!player.allFainted()) {
                    playerSpeedBonus = playerTurn(in, player, enemy);
                }
            }
        }

        if (player.allFainted()) {
            System.out.println("All your Pokémon fainted. You lose.");
        } else {
            System.out.println("All enemy Pokémon fainted. You win.");
        }
    }

    // returns speed bonus for next round if switched
    private static int playerTurn(Scanner in, Team player, Team enemy) {
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
                return 0;
            } else if (choice.equals("2")) {
                if (handlePlayerSwitch(in, player)) {
                    return SWITCH_SPEED_BONUS;
                }
            } else {
                System.out.println("Invalid choice, please enter 1 or 2.");
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

        attacker.standardAttack(defender);
        return 0;
    }

    // forces player to switch if active fainted
    private static boolean ensureActiveNotFainted(Scanner in, Team team, boolean isPlayer) {

        Pokemon active = team.getActive();
        if (active != null && !active.isFainted()) {
            return false;
        }

        if (team.allFainted()) {
            return false;
        }

        if (isPlayer) {
            System.out.println();
            System.out.println("Your active Pokémon has fainted. You must switch.");
            while (true) {
                if (handlePlayerSwitch(in, team)) {
                    return true;
                }
            }
        } else {
            // enemy forced switch
            List<Pokemon> healthy = team.getHealthyMembers();
            if (!healthy.isEmpty()) {
                Pokemon best = healthy.stream()
                        .max(Comparator.comparingDouble(Pokemon::getCurrentHealth))
                        .orElse(null);
                if (best != null) {
                    int index = team.getMembers().indexOf(best);
                    team.switchTo(index);
                    System.out.println("Enemy switches to " + team.getActive().getName() + ".");
                    return true;
                }
            }
        }

        return false;
    }

    // wrapper for clarity, separate from player version
    private static boolean ensureEnemyActiveNotFainted(Team enemy) {
        return ensureActiveNotFainted(null, enemy, false);
    }

    private static boolean handlePlayerSwitch(Scanner in, Team team) {
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
}

