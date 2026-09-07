package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets

class MarinePlugin: IllegalCommodityPlugin() {
    companion object {
    }

    override fun apply() {
    }

    override fun unapply() {
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Banning marines has no effect other than making military logistics even harder.",
            10f
        )
    }
}