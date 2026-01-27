package adapter;

import org.junit.jupiter.api.Test;
import usecase.GroupToJsonDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GroupToJsonDtoTest {
    @Test
    public void group_can_be_put_into_GroupToJsonDto() {
        StickyNoteProcessor stickyNoteProcessor = new StickyNoteProcessor();
        stickyNoteProcessor.process("./src/test/example3.json");
        List<GroupToJsonDto> groupDtos = stickyNoteProcessor.getGroupToJsonDtos();
        for (GroupToJsonDto groupDto : groupDtos) {
            assertNotNull(groupDto);
        }
    }
}
