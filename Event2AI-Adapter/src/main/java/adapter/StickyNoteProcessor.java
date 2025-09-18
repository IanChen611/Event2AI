package adapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.StickyNote;
import usecase.JsonToStickyNote;

import java.util.ArrayList;

public class StickyNoteProcessor {
    private final JsonReader jsonReader;

    public StickyNoteProcessor() {
        jsonReader = new JsonReader();
    }

    public ArrayList<StickyNote> processFile(String filePath){
        JsonArray jsonItems = jsonReader.readBoardItem(filePath).get("stickyNotes").getAsJsonArray();
        ArrayList<StickyNote> items = JsonToStickyNote.convert(jsonItems);

        return items;
    }
}
