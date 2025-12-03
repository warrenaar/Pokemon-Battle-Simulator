import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ReadCSV {

    private ReadCSV() {}

    private static Type parseType(String rawType) {
        if (rawType == null || rawType.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or blank.");
        }
        return Type.valueOf(rawType.trim().toUpperCase());
    }

    public static List<Pokemon> loadPokemonFromFile(String path) throws IOException {

        List<Pokemon> result = new ArrayList<>();
        Map<Type, Integer> perTypeCount = new EnumMap<>(Type.class);

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            List<String> lines = reader.lines()
                    .skip(1)   // skip header
                    .filter(line -> !line.trim().isEmpty())
                    .collect(Collectors.toList());

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue;
                }

                String name = parts[0].trim();
                Type type;
                try {
                    type = parseType(parts[1]);
                } catch (IllegalArgumentException e) {
                    // skip invalid types
                    continue;
                }

                // enforce at most 2 per type
                int currentCount = perTypeCount.getOrDefault(type, 0);
                if (currentCount >= 2) {
                    continue;
                }

                double maxHealth = Double.parseDouble(parts[2].trim());
                double attack = Double.parseDouble(parts[3].trim());
                double defense = Double.parseDouble(parts[4].trim());
                int speed = Integer.parseInt(parts[5].trim());

                Pokemon pokemon = new BattlePokemon(name, type, maxHealth, attack, defense, speed);
                result.add(pokemon);
                perTypeCount.put(type, currentCount + 1);
            }
        }

        return result;
    }
}
