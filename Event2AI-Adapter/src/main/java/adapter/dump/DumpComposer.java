package adapter.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DumpComposer {
    private final DumperRegistry registry;

    public DumpComposer(DumperRegistry registry) {
        this.registry = registry;
    }

    public JsonObject compose(JsonObject rawRoot) {
        JsonObject out = new JsonObject();
        out.addProperty("schemaVersion", "1.0.0");
        if (rawRoot.has("fetchedAt")) out.add("fetchedAt", rawRoot.get("fetchedAt"));

        JsonArray stickyNotes = new JsonArray();
        JsonArray tags = new JsonArray();

        if (rawRoot.has("items") && rawRoot.get("items").isJsonArray()) {
            for (JsonElement e : rawRoot.getAsJsonArray("items")) {
                if (!e.isJsonObject()) continue;
                JsonObject item = e.getAsJsonObject();
                String type = item.has("type") && item.get("type").isJsonPrimitive() ? item.get("type").getAsString() : "";
                registry.find(type).ifPresent(d -> {
                    JsonElement piece = d.dump(item);
                    if ("sticky_note".equals(type)) stickyNotes.add(piece);
                    else if ("tag".equals(type)) tags.add(piece);
                });
            }
        }

        // Attach per-item tags to sticky notes if present in rawRoot
        if (rawRoot.has("tagsByItemId") && rawRoot.get("tagsByItemId").isJsonObject()) {
            JsonObject map = rawRoot.getAsJsonObject("tagsByItemId");
            Dump tagDump = registry.find("tag").orElse(null);
            if (tagDump != null) {
                for (JsonElement se : stickyNotes) {
                    if (!se.isJsonObject()) continue;
                    JsonObject sn = se.getAsJsonObject();
                    String id = sn.has("id") && sn.get("id").isJsonPrimitive() ? sn.get("id").getAsString() : null;
                    if (id == null) continue;
                    if (map.has(id) && map.get(id).isJsonArray()) {
                        JsonArray rawTags = map.getAsJsonArray(id);
                        JsonArray normTags = new JsonArray();
                        for (JsonElement te : rawTags) {
                            if (!te.isJsonObject()) continue;
                            JsonElement nt = tagDump.dump(te.getAsJsonObject());
                            normTags.add(nt);
                        }
                        sn.add("tags", normTags);
                    }
                }
            }
        }

        out.add("stickyNotes", stickyNotes);
        out.add("tags", tags);
        return out;
    }
}
