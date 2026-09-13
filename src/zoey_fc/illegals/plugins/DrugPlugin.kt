package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets

class DrugPlugin: IllegalCommodityPlugin(), MarketImmigrationModifier {
    companion object {
        const val POP_GROWTH_BONUS = 5f
        const val STAB_MALUS = -1f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.addImmigrationModifier(this); it.stability.modifyFlat("ZHCDrugPlugin", STAB_MALUS, "Legal Drugs") }
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.removeImmigrationModifier(this); it.stability.unmodify("ZHCDrugPlugin") }
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Increases faction-wide population growth by ${POP_GROWTH_BONUS.toInt()} if legal",
            10f
        ).color = Misc.getPositiveHighlightColor()
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()} if legal",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }

    override fun modifyIncoming(
        market: MarketAPI,
        incoming: PopulationComposition?
    ) {
        if (incoming == null) return

        if (market.faction.isPlayerFaction) {
            incoming.weight.modifyFlat("ZHCDrugPlugin", POP_GROWTH_BONUS, "Chemical Abundance")
        }
    }

    override fun illegalByDefault(): Boolean = true
}