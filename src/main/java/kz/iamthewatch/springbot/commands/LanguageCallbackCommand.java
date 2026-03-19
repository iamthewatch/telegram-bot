package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.Language;
import kz.iamthewatch.springbot.service.KeyboardFactory;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.TelegramKeyboardBuilder;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.LANGUAGE_SWITCHED;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
@RequiredArgsConstructor
public class LanguageCallbackCommand extends AbstractCallbackCommand {

    private final MessageTrackerService messageTrackerService;
    private final LocalizationService localizationService;
    private final UserSessionService userSessionService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final MessageService messageService;

    @Override
    protected boolean matches(String callbackData) {
        return Language.isCallbackCommand(callbackData);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String callbackData = getCallbackData(update);
        Language.tryFromCallback(callbackData)
                .ifPresent(language -> processLanguageChange(chatId, language));
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.name();
    }

    private void processLanguageChange(Long chatId, Language language) {
        messageTrackerService.deleteLastMessage(chatId);
        userSessionService.setLocale(chatId, language);

        String localizedMessage = localizationService.getLocalizedMessage(chatId, LANGUAGE_SWITCHED);
        ReplyKeyboard localizedKeyboard = keyboardBuilder.build(chatId, keyboardFactory.mainMenu());
        messageService.sendMessage(chatId, localizedMessage, localizedKeyboard);
    }
}