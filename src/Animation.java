public class Animation extends Action{
    private int repeatCount;
    public Animation(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        super(entity, world, imageStore);
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler) {
        executeAnimationAction(scheduler);
    }
    public void executeAnimationAction(EventScheduler scheduler) {
        this.getEntity().nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.getEntity(), createAnimationAction(this.getEntity(), Math.max(this.repeatCount - 1, 0)), this.getEntity().getAnimationPeriod());
        }
    }
    public static Animation createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, null, null, repeatCount);
    }
}
