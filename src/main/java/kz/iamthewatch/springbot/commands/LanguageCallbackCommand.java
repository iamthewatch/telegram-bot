package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.events.MessageEvent;
import kz.iamthewatch.springbot.service.KeyboardService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.iamthewatch.springbot.commands.LanguageCommand.LANG_KZ;
import static kz.iamthewatch.springbot.commands.LanguageCommand.LANG_RU;

@Component
@RequiredArgsConstructor
public class LanguageCallbackCommand implements Command {

    private final MessageTrackerService messageTrackerService;
    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;
    private final UserSessionService userSessionService;
    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }
        String callbackData = update.getCallbackQuery().getData();
        return callbackData.equals(LANG_RU)
                || callbackData.equals(LANG_KZ);
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callbackData = update.getCallbackQuery().getData();
        messageTrackerService.deleteLastMessage(chatId);

        if (callbackData.equals(LANG_RU)) {
            userSessionService.setLocale(chatId, "ru");
            String switched = localizationService.getLocalizedMessage(chatId, "language.switched");
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                    .text(switched)
                    .build();
            eventPublisher.publishEvent(new MessageEvent(this, message));
        }
        else if (callbackData.equals(LANG_KZ)) {
            userSessionService.setLocale(chatId, "kk");
            String switched = localizationService.getLocalizedMessage(chatId, "language.switched");
            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                    .text(switched)
                    .build();
            eventPublisher.publishEvent(new MessageEvent(this, message));
        }
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.name();
    }
}