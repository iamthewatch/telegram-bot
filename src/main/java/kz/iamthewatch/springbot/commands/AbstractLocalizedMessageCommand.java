package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.UserSessionService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getMessageText;

public abstract class AbstractLocalizedMessageCommand extends AbstractMessageCommand {

    protected final LocalizationService localizationService;

    protected AbstractLocalizedMessageCommand(
            UserSessionService userSessionService,
            LocalizationService localizationService
    ) {
        super(userSessionService);
        this.localizationService = localizationService;
    }

    @Override
    public boolean canHandle(Update update) {
        if (!hasTextMessage(update)) {
            return false;
        }

        Long chatId = getChatId(update);
        UserState requiredState = requiredState();
        if (requiredState != null && !isInState(chatId, requiredState)) {
            return false;
        }

        String expectedMessage = localizationService.getLocalizedMessage(chatId, triggerMessageKey());
        return expectedMessage.equals(getMessageText(update));
    }

    protected abstract String triggerMessageKey();
}