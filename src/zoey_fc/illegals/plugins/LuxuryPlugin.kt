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

// reduces stability, but makes the church happy + increases luddic immigration
class LuxuryPlugin: IllegalCommodityPlugin(), MarketImmigrationModifier {
    companion object {
        const val STAB_MALUS = -1f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.addImmigrationModifier(this); it.stability.modifyFlat("ZHCLuxuryPlugin", STAB_MALUS, "Illegal Luxuries")}
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.removeImmigrationModifier(this); it.stability.unmodify("ZHCLuxuryPlugin") }
    }

    val repLossInterval = IntervalUtil(30f, 30.1f) // days
    override fun advance(amount: Float) {
        super.advance(amount)

        val days = Misc.getDays(amount)
        repLossInterval.advance(days)

        if (repLossInterval.intervalElapsed()) {
            for (faction in Global.getSector().allFactions.filter { it.isShowInIntelTab && it.isIllegal(Commodities.LUXURY_GOODS) }) {
                if (faction.relToPlayer.isAtBest(RepLevel.FRIENDLY)) {
                    faction.relToPlayer.rel += 0.01f
                }
            }
        }
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Increases %s, as well as %s with factions that have Luxury Goods banned every month.",
            10f,
            Misc.getHighlightColor(),
            "luddic immigration", "slowly improving relations"
        ).setHighlightColors(
            Global.getSector().getFaction(Factions.LUDDIC_CHURCH).baseUIColor,
            Misc.getPositiveHighlightColor()
        )
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()}",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }

    override fun modifyIncoming(
        market: MarketAPI?,
        incoming: PopulationComposition?
    ) {
        if (market == null || incoming == null) return

        val curr = incoming.comp[Factions.LUDDIC_CHURCH] ?: 0f
        incoming.comp[Factions.LUDDIC_CHURCH] = curr + 5f
        incoming.weight.modifyFlat("ZHCLuxuryPlugin", 5f, "Luddic Immigration (Illegal Luxuries)")
    }
}