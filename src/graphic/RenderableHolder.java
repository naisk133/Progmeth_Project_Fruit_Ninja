package graphic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLService;

import Utility.DrawingUtility;
import gui.ConfigurableSettings;
import javafx.scene.image.Image;
import model.IRenderable;

public class RenderableHolder {
	public static RenderableHolder instance = new RenderableHolder();

	private List<IRenderable> entities;
	private int maxZ;
	private Comparator<IRenderable> comparator;

	private RenderableHolder() {
		entities = new ArrayList<>();
		maxZ = 0;
		comparator = (IRenderable x, IRenderable y) -> {
			if (x.getZ() > y.getZ())
				return 1;
			else if (x.getZ() == y.getZ())
				return 0;
			else
				return -1;
		};
	}

	public void addEntity(IRenderable renderable) {
		synchronized (entities) {
			entities.add(renderable);
			sortEntity();
		}
	}

	public void removeEntity(IRenderable renderable) {
		synchronized (entities) {
			entities.remove(renderable);
			sortEntity();
		}
	}

	public void removeEntity(int index) {
		synchronized (entities) {
			entities.remove(index);
			sortEntity();
		}
	}

	public void sortEntity() {
		synchronized (entities) {
			Collections.sort(entities, comparator);
			if (entities.size() >= 4) {
				maxZ = entities.get(entities.size() - 4).getZ();
			} else {
				maxZ = 0;
			}
		}
	}

	public List<IRenderable> getEntities() {
		return entities;
	}

	public int getMaxZ() {
		return maxZ;
	}

}
