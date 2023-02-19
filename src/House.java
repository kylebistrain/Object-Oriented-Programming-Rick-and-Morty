import processing.core.PImage;

import java.util.List;

public class House extends Entity{
    @Override
    public String toString() {
        return "House";
    }

    public House(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    @Override
    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {

    }


    public static House createHouse(String id, Point position, List<PImage> images) {
        return new House( id, position, images, 0, 0, 0, 0, 0, 0);
    }
}
