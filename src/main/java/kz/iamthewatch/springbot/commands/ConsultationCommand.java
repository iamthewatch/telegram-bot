package kz.iamthewatch.springbot.commands;

import kz.iamthewatch.springbot.enums.CommandName;
import kz.iamthewatch.springbot.events.MessageEvent;
import kz.iamthewatch.springbot.service.LocalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static kz.iamthewatch.springbot.utils.MessageConstants.*;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_FL;
import static kz.iamthewatch.springbot.utils.PersonTypeConstants.PERSON_UL;

@Component
@RequiredArgsConstructor
public class ConsultationCommand implements Command {

    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = update.getMessage().getChatId();
        String localizedMessage = localizationService.getLocalizedMessage(chatId, MENU_CONSULTATION_REQUEST);
        return update.getMessage().getText().equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            Long chatId = update.getMessage().getChatId();
            String localizedMessage = localizationService.getLocalizedMessage(chatId, PERSON_TYPE_SELECT);

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(localizedMessage)
                    .replyMarkup(getPersonTypeKeyboard(chatId))
                    .build();

            eventPublisher.publishEvent(new MessageEvent(this, message));
        }
    }

    @Override
    public String getCommand() {
        return CommandName.CONSULTATION_REQUEST.name();
    }

    private ReplyKeyboard getPersonTypeKeyboard(Long chatId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, PERSON_TYPE_FL))
                .callbackData(PERSON_FL)
                .build()
        ));

        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, PERSON_TYPE_UL))
                .callbackData(PERSON_UL)
                .build()
        ));

        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }
}