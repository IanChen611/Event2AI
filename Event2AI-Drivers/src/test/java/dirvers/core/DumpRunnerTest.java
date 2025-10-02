package dirvers.core;

import adapter.dump.DumpComposer;
import adapter.dump.DumperRegistry;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DumpRunnerTest {

    @Test
    void runConstructsRawDumpAndDelegatesToComposer() throws Exception {
        StubGateway gateway = new StubGateway();
        RecordingComposer composer = new RecordingComposer();

        DumpRunner runner = new DumpRunner(gateway, composer);

        DumpResult result = runner.run("board-123");

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

        assertSame(composer.output, result.getAiDump());
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

    private static class RecordingComposer extends DumpComposer {
        private JsonObject capturedRawRoot;
        private final JsonObject output = new JsonObject();

        RecordingComposer() {
            super(new DumperRegistry(Collections.emptyList()));
            output.addProperty("status", "ok");
        }

        @Override
        public JsonObject compose(JsonObject rawRoot) {
            this.capturedRawRoot = rawRoot;
            return output;
        }
    }
}
