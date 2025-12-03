package pokemonBattleSimulator;

public class BattlePokemon extends Pokemon implements Battle{

	public BattlePokemon(String name, Type type, double maxHealth, double currentHealth,
			double attack, double defense, int speed) {
		super(name, type, maxHealth, attack, defense, speed);
		currentHealth = maxHealth;

	}

	@Override
	public void standardAttack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void specialAttack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flee() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void block() {
		// TODO Auto-generated method stub
		
	}

	


}
