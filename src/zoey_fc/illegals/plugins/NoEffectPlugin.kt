package zoey_fc.illegals.plugins

import com.fs.starfarer.api.ui.TooltipMakerAPI
import nikoblackhearted.illegals.IllegalCommodityPlugin

class NoEffectPlugin: IllegalCommodityPlugin() {
    override fun apply() {
        return
    }

    override fun unapply() {
        return
    }

    override fun createDesc(tooltip: TooltipMakerAPI) {
        tooltip.addPara("No effect", 10f)
    }
}