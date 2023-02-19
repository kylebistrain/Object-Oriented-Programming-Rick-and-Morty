public class Activity extends Action{
    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore);
    }
    public void executeAction(EventScheduler scheduler) {
        executeActivityAction(scheduler);
    }
    public static Activity createActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }

    public void executeActivityAction(EventScheduler scheduler) {
        if(this.getEntity() instanceof Sapling) {
            ((Sapling)this.getEntity()).execute(scheduler, this.getWorld(),this.getImageStore());
        } else if(this.getEntity() instanceof Tree) {
            ((Tree)this.getEntity()).execute(scheduler, this.getWorld(),this.getImageStore());
        } else if(this.getEntity() instanceof Fairy) {
            ((Fairy)this.getEntity()).execute(scheduler, this.getWorld(),this.getImageStore());
        } else if(this.getEntity() instanceof DudeFull) {
            ((DudeFull)this.getEntity()).execute(scheduler, this.getWorld(),this.getImageStore());
        } else if(this.getEntity() instanceof DudeNotFull) {
            ((DudeNotFull)this.getEntity()).execute(scheduler, this.getWorld(),this.getImageStore());
        } else if (this.getEntity() instanceof Rick) {
            ((Rick)this.getEntity()).execute(scheduler, this.getWorld(),this.getImageStore());
        }
        else {
            throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.getEntity().getClass()));
        }
    }
}
