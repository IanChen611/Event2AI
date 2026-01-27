package adapter;

import com.google.gson.JsonObject;
import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.GroupToJsonDto;
import valueobject.DomainEvent;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonFileCreatorTest {
    private JsonFileCreator jsonFileCreator;

    @BeforeEach
    public void setUp() {
        jsonFileCreator = new JsonFileCreator();
    }

    @Test
    public void create_a_json_file() {
        StickyNote stickyNote = new StickyNote("111", "0", new Point2D.Double(0, 0), new Point2D.Double(0, 0), "0");

        jsonFileCreator.create("./src/test/output.json", stickyNote);

        JsonReader jsonReader = new JsonReader();
        JsonObject items = jsonReader.readBoardItem("./src/test/output.json");
        assertFalse(items.isEmpty());
    }

    @Test
    public void create_file_by_GroupToJsonDto() {
        StickyNoteProcessor stickyNoteProcessor = new StickyNoteProcessor();
        stickyNoteProcessor.process("./src/test/example3.json");
        List<GroupToJsonDto> groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
        for (GroupToJsonDto groupDto : groupDtos) {
            jsonFileCreator.create("./src/test/" + groupDto.getUsecaseName().replace(" ", "_").trim() + ".json", groupDto);
        }

        JsonReader jsonReader = new JsonReader();
        JsonObject item1 = jsonReader.readBoardItem("./src/test/Create_Board.json");
        assertFalse(item1.isEmpty());
        JsonObject item2= jsonReader.readBoardItem("./src/test/Create_Board2.json");
        assertFalse(item2.isEmpty());
        JsonObject item3 = jsonReader.readBoardItem("./src/test/Create_Board3.json");
        assertFalse(item3.isEmpty());
    }
}
