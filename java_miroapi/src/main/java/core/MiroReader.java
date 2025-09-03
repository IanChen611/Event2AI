package core;

import com.google.gson.*;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.util.Optional;


public class MiroReader {
    public MiroReader() {}

    public Optional<JsonArray> readBoardItem(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            return Optional.of(JsonParser.parseReader(reader).getAsJsonObject().get("items").getAsJsonArray());
        } catch (IOException e) {
            System.out.println("reading file error: " + e.getMessage());
        }
        return Optional.empty();
    }
}
