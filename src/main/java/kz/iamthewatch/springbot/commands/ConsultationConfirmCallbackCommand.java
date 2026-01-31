package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_REQUEST_ACCEPTED;
import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_RESTART;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getFirstname;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getLastname;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getUsername;

import kz.iamthewatch.springbot.dto.ConsultationDto;
import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.ConfirmationStatus;
import kz.iamthewatch.springbot.service.ConsultationRequestService;
import kz.iamthewatch.springbot.service.KeyboardFactory;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.TelegramKeyboardBuilder;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class ConsultationConfirmCallbackCommand implements Command {

    private final UserSessionService userSessionService;
    private final ConsultationRequestService consultationRequestService;
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
        return ConfirmationStatus.isCallbackCommand(getCallbackData(update));
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String callbackData = getCallbackData(update);
        messageTrackerService.deleteLastMessage(chatId);

        ReplyKeyboard localizedKeyboard = keyboardBuilder.build(chatId, keyboardFactory.mainMenu());

        ConfirmationStatus.tryFromCallback(callbackData).ifPresent(action -> {
            switch (action) {
                case ACCEPTED -> handleAcceptedCallback(chatId, update, localizedKeyboard);
                case REJECTED  -> handleRejectedCallback(chatId, localizedKeyboard);
            }
        });
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }

    private void handleAcceptedCallback(Long chatId, Update update, ReplyKeyboard keyboard) {
        consultationRequestService.saveRequest(createConsultationDto(chatId, update));
        messageService.sendMessage(
                chatId,
                localizationService.getLocalizedMessage(chatId, CONSULTATION_REQUEST_ACCEPTED),
                keyboard
        );
    }

    private void handleRejectedCallback(Long chatId, ReplyKeyboard keyboard) {
        messageService.sendMessage(
                chatId,
                localizationService.getLocalizedMessage(chatId, CONSULTATION_RESTART),
                keyboard
        );
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