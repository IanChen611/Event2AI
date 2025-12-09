package dirvers.core;

import adapter.dump.MiroJsonObjectComposer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiControllerTest {

    @Test
    void runConstructsRawDumpAndDelegatesToComposer() throws Exception {
        StubGateway gateway = new StubGateway();
        RecordingComposer composer = new RecordingComposer();

        ApiController controller = new ApiController(gateway, composer);

        MiroJsonResult result = controller.run("board-123");

        assertEquals("board-123", gateway.fetchBoardCall);
        assertEquals("board-123", gateway.fetchItemsCall);
        assertEquals(Collections.singletonList("board-123:stick-1"), gateway.fetchTagCalls);

        JsonObject rawRoot = result.getRawRoot();
        assertSame(rawRoot, composer.capturedRawRoot);
        assertEquals(2, rawRoot.get("itemCount").getAsInt());
        assertTrue(rawRoot.has("fetchedAt"));
        assertEquals("board-123", rawRoot.getAsJsonObject("board").get("id").getAsString());
        assertEquals(2, rawRoot.getAsJsonArray("items").size());

        JsonObject tagsByItemId = rawRoot.getAsJsonObject("tagsByItemId");
        assertTrue(tagsByItemId.has("stick-1"));
        assertEquals(1, tagsByItemId.getAsJsonArray("stick-1").size());
        assertEquals("tag-1", tagsByItemId.getAsJsonArray("stick-1").get(0).getAsJsonObject().get("id").getAsString());
        assertFalse(tagsByItemId.has("shape-1"));

        assertSame(composer.lastOutput, result.getAiresult());

        JsonObject aiDump = result.getAiresult();
        assertFalse(aiDump.has("schemaVersion"));
        JsonArray stickyNotes = aiDump.getAsJsonArray("stickyNotes");
        assertEquals(1, stickyNotes.size());
        JsonObject sticky = stickyNotes.get(0).getAsJsonObject();
        assertFalse(sticky.has("text"));
        assertEquals("stick-1", sticky.get("id").getAsString());
        assertTrue(sticky.has("tags"));
        assertEquals("tag-1", sticky.getAsJsonArray("tags").get(0).getAsJsonObject().get("id").getAsString());

        JsonArray tags = aiDump.getAsJsonArray("tags");
        assertEquals(0, tags.size());
    }

    private static class StubGateway implements BoardGateway {
        private final JsonObject board = new JsonObject();
        private final JsonArray items = new JsonArray();
        private final JsonArray stickyTags = new JsonArray();
        private String fetchBoardCall;
        private String fetchItemsCall;
        private final List<String> fetchTagCalls = new ArrayList<>();

        StubGateway() {
            board.addProperty("id", "board-123");

            JsonObject sticky = new JsonObject();
            sticky.addProperty("id", "stick-1");
            sticky.addProperty("type", "sticky_note");
            items.add(sticky);

            JsonObject shape = new JsonObject();
            shape.addProperty("id", "shape-1");
            shape.addProperty("type", "shape");
            items.add(shape);

            JsonObject tag = new JsonObject();
            tag.addProperty("id", "tag-1");
            stickyTags.add(tag);
        }

        @Override
        public JsonObject fetchBoard(String boardId) {
            this.fetchBoardCall = boardId;
            return board.deepCopy();
        }

        @Override
        public JsonArray fetchAllItems(String boardId) {
            this.fetchItemsCall = boardId;
            return items.deepCopy();
        }

        @Override
        public JsonArray fetchTagsForItem(String boardId, String itemId) {
            fetchTagCalls.add(boardId + ":" + itemId);
            if ("stick-1".equals(itemId)) {
                return stickyTags.deepCopy();
            }
            return new JsonArray();
        }
    }

    private static class RecordingComposer extends MiroJsonObjectComposer {
        private JsonObject capturedRawRoot;
        private JsonObject lastOutput;

        @Override
        public JsonObject compose(JsonObject rawRoot) {
            this.capturedRawRoot = rawRoot;
            this.lastOutput = super.compose(rawRoot);
            return lastOutput;
        }
    }
}

