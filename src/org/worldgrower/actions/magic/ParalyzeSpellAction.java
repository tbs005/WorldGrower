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
package org.worldgrower.actions.magic;

import java.io.ObjectStreamException;

import org.worldgrower.ArgumentRange;
import org.worldgrower.Constants;
import org.worldgrower.World;
import org.worldgrower.WorldObject;
import org.worldgrower.actions.AttackUtils;
import org.worldgrower.attribute.SkillProperty;
import org.worldgrower.attribute.SkillUtils;
import org.worldgrower.condition.Condition;

public class ParalyzeSpellAction implements MagicSpell {

	private static final int ENERGY_USE = 600;
	
	@Override
	public void execute(WorldObject performer, WorldObject target, int[] args, World world) {
		int turns = (int)(8 * SkillUtils.getSkillBonus(performer, getSkill()));
		target.getProperty(Constants.CONDITIONS).addCondition(Condition.PARALYZED_CONDITION, turns, world);

		SkillUtils.useEnergy(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public boolean isValidTarget(WorldObject performer, WorldObject target, World world) {
		return (target.hasProperty(Constants.CONDITIONS) && target.hasIntelligence() && performer.getProperty(Constants.KNOWN_SPELLS).contains(this));
	}

	@Override
	public int distance(WorldObject performer, WorldObject target, int[] args, World world) {
		return AttackUtils.distanceWithFreeLeftHand(performer, target, 4)
				+ SkillUtils.distanceForEnergyUse(performer, getSkill(), ENERGY_USE);
	}
	
	@Override
	public ArgumentRange[] getArgumentRanges() {
		return ArgumentRange.EMPTY_ARGUMENT_RANGE;
	}
	
	@Override
	public String getDescription(WorldObject performer, WorldObject target, int[] args, World world) {
		return "casting paralyze on " + target.getProperty(Constants.NAME);
	}

	@Override
	public String getSimpleDescription() {
		return "paralyze";
	}
	
	public Object readResolve() throws ObjectStreamException {
		return readResolveImpl();
	}

	@Override
	public int getResearchCost() {
		return 40;
	}

	@Override
	public SkillProperty getSkill() {
		return Constants.ENCHANTMENT_SKILL;
	}

	@Override
	public int getRequiredSkillLevel() {
		return 2;
	}
}
