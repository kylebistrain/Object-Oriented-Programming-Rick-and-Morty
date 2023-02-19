import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;
// Kyle Bistrain
public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int VIEW_COLS = getViewWidth() / getTileWidth();
    private static final int VIEW_ROWS = getViewHeight() / getTileHeight();

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";

    private static final  int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String TL = "portaltl";
    private static final String TR = "portaltr";
    private static final String BL = "portalbl";
    private static final String BR = "portalbr";
    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;


    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;



    public void settings() {
        size(getViewWidth(), getViewHeight());
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(getImageListFileName());
        loadWorld(getLoadFile(), this.getImageStore());

        this.setView(new WorldView(getViewRows(), getViewCols(), this, getWorld(), getTileWidth(), getTileHeight()));
        this.setScheduler(new EventScheduler());
        this.setStartTimeMillis(System.currentTimeMillis());
        this.scheduleActions(getWorld(), getScheduler(), getImageStore());
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - getStartTimeMillis()) * 0.001;
        double frameTime = (appTime - getScheduler().getCurrentTime()) / getTimeScale();
        this.update(frameTime);
        view.drawViewport();
    }

    public void update(double frameTime) {
        this.scheduler.updateOnTime(frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());
        Optional<Entity> entityOptional = this.getWorld().getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId() + ": " + entity.getClass() + " : " + entity.getHealth());
        } else if(entityOptional.isEmpty()) {
            world.getBackgroundsquare(pressed, new Background(TL,imageStore.getImageList(TL)));
            Point ptl = new Point(pressed.getX() + 1, pressed.getY());
            Point pbl = new Point(pressed.getX() , pressed.getY() + 1);
            Point pbr = new Point(pressed.getX() + 1, pressed.getY() + 1);
            if (world.withinBounds(ptl)) {
                world.getBackgroundsquare(new Point(pressed.getX() + 1, pressed.getY()), new Background(TR, imageStore.getImageList(TR)));
            } if (world.withinBounds(pbl)) {
                world.getBackgroundsquare(new Point(pressed.getX(), pressed.getY() + 1), new Background(BL, imageStore.getImageList(BL)));
            } if (world.withinBounds(pbr)) {
                world.getBackgroundsquare(new Point(pressed.getX() + 1, pressed.getY() + 1), new Background(BR, imageStore.getImageList(BR)));
            }

            world.parseRick(pressed, imageStore, scheduler, world);






        }



    }

    private Point mouseToPoint() {
        return getView().getViewport().viewportToWorld( mouseX / getTileWidth(), mouseY / getTileHeight());
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            shiftView(getView(), dx, dy);
        }
    }

    public Background createDefaultBackground(ImageStore imageStore) {
        return new Background(getDefaultImageName(), imageStore.getImageList(getDefaultImageName()));
    }

    public PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }



    public void loadWorld(String file, ImageStore imageStore) {
        this.setWorld(new WorldModel());
        try {
            Scanner in = new Scanner(new File(file));
            load(getWorld(), in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            load(getWorld(), in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> setTimeScale(Math.min(getFastScale(), getTimeScale()));
                case FASTER_FLAG -> setTimeScale(Math.min(getFasterScale(), getTimeScale()));
                case FASTEST_FLAG -> setTimeScale(Math.min(getFastestScale(), getTimeScale()));
                default -> setLoadFile(arg);
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime) {
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.getWorld().log();
    }


    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
           // scheduler.scheduleActions(entity, world, imageStore);
            entity.scheduleActions(scheduler, world, imageStore);
        }
    }

    public void load(WorldModel world, Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
        this.getWorld().parseSaveFile(saveFile, imageStore, defaultBackground);
        if (world.getBackground() == null) {
            world.setBackground(new Background[world.getNumRows()][world.getNumCols()]);
            for (Background[] row : world.getBackground())
                Arrays.fill(row, defaultBackground);
        }
        if (world.getOccupancy() == null) {
            world.setOccupancy(new Entity[world.getNumRows()][world.getNumCols()]);
            world.setEntities(new HashSet<>());
        }
    }

    public void loadImages(String filename) {
        this.setImageStore(new ImageStore(createImageColored(getTileWidth(), getTileHeight(), getDefaultImageColor())));
        try {
            Scanner in = new Scanner(new File(filename));
            this.getImageStore().loadImages(in, this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
    public void shiftView(WorldView view, int colDelta, int rowDelta) {
        int newCol = clamp(view.getViewport().getCol() + colDelta, 0, view.getWorld().getNumCols() - view.getViewport().getNumCols());
        int newRow = clamp(view.getViewport().getRow() + rowDelta, 0, view.getWorld().getNumRows() - view.getViewport().getNumRows());

        Viewport.shift(view.getViewport(), newCol, newRow);
    }

    public int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public String getLoadFile() {
        return loadFile;
    }

    public void setLoadFile(String loadFile) {
        this.loadFile = loadFile;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public double getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    public void setImageStore(ImageStore imageStore) {
        this.imageStore = imageStore;
    }

    public WorldModel getWorld() {
        return world;
    }

    public void setWorld(WorldModel world) {
        this.world = world;
    }

    public WorldView getView() {
        return view;
    }

    public void setView(WorldView view) {
        this.view = view;
    }

    public EventScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(EventScheduler scheduler) {
        this.scheduler = scheduler;
    }
    public static int getViewWidth() {
        return VIEW_WIDTH;
    }

    public static int getViewHeight() {
        return VIEW_HEIGHT;
    }

    public static int getTileWidth() {
        return TILE_WIDTH;
    }

    public static int getTileHeight() {
        return TILE_HEIGHT;
    }

    public static int getViewCols() {
        return VIEW_COLS;
    }

    public static int getViewRows() {
        return VIEW_ROWS;
    }

    public static String getImageListFileName() {
        return IMAGE_LIST_FILE_NAME;
    }

    public static String getDefaultImageName() {
        return DEFAULT_IMAGE_NAME;
    }

    public static int getDefaultImageColor() {
        return DEFAULT_IMAGE_COLOR;
    }


    public static double getFastScale() {
        return FAST_SCALE;
    }

    public static double getFasterScale() {
        return FASTER_SCALE;
    }

    public static double getFastestScale() {
        return FASTEST_SCALE;
    }
}



