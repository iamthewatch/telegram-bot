package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.service.KeyboardService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_CONSULTATION_REQUEST;
import static kz.iamthewatch.springbot.utils.MessageConstants.PERSON_TYPE_SELECT;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@RequiredArgsConstructor
public class ConsultationCommand implements Command {


    private final LocalizationService localizationService;
    private final KeyboardService keyboardService;
    private final MessageService messageService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, MENU_CONSULTATION_REQUEST);
        return getMessageText(update).equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, PERSON_TYPE_SELECT);
        ReplyKeyboard replyKeyboard = keyboardService.getPersonTypeKeyboard(chatId);
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }
}