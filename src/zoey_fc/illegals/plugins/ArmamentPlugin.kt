package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.impl.campaign.ids.Stats
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets
import zoey_fc.MathUtils.trimHangingZero

class ArmamentPlugin: IllegalCommodityPlugin() {
    companion object {
        const val STAB_MALUS = -1f
        const val GROUND_DEFENSE_BONUS_PER_SIZE = 5f
    }

    override fun apply() {
        for (market in getPlayerFac().getFactionMarkets().filter { !it.isFreePort }) {
            val bonus = (market.size - 2) * GROUND_DEFENSE_BONUS_PER_SIZE
            market.stability.modifyFlat("ZHCWeaponPlugin", STAB_MALUS, "Legal Weaponry")
            market.stats.dynamic.getMod(Stats.GROUND_DEFENSES_MOD).modifyFlat("ZHCWeaponPlugin", bonus, "Heavily armed populace")
        }
    }

    override fun unapply() {
        for (market in getPlayerFac().getFactionMarkets().filter { !it.isFreePort }) {
            market.stability.unmodify("ZHCWeaponPlugin")
            market.stats.dynamic.getMod(Stats.GROUND_DEFENSES_MOD).unmodify("ZHCWeaponPlugin")
        }
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Increases faction-wide Ground Defense rating by size * ${GROUND_DEFENSE_BONUS_PER_SIZE.trimHangingZero()} if legal",
            10f
        ).color = Misc.getPositiveHighlightColor()
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()} if legal",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }

    override fun illegalByDefault(): Boolean = true
}