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

import static kz.iamthewatch.springbot.utils.MessageConstants.ASK_QUESTION;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_ASK_QUESTION;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
public class AskQuestionCommand extends AbstractLocalizedMessageCommand {

    private final MessageService messageService;
    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;

    public AskQuestionCommand(
            UserSessionService userSessionService,
            MessageService messageService,
            LocalizationService localizationService,
            TelegramKeyboardBuilder keyboardBuilder,
            KeyboardFactory keyboardFactory
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
        return MENU_ASK_QUESTION;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        userSessionService.setUserState(chatId, UserState.AI_CHAT);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, ASK_QUESTION);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.aiChat());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.ASK_QUESTION.name();
    }
}
