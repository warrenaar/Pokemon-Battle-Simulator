package pokemonBattleSimulator;


public abstract class Pokemon {
	
	protected String name;
	protected Type type;
	protected double maxHealth;
	protected double currentHealth;
	protected double attack;
	protected double defense;
	protected int speed;
	
	public Pokemon(String name, Type type, double maxHealth,
			double attack, double defense, int speed) {
		//name
		if ( name.length() == 0 ||  name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty. ");
		}
		this.name = name;
		
		//type (params that will be read only)
		//maxhealth
		//speed
		
		
		
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public double getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(double currentHealth) {
		if (currentHealth > maxHealth) { 
			throw new IllegalArgumentException("currentHealth cannot exceed maxHealth ");
		}
		this.currentHealth = currentHealth;
	}

	public double getAttack() {
		return attack;
	}

	public void setAttack(double attack) {
		if (attack < 0) { 
			throw new IllegalArgumentException("Attack stat cannot be negative.");
		}
		this.attack = attack;
	}

	public double getDefense() {
		return defense;
	}

	public void setDefense(double defense) {
		if ( defense < 0) { 
			throw new IllegalArgumentException("Defense stat cannot be negative. ");
		}
		this.defense = defense;
	}

	public int getSpeed() {
		return speed;
	}
	
	public String toString()	{
		return String.format("%s: Type: %s, Max Health: %.2f, Current Health: %.2f, Attack: %.2f, Defense: %.2f, Speed: %d",
				name, type, maxHealth, currentHealth, attack, defense, speed );
	}

}
