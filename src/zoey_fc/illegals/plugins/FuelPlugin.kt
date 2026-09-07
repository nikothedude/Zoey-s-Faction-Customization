package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.campaign.RepLevel
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier
import com.fs.starfarer.api.impl.campaign.ids.Commodities
import com.fs.starfarer.api.impl.campaign.ids.Factions
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.IntervalUtil
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets

// reduces stability. why would you do this. i guess the church likes it
class FuelPlugin: IllegalCommodityPlugin() {
    companion object {
        const val STAB_BONUS = 1f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.stability.modifyFlat("ZHCFuelPlugin", STAB_BONUS, "Illegal Fuel") }
        Global.getSector().listenerManager.addListener(this)
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.stability.unmodify("ZHCFuelPlugin") }
        Global.getSector().listenerManager.removeListener(this)
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Increases faction-wide stability by ${STAB_BONUS.toInt()}",
            10f
        ).color = Misc.getPositiveHighlightColor()
    }
}