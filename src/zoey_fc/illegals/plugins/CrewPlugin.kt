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

class CrewPlugin: IllegalCommodityPlugin(), MarketImmigrationModifier {
    companion object {
        const val STAB_MALUS = -5f
        const val POP_GROWTH_MALUS = -5000f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.addImmigrationModifier(this); it.stability.modifyFlat("ZHCCrewPlugin", STAB_MALUS, "Everyone Is Illegal") }
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.removeImmigrationModifier(this); it.stability.unmodify("ZHCCrewPlugin") }
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Banning humans, predictably, has disastrous effects.",
            10f
        )
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()}",
            10f
        ).color = Misc.getNegativeHighlightColor()
        tooltip.addPara(
            "Reduces faction-wide population growth by ${-POP_GROWTH_MALUS.toInt()}",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }

    override fun modifyIncoming(
        market: MarketAPI,
        incoming: PopulationComposition?
    ) {
        if (incoming == null) return

        if (market.faction.isPlayerFaction) {
            incoming.weight.modifyFlat("ZHCAIPlugin", POP_GROWTH_MALUS, "No immigration")
        }
    }
}