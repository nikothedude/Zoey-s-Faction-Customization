package zoey_fc.illegals

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.SectorMapAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaExtensions.addLunaToggleButton
import nikoblackhearted.illegals.CommCategory
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets
import zoey_fc.ZFCSettings

class IllegalIntelPlugin: BaseIntelPlugin() {
    companion object {
        fun get(): IllegalIntelPlugin {
            if (!Global.getSector().intelManager.hasIntelOfClass(IllegalIntelPlugin::class.java)) {
                val newInstance = IllegalIntelPlugin()
                Global.getSector().memoryWithoutUpdate["\$ZFCIllegalIntelPlugin"] = newInstance
                Global.getSector().intelManager.addIntel(newInstance, true)
            }
            return Global.getSector().memoryWithoutUpdate["\$ZFCIllegalIntelPlugin"] as IllegalIntelPlugin
        }
    }

    override fun hasLargeDescription(): Boolean {
        return true
    }

    override fun createLargeDescription(panel: CustomPanelAPI?, width: Float, height: Float) {
        if (panel == null) return
        val tooltip = panel.createUIElement(width, height, true)
        tooltip.setParaOrbitronVeryLarge()
        tooltip.addPara(name, 5f).setAlignment(Alignment.MID)
        tooltip.setParaFontDefault()
        val label = tooltip.addPara(
            "This panel allows you to %s of commodities within %s.",
            10f,
            Misc.getHighlightColor(),
            "change the legality", Global.getSector().playerFaction.displayName
        )
        label.setHighlightColors(
            Misc.getHighlightColor(),
            Global.getSector().playerFaction.baseUIColor
        )
        label.setAlignment(Alignment.MID)
        val labelTwo = tooltip.addPara(
            "Colonies under %s status will most likely be %s from the effects of legal or illegal commodities.",
            10f,
            Misc.getHighlightColor(),
            "free port", "exempt"
        )
        labelTwo.color = Misc.getGrayColor()
        labelTwo.setAlignment(Alignment.MID)
        val categorizedEntries = HashMap<CommCategory, List<IllegalCommodityPlugin>>()
        for (category in CommCategory.entries) {
            val list = ArrayList<IllegalCommodityPlugin>()
            categorizedEntries[category] = list
            for (spec in ZFCSettings.specs) {
                if (spec.value.category == category) {
                    list += spec.value
                }
            }
        }

        for (entry in categorizedEntries.toSortedMap()) {
            if (entry.value.isEmpty()) continue

            tooltip.addSectionHeading(entry.key.getUIName(), Alignment.MID, 10f)

            var last: UIComponentAPI? = null
            for (spec in entry.value) {
                if (last != null) {
                    tooltip.addSpacer(10f)
                }
                val commSpec = Global.getSettings().getCommoditySpec(spec.commodityId)
                val currentlyIllegal = Global.getSector().playerFaction.isIllegal(commSpec.id)
                val currImage = tooltip.beginImageWithText(commSpec.iconName, 40f)
                currImage.addPara(commSpec.name, 0f)
                spec.createDesc(currImage)
                currImage.addSpacer(10f)
                val button = currImage.addLunaToggleButton(currentlyIllegal, 50f, 15f)
                button.changeStateText("Illegal", "Legal")
                button.centerText()
                button.onClick {
                    if (button.value) {
                        Global.getSector().playerFaction.illegalCommodities += commSpec.id
                    } else {
                        Global.getSector().playerFaction.illegalCommodities -= commSpec.id
                    }

                    IllegalCommodityHandler.syncEffects()
                }
                val curr = tooltip.addImageWithText(10f)
                if (last != null) {
                    //curr.position.rightOfMid(last, 0f)
                }
                last = curr as UIComponentAPI?
            }
        }

        panel.addUIElement(tooltip).inTMid(0f)
        return
    }

    override fun getName(): String {
        return "Illegal Commodities"
    }

    override fun getIcon(): String {
        return "graphics/icons/reports/exports24.png"
    }

    override fun getIntelTags(map: SectorMapAPI?): Set<String?>? {
        return super.getIntelTags(map) + setOf(Factions.PLAYER)
    }

    override fun isHidden(): Boolean {
        return Global.getSector().playerFaction.getFactionMarkets().isEmpty()
    }
}