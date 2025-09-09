package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Simple .env loader (no external deps). Lines: KEY=VALUE, supports # comments. */
public class Dotenv {
    private final Map<String,String> map = new HashMap<>();

    public static Dotenv load() {
        return load(Path.of(".env"));
    }

    public static Dotenv load(Path path) {
        Dotenv d = new Dotenv();
        if (Files.exists(path)) {
            try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    int eq = line.indexOf('=');
                    if (eq <= 0) continue;
                    String key = line.substring(0, eq).trim();
                    String val = line.substring(eq + 1).trim();
                    // Remove optional surrounding quotes
                    if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
                        val = val.substring(1, val.length()-1);
                    }
                    d.map.put(key, val);
                }
            } catch (IOException e) {
                System.out.println("[dotenv] Failed reading .env: " + e.getMessage());
            }
        } else {
            System.out.println("[dotenv] .env not found, skipping (you can copy .env.example)");
        }
        return d;
    }

    public String get(String key) {
        return map.get(key);
    }
}