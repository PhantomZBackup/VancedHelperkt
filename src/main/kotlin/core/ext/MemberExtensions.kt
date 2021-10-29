package core.ext

import core.database.allowedQuoteRoleIds
import core.database.deleteUserWarns
import core.database.getUserWarns
import core.database.moderatorRoleIds
import core.util.Infraction
import core.util.sendInfractionToModLogChannel
import dev.kord.core.entity.Member
import kotlinx.coroutines.flow.first

suspend fun Member.checkWarnForTooManyInfractions() {
    val userId = id.asString
    val warns = getUserWarns(userId)

    if (warns != null && warns.reasons.size >= 3) {
        val reason = "Too many infractions"
        kick(reason)
        sendInfractionToModLogChannel(
            Infraction.Kick(this, kord.getSelf(), reason)
        )
        deleteUserWarns(userId)
    }
}

suspend fun Member.canInteractWith(target: Member): Boolean {
    val issuer = this

    if (issuer.isMod && target.isMod) {
        val issuerHighestModeratorRolePosition = issuer.roles.first {
            moderatorRoleIds.contains(it.id.value.toLong())
        }.getPosition()

        val targetHighestModeratorRolePosition = target.roles.first {
            moderatorRoleIds.contains(it.id.value.toLong())
        }.getPosition()


        return issuerHighestModeratorRolePosition > targetHighestModeratorRolePosition
    }

    val issuerHighestRolePosition = issuer.roles.first().getPosition()
    val targetHighestRolePosition = target.roles.first().getPosition()

    return issuerHighestRolePosition > targetHighestRolePosition
}

val Member.isMod
    get() = moderatorRoleIds.any { modRoleId ->
        roleIds.map { roleId ->
            roleId.value.toLong()
        }.contains(modRoleId)
    }

val Member.isQuoter
    get() = allowedQuoteRoleIds.any { modRoleId ->
        roleIds.map { roleId ->
            roleId.value.toLong()
        }.contains(modRoleId)
    }