package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CargoAPI
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
import com.fs.starfarer.api.util.WeightedRandomPicker
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets
import org.magiclib.kotlin.getStorageCargo
import zoey_fc.MathUtils.prob

// stockpile ai cores scaling with access, capped at 400%
// rep loss with all facs
// pather interest on all planets
// luddic majority is perma disabled(?)
// TODO
class AIPlugin: IllegalCommodityPlugin() {
    companion object {
        const val BASE_CHANCE = 10f
        const val ACCESS_COEFF = 0.75f

        val coreToWeight = hashMapOf(
            Pair(Commodities.GAMMA_CORE, 100f),
            Pair(Commodities.BETA_CORE, 25f),
            Pair(Commodities.ALPHA_CORE, 1f)
        )

        fun getPicker(): WeightedRandomPicker<String> {
            val picker = WeightedRandomPicker<String>()
            coreToWeight.forEach { picker.add(it.key, it.value) }
            return picker
        }
    }

    override fun apply() {
        Global.getSector().getFaction(Factions.HEGEMONY).relToPlayer.ensureAtBest(RepLevel.INHOSPITABLE)
        Global.getSector().getFaction(Factions.LUDDIC_CHURCH).relToPlayer.ensureAtBest(RepLevel.HOSTILE)
        Global.getSector().getFaction(Factions.LUDDIC_PATH).relToPlayer.ensureAtBest(RepLevel.HOSTILE)
    }

    override fun unapply() {
        return
    }

    val repLossInterval = IntervalUtil(30f, 30.1f) // days
    val aiCoreGenInterval = IntervalUtil(30f, 30.1f)
    override fun advance(amount: Float) {
        super.advance(amount)

        val days = Misc.getDays(amount)
        repLossInterval.advance(days)
        aiCoreGenInterval.advance(days)

        if (repLossInterval.intervalElapsed()) {
            for (faction in Global.getSector().allFactions.filter { it.isShowInIntelTab && it.isIllegal(Commodities.AI_CORES) }) {
                if (faction.relToPlayer.isAtWorst(RepLevel.SUSPICIOUS)) {
                    faction.relToPlayer.rel -= 0.01f
                }
            }
        }

        if (aiCoreGenInterval.intervalElapsed()) {
            for (market in getPlayerFac().getFactionMarkets()) {
                var access = market.accessibilityMod.computeEffective(0f).coerceAtMost(4f)
                if (prob(BASE_CHANCE * (access * ACCESS_COEFF))) {
                    spawnCoreOn(market)
                }
            }
        }
    }

    private fun spawnCoreOn(market: MarketAPI) {
        val picker = getPicker()
        val coreId = picker.pick()
        val storage = market.getStorageCargo()
        storage.addCommodity(coreId, 1f)
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Legalizing AI use is a intensely suspicious act, though only a few parties - the %s and %s most notably - will take it as a declaration of %s.",
            10f,
            Misc.getNegativeHighlightColor(),
            "Luddic Church", "Luddic Path", "war"
        ).setHighlightColors(
            Global.getSector().getFaction(Factions.LUDDIC_CHURCH).baseUIColor,
            Global.getSector().getFaction(Factions.LUDDIC_PATH).baseUIColor,
            Misc.getNegativeHighlightColor()
        )

        tooltip.addPara(
            "%s of all factions without legalized AI every month.",
            10f,
            Misc.getNegativeHighlightColor(),
            "Decreases reputation"
        )

        tooltip.addPara(
            "%s feel more comfortable trading %s with %s, resulting in colonies %s and %s AI cores at a rate proportional to %s.",
            10f,
            Misc.getNegativeHighlightColor(),
            "Independent parties", "AI cores", "${getPlayerFac().displayNameWithArticle}", "purchasing", "stockpiling", "accessibility"
        ).setHighlightColors(
            Global.getSector().getFaction(Factions.INDEPENDENT).baseUIColor,
            Misc.getHighlightColor(),
            getPlayerFac().baseUIColor,
            Misc.getPositiveHighlightColor(),
            Misc.getPositiveHighlightColor(),
            Misc.getHighlightColor()
        )
    }

    override fun illegalByDefault(): Boolean = true
}