package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.PersonType;
import kz.iamthewatch.springbot.service.KeyboardService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CREDIT_TYPE_SELECT;
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
        return PersonType.isCallbackCommand(callbackData);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String callbackData = getCallbackData(update);
        PersonType.tryFromCallback(callbackData)
                .ifPresent(personType -> processPersonTypeAssign(chatId, personType));
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }

    private void processPersonTypeAssign(Long chatId, PersonType personType) {
        messageTrackerService.deleteLastMessage(chatId);
        userSessionService.setConsultationPersonType(chatId, personType);

        String localizedMessage = localizationService.getLocalizedMessage(chatId, CONSULTATION_CREDIT_TYPE_SELECT);
        ReplyKeyboard localizedKeyboard = keyboardService.getCreditTypeKeyboardByPersonType(chatId, personType);
        messageService.sendMessage(chatId, localizedMessage, localizedKeyboard);
    }
}