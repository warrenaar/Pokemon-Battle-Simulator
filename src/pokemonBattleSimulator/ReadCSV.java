package pokemonBattleSimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {

	private static Type parseType(String rawType) {
		if ( rawType == null || rawType.length() == 0) {
			throw new IllegalArgumentException("Type cannot be null or blank");
		}
		try {
			return Type.valueOf(rawType.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Type is not member of enums");
		}

	}

	//	public static Pokemon parsePokemon(String line) {
	//		String[] parts = line.split(",");
	//
	//		if ( parts.length != 6 ) {
	//			throw new IllegalArgumentException("Invalid number of parts to parse.");
	//		}
	//		
	//		String name = parts[0];
	//		Type type = parseType(parts[1]);
	//		double maxHealth = Double.parseDouble(parts[2]);
	//		double attack = Double.parseDouble(parts[3]);
	//		double defense = Double.parseDouble(parts[4]);
	//		int speed = Integer.parseInt(parts[5]);
	//		
	//	}


	//	public static List<Pokemon> readPokemonCSV(String filePath) {
	//		List<Pokemon> pokemons = new ArrayList<>();
	//		
	//		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	//			String line;
	//			
	//			while((line = reader.readLine()) != null){
	//				Pokemon pokemon = parsePokemon(line);
	//				pokemons.add(pokemon);
	//				
	//			} 
	//			
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//
	//	}

	private static List<Pokemon> loadPokemonFromFile(String path) throws Exception {
		File file = new File(path);

		List<Pokemon> pokemons = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = reader.readLine();

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");

				String name = parts[0];
				Type type = parseType(parts[1]);
				double maxHealth = Double.parseDouble(parts[2]);
				double currentHealth = 0;
				double attack = Double.parseDouble(parts[3]);
				double defense = Double.parseDouble(parts[4]);
				int speed = Integer.parseInt(parts[5]);
				
				Pokemon pokemon = new BattlePokemon(name, type, maxHealth, currentHealth, attack, defense, speed);
				pokemons.add(pokemon);
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not read file: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unexpected error: " + e.getMessage());
		}

		return pokemons;
	}
}