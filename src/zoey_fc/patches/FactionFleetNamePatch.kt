package zoey_fc.patches

import com.fs.starfarer.campaign.Faction
import patchlib.api.context.AfterContext
import patchlib.api.match.ClassMatch
import patchlib.api.match.MethodMatch
import patchlib.api.patch.After
import patchlib.api.patch.Patch
import zoey_fc.FleetNameIntelPlugin
import zoey_fc.FleetNameIntelPlugin.Companion.getManualFleetTypeName

@Patch(target = ClassMatch(type = Faction::class))
object FactionFleetNamePatch {
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
}