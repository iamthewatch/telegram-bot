package kz.iamthewatch.springbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final LocalizationService localizationService;

    public ReplyKeyboard getMainMenuKeyboard(Long chatId) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(localizationService.getLocalizedMessage(chatId, "menu.about"));
        keyboardRows.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row1.add(localizationService.getLocalizedMessage(chatId, "menu.language"));
        keyboardRows.add(row2);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        return keyboardMarkup;
    }
}