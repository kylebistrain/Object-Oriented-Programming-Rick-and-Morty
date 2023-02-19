/**
 * An event is made up of an Entity that is taking an
 * Action a specified time.
 */
public final class Event {
    private Action action;
    private double time;
    private Entity entity;

    public Event(Action action, double time, Entity entity) {
        this.setAction(action);
        this.setTime(time);
        this.setEntity(entity);
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
