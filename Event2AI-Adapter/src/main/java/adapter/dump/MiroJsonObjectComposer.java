package adapter.dump;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class MiroJsonObjectComposer {

    public JsonObject compose(JsonObject rawRoot) {
        JsonObject out = new JsonObject();
        copyFetchedAt(rawRoot, out);

        JsonArray stickyNotes = new JsonArray();
        JsonArray tags = new JsonArray();

        normalizeItems(rawRoot, stickyNotes, tags);
        attachTagsToStickyNotes(rawRoot, stickyNotes);

        out.add("stickyNotes", stickyNotes);
        out.add("tags", tags);
        return out;
    }

    private void copyFetchedAt(JsonObject rawRoot, JsonObject target) {
        if (rawRoot == null) return;
        if (rawRoot.has("fetchedAt") && rawRoot.get("fetchedAt").isJsonPrimitive()) {
            target.add("fetchedAt", rawRoot.get("fetchedAt"));
        }
    }

    private void normalizeItems(JsonObject rawRoot, JsonArray stickyNotes, JsonArray tags) {
        if (rawRoot == null || !rawRoot.has("items") || !rawRoot.get("items").isJsonArray()) return;
        for (JsonElement element : rawRoot.getAsJsonArray("items")) {
            if (!element.isJsonObject()) continue;
            JsonObject item = element.getAsJsonObject();
            String type = extractString(item, "type");
            if ("sticky_note".equals(type)) {
                JsonObject normalized = normalizeStickyNote(item);
                if (!normalized.isEmpty()) {
                    stickyNotes.add(normalized);
                }
            } else if ("tag".equals(type)) {
                JsonObject normalized = normalizeTag(item);
                if (!normalized.isEmpty()) {
                    tags.add(normalized);
                }
            }
        }
    }

    private JsonObject normalizeStickyNote(JsonObject raw) {
        JsonObject out = new JsonObject();

        String id = extractString(raw, "id");
        if (id != null) {
            out.addProperty("id", id);
        }

        JsonObject data = getObject(raw, "data");
        if (data != null) {
            String html = extractString(data, "content");
            if (html != null) {
                out.addProperty("html", html);
            }
        }

        JsonObject style = getObject(raw, "style");
        if (style != null) {
            String color = extractString(style, "fillColor");
            if (color != null) {
                out.addProperty("color", color);
            }
        }

        JsonObject geometry = getObject(raw, "geometry");
        if (geometry != null) {
            Double width = extractDouble(geometry, "width");
            if (width != null) {
                out.addProperty("width", width);
            }
            Double height = extractDouble(geometry, "height");
            if (height != null) {
                out.addProperty("height", height);
            }
        }

        JsonObject position = getObject(raw, "position");
        if (position != null) {
            Double x = extractDouble(position, "x");
            if (x != null) {
                out.addProperty("x", x);
            }
            Double y = extractDouble(position, "y");
            if (y != null) {
                out.addProperty("y", y);
            }
        }

        return out;
    }

    private JsonObject normalizeTag(JsonObject raw) {
        JsonObject out = new JsonObject();

        String id = extractString(raw, "id");
        if (id != null) {
            out.addProperty("id", id);
        }

        String name = extractString(raw, "title");
        if (name == null) {
            name = extractString(raw, "name");
        }
        if (name == null) {
            name = extractString(raw, "text");
        }
        if (name != null) {
            out.addProperty("name", name);
        }

        String color = extractString(raw, "color");
        if (color == null) {
            color = extractString(raw, "backgroundColor");
        }
        if (color == null) {
            color = extractString(raw, "fillColor");
        }
        if (color == null) {
            JsonObject style = getObject(raw, "style");
            if (style != null) {
                color = extractString(style, "color");
            }
        }
        if (color != null) {
            out.addProperty("color", color);
        }

        return out;
    }

    private void attachTagsToStickyNotes(JsonObject rawRoot, JsonArray stickyNotes) {
        JsonObject tagsMap = rawRoot != null && rawRoot.has("tagsByItemId") && rawRoot.get("tagsByItemId").isJsonObject()
                ? rawRoot.getAsJsonObject("tagsByItemId")
                : new JsonObject();
        for (JsonElement element : stickyNotes) {
            if (!element.isJsonObject()) continue;
            JsonObject sticky = element.getAsJsonObject();
            String id = extractString(sticky, "id");
            if (id == null) continue;
            JsonArray normalized = new JsonArray();
            if (tagsMap.has(id) && tagsMap.get(id).isJsonArray()) {
                JsonArray rawTags = tagsMap.getAsJsonArray(id);
                for (JsonElement tagElement : rawTags) {
                    if (!tagElement.isJsonObject()) continue;
                    JsonObject normalizedTag = normalizeTag(tagElement.getAsJsonObject());
                    if (!normalizedTag.isEmpty()) {
                        normalized.add(normalizedTag);
                    }
                }
            }
            sticky.add("tags", normalized);
        }
    }

    private JsonObject getObject(JsonObject parent, String key) {
        if (parent == null || key == null) return null;
        if (parent.has(key) && parent.get(key).isJsonObject()) {
            return parent.getAsJsonObject(key);
        }
        return null;
    }

    private String extractString(JsonObject obj, String key) {
        if (obj == null || key == null) return null;
        if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
            try {
                return obj.get(key).getAsString();
            } catch (UnsupportedOperationException ex) {
                return null;
            }
        }
        return null;
    }

    private Double extractDouble(JsonObject obj, String key) {
        if (obj == null || key == null) return null;
        if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
            try {
                return obj.get(key).getAsDouble();
            } catch (UnsupportedOperationException | NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }
}
