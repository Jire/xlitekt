package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceEvent
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.UserInterfaceEvent
import com.runetopic.xlitekt.game.ui.onInterface
import com.runetopic.xlitekt.game.vars.VarBit
import com.runetopic.xlitekt.game.vars.VarPlayer
import com.runetopic.xlitekt.game.vars.Vars

private val closeSettingsChildId = 4
private val optionsChildId = 19
private val categoriesChildId = 23

/**
 * @author Jordan Abraham
 */
enum class Categories(val id: Int) {
    ACTIVITIES(0),
    AUDIO(1),
    CHAT(2),
    CONTROLS(3),
    DISPLAY(4),
    GAMEPLAY(5),
    INTERFACES(6),
    WARNINGS(7)
}

onInterface<UserInterface.AdvancedSettings> {
    onOpen {
        setEvent(21, 0..147, InterfaceEvent.CLICK_OPTION_1)
        setEvent(23, 0..7, InterfaceEvent.CLICK_OPTION_1)
        setEvent(19, 0..240, InterfaceEvent.CLICK_OPTION_1)
        setEvent(28, 0..122, InterfaceEvent.CLICK_OPTION_1)
    }

    onClick(childId = closeSettingsChildId) {
        interfaces -= UserInterface.AdvancedSettings
    }

    onClick(childId = categoriesChildId) {
        enumValues<Categories>().find { category -> category.id == it.slotId }?.run {
            vars[VarBit.AdvancedSettingsCategory] = id
        }
    }

    onClick(childId = optionsChildId) {
        enumValues<Categories>().find { category -> category.id == vars[VarBit.AdvancedSettingsCategory] }?.run {
            when (this) {
                Categories.ACTIVITIES -> it.onActivitiesClick(vars = this@onClick.vars)
                Categories.AUDIO -> it.onAudioClick(vars = this@onClick.vars)
                Categories.CHAT -> it.onChatClick(vars = this@onClick.vars)
                Categories.CONTROLS -> it.onControlsClick(vars = this@onClick.vars)
                Categories.DISPLAY -> it.onDisplayClick(vars = this@onClick.vars)
                Categories.GAMEPLAY -> it.onGameplayClick(vars = this@onClick.vars)
                Categories.INTERFACES -> it.onInterfacesClick(vars = this@onClick.vars)
                Categories.WARNINGS -> it.onWarningsClick(vars = this@onClick.vars)
            }
        }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onActivitiesClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onAudioClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onChatClick(vars: Vars) {
    when (slotId) {
        1 -> vars.flip(VarPlayer.ProfanityFilter)
        3 -> vars.flip(VarPlayer.ChatEffects)
        4 -> vars.flip(VarPlayer.SplitFriendsPrivateChat)
        5 -> if (vars[VarPlayer.SplitFriendsPrivateChat] == 1) {
            vars.flip(VarBit.HidePrivateChatWhenChatboxHidden)
        }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onControlsClick(vars: Vars) {
    when (slotId) {
        4 -> vars.flip(VarPlayer.SingleMouseButtonMode)
        5 -> vars.flip(VarBit.MiddleMouseButtonCameraControl)
        6 -> vars.flip(VarBit.ShiftClickDropItems)
        8 -> vars.flip(VarBit.MoveFollowerOptionsLowerDown)
        30 -> vars.flip(VarBit.EscClosesCurrentModal)
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onDisplayClick(vars: Vars) {
    when (childId) {
        19 -> when (slotId) {
            5 -> vars.flip(VarBit.ScrollWheelChangesZoomDistance)
        }
        21 -> when (slotId) {
            0, 5, 10, 15, 20 -> {
                vars[VarPlayer.ScreenBrightness] = slotId / 5
            }
        }
    }
}

fun UserInterfaceEvent.ButtonClickEvent.onGameplayClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onInterfacesClick(vars: Vars) {
}

fun UserInterfaceEvent.ButtonClickEvent.onWarningsClick(vars: Vars) {
}