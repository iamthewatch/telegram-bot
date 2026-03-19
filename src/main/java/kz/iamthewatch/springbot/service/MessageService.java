package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.events.MessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ApplicationEventPublisher eventPublisher;

    public void sendMessage(Long chatId, String localizedMessage) {
        sendMessage(chatId, localizedMessage, null);
    }

    public void sendMessage(Long chatId, String localizedMessage, ReplyKeyboard keyboard) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(localizedMessage)
                .replyMarkup(keyboard)
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    public void sendTyping(Long chatId) {
        SendChatAction typing = SendChatAction.builder()
                .chatId(chatId)
                .action(ActionType.TYPING.toString())
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, typing));
    }
}