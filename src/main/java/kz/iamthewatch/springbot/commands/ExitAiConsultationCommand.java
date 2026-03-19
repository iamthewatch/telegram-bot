package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.KeyboardFactory;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.TelegramKeyboardBuilder;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXIT;
import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXITED;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@RequiredArgsConstructor
public class ExitAiConsultationCommand implements Command {

    private final UserSessionService userSessionService;
    private final LocalizationService localizationService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final MessageService messageService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        Long chatId = getChatId(update);
        if (!UserState.AI_CHAT.equals(userSessionService.getUserState(chatId))) {
            return false;
        }

        String localizedMessage = localizationService.getLocalizedMessage(chatId, AI_CHAT_EXIT);
        return getMessageText(update).equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        userSessionService.setUserState(chatId, UserState.IDLE);

        String localizedMessage = localizationService.getLocalizedMessage(chatId, AI_CHAT_EXITED);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.mainMenu());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.EXIT_AI_CONSULTATION.name();
    }
}