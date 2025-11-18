package dirvers.core;

import adapter.dump.MiroJsonObjectComposer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Instant;

/**
 * Acts as a thin controller coordinating gateway calls and JSON composition.
 */
public class ApiController {
    private final BoardGateway gateway;
    private final MiroJsonObjectComposer composer;

    public ApiController(String accessToken) {
        this(new MiroBoardGateway(accessToken), new MiroJsonObjectComposer());
    }

    ApiController(BoardGateway gateway, MiroJsonObjectComposer composer) {
        this.gateway = gateway;
        this.composer = composer;
    }

    public MiroJsonResult run(String boardId) throws Exception {
        JsonObject board = gateway.fetchBoard(boardId);
        JsonArray items = gateway.fetchAllItems(boardId);

        JsonObject tagsByItemId = new JsonObject();
        for (JsonElement element : items) {
            if (!element.isJsonObject()) continue;
            JsonObject item = element.getAsJsonObject();
            String type = item.has("type") && item.get("type").isJsonPrimitive() ? item.get("type").getAsString() : "";
            if (!"sticky_note".equals(type)) continue;
            String id = item.has("id") && item.get("id").isJsonPrimitive() ? item.get("id").getAsString() : null;
            if (id == null) continue;
            JsonArray tags = gateway.fetchTagsForItem(boardId, id);
            tagsByItemId.add(id, tags);
        }

        JsonObject rawRoot = new JsonObject();
        rawRoot.addProperty("fetchedAt", Instant.now().toString());
        rawRoot.add("board", board);
        rawRoot.add("items", items);
        rawRoot.addProperty("itemCount", items.size());
        rawRoot.add("tagsByItemId", tagsByItemId);

        JsonObject ai = composer.compose(rawRoot);
        return new MiroJsonResult(rawRoot, ai);
    }
}

