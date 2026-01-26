package usecase;

import entity.Group;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortGroupByPositionUseCaseTest {

    @Test
    public void groups_with_close_usecase_pos_should_be_in_same_group() {
        // Arrange: 3 groups with close useCasePos, should be grouped together
        Group group1 = createGroup("001", new Point2D.Double(0, 0), new Point2D.Double(100, 100));
        Group group2 = createGroup("002", new Point2D.Double(10, 10), new Point2D.Double(100, 100));
        Group group3 = createGroup("003", new Point2D.Double(20, 20), new Point2D.Double(100, 100));

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);

        // Act
        SortGroupByPositionUseCase useCase = new SortGroupByPositionUseCase();
        List<List<Group>> result = useCase.sortGroupByPosition(groups);

        // Assert: all groups should be in one group (distance < 0.5 * 100 = 50)
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }

    @Test
    public void groups_with_far_usecase_pos_should_be_in_different_groups() {
        // Arrange: 3 groups with far useCasePos, should be in separate groups
        Group group1 = createGroup("001", new Point2D.Double(0, 0), new Point2D.Double(100, 100));
        Group group2 = createGroup("002", new Point2D.Double(200, 200), new Point2D.Double(100, 100));
        Group group3 = createGroup("003", new Point2D.Double(400, 400), new Point2D.Double(100, 100));

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);

        // Act
        SortGroupByPositionUseCase useCase = new SortGroupByPositionUseCase();
        List<List<Group>> result = useCase.sortGroupByPosition(groups);

        // Assert: each group should be in its own group (distance > 0.5 * 100 = 50)
        assertEquals(3, result.size());
    }

    @Test
    public void groups_with_mixed_distances_should_be_grouped_correctly() {
        // Arrange: 4 groups, 2 close pairs
        Group group1 = createGroup("001", new Point2D.Double(0, 0), new Point2D.Double(200, 100));
        Group group2 = createGroup("002", new Point2D.Double(10, 10), new Point2D.Double(200, 100));
        Group group3 = createGroup("003", new Point2D.Double(500, 500), new Point2D.Double(200, 100));
        Group group4 = createGroup("004", new Point2D.Double(510, 510), new Point2D.Double(200, 100));

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);

        // Act
        SortGroupByPositionUseCase useCase = new SortGroupByPositionUseCase();
        List<List<Group>> result = useCase.sortGroupByPosition(groups);

        // Assert: should have 2 groups, each with 2 members
        // threshold = 0.5 * 200 = 100, distance between close pairs < 100
        assertEquals(2, result.size());
        assertEquals(2, result.get(0).size());
        assertEquals(2, result.get(1).size());
    }

    @Test
    public void single_group_should_return_one_group_with_one_member() {
        // Arrange
        Group group1 = createGroup("001", new Point2D.Double(0, 0), new Point2D.Double(100, 100));

        List<Group> groups = new ArrayList<>();
        groups.add(group1);

        // Act
        SortGroupByPositionUseCase useCase = new SortGroupByPositionUseCase();
        List<List<Group>> result = useCase.sortGroupByPosition(groups);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
    }

    @Test
    public void empty_groups_should_return_empty_result() {
        // Arrange
        List<Group> groups = new ArrayList<>();

        // Act
        SortGroupByPositionUseCase useCase = new SortGroupByPositionUseCase();
        List<List<Group>> result = useCase.sortGroupByPosition(groups);

        // Assert
        assertEquals(0, result.size());
    }

    private Group createGroup(String id, Point2D useCasePos, Point2D eventStormingGeo) {
        Group group = new Group();
        group.setGroupId(id);
        group.setUseCasePos(useCasePos);
        group.setEventStormingGeo(eventStormingGeo);
        return group;
    }
}
