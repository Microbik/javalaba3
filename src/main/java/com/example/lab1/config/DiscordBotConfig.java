package com.example.lab1.config;

import com.example.lab1.bot.DiscordBot;
import com.example.lab1.service.JokeService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordBotConfig {

    @Value("${discord.token}")
    private String discordToken;

    @Bean
    public DiscordBot discordBot(JokeService jokeService) throws Exception {
        DiscordBot bot = new DiscordBot(jokeService);

        JDABuilder.createDefault(discordToken)
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(bot)
                .build();

        return bot;
    }
}

