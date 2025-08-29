package app;

import core.FrameService;
import model.Frame;

/**
 * Encapsulates the demo flow: create a frame and then fetch it.
 * Keeping demo logic out of Main improves separation of concerns.
 */
public class FrameExample {
    public static void run(FrameService frames) throws Exception {
        Frame created = frames.createFrame("Java Frame", 100, 200);
        System.out.println("Created: " + created);

        Frame fetched = frames.getFrame(created.getId());
        System.out.println("Fetched: " + fetched);
    }
}
