package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI
import com.fs.starfarer.api.util.Misc
import nikoblackhearted.illegals.IllegalCommodityPlugin
import org.magiclib.kotlin.getFactionMarkets

class ArmamentPlugin: IllegalCommodityPlugin() {
    companion object {
        const val STAB_MALUS = -1f
    }

    override fun apply() {
        getPlayerFac().getFactionMarkets().filter { !it.isFreePort }.forEach { it.stability.modifyFlat("ZHCWeaponPlugin", STAB_MALUS, "Legal Weaponry") }
    }

    override fun unapply() {
        Global.getSector().economy.marketsCopy.forEach { it.stability.unmodify("ZHCWeaponPlugin") }
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara(
            "Reduces faction-wide stability by ${-STAB_MALUS.toInt()} if legal",
            10f
        ).color = Misc.getNegativeHighlightColor()
    }

    override fun illegalByDefault(): Boolean = true
}