package kz.iamthewatch.springbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static kz.iamthewatch.springbot.utils.LanguageConstants.LANG_KZ;
import static kz.iamthewatch.springbot.utils.LanguageConstants.LANG_RU;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final LocalizationService localizationService;

    public ReplyKeyboard getMainMenuKeyboard(Long chatId) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(localizationService.getLocalizedMessage(chatId, "menu.about"));
        row1.add(localizationService.getLocalizedMessage(chatId, "menu.language"));
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(localizationService.getLocalizedMessage(chatId, "menu.consultation.request"));
        row2.add(localizationService.getLocalizedMessage(chatId, "menu.ask.question"));
        keyboardRows.add(row2);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        return keyboardMarkup;
    }

    public ReplyKeyboard getLanguageKeyboard(Long chatId) {
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

    public ReplyKeyboard getConfirmationKeyboard(Long chatId) {
        InlineKeyboardRow row = new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text(localizationService.getLocalizedMessage(chatId, "consultation.confirm.yes"))
                        .callbackData(kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_YES)
                        .build(),
                InlineKeyboardButton.builder()
                        .text(localizationService.getLocalizedMessage(chatId, "consultation.confirm.no"))
                        .callbackData(kz.iamthewatch.springbot.utils.ConfirmationConstants.CONFIRM_NO)
                        .build()
        );

        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(row)
                .build();
    }
}