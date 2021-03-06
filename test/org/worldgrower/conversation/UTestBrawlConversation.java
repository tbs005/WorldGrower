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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.worldgrower.Constants;
import org.worldgrower.DefaultConversationFormatter;
import org.worldgrower.DungeonMaster;
import org.worldgrower.MockMetaInformation;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.goal.BrawlPropertyUtils;
import org.worldgrower.goal.Goals;

public class UTestBrawlConversation {

	private final BrawlConversation conversation = Conversations.BRAWL_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		MockMetaInformation.setMetaInformation(target, Goals.COLLECT_WATER_GOAL);
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(5, replyPhrases.size());
		assertEquals("Yes, while we brawl, only unarmed non-lethal melee attacks are allowed.", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("No", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I'd love to, but I'm currently attacking targetName", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Not for the moment, I can't match your bet.", replyPhrases.get(3).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(4).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}

	private WorldImpl createWorld() {
		int worldDimension = 1;
		return new WorldImpl(worldDimension, worldDimension, new DungeonMaster(worldDimension, worldDimension), null);
	}
	
	@Test
	public void testGetReplyPhrasesNullImmediateGoal() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(5, replyPhrases.size());
		assertEquals("Yes, while we brawl, only unarmed non-lethal melee attacks are allowed.", replyPhrases.get(0).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("No", replyPhrases.get(1).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("I'd love to, but I'm currently resting", replyPhrases.get(2).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Not for the moment, I can't match your bet.", replyPhrases.get(3).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
		assertEquals("Get lost", replyPhrases.get(4).getResponsePhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = createWorld();
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GOLD, 0);
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Goals.COLLECT_WATER_GOAL);
		
		target.setProperty(Constants.GOLD, 0);
		MockMetaInformation.setMetaInformation(target, Goals.COLLECT_WATER_GOAL);
		
		int brawlStakeGold = 10;
		ConversationContext context = new ConversationContext(performer, target, null, null, world, brawlStakeGold);
		assertEquals(3, conversation.getReplyPhrase(context).getId());
		
		target.setProperty(Constants.GOLD, 100);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
	}
	
	@Test
	public void testGetQuestionPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.GROUP, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.GROUP, new IdList());
		
		List<Question> questions = conversation.getQuestionPhrases(performer, target, null, null, null);
		assertEquals(5, questions.size());
		assertEquals("I want to brawl with you and I bet 20 gold that I'm going to win. Do you accept?", questions.get(0).getQuestionPhrase(DefaultConversationFormatter.FORMATTER));
	}
	
	@Test
	public void testHandleResponse0() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		int brawlStakeGold = 10;
		ConversationContext context = new ConversationContext(performer, target, null, null, null, brawlStakeGold);
		
		conversation.handleResponse(0, context);
		assertEquals(10, performer.getProperty(Constants.BRAWL_STAKE_GOLD).intValue());
		assertEquals(10, target.getProperty(Constants.BRAWL_STAKE_GOLD).intValue());
		assertEquals(2, performer.getProperty(Constants.BRAWL_OPPONENT_ID).intValue());
		assertEquals(1, target.getProperty(Constants.BRAWL_OPPONENT_ID).intValue());
	}
	
	@Test
	public void testHandleResponse4() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		int brawlStakeGold = 10;
		ConversationContext context = new ConversationContext(performer, target, null, null, null, brawlStakeGold);
		
		conversation.handleResponse(4, context);
		assertEquals(-100, performer.getProperty(Constants.RELATIONSHIPS).getValue(target));
		assertEquals(-100, target.getProperty(Constants.RELATIONSHIPS).getValue(performer));
	}
	
	@Test
	public void testIsConversationAvailable() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());

		assertEquals(true, conversation.isConversationAvailable(performer, target, null, null));
		
		BrawlPropertyUtils.startBrawl(performer, target, 10);
		assertEquals(false, conversation.isConversationAvailable(performer, target, null, null));
	}
}
