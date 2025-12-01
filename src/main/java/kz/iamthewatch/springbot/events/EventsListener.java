package kz.iamthewatch.springbot.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

@Component
public class EventsListener {

    private final TelegramClient telegramClient;

    public EventsListener(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @EventListener
    public void on(MessageEvent event) throws TelegramApiException {
        BotApiMethod<? extends Serializable> message = event.getMessage();
        telegramClient.execute(message);
    }
}