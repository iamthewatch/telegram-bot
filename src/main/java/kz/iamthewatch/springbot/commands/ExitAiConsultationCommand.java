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

import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXIT;
import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXITED;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
public class ExitAiConsultationCommand extends AbstractLocalizedMessageCommand {

    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final MessageService messageService;

    public ExitAiConsultationCommand(
            UserSessionService userSessionService,
            LocalizationService localizationService,
            TelegramKeyboardBuilder keyboardBuilder,
            KeyboardFactory keyboardFactory,
            MessageService messageService
    ) {
        super(userSessionService, localizationService);
        this.keyboardBuilder = keyboardBuilder;
        this.keyboardFactory = keyboardFactory;
        this.messageService = messageService;
    }

    @Override
    protected UserState requiredState() {
        return UserState.AI_CHAT;
    }

    @Override
    protected String triggerMessageKey() {
        return AI_CHAT_EXIT;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        userSessionService.setUserState(chatId, UserState.IDLE);

        String localizedMessage = localizationService.getLocalizedMessage(chatId, AI_CHAT_EXITED);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.mainMenu());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.EXIT_AI_CONSULTATION.name();
    }
}