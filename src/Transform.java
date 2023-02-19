import processing.core.PImage;

import java.util.List;

public abstract class Transform extends Entity{
    public Transform(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super( id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public abstract boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
//    public  boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this instanceof Tree) {
//            return transformTree( world, scheduler, imageStore);
//        } else if (this instanceof Sapling) {
//            return transformSapling(world, scheduler, imageStore);
//        } else {
//            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
//        }
//    }
}
