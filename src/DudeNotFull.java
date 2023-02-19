import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude implements executable{
    @Override
    public String toString() {
        return "DudeNotFull";
    }

    public DudeNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super( id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public void execute(EventScheduler scheduler, WorldModel model, ImageStore image) {
        Optional<Entity> target = model.findNearesttree(this.getPosition(), new ArrayList<>(Arrays.asList("Tree", "Sapling")));

        if (target.isEmpty() || !moveToNotFull(model, target.get(), scheduler) || !this.transformNotFull( model, scheduler, image)) {
              scheduler.scheduleEvent( this,Activity.createActivityAction(this, model, image), this.getActionPeriod());
        }
    }
    public static DudeNotFull createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeNotFull( id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public boolean transformNotFull( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Entity dude = DudeFull.createDudeFull(this.getId(), this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), this.getImages());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity(dude);
            ((DudeFull)dude).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public boolean moveToNotFull(WorldModel model, Entity target, EventScheduler scheduler) {
        if (model.adjacent(this.getPosition(), target.getPosition())) {
            this.setResourceCount(this.getResourceCount() + 1);
            target.setHealth(target.getHealth() - 1);
            return true;
        } else {
            Point nextPos = nextPositionDude(model, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                model.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
