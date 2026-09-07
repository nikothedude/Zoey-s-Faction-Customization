package nikoblackhearted.illegals

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.ui.TooltipMakerAPI

abstract class IllegalCommodityPlugin() {
    lateinit var commodityId: String
    lateinit var category: CommCategory

    abstract fun apply()
    abstract fun unapply()
    open fun reapply() {
        unapply()
        apply()
    }
    abstract fun createDesc(tooltip: TooltipMakerAPI)
    open fun advance(amount: Float) {}
    open fun illegalByDefault() = false

    fun getPlayerFac(): FactionAPI = Global.getSector().playerFaction
}