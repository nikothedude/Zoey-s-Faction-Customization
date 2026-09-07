package zoey_fc.patches

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin
import com.fs.starfarer.api.campaign.CoreUITabId
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.impl.campaign.intel.events.ht.HyperspaceTopographyEventIntel
import com.fs.starfarer.api.ui.CustomPanelAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.api.ui.UIPanelAPI
import org.magiclib.kotlin.getFactionMarkets
import patchlib.api.context.AfterContext
import patchlib.api.match.ClassMatch
import patchlib.api.match.MethodMatch
import patchlib.api.patch.After
import patchlib.api.patch.Patch
import zoey_fc.ReflectionUtils
import zoey_fc.illegals.IllegalIntelPlugin

@Patch(target = ClassMatch(targetPackage = "com.fs.starfarer.campaign.ui.intel", methodMatches = arrayOf(MethodMatch(methodName = "hasIllegal"))))
object FactionIllegalsPanelPatch {

    @JvmStatic
    @After(target = MethodMatch(parameters = arrayOf(Float::class), parameterCount = 1))
    fun modify(context: AfterContext) {
        val panel = context.self as UIPanelAPI
        val fac = ReflectionUtils.getDeclaredField(panel,  3) as? FactionAPI ?: return
        if (!fac.isPlayerFaction) return
        if (fac.getFactionMarkets().isEmpty()) return
        val plugin = IllegalPanelButtonPlugin(panel)
        val customPanel = Global.getSettings().createCustom(50f, 20f, plugin)
        plugin.custom = customPanel

        val tooltip = customPanel.createUIElement(50f, 20f, false)

        tooltip.addButton("Edit", "ZFCChangeIllegals", 50f, 20f, 0f).position.inRMid(5f)

        customPanel.addUIElement(tooltip).inMid()
        panel.addComponent(customPanel).inRMid(0f)
    }

    class IllegalPanelButtonPlugin(val panel: UIPanelAPI): BaseCustomUIPanelPlugin() {
        lateinit var custom: CustomPanelAPI
        override fun buttonPressed(buttonId: Any?) {
            when (buttonId) {
                "ZFCChangeIllegals" -> {
                    Global.getSector().campaignUI.showCoreUITab(CoreUITabId.INTEL, IllegalIntelPlugin.get())
                }
            }
        }
    }
}