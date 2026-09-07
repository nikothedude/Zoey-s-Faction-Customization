package zoey_fc

import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.campaign.econ.MarketAPI
import zoey_fc.illegals.IllegalCommodityHandler

class ZFCCampaignListener: BaseCampaignEventListener(false) {

    override fun reportPlayerOpenedMarket(market: MarketAPI?) {
        super.reportPlayerOpenedMarket(market)

        IllegalCommodityHandler.syncEffects()
    }

    override fun reportEconomyMonthEnd() {
        super.reportEconomyMonthEnd()

        IllegalCommodityHandler.syncEffects()
    }
}