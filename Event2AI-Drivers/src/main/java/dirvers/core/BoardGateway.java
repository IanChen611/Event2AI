package dirvers.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface BoardGateway {
    JsonObject fetchBoard(String boardId) throws Exception;
    JsonArray fetchAllItems(String boardId) throws Exception;
    JsonArray fetchTagsForItem(String boardId, String itemId) throws Exception;
}
