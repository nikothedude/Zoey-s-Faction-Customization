package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.RepLevel
import com.fs.starfarer.api.impl.campaign.ids.Commodities
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.IntervalUtil
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets

// reduces fleet size, increases stability
class ShipPlugin: IllegalCommodityPlugin() {
    companion object {
        const val STAB_BONUS = 2f
        const val FLEET_SIZE_MULT = 0.5f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.stability.modifyFlat("ZHCShipPlugin", STAB_BONUS, "Regulated Shipworks") }
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.stats.dynamic.getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyMult("ZHCShipPlugin", FLEET_SIZE_MULT, "Regulated Shipworks") }
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.stability.unmodify("ZHCShipPlugin") }
        Global.getSector().economy.marketsCopy.forEach { it.stats.dynamic.getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodify("ZHCShipPlugin") }
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Increases faction-wide stability by ${STAB_BONUS.toInt()}",
            10f
        ).color = Misc.getPositiveHighlightColor()
        tooltip.addPara(
            "Reduces faction-wide fleet size by ${FLEET_SIZE_MULT}x",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }
}