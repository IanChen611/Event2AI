package common;

import java.util.Arrays;
import java.util.List;

public class TestTool {
    public static <T> Boolean checkListOfObject(List<T> expectedGroup, List<T> actualGroup) {
        if(actualGroup.size() != expectedGroup.size()) {
            return false;
        }
        boolean[] isMatch = new boolean[expectedGroup.size()];
        Arrays.fill(isMatch, false);
        for(int i = 0; i < expectedGroup.size(); i++) {
            for (Object actual : actualGroup) {
                if (expectedGroup.get(i).equals(actual)) {
                    isMatch[i] = true;
                    break;
                }
            }
            if(!isMatch[i]) {
                return false;
            }
        }
        return true;
    }
}
