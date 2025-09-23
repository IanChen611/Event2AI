package dirvers.core;

import adapter.dump.DumpComposer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.Instant;

public class DumpRunner {
    private final BoardGateway gateway;
    private final DumpComposer composer;

    public DumpRunner(BoardGateway gateway, DumpComposer composer) {
        this.gateway = gateway;
        this.composer = composer;
    }

    public DumpResult run(String boardId) throws Exception {
        JsonObject board = gateway.fetchBoard(boardId);
        JsonArray items = gateway.fetchAllItems(boardId);

        JsonObject tagsByItemId = new JsonObject();
        for (JsonElement e : items) {
            if (!e.isJsonObject()) continue;
            JsonObject it = e.getAsJsonObject();
            String type = it.has("type") && it.get("type").isJsonPrimitive() ? it.get("type").getAsString() : "";
            if (!"sticky_note".equals(type)) continue;
            String id = it.has("id") && it.get("id").isJsonPrimitive() ? it.get("id").getAsString() : null;
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
        return new DumpResult(rawRoot, ai);
    }
}
