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
package org.worldgrower.goal;

import java.util.List;

import org.worldgrower.Args;
import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.BuildWorkbenchAction;

public class WorkbenchGoal implements Goal {

	public WorkbenchGoal(List<Goal> allGoals) {
		allGoals.add(this);
	}

	@Override
	public OperationInfo calculateGoal(WorldObject performer, World world) {
		if (!BuildWorkbenchAction.hasEnoughStone(performer)) {
			return Goals.STONE_GOAL.calculateGoal(performer, world);
		} else {
			WorldObject target = BuildLocationUtils.findOpenLocationNearExistingProperty(performer, 2, 2, world);
			if (target != null) {
				return new OperationInfo(performer, target, Args.EMPTY, Actions.BUILD_WORKBENCH_ACTION);
			} else {
				return null;
			}
		}
	}
	
	@Override
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet) {
	}

	@Override
	public boolean isGoalMet(WorldObject performer, World world) {
		Integer workbenchId = performer.getProperty(Constants.WORKBENCH_ID);
		if (workbenchId != null) {
			WorldObject workbench = world.findWorldObject(Constants.ID, workbenchId);
			return (workbench.getProperty(Constants.WORKBENCH_QUALITY) > 0);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isUrgentGoalMet(WorldObject performer, World world) {
		return isGoalMet(performer, world);
	}

	@Override
	public String getDescription() {
		return "building a workbench";
	}

	@Override
	public int evaluate(WorldObject performer, World world) {
		return (performer.getProperty(Constants.WORKBENCH_ID) != null) ? 1 : 0;
	}
}