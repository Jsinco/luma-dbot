package dev.jsinco.luma.discord

import dev.jsinco.discord.framework.FrameWork
import dev.jsinco.discord.framework.commands.DiscordCommand
import dev.jsinco.discord.framework.util.Module
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

@DiscordCommand(
    name = "suggest",
    description = "Make a new suggestion",
    permission = Permission.MESSAGE_SEND,
    guildOnly = true
)
class SuggestionsModule : Module {

    override fun persistRegistration(): Boolean = true

    override fun execute(event: SlashCommandInteractionEvent) {
        val suggestion = event.getOption("suggestion")?.asString ?: return
        sendSuggestionEmbed(event.user, suggestion)
        event.reply("Your suggestion has been posted!").queue()
    }

    override fun getOptions(): List<OptionData> {
        return listOf(
            OptionData(OptionType.STRING, "suggestion", "Enter suggestion", true)
        )
    }

    @SubscribeEvent
    fun onMessageReceived(event: MessageReceivedEvent) {
        if (!event.message.contentRaw.startsWith("!suggest ")) return

        sendSuggestionEmbed(event.author, event.message.contentRaw.removePrefix("!suggest "))
    }


    private fun sendSuggestionEmbed(user: User, txt: String) {
        val channel = FrameWork.getJda().getTextChannelById("1188329205973401621") ?: return

        val embedBuilder = EmbedBuilder()
        embedBuilder.setTitle("**Suggestion from ${user.effectiveName}**")
        embedBuilder.setDescription(txt)
        embedBuilder.setColor(16029942)
        embedBuilder.setThumbnail(user.effectiveAvatarUrl)
        val message = channel.sendMessageEmbeds(embedBuilder.build()).complete()
        message.addReaction(Emoji.fromUnicode("U+2705")).queue()
        message.addReaction(Emoji.fromUnicode("U+274C")).queue()
    }
}