package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_NO;
import static kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_YES;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getFirstname;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getLastname;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getUsername;

import java.util.Set;

import kz.iamthewatch.springbot.dto.ConsultationDto;
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
        return SUPPORTED.contains(getCallbackData(update));
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String data = getCallbackData(update);
        messageTrackerService.deleteLastMessage(chatId);

        if (CONFIRM_YES.equals(data)) {

            consultationRequestService.saveRequest(createConsultationDto(chatId, update));

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(localizationService.getLocalizedMessage(chatId, "consultation.request.accepted"))
                    .replyMarkup(keyboardService.getMainMenuKeyboard(chatId))
                    .build();
            eventPublisher.publishEvent(new MessageEvent(this, message));
            return;
        }

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

    private ConsultationDto createConsultationDto(Long chatId, Update update) {
        return new ConsultationDto(
                chatId,
                getUsername(update),
                getFirstname(update),
                getLastname(update),
                userSessionService.getConsultationPersonType(chatId),
                userSessionService.getConsultationCreditType(chatId)
        );
    }
}