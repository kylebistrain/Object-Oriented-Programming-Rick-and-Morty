import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends Entity implements executable{
    @Override
    public String toString() {
        return "Fairy";
    }

    public Fairy(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }


    public void execute(EventScheduler scheduler, WorldModel model, ImageStore image) {
        Optional<Entity> fairyTarget = model.findNeareststump( this.getPosition(), new ArrayList<>(List.of("Stump")));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveToFairy(model,this, fairyTarget.get(), scheduler)) {

                Entity sapling = Sapling.createSapling(WorldModel.getSaplingKey() + "_" + fairyTarget.get().getId(), tgtPos, image.getImageList( WorldModel.getSaplingKey()), 0);

                model.addEntity(sapling);
                ((Sapling)sapling).scheduleActions(scheduler, model, image);
            }
        }

        scheduler.scheduleEvent(this, Activity.createActivityAction(this, model, image), this.getActionPeriod());
    }
    public static Fairy createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy(id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public Point nextPositionFairy(WorldModel model, Entity entity, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - entity.getPosition().getX());
        Point newPos = new Point(entity.getPosition().getX() + horiz, entity.getPosition().getY());

        if (horiz == 0 || model.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - entity.getPosition().getY());
            newPos = new Point(entity.getPosition().getX(), entity.getPosition().getY() + vert);

            if (vert == 0 || model.isOccupied(newPos)) {
                newPos = entity.getPosition();
            }
        }
        return newPos;
    }
    public boolean moveToFairy(WorldModel model, Entity fairy, Entity target, EventScheduler scheduler) {
        if (model.adjacent(fairy.getPosition(), target.getPosition())) {
            model.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionFairy(model,fairy, target.getPosition());

            if (!fairy.getPosition().equals(nextPos)) {
                model.moveEntity(scheduler, fairy, nextPos);
            }
            return false;
        }
    }

    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());
    }
}
