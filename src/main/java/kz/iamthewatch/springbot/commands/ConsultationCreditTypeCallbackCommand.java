package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CONFIRM;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.CreditType;
import kz.iamthewatch.springbot.enums.PersonType;
import kz.iamthewatch.springbot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class ConsultationCreditTypeCallbackCommand implements Command {

    private final UserSessionService userSessionService;
    private final MessageTrackerService messageTrackerService;
    private final LocalizationService localizationService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final MessageService messageService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }
        return CreditType.isCallbackCommand(getCallbackData(update));
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String callbackData = getCallbackData(update);
        String personTypeCode = userSessionService.getConsultationPersonType(chatId);

        PersonType personType = PersonType.tryFromCallback(personTypeCode)
                .orElse(PersonType.PERSON_FL);

        CreditType creditType = CreditType.tryFromCallback(callbackData)
                .orElse(CreditType.OTHER);

        messageTrackerService.deleteLastMessage(chatId);
        userSessionService.setConsultationCreditType(chatId, creditType);

        String localizedMessage = localizationService.getLocalizedMessage(
                chatId,
                CONSULTATION_CONFIRM,
                localizationService.getLocalizedMessage(chatId, personType.getMessageKey()),
                localizationService.getLocalizedMessage(chatId, creditType.getMessageKey())
        );

        ReplyKeyboard localizedKeyboard = keyboardBuilder.build(chatId, keyboardFactory.confirmation());
        messageService.sendMessage(chatId, localizedMessage,  localizedKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }
}