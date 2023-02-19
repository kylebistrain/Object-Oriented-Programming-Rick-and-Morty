import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity{
    @Override
    public String toString() {
        return "Obstacle";
    }

    public Obstacle(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static Obstacle createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, position, images, 0, 0, 0, animationPeriod, 0, 0);
    }

    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());

    }
}
