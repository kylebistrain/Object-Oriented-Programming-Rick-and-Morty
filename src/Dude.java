import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Entity{
    public Dude( String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super( id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }


    public Point nextPositionDude(WorldModel model, Point destPos) {
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());

        if (horiz == 0 || model.isOccupied( newPos) && !(model.getOccupancyCell( newPos) instanceof Stump) ) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);

            if (vert == 0 || model.isOccupied( newPos) && !(model.getOccupancyCell( newPos) instanceof Stump)) {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }
}
