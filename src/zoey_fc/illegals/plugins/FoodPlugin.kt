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
class FoodPlugin: IllegalCommodityPlugin() {
    companion object {
        const val STAB_MALUS = -3f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.stability.modifyFlat("ZHCFoodPlugin", STAB_MALUS, "Illegal Food") }
        Global.getSector().listenerManager.addListener(this)
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.stability.unmodify("ZHCFoodPlugin") }
        Global.getSector().listenerManager.removeListener(this)
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()}",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }
}