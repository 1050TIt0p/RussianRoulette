package ru.matveylegenda.russianroulette;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

public final class RussianRoulette extends JavaPlugin {
    private static RussianRoulette instance;
    private JDA jda;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        String token = getConfig().getString("token");
        try {
            jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(new Listener())
                    .build();
        } catch (Exception e) {
            getLogger().severe("Ошибка: " + e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static RussianRoulette getInstance() {
        return instance;
    }
}
