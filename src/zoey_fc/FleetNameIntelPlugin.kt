package zoey_fc

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.impl.campaign.ids.FleetTypes
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.Fonts
import com.fs.starfarer.api.ui.SectorMapAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.api.util.Misc
import lunalib.lunaExtensions.addLunaTextfield
import lunalib.lunaExtensions.addLunaToggleButton
import nikoblackhearted.illegals.CommCategory
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets
import zoey_fc.illegals.IllegalCommodityHandler
import zoey_fc.illegals.IllegalIntelPlugin

class FleetNameIntelPlugin: BaseIntelPlugin() {
    companion object {
        fun get(): FleetNameIntelPlugin {
            if (!Global.getSector().intelManager.hasIntelOfClass(FleetNameIntelPlugin::class.java)) {
                val newInstance = FleetNameIntelPlugin()
                Global.getSector().memoryWithoutUpdate["\$ZFCFleetNameIntelPlugin"] = newInstance
                Global.getSector().intelManager.addIntel(newInstance, true)
            }
            return Global.getSector().memoryWithoutUpdate["\$ZFCFleetNameIntelPlugin"] as FleetNameIntelPlugin
        }

        fun getManualFleetTypeName(fac: FactionAPI, type: String): String? = fac.memoryWithoutUpdate.getString("\$ZFCCustomFleetTypeName_$type")
        fun setManualFleetTypeName(fac: FactionAPI, type: String, text: String) = fac.memoryWithoutUpdate.set("\$ZFCCustomFleetTypeName_$type", text)
    }

    override fun hasLargeDescription(): Boolean {
        return true
    }

    override fun createLargeDescription(panel: CustomPanelAPI?, width: Float, height: Float) {
        Global.getSettings().loadTexture(icon)

        if (panel == null) return
        val fac = Global.getSector().playerFaction
        val tooltip = panel.createUIElement(width, height, true)
        tooltip.setParaOrbitronVeryLarge()
        tooltip.addPara(name, 5f).setAlignment(Alignment.MID)
        tooltip.setParaFontDefault()
        val label = tooltip.addPara(
            "This panel allows you to %s of %s operating under %s.",
            10f,
            Misc.getHighlightColor(),
            "change the names", "fleets", Global.getSector().playerFaction.displayName
        )
        label.setHighlightColors(
            Misc.getHighlightColor(),
            Misc.getHighlightColor(),
            Global.getSector().playerFaction.baseUIColor
        )
        label.setAlignment(Alignment.MID)

        var last: UIComponentAPI? = null
        for (entry in FleetType.entries) {
            val iterLabel = tooltip.addPara(entry.getUIName(), 10f)
            val text = getManualFleetTypeName(fac, entry.getTypeString()) ?: fac.getFleetTypeName(entry.getTypeString())
            val field = tooltip.addLunaTextfield(text, false, 500f, 30f)
            field.centerText()
            field.onInput {
                if (field.isSelected()) {
                    setManualFleetTypeName(fac, entry.getTypeString(), field.getText())
                }
            }
        }

        panel.addUIElement(tooltip).inTMid(0f)
        return
    }

    override fun getName(): String {
        return "Fleet Names"
    }

    override fun getIcon(): String {
        return "graphics/icons/reports/fleet24b.png"
    }

    override fun getIntelTags(map: SectorMapAPI?): Set<String?>? {
        return super.getIntelTags(map) + setOf(Factions.PLAYER)
    }

    override fun isHidden(): Boolean {
        return Global.getSector().playerFaction.getFactionMarkets().isEmpty()
    }
}