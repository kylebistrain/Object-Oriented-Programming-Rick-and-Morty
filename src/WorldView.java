import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    private PApplet screen;

    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.setScreen(screen);
        this.setWorld(world);
        this.setTileWidth(tileWidth);
        this.setTileHeight(tileHeight);
        this.setViewport(new Viewport(numRows, numCols));
    }
    public void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }
    public void drawEntities() {
        for (Entity entity : this.getWorld().getEntities()) {
            Point pos = entity.getPosition();

            if (this.getViewport().contains( pos)) {
                Point viewPoint = this.getViewport().worldToViewport( pos.getX(), pos.getY());
                this.getScreen().image(getCurrentImage(entity), viewPoint.getX() * this.getTileWidth(), viewPoint.getY() * this.getTileHeight());
            }
        }
    }

    public void drawBackground() {
        for (int row = 0; row < this.getViewport().getNumRows(); row++) {
            for (int col = 0; col < this.getViewport().getNumCols(); col++) {
                Point worldPoint = viewport.viewportToWorld( col, row);
                Optional<PImage> image = this.getBackgroundImage(this.getWorld(), worldPoint);
                if (image.isPresent()) {
                    this.getScreen().image(image.get(), col * this.getTileWidth(), row * this.getTileHeight());
                }
            }
        }
    }
    public Optional<PImage> getBackgroundImage(WorldModel world, Point pos) {
        if (world.withinBounds(pos)) {
            return Optional.of(getCurrentImage(getBackgroundCell(world, pos)));
        } else {
            return Optional.empty();
        }
    }
    public PImage getCurrentImage(Object object) {
        if (object instanceof Background background) {
            return background.getImages().get(background.getImageIndex());
        } else if (object instanceof Entity entity) {
            return entity.getImages().get(entity.getImageIndex() % entity.getImages().size());
        } else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not supported for %s", object));
        }
    }


    public Background getBackgroundCell(WorldModel world, Point pos) {
        return world.getBackground()[pos.getY()][pos.getX()];
    }


    public PApplet getScreen() {
        return screen;
    }

    public void setScreen(PApplet screen) {
        this.screen = screen;
    }

    public WorldModel getWorld() {
        return world;
    }

    public void setWorld(WorldModel world) {
        this.world = world;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
