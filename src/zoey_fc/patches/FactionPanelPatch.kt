package zoey_fc.patches

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin
import com.fs.starfarer.api.campaign.CoreUITabId
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.ui.Alignment
import com.fs.starfarer.api.ui.Fonts
import com.fs.starfarer.api.ui.PositionAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.api.ui.UIPanelAPI
import com.fs.starfarer.campaign.Faction
import patchlib.api.context.AfterContext
import patchlib.api.match.ClassMatch
import patchlib.api.match.FieldMatch
import patchlib.api.match.MethodMatch
import patchlib.api.patch.After
import patchlib.api.patch.Patch
import zoey_fc.FleetNameIntelPlugin
import zoey_fc.ReflectionUtils
import zoey_fc.illegals.IllegalIntelPlugin
import java.awt.Color
import java.lang.reflect.Method

@Patch(target = ClassMatch(targetPackage = "com.fs.starfarer.campaign.ui.marketinfo", fieldMatches = arrayOf(
    FieldMatch(type = Faction::class),
    FieldMatch(type = Color::class),
    FieldMatch(type = Color::class),
    FieldMatch(type = Color::class),
    FieldMatch(type = Color::class),
    FieldMatch(type = MarketAPI::class)
)))
object FactionPanelPatch {

    @JvmStatic
    @After(target = MethodMatch("createUI"))
    fun modify(context: AfterContext) {
        val panel = context.self as? UIPanelAPI ?: return

        val pos = panel.position

        val width = pos.width
        val height = pos.height * 0.65f
        val newPanel = Global.getSettings().createCustom(width, height, FactionPanelCustomPanelPlugin())
        val tooltip = newPanel.createUIElement(width, height, false)

        tooltip.addButton("Change Fleet Names", "ZFCChangeFleetNamesButton", 150f, 20f, 0f).position.inTR(40f, 0f)

        newPanel.addUIElement(tooltip).inBR(0f, 0f)
        panel.addComponent(newPanel).inBR(0f, 0f)
    }

    class FactionPanelCustomPanelPlugin: BaseCustomUIPanelPlugin() {
        override fun buttonPressed(buttonId: Any?) {
            when (buttonId) {
                "ZFCChangeFleetNamesButton" -> {
                    Global.getSector().campaignUI.showCoreUITab(CoreUITabId.INTEL, FleetNameIntelPlugin.get())
                }
                "ZFCChangePostNamesButton" -> {
                    Global.getSector().campaignUI.showCoreUITab(CoreUITabId.INTEL, FleetNameIntelPlugin.get())
                }
            }
        }
    }

}