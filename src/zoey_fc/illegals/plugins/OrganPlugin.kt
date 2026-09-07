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

class OrganPlugin: IllegalCommodityPlugin(), MarketImmigrationModifier {
    companion object {
        const val STAB_MALUS = -1f
        const val POP_GROWTH_MALUS = -10f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.stability.modifyFlat("ZHCOrganPlugin", STAB_MALUS, "Legal Organs") }
        Global.getSector().listenerManager.addListener(this)
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.stability.unmodify("ZHCOrganPlugin") }
        Global.getSector().listenerManager.removeListener(this)
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()} if legal",
            10f
        ).color = Misc.getNegativeHighlightColor()
        tooltip.addPara(
            "Reduces faction-wide population growth by ${-POP_GROWTH_MALUS.toInt()} if legal",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }

    override fun modifyIncoming(
        market: MarketAPI,
        incoming: PopulationComposition?
    ) {
        if (incoming == null) return

        if (market.faction.isPlayerFaction) {
            incoming.weight.modifyFlat("ZHCAIPlugin", POP_GROWTH_MALUS, "Legal Organs")
        }
    }

    override fun illegalByDefault(): Boolean = true
}