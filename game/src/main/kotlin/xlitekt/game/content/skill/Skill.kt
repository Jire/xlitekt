package xlitekt.game.content.skill

import kotlin.math.floor
import kotlin.math.pow

enum class Skill(val id: Int) {
    ATTACK(0),
    DEFENCE(1),
    STRENGTH(2),
    HITPOINTS(3),
    RANGED(4),
    PRAYER(5),
    MAGIC(6),
    COOKING(7),
    WOODCUTTING(8),
    FLETCHING(9),
    FISHING(10),
    FIREMAKING(11),
    CRAFTING(12),
    SMITHING(13),
    MINING(14),
    HERBLORE(15),
    AGILITY(16),
    THIEVING(17),
    SLAYER(18),
    FARMING(19),
    RUNECRAFTING(20),
    HUNTER(21),
    CONSTRUCTION(22);

    companion object {
        const val MAX_SKILLS = 25
        const val DEFAULT_HITPOINTS_LEVEL = 10
        const val DEFAULT_HERBLORE_LEVEL = 3
        const val DEFAULT_LEVEL = 1

        fun getXPForLevel(inputLevel: Int): Double {
            var experience = 0.0
            var output = 0.0
            for (level in 1..inputLevel) {
                experience += floor(level + 300.0 * 2.0.pow(level / 7.0))
                when {
                    level >= inputLevel -> {
                        return output
                    }
                    else -> output = floor((experience / 4.0))
                }
            }
            return 0.0
        }

        fun getLevelForXp(xp: Double, maxLevel: Int = 99): Int {
            var totalXp = 0
            var output: Int
            for (level in 1..maxLevel) {
                totalXp += floor(level + 300.0 * 2.0.pow(level / 7.0)).toInt()
                output = floor(totalXp / 4.0).toInt()
                when {
                    output - 1 >= totalXp -> {
                        return level
                    }
                }
            }
            return 99
        }

        fun valueOf(int: Int): Skill = values().first { it.id == int }

        fun valueOf(value: String): Skill {
            return when (value) {
                "Attack" -> ATTACK
                "Defence" -> DEFENCE
                "Strength" -> STRENGTH
                "Hitpoints" -> HITPOINTS
                "Ranged" -> RANGED
                "Prayer" -> PRAYER
                "Magic" -> MAGIC
                "Cooking" -> COOKING
                "Woodcutting" -> WOODCUTTING
                "Fletching" -> FLETCHING
                "Fishing" -> FISHING
                "Firemaking" -> FIREMAKING
                "Crafting" -> CRAFTING
                "Smithing" -> SMITHING
                "Mining" -> MINING
                "Herblore" -> HERBLORE
                "Agility" -> AGILITY
                "Thieving" -> THIEVING
                "Slayer" -> SLAYER
                "Farming" -> FARMING
                "Runecrafting" -> RUNECRAFTING
                "Hunter" -> HUNTER
                "Construction" -> CONSTRUCTION
                else -> throw IllegalArgumentException("Unhandled skill xlitekt.game.actor.skill.Skill.$value")
            }
        }
    }
}
