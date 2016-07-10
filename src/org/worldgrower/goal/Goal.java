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

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.worldgrower.Constants;
import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.attribute.ManagedProperty;
import org.worldgrower.conversation.Conversation;
import org.worldgrower.conversation.Conversations;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.personality.PersonalityTrait;

/**
 * A Goal describes something a non-player character wants to achieve.
 * This class is responsible for calculating the next ManagedOperation to perform, if the Goal is achieved or not and if the Goal is getting closer or not.
 */
public interface Goal extends Serializable {
	public OperationInfo calculateGoal(WorldObject performer, World world);
	public boolean isGoalMet(WorldObject performer, World world);
	public boolean isUrgentGoalMet(WorldObject performer, World world);
	public String getDescription();
	
	public void goalMetOrNot(WorldObject performer, World world, boolean goalMet);
	public int evaluate(WorldObject performer, World world);
	
	public default void defaultGoalMetOrNot(WorldObject performer, World world, boolean goalMet, ManagedProperty<?> property) {
		if (performer.hasProperty(Constants.DEMANDS)) {
			if (goalMet) {
				performer.getProperty(Constants.DEMANDS).remove(property);
			} else {
				performer.getProperty(Constants.DEMANDS).add(property, 1);
			}
		}
	}
	
	public default void changePersonality(WorldObject performer, PersonalityTrait personalityTrait, int value, boolean goalMet, String reason, World world) {
		if (!goalMet && !isUrgentGoalMet(performer, world) && performer.hasProperty(Constants.PERSONALITY)) {
			int sign = calculateSign(performer, personalityTrait);
			performer.getProperty(Constants.PERSONALITY).changeValue(personalityTrait, sign * value, reason);
		}
	}

	public default int calculateSign(WorldObject performer, PersonalityTrait personalityTrait) {
		String performerName = performer.getProperty(Constants.NAME);
		String traitName = personalityTrait.getClass().getSimpleName();
		int firstChars = performerName.charAt(0) + traitName.charAt(0);
		return firstChars % 2 == 0 ? 1 : -1;
	}
	
	public default List<Integer> getPreviousResponseIds(WorldObject performer, WorldObject target, Conversation conversation, World world) {
		List<HistoryItem> historyItems = world.getHistory().findHistoryItems(performer, target, Actions.TALK_ACTION);
		historyItems = historyItems.stream().filter(h -> h.getArgs()[0] == Conversations.createArgs(conversation)[0]).collect(Collectors.toList());
		return historyItems.stream().map(h -> (Integer)h.getAdditionalValue()).collect(Collectors.toList());
	}
}
