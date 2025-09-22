package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class JsonFileCreator {
    private Gson gson;

    public JsonFileCreator() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void create(String path, Object content){
        String jsonString = gson.toJson(content);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
