package nikoblackhearted.patches

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin
import com.fs.starfarer.api.campaign.FactionAPI
import com.fs.starfarer.api.campaign.TextPanelAPI
import com.fs.starfarer.api.loading.Description
import com.fs.starfarer.api.ui.PositionAPI
import com.fs.starfarer.api.ui.UIComponentAPI
import com.fs.starfarer.campaign.ui.intel.SelectedFactionInfo
import lunalib.lunaUI.elements.LunaTextfield
import org.lwjgl.input.Keyboard
import org.magiclib.kotlin.getFactionMarkets
import patchlib.api.context.AfterContext
import patchlib.api.match.ClassMatch
import patchlib.api.match.MethodMatch
import patchlib.api.patch.After
import patchlib.api.patch.Patch
import zoey_fc.ReflectionUtils
import java.awt.Color

@Patch(target = ClassMatch(SelectedFactionInfo::class))
object SelectedFactionInfoPatch {
    @JvmStatic
    @After(target = MethodMatch("sizeChanged"))
    fun modify(context: AfterContext) {
        val panel = context.self as SelectedFactionInfo
        val fac = ReflectionUtils.getDeclaredField(panel, 6) as? FactionAPI ?: return
        if (!fac.isPlayerFaction) return
        modifyUpper(context, panel, fac)
        modifyLower(context, panel, fac)
    }

    private fun modifyUpper(
        context: AfterContext,
        panel: SelectedFactionInfo,
        fac: FactionAPI
    ) {
        val textPanel = (ReflectionUtils.getDeclaredField(panel, 10)) as? TextPanelAPI ?: return
        val pos = ReflectionUtils.invoke("getPosition", textPanel) as? PositionAPI ?: return
        textPanel.replaceLastParagraph("")

        val width = pos.width + 10f
        val height = pos.height - 25f
        val newPanel = Global.getSettings().createCustom(width, height, BaseCustomUIPanelPlugin())
        val tooltip = newPanel.createUIElement(width, height, false)

        val text = fac.getCustomAttitude() ?: "For all intents and purposes, you are the sole arbiter of everything that occurs within ${fac.displayNameWithArticle}."
        val field = LunaTextfield(text, true, Color.WHITE, tooltip, width, height)
        field.position.inMid()
        field.borderColor = Color(0, 0, 0, 0)
        field.backgroundColor = Color(0, 0, 0, 0)
        field.onInput { inputs ->
            if (field.isSelected()) {
                if (inputs.any { !it.isConsumed && it.isShiftDown && it.eventValue == Keyboard.KEY_BACK && it.isKeyDownEvent }) {
                    inputs.forEach { it.consume() }
                }
                if (inputs.any { !it.isConsumed && it.isShiftDown && it.eventValue == Keyboard.KEY_RETURN && it.isKeyDownEvent }) {
                    inputs.forEach { it.consume() }
                    field.changePara(field.getText() + "\n")
                }
                fac.setCustomAttitude(field.getText())
            }
        }

        newPanel.addUIElement(tooltip).inTL(0f, 0f)
        panel.addComponent(newPanel).inTL(10f, 60f)
    }

    private fun modifyLower(
        context: AfterContext,
        panel: SelectedFactionInfo,
        fac: FactionAPI
    ) {
        val textPanel = (ReflectionUtils.getDeclaredField(panel, 9)) as? TextPanelAPI ?: return
        val pos = ReflectionUtils.invoke("getPosition", textPanel) as? PositionAPI ?: return
        textPanel.replaceLastParagraph("")
        val width = pos.width + 20f
        val height = pos.height + 15f
        val newPanel = Global.getSettings().createCustom(width, height, BaseCustomUIPanelPlugin())
        val tooltip = newPanel.createUIElement(width, height, false)

        val text = fac.getCustomDesc() ?: Global.getSettings().getDescription(fac.id, Description.Type.FACTION).text1
        val field = LunaTextfield(text, true, Color.WHITE, tooltip, width, height)
        field.position.inBL(5f, 5f)
        field.borderColor = Color(0, 0, 0, 0)
        field.backgroundColor = Color(0, 0, 0, 0)
        field.onInput { inputs ->
            if (field.isSelected()) {
                if (inputs.any { !it.isConsumed && it.isShiftDown && it.eventValue == Keyboard.KEY_BACK && it.isKeyDownEvent }) {
                    inputs.forEach { it.consume() }
                }
                if (inputs.any { !it.isConsumed && it.isShiftDown && it.eventValue == Keyboard.KEY_RETURN && it.isKeyDownEvent }) {
                    inputs.forEach { it.consume() }
                    field.changePara(field.getText() + "\n")
                }
                fac.setCustomDesc(field.getText())
            }
        }

        newPanel.addUIElement(tooltip).inBL(0f, 0f)
        panel.addComponent(newPanel).inBL(0f, 0f)
    }

    fun FactionAPI.getCustomDesc(): String? = memoryWithoutUpdate.getString("\$ZFCCustomFacDesc")
    fun FactionAPI.setCustomDesc(text: String) = memoryWithoutUpdate.set("\$ZFCCustomFacDesc", text)

    fun FactionAPI.getCustomAttitude(): String? = memoryWithoutUpdate.getString("\$ZFCCustomFacAtt")
    fun FactionAPI.setCustomAttitude(text: String) = memoryWithoutUpdate.set("\$ZFCCustomFacAtt", text)
}