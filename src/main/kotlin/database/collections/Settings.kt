package database.collections

data class Settings(
    val prefix: String = "-",
    val guildId: String,
    val logChannel: String = "",
    val modlogChannel: String = "",
    val boosterChannel: String = "",
    val errorChannel: String = "",
    val muteRole: String = "",
    val boosterRole: String = "",
    val boosterChat: String = "",
    val owners: List<String> = listOf("202115709231300617", "256143257472335872", "423915768191647755"),
    val modRoles: List<String> = emptyList(),
    val allowedQuoteRoles: List<String> = emptyList()
)