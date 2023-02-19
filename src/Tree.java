import processing.core.PImage;

import java.util.List;

public class Tree extends Transform implements executable{
    @Override
    public String toString() {
        return "Tree";
    }

    public Tree(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    @Override
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return transformTree(world, scheduler, imageStore);
    }

    public void execute(EventScheduler scheduler, WorldModel model, ImageStore image) {
        if (!transformPlant(model,scheduler,image)) {

            scheduler.scheduleEvent(this, Activity.createActivityAction(this, model, image), this.getActionPeriod());
        }
    }
    public static Tree createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, images, 0, 0, actionPeriod, animationPeriod, health, 0);
    }
    @Override
    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
    public  boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Entity stump = Stump.createStump(WorldModel.getStumpKey() + "_" + this.getId(), this.getPosition(), imageStore.getImageList( WorldModel.getStumpKey()));
            world.removeEntity( scheduler, this);
            world.addEntity( stump);

            return true;
        }

        return false;
    }
}
