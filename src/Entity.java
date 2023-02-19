import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private double actionPeriod;
    private double animationPeriod;
    private int health;
    private int healthLimit;

    public abstract String toString();

    public Entity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.setResourceCount(resourceCount);
        this.setActionPeriod(actionPeriod);
        this.setAnimationPeriod(animationPeriod);
        this.setHealth(health);
        this.setHealthLimit(healthLimit);
    }

    public static double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }


    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getY(), this.imageIndex);
    }
//    public abstract double getAnimationPeriod();
    public double getAnimationPeriod() {
        if (DudeFull.class.equals(this.getClass())|| Rick.class.equals(this.getClass()) || DudeNotFull.class.equals(this.getClass()) || Obstacle.class.equals(this.getClass()) || Fairy.class.equals(this.getClass()) || Sapling.class.equals(this.getClass()) || Tree.class.equals(this.getClass())) {
            return this.getAnimationPeriods();
        }
        throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.getClass()));
    }

//    public  boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this.getHealth() <= 0) {
//            Entity stump = Stump.createStump(WorldModel.getStumpKey() + "_" + this.id, this.position, imageStore.getImageList( WorldModel.getStumpKey()));
//
//            world.removeEntity( scheduler, this);
//
//            world.addEntity( stump);
//
//            return true;
//        }
//
//        return false;
//    }
//    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this instanceof Tree) {
//            return transformTree( world, scheduler, imageStore);
//        } else if (this instanceof Sapling) {
//            return transformSapling(world, scheduler, imageStore);
//        } else {
//            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
//        }
//    }



    public abstract void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore);

    public int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }
    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
    public void nextImage() {
        setImageIndex(this.getImageIndex() + 1);
    }

    public Point getPosition() {
        return position;
    }
    public void setPosition(Point position) {
        this.position = position;
    }
    public List<PImage> getImages() {
        return images;
    }
    public String getId() {
        return id;
    }


    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public double getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(double actionPeriod) {
        this.actionPeriod = actionPeriod;
    }

    public double getAnimationPeriods() {
        return animationPeriod;
    }

    public void setAnimationPeriod(double animationPeriod) {
        this.animationPeriod = animationPeriod;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthLimit() {
        return healthLimit;
    }

    public void setHealthLimit(int healthLimit) {
        this.healthLimit = healthLimit;
    }
}
