package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileCreator {
    private Gson gson;

    public JsonFileCreator() {
        gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    }

    public void create(String path, Object content) {
        ensureParentDirectoryExists(path);

        String jsonString = gson.toJson(content);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create json file: " + path, e);
        }
    }

    private void ensureParentDirectoryExists(String path) {
        File file = new File(path);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}
