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

@Component
@RequiredArgsConstructor
public class LanguageCommand implements Command {

    public final static String LANG_RU = "lang_ru";
    public final static String LANG_KZ = "lang_kk";
    private final ApplicationEventPublisher eventPublisher;
    private final LocalizationService localizationService;

    @Override
    public boolean canHandle(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }
        Long chatId = update.getMessage().getChatId();
        String localizedMessage = localizationService.getLocalizedMessage(chatId, "menu.language");
        return update.getMessage().getText().equals(localizedMessage);
    }

    @Override
    public void handle(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            Long chatId = update.getMessage().getChatId();
            String localizedMessage = localizationService.getLocalizedMessage(chatId, "language.select");

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(localizedMessage)
                    .replyMarkup(getLanguageInline(chatId))
                    .build();

            eventPublisher.publishEvent(new MessageEvent(this, message));
        }
    }

    @Override
    public String getCommand() {
        return CommandName.LANGUAGE.name();
    }

    private ReplyKeyboard getLanguageInline(Long chatId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "language.ru"))
                .callbackData(LANG_RU)
                .build()
        ));

        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "language.kk"))
                .callbackData(LANG_KZ)
                .build()
        ));

        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }
}