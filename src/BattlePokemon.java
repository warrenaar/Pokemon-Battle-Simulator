import java.util.Random;

public class BattlePokemon extends Pokemon implements Battle {

    private static final Random RNG = new Random();

    public BattlePokemon(String name, Type type, double maxHealth,
                         double attack, double defense, int speed) {
        super(name, type, maxHealth, attack, defense, speed);
    }

    @Override
    public String battleCry() {
        return name + " is ready to battle.";
    }

    @Override
    public void standardAttack(BattlePokemon target) {
        if (this.isFainted()) {
            System.out.println(name + " has fainted and cannot attack.");
            return;
        }
        if (target.isFainted()) {
            System.out.println(target.name + " has already fainted.");
            return;
        }

        double typeMultiplier = this.type.effectivenessAgainst(target.type);
        double base = this.attack - target.defense / 2.0;
        if (base < 1) {
            base = 1;
        }

        double randomFactor = 0.85 + (RNG.nextDouble() * 0.30); // between 0.85 and 1.15
        double rawDamage = base * typeMultiplier * randomFactor;
        int damage = (int) Math.round(rawDamage);
        if (damage < 1) {
            damage = 1;
        }

        target.takeDamage(damage);

        System.out.println(name + " attacks " + target.name + " for " + damage + " damage.");

        if (typeMultiplier > 1.0) {
            System.out.println("It is super effective.");
        } else if (typeMultiplier == 0.0) {
            System.out.println("It has no effect.");
        } else if (typeMultiplier < 1.0) {
            System.out.println("It is not very effective.");
        }

        if (target.isFainted()) {
            System.out.println(target.name + " fainted.");
        }
    }
}
