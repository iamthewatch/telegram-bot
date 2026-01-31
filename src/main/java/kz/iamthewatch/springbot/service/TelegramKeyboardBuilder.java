package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.dto.keyboard.InlineKeyboardDef;
import kz.iamthewatch.springbot.dto.keyboard.KeyboardDef;
import kz.iamthewatch.springbot.dto.keyboard.ReplyKeyboardDef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramKeyboardBuilder {

    private final LocalizationService localizationService;

    public ReplyKeyboard build(Long chatId, KeyboardDef def) {

        if (def instanceof ReplyKeyboardDef reply) {
            return buildReply(chatId, reply);
        }

        if (def instanceof InlineKeyboardDef inline) {
            return buildInline(chatId, inline);
        }

        throw new IllegalArgumentException("Unsupported keyboard type");
    }

    private ReplyKeyboard buildReply(Long chatId, ReplyKeyboardDef def) {
        List<KeyboardRow> rows = def.rows().stream()
                .map(r -> {
                    KeyboardRow row = new KeyboardRow();
                    r.forEach(key ->
                            row.add(localizationService.getLocalizedMessage(chatId, key))
                    );
                    return row;
                })
                .toList();

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        markup.setResizeKeyboard(def.resize());
        markup.setOneTimeKeyboard(def.oneTime());
        return markup;
    }

    private InlineKeyboardMarkup buildInline(Long chatId, InlineKeyboardDef def) {
        List<InlineKeyboardRow> rows = def.rows().stream()
                .map(r -> new InlineKeyboardRow(
                        r.stream()
                                .map(b -> InlineKeyboardButton.builder()
                                        .text(localizationService.getLocalizedMessage(chatId, b.textKey()))
                                        .callbackData(b.callback())
                                        .build())
                                .toList()
                ))
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }
}