package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_ABOUT;
import static kz.iamthewatch.springbot.utils.MessageConstants.SYSTEM_ABOUT;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

@Component
@RequiredArgsConstructor
public class AboutCommand implements Command {

    private final MessageService messageService;
    private final LocalizationService localizationService;
    private final UserSessionService userSessionService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = getChatId(update);
        if (!UserState.IDLE.equals(userSessionService.getUserState(chatId))) {
            return false;
        }
        String localizedMessage = localizationService.getLocalizedMessage(chatId, MENU_ABOUT);
        return getMessageText(update).equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, SYSTEM_ABOUT);
        messageService.sendMessage(chatId, localizedMessage);
    }

    @Override
    public String getCommand() {
        return CommandName.ABOUT.name();
    }
}
