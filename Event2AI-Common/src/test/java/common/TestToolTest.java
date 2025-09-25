package common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.TestTool.checkListOfObject;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestToolTest {
    @Test
    public void Test_for_two_list_int() {
        List<Integer> expectedList = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> actualList = new ArrayList<>(Arrays.asList(3, 1, 2));
        List<Integer> wrongList = new ArrayList<>(Arrays.asList(4, 1, 2));

        assertTrue(checkListOfObject(expectedList, actualList));
        assertFalse(checkListOfObject(expectedList, wrongList));
    }
}
