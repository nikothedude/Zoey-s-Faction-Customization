package nikoblackhearted.illegals

enum class CommCategory {
    STANDARD {
        override fun getUIName(): String {
            return "Standard"
        }
    },
    TABOO {
        override fun getUIName(): String {
            return "Taboo items - Things that the sector would rather remain restricted"
        }
    },
    AI {
        override fun getUIName(): String {
            return "Artificial Intelligence"
        }
    },
    MISC {
        override fun getUIName(): String {
            return "Misc - Strange things to make illegal"
        }
    };

    abstract fun getUIName(): String
}