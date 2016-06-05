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
package org.worldgrower.gui;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;

import org.worldgrower.Args;
import org.worldgrower.DungeonMaster;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.Actions;
import org.worldgrower.actions.magic.MagicSpell;
import org.worldgrower.actions.magic.ResearchSpellAction;
import org.worldgrower.gui.music.SoundIdReader;
import org.worldgrower.gui.start.Game;
import org.worldgrower.gui.util.ListInputDialog;
import org.worldgrower.gui.util.TextInputDialog;
import org.worldgrower.util.NumberUtils;

public class GuiResearchMagicSpellAction extends AbstractAction {

	private WorldObject playerCharacter;
	private ImageInfoReader imageInfoReader;
	private SoundIdReader soundIdReader;
	private World world;
	private WorldPanel parent;
	private DungeonMaster dungeonMaster;
	private WorldObject target;
	
	public GuiResearchMagicSpellAction(WorldObject playerCharacter, ImageInfoReader imageInfoReader, SoundIdReader soundIdReader, World world, WorldPanel parent, DungeonMaster dungeonMaster, WorldObject target) {
		super();
		this.playerCharacter = playerCharacter;
		this.imageInfoReader = imageInfoReader;
		this.soundIdReader = soundIdReader;
		this.world = world;
		this.parent = parent;
		this.dungeonMaster = dungeonMaster;
		this.target = target;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<MagicSpell> magicSpellsToResearch =  Actions.getMagicSpellsToResearch(playerCharacter);
		if (magicSpellsToResearch.size() > 0) { 
			String[] magicSpellDescriptions = Actions.getMagicSpellDescriptions(magicSpellsToResearch).toArray(new String[0]);
			String magicSpellDescription = new ListInputDialog("Choose Magic Spell", magicSpellDescriptions, soundIdReader).showMe();
			if (magicSpellDescription != null) {
				
				int indexOfMagicSpell = Arrays.asList(magicSpellDescriptions).indexOf(magicSpellDescription);
				MagicSpell magicSpell = magicSpellsToResearch.get(indexOfMagicSpell);
				ResearchSpellAction researchSpellAction = Actions.getResearchSpellActionFor(magicSpell);
				
				String textDialogMessage = "Research for how many turns? (0 - " + (magicSpell.getResearchCost()+1) + ")";
				String turnsString = new TextInputDialog(textDialogMessage, true, soundIdReader).showMe();
				if ((turnsString != null) && (NumberUtils.isNumeric(turnsString))) {
					int turns = Integer.parseInt(turnsString);
					
					Game.executeMultipleTurns(playerCharacter, researchSpellAction, Args.EMPTY, world, dungeonMaster, target, parent, turns, soundIdReader);
				}
			}
		}
	}
}