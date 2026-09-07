package zoey_fc

import com.fs.starfarer.api.impl.campaign.ids.FleetTypes

enum class FleetType {
    PATROL_SMALL {
        override fun getTypeString(): String {
            return FleetTypes.PATROL_SMALL
        }

        override fun getUIName(): String {
            return "Small Patrol"
        }
    },
    PATROL_MEDIUM {
        override fun getTypeString(): String {
            return FleetTypes.PATROL_MEDIUM
        }

        override fun getUIName(): String {
            return "Medium Patrol"
        }
    },
    PATROL_LARGE {
        override fun getTypeString(): String {
            return FleetTypes.PATROL_LARGE
        }

        override fun getUIName(): String {
            return "Large Patrol"
        }
    },
    TRADE_SMALL {
        override fun getTypeString(): String {
            return FleetTypes.TRADE_SMALL
        }

        override fun getUIName(): String {
            return "Small Trader"
        }
    },
    TRADE_MEDIUM {
        override fun getTypeString(): String {
            return FleetTypes.TRADE
        }

        override fun getUIName(): String {
            return "Trader"
        }
    },
    TASK_FORCE {
        override fun getTypeString(): String {
            return FleetTypes.TASK_FORCE
        }

        override fun getUIName(): String {
            return "Task Force"
        }
    },
    RELIEF_FLEET {
        override fun getTypeString(): String {
            return FleetTypes.FOOD_RELIEF_FLEET
        }

        override fun getUIName(): String {
            return "Relief Fleet"
        }
    };

    abstract fun getTypeString(): String
    abstract fun getUIName(): String
}