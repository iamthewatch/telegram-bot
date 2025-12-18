package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_NO;
import static kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_YES;

import java.util.Set;
import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.events.MessageEvent;
import kz.iamthewatch.springbot.service.ConsultationRequestService;
import kz.iamthewatch.springbot.service.KeyboardService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ConsultationConfirmCallbackCommand implements Command {

    private static final Set<String> SUPPORTED = Set.of(CONFIRM_YES, CONFIRM_NO);

    private final UserSessionService userSessionService;
    private final ConsultationRequestService consultationRequestService;
    private final MessageTrackerService messageTrackerService;
    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;
    private final KeyboardService keyboardService;

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
        String data = update.getCallbackQuery().getData();
        messageTrackerService.deleteLastMessage(chatId);

        if (CONFIRM_YES.equals(data)) {
            String personType = userSessionService.getConsultationPersonType(chatId);
            String creditType = userSessionService.getConsultationCreditType(chatId);
            consultationRequestService.saveRequest(chatId, personType == null ? "unknown" : personType, creditType == null ? "unknown" : creditType);
            userSessionService.clearConsultationFlow(chatId);

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(localizationService.getLocalizedMessage(chatId, "consultation.request.accepted"))
                    .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                    .build();
            eventPublisher.publishEvent(new MessageEvent(this, message));
            return;
        }

        userSessionService.clearConsultationFlow(chatId);
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(localizationService.getLocalizedMessage(chatId, "consultation.restart"))
                .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                .build();
        eventPublisher.publishEvent(new MessageEvent(this, message));
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }
}