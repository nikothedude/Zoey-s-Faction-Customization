package zoey_fc.illegals

import com.fs.starfarer.api.Global
import nikoblackhearted.illegals.IllegalCommodityPlugin
import zoey_fc.ZFCSettings

object IllegalCommodityHandler {
    const val MEM_ID = "\$ZFC_illegalCommodityPlugins"

    fun getActiveEffects(): HashMap<String, IllegalCommodityPlugin> {
        if (Global.getSector().memoryWithoutUpdate[MEM_ID] == null) {
            Global.getSector().memoryWithoutUpdate[MEM_ID] = HashMap<String, IllegalCommodityPlugin>()
        }

        return Global.getSector().memoryWithoutUpdate[MEM_ID] as HashMap<String, IllegalCommodityPlugin>
    }

    fun syncEffects() {
        val active = getActiveEffects()
        val fac = Global.getSector().playerFaction
        for (iter in active.toMap()) {
            if (iter.value.illegalByDefault() == fac.isIllegal(iter.key)) {
                iter.value.unapply()
                active -= iter.key
                continue
            }
        }
        for (commodity in Global.getSettings().allCommoditySpecs) {
            val id = commodity.id
            val existing = active[id]
            if (existing != null) continue

            val plugin = ZFCSettings.specs[id] ?: continue
            if (plugin.illegalByDefault()) {
                if (fac.isIllegal(commodity.id)) continue
            } else if (!fac.isIllegal(commodity.id)) continue
            active[id] = plugin
        }

        active.forEach { it.value.reapply() }
    }

    fun getInvertedCommodities(): Map<String, IllegalCommodityPlugin> {
        return ZFCSettings.specs.filter { it.value.illegalByDefault() && !Global.getSector().playerFaction.isIllegal(it.key) }
    }

}
