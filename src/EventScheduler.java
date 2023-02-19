import java.util.*;

/**
 * Keeps track of events that have been scheduled.
 */
public final class EventScheduler {
    private PriorityQueue<Event> eventQueue;
    private Map<Entity, List<Event>> pendingEvents;
    private double currentTime;

    public EventScheduler() {
        this.setEventQueue(new PriorityQueue<>(new EventComparator()));
        this.setPendingEvents(new HashMap<>());
        this.setCurrentTime(0);
    }

    public void unscheduleAllEvents(Entity entity) {
        List<Event> pending = this.getPendingEvents().remove(entity);
        if (pending != null) {
            for (Event event : pending) {
                this.getEventQueue().remove(event);
            }
        }
    }
    public  void scheduleEvent(Entity entity, Action action, double afterPeriod) {
        double time = this.getCurrentTime() + afterPeriod;

        Event event = new Event(action, time, entity);

        this.getEventQueue().add(event);
        // update list of pending events for the given entity
        List<Event> pending = this.getPendingEvents().getOrDefault(entity, new LinkedList<>());
        pending.add(event);
        this.getPendingEvents().put(entity, pending);
    }
    public void updateOnTime( double time) {
        double stopTime = this.getCurrentTime() + time;
        while (!this.getEventQueue().isEmpty() && this.getEventQueue().peek().getTime() <= stopTime) {
            Event next = this.getEventQueue().poll();
            removePendingEvent( next);
            this.setCurrentTime(next.getTime());
            next.getAction().executeAction(this);

        }
        this.setCurrentTime(stopTime);
    }
    public void removePendingEvent( Event event) {
        List<Event> pending = this.getPendingEvents().get(event.getEntity());
        if (pending != null) {
            pending.remove(event);
        }
    }

//    public void scheduleActions(Entity entity, WorldModel world, ImageStore imageStore) {
//        switch (entity.getKind()) {
//            case DUDE_FULL -> {
//                scheduleEvent( entity, Activity.createActivityAction(entity, world, imageStore), entity.getActionPeriod());
//                scheduleEvent( entity, Animation.createAnimationAction(entity, 0), entity.getAnimationPeriod());
//            }
//            case DUDE_NOT_FULL -> {
//                scheduleEvent( entity, Activity.createActivityAction(entity, world, imageStore), entity.getActionPeriod());
//                scheduleEvent( entity, Animation.createAnimationAction(entity, 0), entity.getAnimationPeriod());
//            }
//            case OBSTACLE ->
//                    scheduleEvent( entity, Animation.createAnimationAction(entity, 0), entity.getAnimationPeriod());
//            case FAIRY -> {
//                scheduleEvent( entity, Activity.createActivityAction(entity, world, imageStore), entity.getActionPeriod());
//                scheduleEvent( entity, Animation.createAnimationAction(entity, 0), entity.getAnimationPeriod());
//            }
//            case SAPLING -> {
//                scheduleEvent( entity, Activity.createActivityAction(entity, world, imageStore), entity.getActionPeriod());
//                scheduleEvent( entity, Animation.createAnimationAction(entity, 0), entity.getAnimationPeriod());
//            }
//            case TREE -> {
//                scheduleEvent( entity, Activity.createActivityAction(entity, world, imageStore), entity.getActionPeriod());
//                scheduleEvent( entity, Animation.createAnimationAction(entity, 0), entity.getAnimationPeriod());
//            }
//            default -> {
//            }
//        }
//    }


    public PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }

    public void setEventQueue(PriorityQueue<Event> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public Map<Entity, List<Event>> getPendingEvents() {
        return pendingEvents;
    }

    public void setPendingEvents(Map<Entity, List<Event>> pendingEvents) {
        this.pendingEvents = pendingEvents;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }
}
