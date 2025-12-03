import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ReadCSV {

    private ReadCSV() {
        // utility class
    }

    private static Type parseType(String rawType) {
        if (rawType == null || rawType.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or blank.");
        }
        try {
            return Type.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type " + rawType + " is not a valid enum value.");
        }
    }

    public static List<Pokemon> loadPokemonFromFile(String path) throws IOException {
        List<Pokemon> pokedex = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine(); // header

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 6) {
                    throw new IllegalArgumentException("Invalid line, expected 6 parts: " + line);
                }

                String name = parts[0].trim();
                Type type = parseType(parts[1]);
                double maxHealth = Double.parseDouble(parts[2].trim());
                double attack = Double.parseDouble(parts[3].trim());
                double defense = Double.parseDouble(parts[4].trim());
                int speed = Integer.parseInt(parts[5].trim());

                Pokemon pokemon = new BattlePokemon(name, type, maxHealth, attack, defense, speed);
                pokedex.add(pokemon);
            }
        }

        return pokedex;
    }
}
