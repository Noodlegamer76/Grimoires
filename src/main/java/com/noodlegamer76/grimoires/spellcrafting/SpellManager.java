package com.noodlegamer76.grimoires.spellcrafting;

import java.util.ArrayList;
import java.util.List;

public class SpellManager {
    private static final List<Spell> spells = new ArrayList<>();

    public static List<Spell> getSpells() {
        return spells;
    }

    public static void removeSpell(Spell spell) {
        spells.remove(spell);
    }

    public static void addSpell(Spell spell) {
        for (Spell current: spells) {
            if (current.getName().equals(spell.getName())) {
                removeSpell(current);
                spells.add(spell);
                return;
            }
        }
        spells.add(spell);
    }

    public static void addAllSpells(List<Spell> spells) {
        spells.forEach(SpellManager::addSpell);
    }
}
