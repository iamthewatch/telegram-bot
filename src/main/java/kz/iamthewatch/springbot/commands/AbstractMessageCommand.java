package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.UserSessionService;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractMessageCommand implements Command {

    protected final UserSessionService userSessionService;

    protected AbstractMessageCommand(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    protected boolean hasTextMessage(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    protected boolean isInState(Long chatId, UserState state) {
        return state.equals(userSessionService.getUserState(chatId));
    }

    protected UserState requiredState() {
        return null;
    }
}