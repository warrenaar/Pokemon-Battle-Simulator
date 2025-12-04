import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ReadCSV {
    public static List<Pokemon> loadPokemonFromFile(String path) throws IOException {
        List<Pokemon> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            String line = reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length != 6) continue;

                String name = parts[0].trim();
                Type type = Type.valueOf(parts[1].trim().toUpperCase());
                double maxHealth = Double.parseDouble(parts[2].trim());
                double attack = Double.parseDouble(parts[3].trim());
                double defense = Double.parseDouble(parts[4].trim());
                int speed = Integer.parseInt(parts[5].trim());

                Pokemon p = new BattlePokemon(
                        name,
                        type,
                        maxHealth,
                        attack,
                        defense,
                        speed
                );
                result.add(p);
            }
        }
        return result;
    }
}
