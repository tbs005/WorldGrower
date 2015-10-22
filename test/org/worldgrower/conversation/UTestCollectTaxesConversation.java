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
import org.worldgrower.DoNothingWorldOnTurn;
import org.worldgrower.TestUtils;
import org.worldgrower.World;
import org.worldgrower.WorldImpl;
import org.worldgrower.WorldObject;
import org.worldgrower.attribute.IdList;
import org.worldgrower.attribute.IdRelationshipMap;
import org.worldgrower.generator.BuildingGenerator;
import org.worldgrower.goal.GroupPropertyUtils;

public class UTestCollectTaxesConversation {

	private final CollectTaxesConversation conversation = Conversations.COLLECT_TAXES_CONVERSATION;
	
	@Test
	public void testGetReplyPhrases() {
		WorldObject performer = TestUtils.createIntelligentWorldObject(1, Constants.RELATIONSHIPS, new IdRelationshipMap());
		WorldObject target = TestUtils.createIntelligentWorldObject(2, Constants.RELATIONSHIPS, new IdRelationshipMap());
		
		ConversationContext context = new ConversationContext(performer, target, null, null, null, 0);
		List<Response> replyPhrases = conversation.getReplyPhrases(context);
		assertEquals(2, replyPhrases.size());
		assertEquals("Yes, I'll pay my taxes", replyPhrases.get(0).getResponsePhrase());
		assertEquals("No, I won't pay my taxes", replyPhrases.get(1).getResponsePhrase());
	}
	
	@Test
	public void testGetReplyPhrase() {
		World world = new WorldImpl(0, 0, null, new DoNothingWorldOnTurn());
		WorldObject performer = TestUtils.createIntelligentWorldObject(7, Constants.HOUSES, new IdList());
		WorldObject target = TestUtils.createIntelligentWorldObject(8, Constants.HOUSES, new IdList());

		int houseId = BuildingGenerator.generateHouse(0, 0, world, 1f);
		target.getProperty(Constants.HOUSES).add(houseId);
		target.setProperty(Constants.GOLD, 200);
		
		createDefaultVillagersOrganization(world, target);
				
		ConversationContext context = new ConversationContext(performer, target, null, null, world, 0);
		assertEquals(0, conversation.getReplyPhrase(context).getId());
		
		moveTurnsForword(world, 2000);
		target.setProperty(Constants.GOLD, 0);
		assertEquals(1, conversation.getReplyPhrase(context).getId());
	}

	private void createDefaultVillagersOrganization(World world, WorldObject target) {
		WorldObject organization = createVillagersOrganization(world);
		organization.getProperty(Constants.TAXES_PAID_TURN).incrementValue(target, 1);
		organization.setProperty(Constants.HOUSE_TAX_RATE, 2);
	}

	private void moveTurnsForword(World world, int count) {
		for(int i=0; i<count; i++) {
			world.nextTurn();
		}
	}

	private WorldObject createVillagersOrganization(World world) {
		WorldObject organization = GroupPropertyUtils.createVillagersOrganization(world);
		organization.setProperty(Constants.ID, 1);
		world.addWorldObject(organization);
		return organization;
	}
	
	//TODO: less setup for unit tests?
}