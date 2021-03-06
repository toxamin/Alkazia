package org.bukkit.craftbukkit.scoreboard;

import java.util.Map;

import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.ScoreboardObjective;

import com.google.common.collect.ImmutableMap;

final class CraftCriteria {
	static final Map<String, CraftCriteria> DEFAULTS;
	static final CraftCriteria DUMMY;

	static {
		ImmutableMap.Builder<String, CraftCriteria> defaults = ImmutableMap.builder();

		for (Map.Entry<?, ?> entry : ((Map<?, ?>) IScoreboardCriteria.criteria).entrySet()) {
			String name = entry.getKey().toString();
			IScoreboardCriteria criteria = (IScoreboardCriteria) entry.getValue();
			if (!criteria.getName().equals(name))
				throw new AssertionError("Unexpected entry " + name + " to criteria " + criteria + "(" + criteria.getName() + ")");

			defaults.put(name, new CraftCriteria(criteria));
		}

		DEFAULTS = defaults.build();
		DUMMY = DEFAULTS.get("dummy");
	}

	final IScoreboardCriteria criteria;
	final String bukkitName;

	private CraftCriteria(String bukkitName) {
		this.bukkitName = bukkitName;
		criteria = DUMMY.criteria;
	}

	private CraftCriteria(IScoreboardCriteria criteria) {
		this.criteria = criteria;
		bukkitName = criteria.getName();
	}

	static CraftCriteria getFromNMS(ScoreboardObjective objective) {
		return DEFAULTS.get(objective.getCriteria().getName());
	}

	static CraftCriteria getFromBukkit(String name) {
		final CraftCriteria criteria = DEFAULTS.get(name);
		if (criteria != null)
			return criteria;
		return new CraftCriteria(name);
	}

	@Override
	public boolean equals(Object that) {
		if (!(that instanceof CraftCriteria))
			return false;
		return ((CraftCriteria) that).bukkitName.equals(bukkitName);
	}

	@Override
	public int hashCode() {
		return bukkitName.hashCode() ^ CraftCriteria.class.hashCode();
	}
}
