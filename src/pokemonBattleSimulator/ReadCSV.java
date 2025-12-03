package pokemonBattleSimulator;

public class ReadCSV {

	public static Type parseType(String rawType) {
		if ( rawType == null || rawType.length() == 0) {
			throw new IllegalArgumentException("Type cannot be null or blank");
		}
		try {
			return Type.valueOf(rawType.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Type is not member of enums");
		}
		
	}
	
	public static Pokemon readPokemon(String line) {
		
		
		
		
	}
	
	
	
}
