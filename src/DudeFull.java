import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude implements executable{
    @Override
    public String toString() {
        return "DudeFull";
    }

    public DudeFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super( id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }




    public void execute(EventScheduler scheduler, WorldModel model, ImageStore image) {
        Optional<Entity> fullTarget = model.findNearesthouse(this.getPosition(), new ArrayList<>(List.of("House")));

        if (fullTarget.isPresent() && moveToFull(model,this, fullTarget.get(), scheduler)) {
            this.transformFull( model, scheduler,image);
        } else {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, model, image), this.getActionPeriod());
        }
    }
    public static DudeFull createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeFull( id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = DudeNotFull.createDudeNotFull(this.getId(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        ((DudeNotFull)dude).scheduleActions(scheduler, world, imageStore);
    }
    public boolean moveToFull(WorldModel model,Entity dude, Entity target, EventScheduler scheduler) {
        if (model.adjacent(dude.getPosition(), target.getPosition())) {
            return true;
        } else {

            Point nextPos = nextPositionDude(model, target.getPosition());

            if (!dude.getPosition().equals(nextPos)) {
                model.moveEntity(scheduler, dude, nextPos);
            }
            return false;
        }
    }

    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());

    }


}
