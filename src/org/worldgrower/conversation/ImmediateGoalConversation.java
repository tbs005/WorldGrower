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
package org.worldgrower.conversation;

import java.util.Arrays;
import java.util.List;

import org.worldgrower.OperationInfo;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.goal.Goal;
import org.worldgrower.history.HistoryItem;
import org.worldgrower.text.FormattableText;
import org.worldgrower.text.TextId;

public class ImmediateGoalConversation implements Conversation {

	private static final int YES = 0;
	private static final int NO = 1;
	
	@Override
	public Response getReplyPhrase(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		final int replyId;
		Goal goal = world.getGoal(target);
		OperationInfo operationInfo = world.getImmediateGoal(target, world);

		if ((goal != null) && (operationInfo != null)) {
			replyId = YES;
		} else {
			replyId = NO;
		}
		
		return getReply(getReplyPhrases(conversationContext), replyId);
	}

	@Override
	public List<Question> getQuestionPhrases(WorldObject performer, WorldObject target, HistoryItem questionHistoryItem, WorldObject subjectWorldObject, World world) {
		return Arrays.asList(new Question(TextId.QUESTION_IMMEDIATE_GOAL));
	}
	
	@Override
	public List<Response> getReplyPhrases(ConversationContext conversationContext) {
		WorldObject target = conversationContext.getTarget();
		World world = conversationContext.getWorld();
		
		Goal goal = world.getGoal(target);
		OperationInfo operationInfo = world.getImmediateGoal(target, world);
		String immediateGoalDescription = (operationInfo != null ? operationInfo.getDescription(world) : "");
		final Object goalDescription;
		if (goal != null) {
			goalDescription = goal.getDescription();
		} else {
			goalDescription = new FormattableText(TextId.GOAL_DOING_NOTHING);
		}
		return Arrays.asList(
			new Response(YES, TextId.ANSWER_IMMEDIATE_GOAL_YES, immediateGoalDescription, goalDescription),
			new Response(NO, TextId.ANSWER_IMMEDIATE_GOAL_NO)
			);
	}
	
	@Override
	public boolean isConversationAvailable(WorldObject performer, WorldObject target, WorldObject subject, World world) {
		return true;
	}
	
	@Override
	public void handleResponse(int replyIndex, ConversationContext conversationContext) {
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, World world) {
		return "talking about what I'm doing";
	}
}
