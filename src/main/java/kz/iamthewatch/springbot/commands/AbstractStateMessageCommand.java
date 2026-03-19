package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.service.UserSessionService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

public abstract class AbstractStateMessageCommand extends AbstractMessageCommand {

    protected AbstractStateMessageCommand(UserSessionService userSessionService) {
        super(userSessionService);
    }

    @Override
    public boolean canHandle(Update update) {
        if (!hasTextMessage(update)) {
            return false;
        }
        Long chatId = getChatId(update);
        return isInState(chatId, requiredState()) && additionalCondition(update, chatId);
    }

    protected boolean additionalCondition(Update update, Long chatId) {
        return true;
    }
}