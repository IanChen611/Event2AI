package adapter;

import com.google.gson.*;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;


public class JsonReader {
    public JsonReader() {}

    public JsonObject readBoardItem(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            System.out.println("reading file error: " + e.getMessage());
            return new JsonObject();
        }
    }
}
