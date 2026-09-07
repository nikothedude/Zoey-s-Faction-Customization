package zoey_fc.illegals.plugins

import com.fs.starfarer.api.Global
import zoey_fc.ZFCBaseZoeyScript
import zoey_fc.illegals.IllegalCommodityHandler

class IllegalCommodityScript: ZFCBaseZoeyScript() {
    override fun startImpl() {
        Global.getSector().addTransientScript(this)
    }

    override fun stopImpl() {
        Global.getSector().removeTransientScript(this)
    }

    override fun runWhilePaused(): Boolean = false

    override fun advance(amount: Float) {
        IllegalCommodityHandler.getActiveEffects().forEach { it.value.advance(amount) }
    }
}