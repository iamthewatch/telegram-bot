package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.KeyboardFactory;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.TelegramKeyboardBuilder;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.ASK_QUESTION;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_ASK_QUESTION;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@AllArgsConstructor
public class AskQuestionCommand implements Command {

    private final UserSessionService userSessionService;
    private final MessageService messageService;
    private final LocalizationService localizationService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = getChatId(update);
        if (!UserState.IDLE.equals(userSessionService.getUserState(chatId))) {
            return false;
        }
        String localizedMessage = localizationService.getLocalizedMessage(chatId, MENU_ASK_QUESTION);
        return getMessageText(update).equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        userSessionService.setUserState(chatId, UserState.AI_CHAT);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, ASK_QUESTION);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.aiChat());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.ASK_QUESTION.name();
    }
}