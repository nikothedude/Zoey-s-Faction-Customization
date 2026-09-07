import com.fs.starfarer.api.impl.campaign.ids.Ranks

enum class Post {
    FACTION_LEADER {
        override fun getTypeString(): String {
            return Ranks.POST_FACTION_LEADER
        }

        override fun getUIName(): String {
            return "Faction Leader"
        }
    },

    CITIZEN {
        override fun getTypeString(): String {
            return Ranks.POST_CITIZEN
        }

        override fun getUIName(): String {
            return "Citizen"
        }
    },

    OFFICER {
        override fun getTypeString(): String {
            return Ranks.POST_OFFICER
        }

        override fun getUIName(): String {
            return "Officer"
        }
    },
    OFFICER_FOR_HIRE {
        override fun getTypeString(): String {
            return Ranks.POST_OFFICER_FOR_HIRE
        }

        override fun getUIName(): String {
            return "Officer (For hire)"
        }
    },
    MERCENARY {
        override fun getTypeString(): String {
            return Ranks.POST_MERCENARY
        }

        override fun getUIName(): String {
            return "Mercenary Officer"
        }
    },

    PATROL_COMMANDER {
        override fun getTypeString(): String {
            return Ranks.POST_PATROL_COMMANDER
        }

        override fun getUIName(): String {
            return "Patrol Commander"
        }
    },
    FLEET_COMMANDER {
        override fun getTypeString(): String {
            return Ranks.POST_FLEET_COMMANDER
        }

        override fun getUIName(): String {
            return "Fleet Commander"
        }
    },
    BASE_COMMANDER {
        override fun getTypeString(): String {
            return Ranks.POST_BASE_COMMANDER
        }

        override fun getUIName(): String {
            return "Base Commander"
        }
    },
    STATION_COMMANDER {
        override fun getTypeString(): String {
            return Ranks.POST_STATION_COMMANDER
        }

        override fun getUIName(): String {
            return "Station Commander"
        }
    },

    ADMINISTRATOR {
        override fun getTypeString(): String {
            return Ranks.POST_ADMINISTRATOR
        }

        override fun getUIName(): String {
            return "Administrator"
        }
    },
    QUARTERMASTER {
        override fun getTypeString(): String {
            return Ranks.POST_SUPPLY_OFFICER
        }

        override fun getUIName(): String {
            return "Quartermaster"
        }
    },
    PORTMASTER {
        override fun getTypeString(): String {
            return Ranks.POST_PORTMASTER
        }

        override fun getUIName(): String {
            return "Portmaster"
        }
    },

    AGENT {
        override fun getTypeString(): String {
            return Ranks.POST_AGENT
        }

        override fun getUIName(): String {
            return "Agent"
        }
    },
    SPECIAL_AGENT {
        override fun getTypeString(): String {
            return Ranks.POST_SPECIAL_AGENT
        }

        override fun getUIName(): String {
            return "Special Agent"
        }
    };

    abstract fun getTypeString(): String
    abstract fun getUIName(): String
}