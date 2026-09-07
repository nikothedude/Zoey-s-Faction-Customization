package zoey_fc.patches

import com.fs.starfarer.campaign.Faction
import patchlib.api.context.AfterContext
import patchlib.api.match.ClassMatch
import patchlib.api.match.MethodMatch
import patchlib.api.patch.After
import patchlib.api.patch.Patch
import zoey_fc.FleetNameIntelPlugin.Companion.getManualFleetTypeName
import zoey_fc.RankAndPostIntelPlugin.Companion.getManualPostName
import zoey_fc.RankAndPostIntelPlugin.Companion.getManualRankName

@Patch(target = ClassMatch(type = Faction::class))
object FactionNamesPatch {
    @JvmStatic
    @After(target = MethodMatch("getFleetTypeName"))
    fun modifyFleetNames(context: AfterContext) {
        val self = context.self as Faction
        val type = context.args[0] as? String ?: return
        val custom = getManualFleetTypeName(self, type)
        if (!custom.isNullOrEmpty()) {
            context.returnValue = custom
        }
    }

    @JvmStatic
    @After(target = MethodMatch("getRank"))
    fun modifyRankNames(context: AfterContext) {
        val self = context.self as Faction
        val type = context.args[0] as? String ?: return
        val custom = getManualRankName(self, type)
        if (!custom.isNullOrEmpty()) {
            context.returnValue = custom
        }
    }

    @JvmStatic
    @After(target = MethodMatch("getPost"))
    fun modifyPostNames(context: AfterContext) {
        val self = context.self as Faction
        val type = context.args[0] as? String ?: return
        val custom = getManualPostName(self, type)
        if (!custom.isNullOrEmpty()) {
            context.returnValue = custom
        }
    }
}