package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CREDIT_TYPE_SELECT;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_FL;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_UL;

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

import java.util.ArrayList;
import java.util.List;

import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_AUTO;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_CONSUMER;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_MORTGAGE;
import static kz.iamthewatch.springbot.utils.CreditTypeConstants.CREDIT_OTHER;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
@RequiredArgsConstructor
public class ConsultationCallbackCommand implements Command {

    private final UserSessionService userSessionService;
    private final MessageTrackerService messageTrackerService;
    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;

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

        messageTrackerService.deleteLastMessage(chatId);
        userSessionService.setConsultationPersonType(chatId, personType);

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(localizationService.getLocalizedMessage(chatId, CONSULTATION_CREDIT_TYPE_SELECT))
                .replyMarkup(getCreditTypeKeyboard(chatId))
                .build();

        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }

    private ReplyKeyboard getCreditTypeKeyboard(Long chatId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "credit.type.consumer"))
                .callbackData(CREDIT_CONSUMER)
                .build()));
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "credit.type.mortgage"))
                .callbackData(CREDIT_MORTGAGE)
                .build()));
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "credit.type.auto"))
                .callbackData(CREDIT_AUTO)
                .build()));
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "credit.type.other"))
                .callbackData(CREDIT_OTHER)
                .build()));

        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }
}