package zoey_fc

import com.fs.starfarer.api.impl.campaign.ids.Ranks

enum class Rank {
    SPACE_SAILOR {
        override fun getTypeString(): String {
            return Ranks.SPACE_SAILOR
        }

        override fun getUIName(): String {
            return "Crewman (Space Force)"
        }
    },
    SPACE_ENSIGN {
        override fun getTypeString(): String {
            return Ranks.SPACE_ENSIGN
        }

        override fun getUIName(): String {
            return "Ensign (Space Force)"
        }
    },
    SPACE_LIEUTENANT {
        override fun getTypeString(): String {
            return Ranks.SPACE_LIEUTENANT
        }

        override fun getUIName(): String {
            return "Lieutenant (Space Force)"
        }
    },
    SPACE_COMMANDER {
        override fun getTypeString(): String {
            return Ranks.SPACE_COMMANDER
        }

        override fun getUIName(): String {
            return "Commander (Space Force)"
        }
    },
    SPACE_CAPTAIN {
        override fun getTypeString(): String {
            return Ranks.SPACE_CAPTAIN
        }

        override fun getUIName(): String {
            return "Captain (Space Force)"
        }
    },
    SPACE_ADMIRAL {
        override fun getTypeString(): String {
            return Ranks.SPACE_ADMIRAL
        }

        override fun getUIName(): String {
            return "Admiral (Space Force)"
        }
    },

    GROUND_PRIVATE {
        override fun getTypeString(): String {
            return Ranks.GROUND_PRIVATE
        }

        override fun getUIName(): String {
            return "Private (Ground Force)"
        }
    },
    GROUND_SERGEANT {
        override fun getTypeString(): String {
            return Ranks.GROUND_SERGEANT
        }

        override fun getUIName(): String {
            return "Sergeant (Ground Force)"
        }
    },
    GROUND_LIEUTENANT {
        override fun getTypeString(): String {
            return Ranks.GROUND_LIEUTENANT
        }

        override fun getUIName(): String {
            return "Lieutenant (Ground Force)"
        }
    },
    GROUND_CAPTAIN {
        override fun getTypeString(): String {
            return Ranks.GROUND_CAPTAIN
        }

        override fun getUIName(): String {
            return "Captain (Ground Force)"
        }
    },
    GROUND_MAJOR {
        override fun getTypeString(): String {
            return Ranks.GROUND_MAJOR
        }

        override fun getUIName(): String {
            return "Major (Ground Force)"
        }
    },
    GROUND_COLONEL {
        override fun getTypeString(): String {
            return Ranks.GROUND_COLONEL
        }

        override fun getUIName(): String {
            return "Colonel (Ground Force)"
        }
    },
    GROUND_GENERAL {
        override fun getTypeString(): String {
            return Ranks.GROUND_GENERAL
        }

        override fun getUIName(): String {
            return "General (Ground Force)"
        }
    },

    CITIZEN {
        override fun getTypeString(): String {
            return Ranks.CITIZEN
        }

        override fun getUIName(): String {
            return "Citizen"
        }
    },
    AGENT {
        override fun getTypeString(): String {
            return Ranks.AGENT
        }

        override fun getUIName(): String {
            return "Agent"
        }
    },
    SPECIAL_AGENT {
        override fun getTypeString(): String {
            return Ranks.SPECIAL_AGENT
        }

        override fun getUIName(): String {
            return "Special Agent"
        }
    },

    PILOT {
        override fun getTypeString(): String {
            return Ranks.PILOT
        }

        override fun getUIName(): String {
            return "Pilot"
        }
    };

    abstract fun getTypeString(): String
    abstract fun getUIName(): String
}