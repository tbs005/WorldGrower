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

import org.worldgrower.Constants;
import org.worldgrower.WorldObject;
import org.worldgrower.condition.Condition;
import org.worldgrower.condition.Conditions;

public class SmithPropertyUtils {
	private static boolean performerHasBoon(WorldObject performer) {
		Conditions conditions = performer.getProperty(Constants.CONDITIONS);
		return conditions.hasCondition(Condition.HEPHAESTUS_BOON_CONDITION);
	}
	
	public static int calculateSmithingQuantity(WorldObject performer, WorldObject smith) {
		int quantity = smith.getProperty(Constants.SMITH_QUALITY);

		if (leftHandContainsRepairHammer(performer)) {
			quantity += 1;
		}
		
		if (performerHasBoon(performer)) {
			quantity += 1;
		}
		return quantity;
	}
	
	public static boolean leftHandContainsRepairHammer(WorldObject performer) {
		WorldObject leftHand = performer.getProperty(Constants.LEFT_HAND_EQUIPMENT);
		return (leftHand != null && leftHand.hasProperty(Constants.REPAIR_QUALITY));
	}
}
