package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_AUTO;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_CONSUMER;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_MORTGAGE;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_OTHER;
import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CONFIRM;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_FL;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class ConsultationCreditTypeCallbackCommand implements Command {

    private static final Set<String> SUPPORTED = new HashSet<>(Arrays.asList(
            CREDIT_CONSUMER, CREDIT_MORTGAGE, CREDIT_AUTO, CREDIT_OTHER
    ));

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
        return SUPPORTED.contains(getCallbackData(update));
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String creditType = getCallbackData(update);

        messageTrackerService.deleteLastMessage(chatId);
        userSessionService.setConsultationCreditType(chatId, creditType);

        String personType = userSessionService.getConsultationPersonType(chatId);
        String personLabel = PERSON_FL.equals(personType)
                ? localizationService.getLocalizedMessage(chatId, "person.type.fl")
                : localizationService.getLocalizedMessage(chatId, "person.type.ul");

        String creditLabel = switch (creditType) {
            case CREDIT_CONSUMER -> localizationService.getLocalizedMessage(chatId, "credit.type.consumer");
            case CREDIT_MORTGAGE -> localizationService.getLocalizedMessage(chatId, "credit.type.mortgage");
            case CREDIT_AUTO -> localizationService.getLocalizedMessage(chatId, "credit.type.auto");
            default -> localizationService.getLocalizedMessage(chatId, "credit.type.other");
        };

        String localizedMessage = localizationService.getLocalizedMessage(
                chatId,
                CONSULTATION_CONFIRM,
                personLabel,
                creditLabel
        );

        ReplyKeyboard localizedKeyboard = keyboardService.getConfirmationKeyboard(chatId);

        messageService.sendMessage(chatId, localizedMessage,  localizedKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }
}