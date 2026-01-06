package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_AUTO;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_CONSUMER;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_MORTGAGE;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_OTHER;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_FL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.events.MessageEvent;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Component
@RequiredArgsConstructor
public class ConsultationCreditTypeCallbackCommand implements Command {

    private static final Set<String> SUPPORTED = new HashSet<>(Arrays.asList(
            CREDIT_CONSUMER, CREDIT_MORTGAGE, CREDIT_AUTO, CREDIT_OTHER
    ));

    private final UserSessionService userSessionService;
    private final MessageTrackerService messageTrackerService;
    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasCallbackQuery()) {
            return false;
        }
        return SUPPORTED.contains(update.getCallbackQuery().getData());
    }

    @Override
    public void handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String creditType = update.getCallbackQuery().getData();

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

        String confirmationText = localizationService.getLocalizedMessage(
                chatId,
                "consultation.confirm",
                personLabel,
                creditLabel
        );

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(confirmationText)
                .replyMarkup(getConfirmationKeyboard(chatId))
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }

    private ReplyKeyboard getConfirmationKeyboard(Long chatId) {
        InlineKeyboardRow row = new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text(localizationService.getLocalizedMessage(chatId, "consultation.confirm.yes"))
                        .callbackData(kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_YES)
                        .build(),
                InlineKeyboardButton.builder()
                        .text(localizationService.getLocalizedMessage(chatId, "consultation.confirm.no"))
                        .callbackData(kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_NO)
                        .build()
        );

        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(row)
                .build();
    }
}