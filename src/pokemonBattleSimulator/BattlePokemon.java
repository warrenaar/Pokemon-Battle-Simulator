package pokemonBattleSimulator;

public class BattlePokemon extends Pokemon implements Battle{

	public BattlePokemon(String name, Type type, double maxHealth, double currentHealth,
			double attack, double defense, int speed) {
	super(name, type, maxHealth, attack, defense, speed);
	currentHealth = maxHealth;
	
	}
}
