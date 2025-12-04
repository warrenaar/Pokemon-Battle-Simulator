import java.util.Random;

public class BattlePokemon extends Pokemon implements Battle {

    private static final Random RNG = new Random();

    private double totalDamageDealt = 0;

    public BattlePokemon(String name, Type type, double maxHealth,
                         double attack, double defense, int speed) {

        super(name, type, maxHealth, attack, defense, speed);
    }

    // called at the start of each battle
    public void resetDamageDealt() {
        totalDamageDealt = 0;
    }

    public double getTotalDamageDealt() {
        return totalDamageDealt;
    }

    @Override
    public String battleCry() {
        return name + " is ready to battle.";
    }

    @Override
    public void standardAttack(BattlePokemon target) {

        double base = attack - target.defense / 2.0;
        double typeMultiplier = this.type.effectivenessAgainst(target.type);
        double randomFactor = 0.85 + (RNG.nextDouble() * 0.3);

        double damage = (base * typeMultiplier * randomFactor);
        if (damage < 1) damage = 1;

        // track damage dealt
        totalDamageDealt += damage;

        target.setCurrentHealth(target.getCurrentHealth() - damage);

        System.out.printf("%s used Standard Attack on %s for %.1f damage.", name, target.name, damage );

        if (typeMultiplier > 1) {
            System.out.println(" It is super effective.");
        } else if (typeMultiplier < 1 && typeMultiplier > 0) {
            System.out.println(" It is not very effective.");
        } else if (typeMultiplier == 0) {
            System.out.println(" It has no effect.");
        }

        if (target.isFainted()) {
            System.out.println(target.name + " fainted.");
        }
    }

    @Override
    public void coinFlipAttack(BattlePokemon target) {

        System.out.println(this.name + " used Coin Flip Attack.");

        boolean hits = RNG.nextBoolean();

        if (!hits) {
            System.out.println("Coin flip failed. No damage dealt.");
            return;
        }

        System.out.println("Coin flip succeeded.");

        // base power is stronger than standard attack
        double base = this.attack * 1.5 - target.defense / 2.0;

        // apply type effectiveness
        double typeMultiplier = this.type.effectivenessAgainst(target.type);

        // same random variance as standard attack
        double randomFactor = 0.85 + (RNG.nextDouble() * 0.3);

        double damage = (base * typeMultiplier * randomFactor);
        if (damage < 1) {
        	damage = 1;
        }

        totalDamageDealt += damage;

        target.setCurrentHealth(target.getCurrentHealth() - damage);

        System.out.println(target.name + " took " + damage + " damage.");

        // effectiveness messages
        if (typeMultiplier > 1) {
            System.out.println("It is super effective.");
        } else if (typeMultiplier < 1 && typeMultiplier > 0) {
            System.out.println("It is not very effective.");
        } else if (typeMultiplier == 0) {
            System.out.println("It has no effect.");
        }

        if (target.isFainted()) {
            System.out.println(target.name + " fainted.");
        }
    }
}
