import java.util.*;

/**
 * An action that can be taken by an entity
 */
public abstract class Action {
    private Entity entity;

    public WorldModel getWorld() {
        return world;
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    private WorldModel world;
    private ImageStore imageStore;

    public Action(Entity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
//    public void executeActivityAction(EventScheduler scheduler) {
//        switch (this.entity.getKind()) {
//            case SAPLING:
//                executeSaplingActivity(scheduler);
//                break;
//            case TREE:
//                executeTreeActivity(scheduler);
//                break;
//            case FAIRY:
//                executeFairyActivity(scheduler);
//                break;
//            case DUDE_NOT_FULL:
//                executeDudeNotFullActivity(scheduler);
//                break;
//            case DUDE_FULL:
//                executeDudeFullActivity(scheduler);
//                break;
//            default:
//                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.entity.getKind()));
//        }
//    }

    public abstract void executeAction(EventScheduler scheduler);
//    public void executeAction( EventScheduler scheduler) {
//        switch (this.kind) {
//            case ACTIVITY:
//                executeActivityAction(scheduler);
//                break;
//
//            case ANIMATION:
//                executeAnimationAction(scheduler);
//                break;
//        }
//    }

//    public void executeFairyActivity(EventScheduler scheduler) {
//        Optional<Entity> fairyTarget = this.world.findNearest( this.entity.getPosition(), new ArrayList<>(List.of(EntityKind.STUMP)));
//
//        if (fairyTarget.isPresent()) {
//            Point tgtPos = fairyTarget.get().getPosition();
//
//            if (this.world.moveToFairy(this.entity, fairyTarget.get(), scheduler)) {
//
//                Entity sapling = Entity.createSapling(WorldModel.getSaplingKey() + "_" + fairyTarget.get().getId(), tgtPos, this.imageFStore.getImageList( WorldModel.getSaplingKey()), 0);
//
//                this.world.addEntity(sapling);
//                scheduler.scheduleActions(sapling, this.world, this.imageStore);
//            }
//        }
//
//        scheduler.scheduleEvent(this.entity, Activity.createActivityAction(this.entity, this.world, this.imageStore), this.entity.getActionPeriod());
//    }
//    public void executeDudeNotFullActivity(EventScheduler scheduler) {
//        Optional<Entity> target = this.world.findNearest(this.entity.getPosition(), new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));
//
//        if (target.isEmpty() || !this.world.moveToNotFull(this.entity, target.get(), scheduler) || !this.entity.transformNotFull( this.world, scheduler, this.imageStore)) {
//            scheduler.scheduleEvent(this.entity, Activity.createActivityAction(this.entity, this.world, this.imageStore), this.entity.getActionPeriod());
//        }
//    }
//    public void executeDudeFullActivity(EventScheduler scheduler) {
//        Optional<Entity> fullTarget = this.world.findNearest(this.entity.getPosition(), new ArrayList<>(List.of(EntityKind.HOUSE)));
//
//        if (fullTarget.isPresent() && this.world.moveToFull(this.entity, fullTarget.get(), scheduler)) {
//            this.entity.transformFull( this.world, scheduler,this.imageStore);
//        } else {
//            scheduler.scheduleEvent(this.entity, Activity.createActivityAction(this.entity, this.world, this.imageStore), this.entity.getActionPeriod());
//        }
//    }
//    public void executeTreeActivity(EventScheduler scheduler) {
//
//        if (!this.entity.transformPlant(this.world,scheduler,this.imageStore)) {
//
//            scheduler.scheduleEvent(this.entity, Activity.createActivityAction(this.entity, this.world, this.imageStore), this.entity.getActionPeriod());
//        }
//    }


    public Entity getEntity() {
        return entity;
    }

}
