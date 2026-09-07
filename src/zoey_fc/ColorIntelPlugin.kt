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
import lunalib.backend.ui.components.LunaUIColorPicker
import lunalib.lunaExtensions.addLunaColorPicker
import lunalib.lunaExtensions.addLunaProgressBar
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

class ColorIntelPlugin: BaseIntelPlugin() {
    companion object {
        fun get(): ColorIntelPlugin {
            if (!Global.getSector().intelManager.hasIntelOfClass(ColorIntelPlugin::class.java)) {
                val newInstance = ColorIntelPlugin()
                Global.getSector().memoryWithoutUpdate["\$ZFCColorIntelPlugin"] = newInstance
                Global.getSector().intelManager.addIntel(newInstance, true)
            }
            return Global.getSector().memoryWithoutUpdate["\$ZFCColorIntelPlugin"] as ColorIntelPlugin
        }

        fun getCustomColor(fac: FactionAPI): Color? = fac.memoryWithoutUpdate["\$ZFCCustomColor"] as? Color
        fun setCustomColor(fac: FactionAPI, color: Color) = fac.memoryWithoutUpdate.set("\$ZFCCustomColor", color)

        fun syncFacColor(fac: FactionAPI) {
            val color = getCustomColor(fac) ?: return
            val newColor = Color(color.red, color.green, color.blue, (color.alpha * 0.49).toInt())
            val darkColor = Color(color.red, color.green, color.blue, (color.alpha * 0.68f).toInt()).darker().darker()
            val gridColor = Color(color.red, color.green, color.blue, (color.alpha * 0.29f).toInt()).darker()
            fac.factionSpec.color = newColor
            fac.factionSpec.baseUIColor = color
            fac.factionSpec.darkUIColor = darkColor
            fac.factionSpec.gridUIColor = gridColor
        }
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
            "This panel allows you to %s of %s.",
            10f,
            Misc.getHighlightColor(),
            "change the color", Global.getSector().playerFaction.displayName
        )
        label.setHighlightColors(
            Misc.getHighlightColor(),
            Global.getSector().playerFaction.baseUIColor
        )
        label.setAlignment(Alignment.MID)

        /*val picker = tooltip.addLunaColorPicker(fac.color.getHue(), 500f, 500f)
        val alpha = tooltip.addLunaProgressBar(fac.color.alpha.toFloat(), 0f, 255f, 200f, 50f, Misc.getTextColor())
        picker.onHeld {
            val color = picker.getColor()
            color.setAlpha(alpha.getValue().toInt())

            setCustomColor(fac, color)

            syncFacColor(fac)
        }*/

        val color = getCustomColor(fac) ?: fac.color
        tooltip.addPara("Red", 0f)
        val r = tooltip.addLunaTextfield(color.red.toString(), false, 50f, 50f)
        tooltip.addPara("Green", 0f)
        val g = tooltip.addLunaTextfield(color.green.toString(), false, 50f, 50f)
        tooltip.addPara("Blue", 0f)
        val b = tooltip.addLunaTextfield(color.blue.toString(), false, 50f, 50f)
        tooltip.addPara("Alpha", 0f)
        val a = tooltip.addLunaTextfield(color.alpha.toString(), false, 50f, 50f)

        r.onInput {
            if (r.isSelected() || g.isSelected() || b.isSelected() || a.isSelected()) {

                val rValue = r.getText().toIntOrNull()?.coerceAtMost(255)?.coerceAtLeast(0) ?: 0
                val gValue = g.getText().toIntOrNull()?.coerceAtMost(255)?.coerceAtLeast(0) ?: 0
                val bValue = b.getText().toIntOrNull()?.coerceAtMost(255)?.coerceAtLeast(0) ?: 0
                val aValue = a.getText().toIntOrNull()?.coerceAtMost(255)?.coerceAtLeast(0) ?: 0

                val newColor = Color(rValue, gValue, bValue, aValue)
                setCustomColor(fac, newColor)
                syncFacColor(fac)
            }
        }

        panel.addUIElement(tooltip).inTMid(0f)

        return
    }

    override fun getName(): String {
        return "Color"
    }

    override fun getIcon(): String {
        return "graphics/icons/intel/log_sunburst.png"
    }

    override fun getIntelTags(map: SectorMapAPI?): Set<String?>? {
        return super.getIntelTags(map) + setOf(Factions.PLAYER)
    }
}