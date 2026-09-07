package zoey_fc

import com.fs.starfarer.api.Global
import nikoblackhearted.illegals.CommCategory
import nikoblackhearted.illegals.IllegalCommodityPlugin

object ZFCSettings {
    const val MOD_ID = "zoey_fc"
    const val CSV_PATH = "data/campaign/ZFCCommoditySpecs.csv"

    val specs = HashMap<String, IllegalCommodityPlugin>()

    fun loadSettings() {

    }

    fun generateCommoditySpecs() {
        specs.clear()

        val csv = Global.getSettings().getMergedSpreadsheetDataForMod("id", CSV_PATH, MOD_ID)

        for (index in 0 until csv.length()) {
            val row = csv.getJSONObject(index)

            val id = row.getString("id")
            if (id.startsWith("#") || id.isEmpty()) continue

            val pluginPath = row.getString("pluginPath")
            val category = row.getString("category")

            val plugin = Global.getSettings().scriptClassLoader.loadClass(pluginPath).newInstance() as IllegalCommodityPlugin
            plugin.category = CommCategory.valueOf(category)
            plugin.commodityId = id
            specs[id] = plugin
        }
    }
}