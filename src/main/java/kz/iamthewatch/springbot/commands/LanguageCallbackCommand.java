package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.LanguageConstants.LANG_KZ;
import static kz.iamthewatch.springbot.utils.LanguageConstants.LANG_RU;
import static kz.iamthewatch.springbot.utils.MessageConstants.LANGUAGE_SWITCHED;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
@RequiredArgsConstructor
public class LanguageCallbackCommand implements Command {

    private final MessageTrackerService messageTrackerService;
    private final LocalizationService localizationService;
    private final UserSessionService userSessionService;
    private final KeyboardService keyboardService;
    private final MessageService messageService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }
        String callbackData = getCallbackData(update);
        return callbackData.equals(LANG_RU)
                || callbackData.equals(LANG_KZ);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String callbackData = getCallbackData(update);
        messageTrackerService.deleteLastMessage(chatId);

        if (callbackData.equals(LANG_RU)) {
            userSessionService.setLocale(chatId, "ru");
        }
        else if (callbackData.equals(LANG_KZ)) {
            userSessionService.setLocale(chatId, "kk");
        }

        String localizedMessage = localizationService.getLocalizedMessage(chatId, LANGUAGE_SWITCHED);
        ReplyKeyboard localizedKeyboard = keyboardService.getMainMenuKeyboard(chatId);
        messageService.sendMessage(chatId, localizedMessage, localizedKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.name();
    }
}