package com.gemserk.games.angryships.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.MovementComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.Script;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.angryships.components.ControllerComponent;
import com.gemserk.games.angryships.components.PixmapWorld;
import com.gemserk.games.angryships.entities.Groups;
import com.gemserk.games.angryships.gamestates.Controller;
import com.gemserk.games.angryships.resources.GameResources;
import com.gemserk.games.angryships.scripts.MovementScript;
import com.gemserk.prototypes.pixmap.PixmapHelper;
import com.gemserk.resources.ResourceManager;

public class KamikazeBombEntityTemplate extends EntityTemplateImpl {

	public static class PixmapCollidableScript extends ScriptJavaImpl {

		private static final Vector2 position = new Vector2();

		PixmapWorld pixmapWorld;

		// change it to be an entity processing system

		@Override
		public void update(World world, Entity e) {

			SpatialComponent spatialComponent = Components.spatialComponent(e);
			Spatial spatial = spatialComponent.getSpatial();

			// PixmapCollidableComponent pixmapCollidableComponent = GameComponents.getPixmapCollidableComponent(e);
			// PixmapCollision pixmapCollision = pixmapCollidableComponent.pixmapCollision;

			Array<PixmapHelper> pixmaps = pixmapWorld.getPixmaps();

			for (int i = 0; i < pixmaps.size; i++) {
				PixmapHelper pixmapHelper = pixmaps.get(i);
				// PixmapHelper pixmapHelper = pixmapCollision.getContact(i);
				pixmapHelper.project(position, spatial.getX(), spatial.getY());
				pixmapHelper.eraseCircle(position.x, position.y, 8f);
			}

		}

	}

	ResourceManager<String> resourceManager;
	Injector injector;

	@Override
	public void apply(Entity entity) {

		Spatial spatial = parameters.get("spatial");
		Controller controller = parameters.get("controller");
		// PixmapHelper pixmapHelper = parameters.get("pixmapHelper");

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.BombSprite);

		entity.setGroup(Groups.Bombs);

		entity.addComponent(new RenderableComponent(0));
		entity.addComponent(new SpriteComponent(sprite, 0.5f, 0.5f, Color.WHITE));

		entity.addComponent(new SpatialComponent(spatial));
		// entity.addComponent(new PreviousStateSpatialComponent());

		entity.addComponent(new MovementComponent(150f, 0f, 0f));

		entity.addComponent(new ControllerComponent(controller));

		// entity.addComponent(new PixmapCollidableComponent());

		Script movementScript = injector.getInstance(MovementScript.class);
		Script pixmapCollidableScript = injector.getInstance(PixmapCollidableScript.class);

		entity.addComponent(new ScriptComponent(movementScript, pixmapCollidableScript));

	}

}