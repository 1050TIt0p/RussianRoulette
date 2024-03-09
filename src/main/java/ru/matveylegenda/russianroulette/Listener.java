package ru.matveylegenda.russianroulette;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;
import java.time.Duration;
import java.util.Random;

public class Listener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        FileConfiguration config = RussianRoulette.getInstance().getConfig();
        Message message = event.getMessage();
        Member member = event.getMember();

        if(member != null && message.getContentRaw().startsWith("!рулетка")) {
            Role role = event.getGuild().getRoleById(config.getString("settings.roleID"));

            if(member.getRoles().contains(role)) {

                Member target = message.getMentions().getMembers().get(0);

                Random random = new Random();
                int minTime = config.getInt("settings.minTime");
                int maxTime = config.getInt("settings.maxTime");
                int time = random.nextInt(maxTime - minTime + 1) + minTime;

                String embedTitle = config.getString("messages.roulette.embed.title");
                String embedDescription = config.getString("messages.roulette.embed.description")
                        .replace("%target%", target.getAsMention());

                String embedField1Title = config.getString("messages.roulette.embed.field1.title");
                String embedField1Description = config.getString("messages.roulette.embed.field1.description")
                        .replace("%executor%", member.getAsMention());

                String embedField2Title = config.getString("messages.roulette.embed.field2.title");
                String embedField2Description = config.getString("messages.roulette.embed.field2.description")
                        .replace("%time%", String.valueOf(time));

                String embedImage = config.getString("messages.roulette.embed.image");
                Color embedColor = Color.decode("0x" + config.getString("messages.roulette.embed.color"));

                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle(embedTitle)
                        .setDescription(embedDescription)
                        .addField(embedField1Title, embedField1Description, true)
                        .addField(embedField2Title, embedField2Description, true)
                        .setImage(embedImage)
                        .setColor(embedColor);

                target.timeoutFor(Duration.ofHours(time)).queue();
                message.reply("").setEmbeds(embedBuilder.build()).queue();

            } else {
                message.addReaction(Emoji.fromUnicode("❌")).queue();
            }
        }
    }
}
