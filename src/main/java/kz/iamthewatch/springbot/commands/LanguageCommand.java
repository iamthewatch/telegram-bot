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

import static kz.iamthewatch.springbot.utils.MessageConstants.LANGUAGE_SELECT;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_LANGUAGE;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
public class LanguageCommand extends AbstractLocalizedMessageCommand {

    private final MessageService messageService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;

    public LanguageCommand(
            MessageService messageService,
            TelegramKeyboardBuilder keyboardBuilder,
            KeyboardFactory keyboardFactory,
            LocalizationService localizationService,
            UserSessionService userSessionService
    ) {
        super(userSessionService, localizationService);
        this.messageService = messageService;
        this.keyboardBuilder = keyboardBuilder;
        this.keyboardFactory = keyboardFactory;
    }

    @Override
    protected UserState requiredState() {
        return UserState.IDLE;
    }

    @Override
    protected String triggerMessageKey() {
        return MENU_LANGUAGE;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, LANGUAGE_SELECT);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.language());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.name();
    }
}