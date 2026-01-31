package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.service.KeyboardFactory;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.TelegramKeyboardBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.LANGUAGE_SELECT;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_LANGUAGE;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@RequiredArgsConstructor
public class LanguageCommand implements Command {

    private final MessageService messageService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, MENU_LANGUAGE);
        return getMessageText(update).equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, LANGUAGE_SELECT);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.language());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.name();
    }
}