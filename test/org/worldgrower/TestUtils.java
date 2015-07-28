/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.WorldObjectImpl;
import org.worldgrower.WorldObjectPriorities;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdMap;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.attribute.PropertyCountMap;
import org.worldgrower.attribute.Skill;
import org.worldgrower.attribute.WorldObjectContainer;
import org.worldgrower.creaturetype.CreatureType;
import org.worldgrower.goal.Goal;

public class TestUtils {

	public static WorldObject createWorldObject(int x, int y, int width, int height) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		properties.put(Constants.ENERGY, 1000);
		properties.put(Constants.LUMBERING_SKILL, new Skill());
		WorldObject w1 = new WorldObjectImpl(properties);
		return w1;
	}
	
	public static WorldObject createWorldObject(int x, int y, int width, int height, ManagedProperty<?> property, Object value) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.X, x);
		properties.put(Constants.Y, y);
		properties.put(Constants.WIDTH, width);
		properties.put(Constants.HEIGHT, height);
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties);
		return w1;
	}
	
	public static WorldObject createWorldObject(int id, String name) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.NAME, name);
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList().add(6));
		properties.put(Constants.SOCIAL, 0);
		properties.put(Constants.RELATIONSHIPS, new IdMap());
		properties.put(Constants.CREATURE_TYPE,CreatureType.HUMAN_CREATURE_TYPE);
		properties.put(Constants.DEMANDS, new PropertyCountMap());
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, null);
		return w1;
	}
	
	public static WorldObject createIntelligentWorldObject(int id, ManagedProperty<?> property, Object value) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList().add(6));
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, null);
		return w1;
	}
	
	private static class WorldObjectPrioritiesImpl implements WorldObjectPriorities {

		@Override
		public List<Goal> getPriorities(WorldObject performer, World world) {
			return null;
		}
	}
	
	public static WorldObject createIntelligentWorldObject(int id, ManagedProperty<?> property, Object value, WorldObjectPriorities worldObjectPriorities) {
		Map<ManagedProperty<?>, Object> properties = new HashMap<>();
		properties.put(Constants.ID, id);
		properties.put(Constants.GROUP, new IdList());
		properties.put(property, value);
		WorldObject w1 = new WorldObjectImpl(properties, Actions.ALL_ACTIONS, worldObjectPriorities);
		return w1;
	}
}
