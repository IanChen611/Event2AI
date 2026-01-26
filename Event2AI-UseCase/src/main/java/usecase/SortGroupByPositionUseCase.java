package usecase;

import entity.Group;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class SortGroupByPositionUseCase {

    SortGroupByPositionUseCase() {}

    public List<List<Group>> sortGroupByPosition(List<Group> groups) {
        // compute threshold
        double thresholdX = groups.stream()
                .map(Group::getEventStormingGeo)
                .mapToDouble(Point2D::getX)
                .max()
                .orElse(0);

        double multiple = 0.5;
        double distanceThreshold = multiple * thresholdX;

        List<List<Group>> result = new ArrayList<>();

        for (Group group : groups) {
            Point2D currentPos = group.getUseCasePos();
            boolean addedToExistingGroup = false;

            // Check if you can join an existing group.
            for (List<Group> existingGroup : result) {
                // Use the position of the first group in the group as the representative.
                Point2D groupPos = existingGroup.get(0).getUseCasePos();
                double distance = currentPos.distance(groupPos);

                if (distance < distanceThreshold) {
                    existingGroup.add(group);
                    addedToExistingGroup = true;
                    break;
                }
            }

            // If no suitable group is available, create a new group.
            if (!addedToExistingGroup) {
                List<Group> newGroup = new ArrayList<>();
                newGroup.add(group);
                result.add(newGroup);
            }
        }

        return result;
    }
}