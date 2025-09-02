package core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import common.MiroReader;
import model.MiroItem;
import java.util.ArrayList;
import java.util.Optional;

public class MiroProcessor {
    private final MiroReader miroReader;

    public MiroProcessor() {
        miroReader = new MiroReader();
    }

    public ArrayList<MiroItem> processFile(String filePath){
        ArrayList<MiroItem> miroItems = new ArrayList<>();

        Optional<JsonArray> items = miroReader.readBoardItem(filePath);
        if(items.isPresent()){
            for(int i = 0; i < items.get().size(); i++){
                MiroItem miroItem = new MiroItem(items.get().get(i).getAsJsonObject().get("id").getAsString(),
                        items.get().get(i).getAsJsonObject().get("type").getAsString(),
                        items.get().get(i).getAsJsonObject().get("data").getAsJsonObject(),
                        Optional.ofNullable(items.get().get(i).getAsJsonObject().get("style")).orElse(new JsonObject()).getAsJsonObject(),
                        items.get().get(i).getAsJsonObject().get("geometry").getAsJsonObject(),
                        items.get().get(i).getAsJsonObject().get("position").getAsJsonObject());
                miroItems.add(miroItem);
            }
        }
        else{
            System.out.println("No items found");
        }

        return miroItems;
    }
}
