package adapter;

import com.google.gson.JsonArray;
import entity.Group;
import entity.StickyNote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import usecase.GroupStickyNotesUseCase;
import usecase.GroupToJsonDto;
import usecase.JsonToStickyNote;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StickyNoteProcessorTest {
    private FackStickyNoteProcessor stickyNoteProcessor;

    @BeforeEach
    public void setUp() {
        stickyNoteProcessor = new FackStickyNoteProcessor("./src/test/example.json");
    }

    @Test
    public void JsonToStickyNote_can_return_StickyNote(){
        JsonReader jsonReader = new JsonReader();
        JsonArray jsonItems = jsonReader.readBoardItem("./src/test/example.json").get("stickyNotes").getAsJsonArray();
        ArrayList<StickyNote> items = JsonToStickyNote.convert(jsonItems);

        assertEquals("3458764640206555736", items.get(0).getId());
        assertEquals("Board Created\n", items.get(0).getDescription());
        assertEquals("orange", items.get(0).getColor());
        assertEquals(new Point2D.Double(1903.7814147890028, -386.40794964363045), items.get(0).getPos());
        assertEquals(new Point2D.Double(212.93, 243.96), items.get(0).getGeo());
        assertEquals("", items.get(0).getTag());
    }

    @Test
    public void JsonToStickyNote_get_empty_JsonArray_should_return_empty_ArrayList(){
        JsonArray jsonItems = new JsonArray();
        ArrayList<StickyNote> items = JsonToStickyNote.convert(jsonItems);
        assertEquals(new ArrayList<>(), items);
    }

    @Test
    public void StickyNoteProcessor_can_processFile() {
        List<StickyNote> processedItems = stickyNoteProcessor.processFile("./src/test/example.json");

        assertEquals("3458764640206555736", processedItems.get(0).getId());
        assertEquals("Board Created\n", processedItems.get(0).getDescription());
        assertEquals("orange", processedItems.get(0).getColor());
        assertEquals(new Point2D.Double(1903.7814147890028, -386.40794964363045), processedItems.get(0).getPos());
        assertEquals(new Point2D.Double(212.93, 243.96), processedItems.get(0).getGeo());
        assertEquals("", processedItems.get(0).getTag());
    }

    @Test
    public void StickyNoteProcessor_can_processStickyNotesToGroup(){
        List<StickyNote> processedItems = stickyNoteProcessor.processFile("./src/test/example2.json");
        List<Group> groups = stickyNoteProcessor.processStickyNotesToGroup(processedItems);

        assertEquals(1, groups.size());
    }

    @Test
    public void StickyNoteProcessor_can_processGroupToJsonDto(){
        List<StickyNote> processedItems = stickyNoteProcessor.processFile("./src/test/example2.json");
        List<Group> groups = stickyNoteProcessor.processStickyNotesToGroup(processedItems);
        List<GroupToJsonDto> dtos = stickyNoteProcessor.processGroupToJsonDto(groups);

        assertEquals(1, dtos.size());
    }

    public class FackStickyNoteProcessor {
        private List<StickyNote> stickyNotes;
        private List<Group> groups;
        private List<GroupToJsonDto> groupToJsonDtos;

        public FackStickyNoteProcessor(String filePath) {
        }

        public List<StickyNote> processFile(String filePath){
            JsonReader jsonReader = new JsonReader();
            JsonArray jsonItems = jsonReader.readBoardItem(filePath).get("stickyNotes").getAsJsonArray();
            List<StickyNote> items = JsonToStickyNote.convert(jsonItems);

            return items;
        }

        public List<Group> processStickyNotesToGroup(List<StickyNote> stickyNotes){
            GroupStickyNotesUseCase groupStickyNotesUseCase = new GroupStickyNotesUseCase(stickyNotes);
            return groupStickyNotesUseCase.getGroups();
        }

        public List<GroupToJsonDto> processGroupToJsonDto(List<Group> groups){
            List<GroupToJsonDto> groupToJsonDtos = new ArrayList<>();
            for(Group group : groups){
                GroupToJsonDto groupToJsonDto = new GroupToJsonDto(group);
                groupToJsonDtos.add(groupToJsonDto);
            }
            return groupToJsonDtos;
        }

        public List<GroupToJsonDto> getGroupToJsonDtos(){
            return groupToJsonDtos;
        }
    }
}
