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
import lunalib.lunaExtensions.addLunaColorPicker
import lunalib.lunaExtensions.addLunaTextfield
import lunalib.lunaExtensions.addLunaToggleButton
import nikoblackhearted.illegals.CommCategory
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets
import org.magiclib.kotlin.setAlpha
import zoey_fc.MathUtils.getHue
import zoey_fc.illegals.IllegalCommodityHandler
import zoey_fc.illegals.IllegalIntelPlugin
import java.awt.Color

class RankAndPostIntelPlugin: BaseIntelPlugin() {
    companion object {
        fun get(): RankAndPostIntelPlugin {
            if (!Global.getSector().intelManager.hasIntelOfClass(RankAndPostIntelPlugin::class.java)) {
                val newInstance = RankAndPostIntelPlugin()
                Global.getSector().memoryWithoutUpdate["\$ZFCRankAndPostIntelPlugin"] = newInstance
                Global.getSector().intelManager.addIntel(newInstance, true)
            }
            return Global.getSector().memoryWithoutUpdate["\$ZFCRankAndPostIntelPlugin"] as RankAndPostIntelPlugin
        }

        fun getManualRankName(fac: FactionAPI, type: String): String? = fac.memoryWithoutUpdate.getString("\$ZFCCustomRankTypeName_$type")
        fun setManualRankName(fac: FactionAPI, type: String, text: String) = fac.memoryWithoutUpdate.set("\$ZFCCustomRankTypeName_$type", text)

        fun getManualPostName(fac: FactionAPI, type: String): String? = fac.memoryWithoutUpdate.getString("\$ZFCCustomPostTypeName_$type")
        fun setManualPostName(fac: FactionAPI, type: String, text: String) = fac.memoryWithoutUpdate.set("\$ZFCCustomPostTypeName_$type", text)
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
            "This panel allows you to %s and %s of %s citizenry.",
            10f,
            Misc.getHighlightColor(),
            "change the posts", "ranks", Global.getSector().playerFaction.displayName
        )
        label.setHighlightColors(
            Misc.getHighlightColor(),
            Misc.getHighlightColor(),
            Global.getSector().playerFaction.baseUIColor
        )
        label.setAlignment(Alignment.MID)

        var last: UIComponentAPI? = null
        tooltip.addSectionHeading("Posts - Job positions", Alignment.MID, 5f)
        for (entry in Post.entries) {
            val iterLabel = tooltip.addPara(entry.getUIName(), 10f)
            val text = getManualPostName(fac, entry.getTypeString()) ?: fac.getPost(entry.getTypeString())
            val field = tooltip.addLunaTextfield(text, false, 500f, 30f)
            field.centerText()
            field.onInput {
                if (field.isSelected()) {
                    setManualPostName(fac, entry.getTypeString(), field.getText())
                }
            }
        }
        tooltip.addSectionHeading("Ranks - Prestige", Alignment.MID, 5f)
        for (entry in Rank.entries) {
            val iterLabel = tooltip.addPara(entry.getUIName(), 10f)
            val text = getManualRankName(fac, entry.getTypeString()) ?: fac.getRank(entry.getTypeString())
            val field = tooltip.addLunaTextfield(text, false, 500f, 30f)
            field.centerText()
            field.onInput {
                if (field.isSelected()) {
                    setManualRankName(fac, entry.getTypeString(), field.getText())
                }
            }
        }

        panel.addUIElement(tooltip).inTMid(0f)
        return
    }

    override fun getName(): String {
        return "Ranks & Posts"
    }

    override fun getIcon(): String {
        return "graphics/icons/reports/admins24.png"
    }

    override fun getIntelTags(map: SectorMapAPI?): Set<String?>? {
        return super.getIntelTags(map) + setOf(Factions.PLAYER)
    }

    override fun isHidden(): Boolean {
        return Global.getSector().playerFaction.getFactionMarkets().isEmpty()
    }
}