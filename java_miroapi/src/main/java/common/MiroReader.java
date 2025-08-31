package common;

import com.google.gson.*;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.util.Optional;


public class MiroReader {
    public MiroReader() {}

    public Optional<JsonArray> readBoardData(String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            return Optional.of(JsonParser.parseReader(reader).getAsJsonObject().get("data").getAsJsonArray());
        } catch (IOException e) {
            System.out.println("Error reading file " + fileName + ": " + e.getMessage());
        }
        return Optional.empty();
    }
}
