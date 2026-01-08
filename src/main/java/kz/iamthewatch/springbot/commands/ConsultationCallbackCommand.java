package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CREDIT_TYPE_SELECT;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_FL;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_UL;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.service.KeyboardService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
@RequiredArgsConstructor
public class ConsultationCallbackCommand implements Command {

    private final UserSessionService userSessionService;
    private final MessageTrackerService messageTrackerService;
    private final LocalizationService localizationService;
    private final KeyboardService keyboardService;
    private final MessageService messageService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }
        String callbackData = getCallbackData(update);
        return PERSON_FL.equals(callbackData) || PERSON_UL.equals(callbackData);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String personType = getCallbackData(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, CONSULTATION_CREDIT_TYPE_SELECT);
        ReplyKeyboard localizedKeyboard = keyboardService.getCreditTypeKeyboard(chatId);
        messageTrackerService.deleteLastMessage(chatId);
        userSessionService.setConsultationPersonType(chatId, personType);
        messageService.sendMessage(chatId, localizedMessage,  localizedKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }
}