package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_ABOUT;
import static kz.iamthewatch.springbot.utils.MessageConstants.SYSTEM_ABOUT;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
public class AboutCommand extends AbstractLocalizedMessageCommand {

    private final MessageService messageService;

    public AboutCommand(
            MessageService messageService,
            LocalizationService localizationService,
            UserSessionService userSessionService
    ) {
        super(userSessionService, localizationService);
        this.messageService = messageService;
    }

    @Override
    protected UserState requiredState() {
        return UserState.IDLE;
    }

    @Override
    protected String triggerMessageKey() {
        return MENU_ABOUT;
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
