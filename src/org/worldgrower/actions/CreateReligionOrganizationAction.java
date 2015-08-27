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
package org.worldgrower.actions;

import java.io.ObjectStreamException;
import java.util.List;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.ManagedOperation;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.deity.Deity;
import org.worldgrower.goal.Goal;
import org.worldgrower.goal.GroupPropertyUtils;

public class CreateReligionOrganizationAction implements ManagedOperation {

	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int deityIndex = args[0];
		int organizationIndex = args[1];
		int goalId = args[2];
		
		Deity deity = Deity.ALL_DEITIES.get(deityIndex);
		List<String> organizationNames = new OrganizationNamer().getDeityOrganizationNames(deity, world);
		
		String organizationName = organizationNames.get(organizationIndex);
		Goal goal = goalId != -1 ? deity.getOrganizationGoals().get(goalId) : null;
		
		WorldObject organization = GroupPropertyUtils.createReligionOrganization(performer.getProperty(Constants.ID), organizationName, deity, goal, world);
		performer.getProperty(Constants.GROUP).add(organization);
	}

	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}

	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		int performerId = performer.getProperty(Constants.ID);
		int targetId = target.getProperty(Constants.ID);
		return (performerId == targetId);
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return 0;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "creating a religion organization";
	}
	
	@Override
	public String getSimpleDescription() {
		return "create religion organization";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}
}