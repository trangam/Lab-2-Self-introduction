package sidescroller.animator;

import java.util.Iterator;
import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.player.Player;
import sidescroller.entity.property.Drawable;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;

public class Animator extends AbstractAnimator {
	private Color background = Color.ANTIQUEWHITE;

	@Override
	public void handle(GraphicsContext gc, long now) {
		updateEntities();
		clearAndFill(gc, background);
		drawEntities(gc);

	}

	@Override
	public void drawEntities(GraphicsContext gc) {
		Consumer<Entity> draw = e -> {
			if (e != null && e.isDrawable()) {
				e.getDrawable().draw(gc);
				if (map.getDrawBounds() && e.hasHitbox()) {
					e.getHitBox().getDrawable().draw(gc);
				}
			}
		};
		draw.accept(map.getBackground());
		for (Entity e : map.staticShapes()) {
			draw.accept(e);
		}
		for (Entity e : map.players()) {
			draw.accept(e);
		}

	}

	@Override
	public void updateEntities() {
		for (Entity e : map.players()) {
			e.update();
		}
		for (Entity e : map.staticShapes()) {
			e.update();
		}
		if (map.getDrawBounds()) {
			for (Entity e : map.players()) {
				HitBox hit = e.getHitBox();
				Drawable<?> drawable = hit.getDrawable();
				drawable.setStroke(Color.RED);
			}
		}
		for (Entity e : map.staticShapes()) {
			proccessEntityList(map.players().iterator(), e.getHitBox());
		}

	}

	@Override
	public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {

		while (iterator.hasNext()) {
			Entity entity = iterator.next();
			HitBox bounds = entity.getHitBox();
			if (!map.inMap(bounds)) {
				updateEntity(entity, iterator);
			} else if (shapeHitBox != null && bounds.intersectBounds(shapeHitBox)) {
				if (map.getDrawBounds()) {
					Drawable<?> drawable = bounds.getDrawable();
					drawable.setStroke(Color.BLUEVIOLET);

				}
				updateEntity(entity, iterator);
			}
		}
	}

	public void updateEntity(Entity entity, Iterator<Entity> iterator) {
		if (entity instanceof Player) {
			((Player) entity).stepBack();
		}
	}

}
