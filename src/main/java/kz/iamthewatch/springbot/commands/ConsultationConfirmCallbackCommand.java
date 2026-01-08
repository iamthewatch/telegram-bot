package kz.iamthewatch.springbot.commands;

import static kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_NO;
import static kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_YES;
import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_REQUEST_ACCEPTED;
import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_RESTART;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getFirstname;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getLastname;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getUsername;

import java.util.Set;

import kz.iamthewatch.springbot.dto.ConsultationDto;
import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.service.ConsultationRequestService;
import kz.iamthewatch.springbot.service.KeyboardService;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.MessageTrackerService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@RequiredArgsConstructor
public class ConsultationConfirmCallbackCommand implements Command {

    private static final Set<String> SUPPORTED = Set.of(CONFIRM_YES, CONFIRM_NO);

    private final UserSessionService userSessionService;
    private final ConsultationRequestService consultationRequestService;
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
        String data = getCallbackData(update);
        messageTrackerService.deleteLastMessage(chatId);

        ReplyKeyboard localizedKeyboard = keyboardService.getMainMenuKeyboard(chatId);

        if (CONFIRM_YES.equals(data)) {
            consultationRequestService.saveRequest(createConsultationDto(chatId, update));
            String localizedMessage = localizationService.getLocalizedMessage(chatId, CONSULTATION_REQUEST_ACCEPTED);
            messageService.sendMessage(chatId, localizedMessage,  localizedKeyboard);
            return;
        }

        String localizedMessage = localizationService.getLocalizedMessage(chatId, CONSULTATION_RESTART);
        messageService.sendMessage(chatId, localizedMessage,  localizedKeyboard);
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