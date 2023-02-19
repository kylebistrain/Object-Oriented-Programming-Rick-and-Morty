import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Rick extends Entity implements executable{
    private Point portal;
    public Rick(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
        portal = position;

    }

    @Override
    public String toString() {
        return "rick";
    }

    @Override
    public void scheduleActions(EventScheduler evt, WorldModel world, ImageStore imageStore) {
        evt.scheduleEvent( this, Activity.createActivityAction(this, world, imageStore), this.getActionPeriod());
        evt.scheduleEvent( this, Animation.createAnimationAction(this, 0), this.getAnimationPeriod());

    }

    public static Rick createRick(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Rick( id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }


    public void execute(EventScheduler scheduler, WorldModel model, ImageStore image) {

    Optional<Entity> fullTarget = model.findNearestdude(this.getPosition());

        if (fullTarget.isPresent() && move(model, fullTarget.get(), scheduler)) {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, model, image), this.getActionPeriod());


        } else {
            scheduler.scheduleEvent(this, Activity.createActivityAction(this, model, image), this.getActionPeriod());
        }
    }


    public boolean move(WorldModel model, Entity target, EventScheduler scheduler){
        if (model.adjacent(this.getPosition(), target.getPosition())) {
            model.moveEntity(scheduler,target,portal);
            model.addDudelist(target);
            return true;
        } else {
            Point nextPos = nextPositionRick(model, target.getPosition());
            if (!this.getPosition().equals(nextPos)) {
                model.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionRick(WorldModel model, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());

        if (horiz == 0 || model.isOccupied( newPos) && (!(model.getOccupancyCell( newPos) instanceof DudeFull) || !(model.getOccupancyCell(newPos) instanceof DudeNotFull))) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);

            if (vert == 0 || model.isOccupied( newPos) && (!(model.getOccupancyCell( newPos) instanceof DudeFull) || !(model.getOccupancyCell(newPos) instanceof DudeNotFull))) {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

    }

