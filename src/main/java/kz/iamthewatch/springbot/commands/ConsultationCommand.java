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

import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_CONSULTATION_REQUEST;
import static kz.iamthewatch.springbot.utils.MessageConstants.PERSON_TYPE_SELECT;
import static kz.iamthewatch.springbot.utils.UpdateUtils.getChatId;

@Component
public class ConsultationCommand extends AbstractLocalizedMessageCommand {

    private final TelegramKeyboardBuilder keyboardBuilder;
    private final KeyboardFactory keyboardFactory;
    private final MessageService messageService;

    public ConsultationCommand(
            LocalizationService localizationService,
            TelegramKeyboardBuilder keyboardBuilder,
            KeyboardFactory keyboardFactory,
            MessageService messageService,
            UserSessionService userSessionService
    ) {
        super(userSessionService, localizationService);
        this.keyboardBuilder = keyboardBuilder;
        this.keyboardFactory = keyboardFactory;
        this.messageService = messageService;
    }

    @Override
    protected UserState requiredState() {
        return UserState.IDLE;
    }

    @Override
    protected String triggerMessageKey() {
        return MENU_CONSULTATION_REQUEST;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        String localizedMessage = localizationService.getLocalizedMessage(chatId, PERSON_TYPE_SELECT);
        ReplyKeyboard replyKeyboard = keyboardBuilder.build(chatId, keyboardFactory.personType());
        messageService.sendMessage(chatId, localizedMessage, replyKeyboard);
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }
}