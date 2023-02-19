import processing.core.PImage;

import java.util.List;

public interface executable {

    void execute(EventScheduler scheduler, WorldModel model, ImageStore image);

}
