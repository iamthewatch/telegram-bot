package kz.iamthewatch.springbot.config;

import kz.iamthewatch.springbot.bot.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {

    @Bean
    public TelegramClient telegramClient(TelegramBot bot) {
        return new OkHttpTelegramClient(bot.getBotToken());
    }
}