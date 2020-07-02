package sidescroller.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import sidescroller.animator.AnimatorInterface;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.tile.BackgroundTile;
import sidescroller.entity.sprite.tile.FloraTile;
import sidescroller.entity.sprite.tile.PlatformTile;
import utility.Tuple;

public class MapScene implements MapSceneInterface {
	private Tuple count;
	private Tuple size;
	private double scale;
	private AnimatorInterface animator;
	private List<Entity> players;
	private List<Entity> staticShapes;
	private BooleanProperty drawBounds;
	private BooleanProperty drawFPS;
	private BooleanProperty drawGrid;
	private Entity background;

	public MapScene() {
		players = new ArrayList<Entity>();
		staticShapes = new ArrayList<Entity>();
		drawFPS = new SimpleBooleanProperty();
		drawGrid = new SimpleBooleanProperty();
		drawBounds = new SimpleBooleanProperty();

	}

	public BooleanProperty drawFPSProperty() {
		return drawFPS;
	}

	public boolean getDrawFPS() {
		return drawFPS.get();
	}

	public BooleanProperty drawBoundsProperty() {
		return drawBounds;
	}

	public boolean getDrawBounds() {
		return drawBounds.get();
	}

	public BooleanProperty drawGridProperty() {
		return drawGrid;
	}

	public boolean getDrawGrid() {
		return drawGrid.get();
	}

	public MapScene setRowAndCol(Tuple count, Tuple size, double scale) {
		this.count = count;
		this.size = size;
		this.scale = scale;
		return this;
	}

	public Tuple getGridCount() {
		return count;
	}

	public Tuple getGridSize() {
		return size;
	}

	public void start() {
		if (animator != null) {
			animator.start();
		}
	}

	public void stop() {
		if (animator != null) {
			animator.stop();
		}
	}

	public List<Entity> staticShapes() {
		return staticShapes;
	}

	public List<Entity> players() {
		return players;
	}

	/**
	 * <p>
	 * this method creates the static entities in the game. <br>
	 * use {@link MapBuilder#createBuilder()} to get and instance of MapBuilder
	 * called mb. <br>
	 * on mb call methods {@link MapBuilder#setCanvas(Canvas)},
	 * {@link MapBuilder#setGrid(Tuple, Tuple)}, and
	 * {@link MapBuilder#setGridScale(double)}. <br>
	 * call all or any combination of build methods in MapBuilder to create custom
	 * map, does not have to be complex. one landmass and a tree is good enough.
	 * <br>
	 * call {@link MapBuilder#getBackground()} and
	 * {@link MapBuilder#getEntities(List)} to retrieve the built entities.
	 * </p>
	 * 
	 * @param canvas
	 * @return
	 */
	public MapScene createScene(Canvas canvas) {
		MapBuilder mb = MapBuilder.createBuilder();
		mb.setCanvas(canvas);
		mb.setGrid(count, size);
		mb.setGridScale(scale);

		mb.buildLandMass(10, 15, 4, 12);
		mb.buildLandMass(8, 0, 6, 9);
		mb.buildLandMass(11, 29, 3, 5);
		mb.buildTree(1, 1, FloraTile.TREE);
		mb.buildTree(5, 20, FloraTile.TREE_DEAD);
		mb.buildTree(9, 30, FloraTile.SUNFLOWER_LONG);
		mb.buildTree(9, 31, FloraTile.SUNFLOWER_SHORT);
		mb.buildPlatform(11, 10, 5, PlatformTile.WOOD);
		mb.buildBackground((Integer row, Integer col) -> {
			BackgroundTile[] tiles = new BackgroundTile[] { BackgroundTile.MORNING_CLOUD, BackgroundTile.MORNING };
			if (row <= 3)
				return tiles[new Random().nextInt(tiles.length)];

			return BackgroundTile.MORNING;
		});

		mb.getEntities(staticShapes);
		background = mb.getBackground();

		return this;
	}

	/**
	 * @param hitbox - hitbox of an entity to check it is it still in background
	 *               bounds.
	 * @return true of hitbox of background containsBouns of argument.
	 */
	public boolean inMap(HitBox hitbox) {
		return background.getHitBox().containsBounds(hitbox);
	}

	/**
	 * if current animator instance is not null stop it first. then assign the
	 * newAnimator to animator.
	 * 
	 * @param newAnimator - new animator to set.
	 * @return current instance of this class.
	 * @throws NullPointerException if argument is null.
	 */
	public MapScene setAnimator(AnimatorInterface newAnimator) {
		if (newAnimator != null) {
			newAnimator.stop();
		}
		this.animator = newAnimator;
		return this;

	}

	@Override
	public double getScale() {
		return scale;
	}

	@Override
	public Entity getBackground() {
		return background;
	}

}
