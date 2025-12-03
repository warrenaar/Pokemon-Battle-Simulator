package pokemonBattleSimulator;

public interface Battle {

	public void standardAttack(); 
	public void specialAttack(); //Elemental modifier to attack?
	public void flee(); //swaps with other selected pokemon?
	public void block(); //I assume this depends on defense or maybe defense+speed?
	
}
