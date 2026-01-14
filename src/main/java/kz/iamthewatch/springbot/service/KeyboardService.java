package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.enums.ConfirmationStatus;
import kz.iamthewatch.springbot.enums.CreditType;
import kz.iamthewatch.springbot.enums.Language;
import kz.iamthewatch.springbot.enums.PersonType;
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
import java.util.Set;

import static kz.iamthewatch.springbot.utils.MessageConstants.PERSON_TYPE_FL;
import static kz.iamthewatch.springbot.utils.MessageConstants.PERSON_TYPE_UL;

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
                .callbackData(Language.LANG_RU.getCallbackCode())
                .build()
        ));

        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, "language.kk"))
                .callbackData(Language.LANG_KZ.getCallbackCode())
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
                        .callbackData(ConfirmationStatus.ACCEPTED.getCallbackCode())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(localizationService.getLocalizedMessage(chatId, "consultation.confirm.no"))
                        .callbackData(ConfirmationStatus.REJECTED.getCallbackCode())
                        .build()
        );

        return InlineKeyboardMarkup
                .builder()
                .keyboardRow(row)
                .build();
    }

    public ReplyKeyboard getPersonTypeKeyboard(Long chatId) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, PERSON_TYPE_FL))
                .callbackData(PersonType.PERSON_FL.getCode())
                .build()
        ));

        rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(localizationService.getLocalizedMessage(chatId, PERSON_TYPE_UL))
                .callbackData(PersonType.PERSON_FL.getCode())
                .build()
        ));

        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }

    public ReplyKeyboard getCreditTypeKeyboardByPersonType(Long chatId, PersonType personType) {
        Set<CreditType> creditTypes = CreditType.getAllByPersonType(personType);

        List<InlineKeyboardRow> rows = creditTypes.stream()
                        .map(creditType ->
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text(localizationService.getLocalizedMessage(chatId, creditType.getMessageKey()))
                                                .callbackData(creditType.getCallbackCode())
                                                .build()
                                )
                        )
                        .toList();

        return InlineKeyboardMarkup
                .builder()
                .keyboard(rows)
                .build();
    }
}