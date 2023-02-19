import java.lang.reflect.Executable;
import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private final int TREE_NUM_PROPERTIES = 3;
    private final int STUMP_NUM_PROPERTIES = 0;
    private final int ENTITY_NUM_PROPERTIES = 4;

    private final int RICK_NUM_PROPERTIES = 6;
    private final int PROPERTY_COL = 2;
    private final int PROPERTY_ID = 1;
    private final int PROPERTY_ROW = 3;
    private int numRows;
    private int numCols;

    public void addDudelist(Entity ent) {
        this.dudelst.add(ent);
    }

    private List<Entity> dudelst = new ArrayList<>();

    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;
    private Action action;

    private Set<Sapling> sap;

    private Set<Stump> stump;

    private Set<Tree> tree;
    private Set<House> house;



    private static final String TREE_KEY = "tree";
    private static final int TREE_HEALTH = 2;
    private static final int SAPLING_NUM_PROPERTIES = 1;
    private static final int SAPLING_HEALTH = 0;
    private static final String SAPLING_KEY = "sapling";


    private static final int TREE_ANIMATION_PERIOD = 0;
    private static final int TREE_ACTION_PERIOD = 1;

    private static final String FAIRY_KEY = "fairy";
    private static final int FAIRY_ANIMATION_PERIOD = 0;
    private static final int FAIRY_ACTION_PERIOD = 1;
    private static final int FAIRY_NUM_PROPERTIES = 2;


    private static final int OBSTACLE_ANIMATION_PERIOD = 0;
    private static final int OBSTACLE_NUM_PROPERTIES = 1;
    private static final String OBSTACLE_KEY = "obstacle";


    private static final String HOUSE_KEY = "house";
    private static final int HOUSE_NUM_PROPERTIES = 0;

    private static final String STUMP_KEY = "stump";

    private static final String RICK_KEY = "rick";



    private static final int PROPERTY_KEY = 0;

    private static final int DUDE_ACTION_PERIOD = 0;
    private static final int DUDE_ANIMATION_PERIOD = 1;
    private static final int DUDE_LIMIT = 2;
    private static final int DUDE_NUM_PROPERTIES = 3;
    private static final String DUDE_KEY = "dude";




    public WorldModel() {

    }



    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : getEntities()) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }


    public void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Entity entity = Tree.createTree(id, pt, Double.parseDouble(properties[getTreeActionPeriod()]), Double.parseDouble(properties[getTreeAnimationPeriod()]), Integer.parseInt(properties[getTreeHealth()]), imageStore.getImageList(getTreeKey()));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getTreeKey(), TREE_NUM_PROPERTIES));
        }
    }
    public void parseRick(Point pos ,ImageStore image, EventScheduler scheduler, WorldModel world)  {
        Entity rick = Rick.createRick(RICK_KEY, pos,.2,.2,3,image.getImageList(RICK_KEY));
        tryAddEntity(rick);
        ((Rick)rick).scheduleActions(scheduler,world,image);

//        scheduler.scheduleEvent(rick, Activity.createActivityAction(rick, world, image), rick.getActionPeriod());
//        scheduler.scheduleEvent(rick, Animation.createAnimationAction(rick, 0), 9);

//        scheduler.scheduleEvent(rick, Activity.createActivityAction(rick, world, image), rick.getActionPeriod());


        //rick.scheduleActions(scheduler,world,image);

    }
    public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == getSaplingNumProperties()) {
            int health = Integer.parseInt(properties[getSaplingHealth()]);
            Entity entity = Sapling.createSapling(id, pt, imageStore.getImageList( getSaplingKey()), health);
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getSaplingKey(), getSaplingNumProperties()));
        }
    }
    public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < this.numRows){
            int rows = Math.min(cells.length, this.numCols);
            for (int col = 0; col < rows; col++){
                this.getBackground()[row][col] = new Background(cells[col], imageStore.getImageList( cells[col]));
            }
        }
    }
    public void parseSaveFile(Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while (saveFile.hasNextLine()) {
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if (line.endsWith(":")) {
                headerLine = lineCounter;
                lastHeader = line;
                switch (line) {
                    case "Backgrounds:" -> this.setBackground(new Background[this.getNumRows()][this.getNumCols()]);
                    case "Entities:" -> {
                        this.setOccupancy(new Entity[this.getNumRows()][this.getNumCols()]);
                        this.setEntities(new HashSet<>());
                    }
                }
            } else {
                switch (lastHeader) {
                    case "Rows:" -> this.setNumRows(Integer.parseInt(line));
                    case "Cols:" -> this.setNumCols(Integer.parseInt(line));
                    case "Backgrounds:" -> parseBackgroundRow( line, lineCounter - headerLine - 1, imageStore);
                    case "Entities:" -> parseEntity( line, imageStore);
                }
            }
        }

    }
    public void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == getHouseNumProperties()) {
            Entity entity = House.createHouse(id, pt, imageStore.getImageList(getHouseKey()));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getHouseKey(), getHouseNumProperties()));
        }
    }

    public void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == STUMP_NUM_PROPERTIES) {
            Entity entity = Stump.createStump(id, pt, imageStore.getImageList(getStumpKey()));
            tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getStumpKey(), STUMP_NUM_PROPERTIES));
        }
    }
    public void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == getObstacleNumProperties()) {
            Entity entity = Obstacle.createObstacle(id, pt, Double.parseDouble(properties[getObstacleAnimationPeriod()]), imageStore.getImageList(getObstacleKey()));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getObstacleKey(), getObstacleNumProperties()));
        }
    }
    public void parseEntity(String line, ImageStore imageStore) {
        String[] properties = line.split(" ", ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= ENTITY_NUM_PROPERTIES) {
            String key = properties[getPropertyKey()];
            String id = properties[PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[PROPERTY_COL]), Integer.parseInt(properties[PROPERTY_ROW]));

            properties = properties.length == ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case OBSTACLE_KEY -> parseObstacle(properties, pt, id, imageStore);
                case DUDE_KEY -> parseDude(properties, pt, id, imageStore);
                case FAIRY_KEY -> parseFairy( properties, pt, id, imageStore);
                case HOUSE_KEY -> parseHouse(properties, pt, id, imageStore);
                case TREE_KEY -> parseTree(properties, pt, id, imageStore);
                case SAPLING_KEY-> parseSapling(properties, pt, id, imageStore);
                case STUMP_KEY -> parseStump(properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }
    public void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == getDudeNumProperties()) {
            Entity entity = DudeNotFull.createDudeNotFull(id, pt, Double.parseDouble(properties[getDudeActionPeriod()]), Double.parseDouble(properties[getDudeAnimationPeriod()]), Integer.parseInt(properties[getDudeLimit()]), imageStore.getImageList( getDudeKey()));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getDudeKey(), getDudeNumProperties()));
        }
    }
    public void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == getFairyNumProperties()) {
            Entity entity = Fairy.createFairy(id, pt, Double.parseDouble(properties[getFairyActionPeriod()]), Double.parseDouble(properties[getFairyAnimationPeriod()]), imageStore.getImageList(getFairyKey()));
            tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", getFairyKey(), getFairyNumProperties()));
        }
    }
    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }
    public boolean isOccupied( Point pos) {
        return this.withinBounds(pos) && getOccupancyCell(pos) != null;
    }
    public Entity getOccupancyCell( Point pos) {
        return this.getOccupancy()[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        this.getOccupancy()[pos.getY()][pos.getX()] = entity;
    }
    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            this.getEntities().add(entity);
        }
    }
//    public Optional<Entity> findNearests( Point pos, List<String> kinds) {
//        List<Entity> ofType = new LinkedList<>();
//        for (String kind : kinds) {
//            for (Entity entity : this.getEntities()) {
//                System.out.println(entity.getClass());
//                //    if (entity.toString().equals(kind)) {
//                ofType.add(entity);
//                //   }
//            }
//        }
//
//
//        return nearestEntity(ofType, pos);
//    }
    public Optional<Entity> findNearesthouse( Point pos, List<String> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (String kind : kinds) {
            for (Entity entity : this.getEntities()) {
                    if (entity instanceof House) {
                ofType.add(entity);
                   }
            }
        }

        return nearestEntity(ofType, pos);
    }
    public Optional<Entity> findNearestdude( Point pos) {
        List<Entity> ofType = new LinkedList<>();
        int count = 0;
        for(Entity entity: this.getEntities()) {
            if(entity instanceof  DudeFull || entity instanceof DudeNotFull) {
                count++;
            }
        }
        for (Entity entity : this.getEntities()) {
            if ((entity instanceof DudeNotFull || entity instanceof DudeFull) && !dudelst.contains(entity)) {
                ofType.add(entity);
            }
        }
        if(count == ofType.size()) {
            dudelst.clear();
        }

        return nearestEntity(ofType, pos);
    }
    public Optional<Entity> findNearesttree(Point pos, List<String> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (String kind : kinds) {
            for (Entity entity : this.getEntities()) {

                if (entity instanceof Tree || entity instanceof Sapling) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }
    public Optional<Entity> findNeareststump( Point pos, List<String> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (String kind : kinds) {
            for (Entity entity : this.getEntities()) {
                if (entity instanceof Stump) {
                    ofType.add(entity);
                }
            }
        }
        return nearestEntity(ofType, pos);
    }


    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = getOccupant(pos);
            occupant.ifPresent(target -> removeEntity(scheduler, target));
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }
    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);
            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.getEntities().remove(entity);
            setOccupancyCell( pos, null);
        }
    }
    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = pos.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = pos.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public void removeEntity(EventScheduler scheduler,Entity entity) {
        scheduler.unscheduleAllEvents( entity);
        removeEntityAt(entity.getPosition());
    }
    public boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY() && Math.abs(p1.getX() - p2.getX()) == 1);
    }
    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }
    public boolean withinBounds( Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0 && pos.getX() < this.numCols;
    }

    public void setBackground(Background[][] background) {
        this.background = background;
    }

    public Entity[][] getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Entity[][] occupancy) {
        this.occupancy = occupancy;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }
    public static String getTreeKey() {
        return TREE_KEY;
    }

    public int getTreeHealth() {
        return TREE_HEALTH;
    }

    public int getSaplingNumProperties() {
        return SAPLING_NUM_PROPERTIES;
    }

    public int getSaplingHealth() {
        return SAPLING_HEALTH;
    }

    public static String getSaplingKey() {
        return SAPLING_KEY;
    }

    public int getTreeAnimationPeriod() {
        return TREE_ANIMATION_PERIOD;
    }

    public int getTreeActionPeriod() {
        return TREE_ACTION_PERIOD;
    }

    public String getFairyKey() {
        return FAIRY_KEY;
    }

    public int getFairyAnimationPeriod() {
        return FAIRY_ANIMATION_PERIOD;
    }

    public int getFairyActionPeriod() {
        return FAIRY_ACTION_PERIOD;
    }

    public int getFairyNumProperties() {
        return FAIRY_NUM_PROPERTIES;
    }

    public int getObstacleAnimationPeriod() {
        return OBSTACLE_ANIMATION_PERIOD;
    }

    public int getObstacleNumProperties() {
        return OBSTACLE_NUM_PROPERTIES;
    }

    public String getObstacleKey() {
        return OBSTACLE_KEY;
    }

    public String getHouseKey() {
        return HOUSE_KEY;
    }

    public int getHouseNumProperties() {
        return HOUSE_NUM_PROPERTIES;
    }

    public static String getStumpKey() {
        return STUMP_KEY;
    }

    public int getPropertyKey() {
        return PROPERTY_KEY;
    }

    public int getDudeActionPeriod() {
        return DUDE_ACTION_PERIOD;
    }

    public int getDudeAnimationPeriod() {
        return DUDE_ANIMATION_PERIOD;
    }

    public int getDudeLimit() {
        return DUDE_LIMIT;
    }

    public int getDudeNumProperties() {
        return DUDE_NUM_PROPERTIES;
    }

    public String getDudeKey() {
        return DUDE_KEY;
    }

    public Background[][] getBackground() {
        return background;
    }

    public void getBackgroundsquare(Point p, Background back) {
        this.background[p.getY()][p.getX()] = back;

    }


}
