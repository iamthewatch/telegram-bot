package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.enums.UserState;
import kz.iamthewatch.springbot.service.KeyboardFactory;
import kz.iamthewatch.springbot.service.LocalizationService;
import kz.iamthewatch.springbot.service.MessageService;
import kz.iamthewatch.springbot.service.TelegramKeyboardBuilder;
import kz.iamthewatch.springbot.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_START;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_WELCOME;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
public class StartCommand extends AbstractLocalizedMessageCommand {

    private final MessageService messageService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final UserSessionService userSessionService;

    public StartCommand(
            MessageService messageService,
            LocalizationService localizationService,
            TelegramKeyboardBuilder keyboardBuilder,
            KeyboardFactory keyboardFactory,
            UserSessionService userSessionService
    ) {
        super(userSessionService, localizationService);
        this.messageService = messageService;
        this.keyboardBuilder = keyboardBuilder;
        this.keyboardFactory = keyboardFactory;
        this.userSessionService = userSessionService;
    }

    @Override
    protected String triggerMessageKey() {
        return MENU_START;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        userSessionService.setUserState(chatId, UserState.IDLE);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, MENU_WELCOME);
        ReplyKeyboard localizedKeyboard = keyboardBuilder.build(chatId, keyboardFactory.mainMenu());
        messageService.sendMessage(chatId, localizedMessage, localizedKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.START.name();
    }
}