import processing.core.PImage;

import java.util.List;

public class Sapling extends Transform implements executable{
    private static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final double TREE_ACTION_MAX = 1.400;
    private static final double TREE_ACTION_MIN = 1.000;
    private static final double TREE_ANIMATION_MAX = 0.600;
    private static final double TREE_ANIMATION_MIN = 0.050;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    @Override
    public String toString() {
        return "Sapling";
    }

    public Sapling(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }


    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return transformSapling(world, scheduler, imageStore);
    }


    public void execute(EventScheduler scheduler, WorldModel model, ImageStore image) {
        this.setHealth(this.getHealth() + 1);
        if (!this.transformPlant(model,scheduler,image)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, model, image), this.getActionPeriod());
        }
    }
    public static Sapling createSapling(String id, Point position, List<PImage> images, int health) {
        return new Sapling(id, position, images, 0, 0, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Entity stump = Stump.createStump(WorldModel.getStumpKey() + "_" + this.getId(), this.getPosition(), imageStore.getImageList( WorldModel.getStumpKey()));

            world.removeEntity( scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.getHealth() >= this.getHealthLimit()) {
            Entity tree = Tree.createTree(WorldModel.getTreeKey() + "_" + this.getId(), this.getPosition(), getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN), getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), imageStore.getImageList( WorldModel.getTreeKey()));

            world.removeEntity( scheduler, this);

            world.addEntity(tree);
            ((Tree)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}

//    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this.getHealth() <= 0) {
//            Entity stump = Stump.createStump(WorldModel.getStumpKey() + "_" + this.getId(), this.getPosition(), imageStore.getImageList( WorldModel.getStumpKey()));
//
//            world.removeEntity( scheduler, this);
//
//            world.addEntity(stump);
//
//            return true;
//        } else if (this.getHealth() >= this.getHealthLimit()) {
//            Entity tree = Tree.createTree(WorldModel.getTreeKey() + "_" + this.getId(), this.getPosition(), getNumFromRange(getTreeActionMax(), getTreeActionMin()), getNumFromRange(getTreeAnimationMax(), getTreeAnimationMin()), getIntFromRange(getTreeHealthMax(), getTreeHealthMin()), imageStore.getImageList( WorldModel.getTreeKey()));
//
//            world.removeEntity( scheduler, this);
//
//            world.addEntity( tree);
//            scheduleActions(tree, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//    }
//}