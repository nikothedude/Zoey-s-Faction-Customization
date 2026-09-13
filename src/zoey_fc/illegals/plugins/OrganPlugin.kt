package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier
import com.fs.starfarer.api.impl.campaign.ids.Commodities
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.impl.campaign.intel.events.DiktatFuelBonusScript
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets
import zoey_fc.MathUtils

class OrganPlugin: IllegalCommodityPlugin(), MarketImmigrationModifier {
    companion object {
        const val STAB_MALUS = -1f
        const val POP_GROWTH_MALUS = -10f
        const val BONUS_INCOME = 1.25f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.addImmigrationModifier(this); it.stability.modifyFlat("ZHCOrganPlugin", STAB_MALUS, "Legal Organs") }
        Global.getSector().playerStats.dynamic.getStat(
            Stats.getCommodityExportCreditsMultId(Commodities.ORGANS)
        ).modifyMult(
            "ZHCOrganPlugin", BONUS_INCOME,
            "Legal organ trade"
        )
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.removeImmigrationModifier(this); it.stability.unmodify("ZHCOrganPlugin") }
        Global.getSector().playerStats.dynamic.getStat(
            Stats.getCommodityExportCreditsMultId(Commodities.ORGANS)
        ).unmodify("ZHCOrganPlugin")
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Increases faction-wide organ export value by ${BONUS_INCOME}x if legal",
            10f
        ).color = Misc.getPositiveHighlightColor()
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
            incoming.weight.modifyFlat("ZHCOrganPlugin", POP_GROWTH_MALUS, "Fears of organ harvesting")
        }
    }

    override fun illegalByDefault(): Boolean = true
}