package logic;

import java.util.ArrayList;
import java.util.List;

import graphic.RenderableHolder;
import gui.ConfigurableSettings;
import main.Main;
import model.Entity;
import model.IRenderable;

public class GameLogic {
	private List<Entity> entities;
	private FruitGenerator mainFruitGenerator;

	public GameLogic() {
		entities = new ArrayList<>();
		mainFruitGenerator = new FruitGenerator(this, 2000);
		mainFruitGenerator.start();
		Fruit fruit = new Fruit(0, ConfigurableSettings.screenHeight, 50, 400);
		addEntity(fruit);
	}

	synchronized public void updateLogic() {
		for (int i = entities.size() - 1; i >= 0; i--) {
			Entity e = entities.get(i);
			if (e.isDestroyed()) {
				removeEntity(e);
			} else {
				e.update();
			}
		}
	}

	synchronized public void addEntity(Entity e) {
		entities.add(e);
		RenderableHolder.instance.addEntity(e);
	}

	synchronized public void removeEntity(Entity e) {
		entities.remove(e);
		RenderableHolder.instance.removeEntity(e);
	}

}
