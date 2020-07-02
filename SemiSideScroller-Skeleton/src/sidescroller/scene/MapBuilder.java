package sidescroller.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.BackgroundSprite;
import sidescroller.entity.sprite.LandSprite;
import sidescroller.entity.sprite.PlatformSprite;
import sidescroller.entity.sprite.SpriteFactory;
import sidescroller.entity.sprite.TreeSprite;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

public class MapBuilder implements MapBuilderInterface {
	private Tuple rowColCount;
	private Tuple dimension;
	private double scale;
	private Canvas canvas;
	private Entity background;
	private List<Entity> landMass;
	private List<Entity> other;

	protected MapBuilder() {
		landMass = new ArrayList<Entity>();
		other = new ArrayList<Entity>();

	}

	public static MapBuilder createBuilder() {
		return new MapBuilder();
	}

	public MapBuilder setCanvas(Canvas canvas) {
		this.canvas = canvas;
		return this;
	}

	public MapBuilder setGrid(Tuple rowColCount, Tuple dimension) {
		this.rowColCount = rowColCount;
		this.dimension = dimension;
		return this;
	}

	public MapBuilder buildBackground(BiFunction<Integer, Integer, Tile> callback) {

		BackgroundSprite sprite = SpriteFactory.get("Background");
		sprite.init(scale, dimension, Tuple.pair(0, 0));
		sprite.createSnapshot(this.canvas, this.rowColCount, callback);
		HitBox hitbox = HitBox.build(0, 0, (scale * dimension.x() * rowColCount.y()),
				(scale * dimension.y() * rowColCount.x()));
		background = new GenericEntity(sprite, hitbox);

		return this;
	}

	public MapBuilder buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {
		LandSprite sprite = SpriteFactory.get("Land");
		sprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		sprite.createSnapshot(this.canvas, rowConut, colCount);
		HitBox hitbox = HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale,
				scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
		landMass.add(new GenericEntity(sprite, hitbox));

		return this;
	}

	public MapBuilder buildTree(int rowPos, int colPos, Tile tile) {
		TreeSprite sprite = SpriteFactory.get("Tree");
		sprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		sprite.createSnapshot(this.canvas, tile);
		other.add(new GenericEntity(sprite, null));
		return this;
	}

	public MapBuilder buildPlatform(int rowPos, int colPos, int length, Tile tile) {
		PlatformSprite sprite = SpriteFactory.get("Platform");
		sprite.init(scale, dimension, Tuple.pair(colPos, rowPos));
		sprite.createSnapshot(canvas, tile, length);
		HitBox hitbox = HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale,
				scale * dimension.x() * (length - 1), scale * dimension.y() / 2);
		other.add(new GenericEntity(sprite, hitbox));

		return this;
	}

	public List<Entity> getEntities(List<Entity> list) {
		list.addAll(landMass);
		list.addAll(other);
		return list;
	}

	public MapBuilder setGridScale(double scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public Entity getBackground() {

		return background;
	}

}
