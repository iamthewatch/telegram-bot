package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.dto.keyboard.InlineKeyboardDef;
import kz.iamthewatch.springbot.dto.keyboard.KeyboardButtonDef;
import kz.iamthewatch.springbot.dto.keyboard.KeyboardDef;
import kz.iamthewatch.springbot.dto.keyboard.ReplyKeyboardDef;
import kz.iamthewatch.springbot.enums.ConfirmationStatus;
import kz.iamthewatch.springbot.enums.CreditType;
import kz.iamthewatch.springbot.enums.Language;
import kz.iamthewatch.springbot.enums.PersonType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_ABOUT;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_ASK_QUESTION;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_CONSULTATION_REQUEST;
import static kz.iamthewatch.springbot.utils.MessageConstants.MENU_LANGUAGE;
import static kz.iamthewatch.springbot.utils.MessageConstants.AI_CHAT_EXIT;

@Service
public class DefaultKeyboardFactory implements KeyboardFactory {

    @Override
    public KeyboardDef mainMenu() {
        return new ReplyKeyboardDef(
                List.of(
                        List.of(MENU_ABOUT, MENU_LANGUAGE),
                        List.of(MENU_CONSULTATION_REQUEST, MENU_ASK_QUESTION)
                ),
                true, false
        );
    }

    @Override
    public KeyboardDef aiChat() {
        return new ReplyKeyboardDef(
                List.of(
                        List.of(AI_CHAT_EXIT)
                ),
                true, false
        );
    }

    @Override
    public KeyboardDef language() {
        return inlineEnumKeyboard(
                Language.getAll(),
                Language::getMessageKey,
                Language::getCallbackCode
        );
    }

    @Override
    public KeyboardDef confirmation() {
        return inlineEnumKeyboard(
                ConfirmationStatus.getAll(),
                ConfirmationStatus::getMessageKey,
                ConfirmationStatus::getCallbackCode
        );
    }

    @Override
    public KeyboardDef personType() {
        return inlineEnumKeyboard(
                PersonType.getAll(),
                PersonType::getMessageKey,
                PersonType::getCode
        );
    }

    @Override
    public KeyboardDef creditTypes(PersonType personType) {
        return inlineEnumKeyboard(
                CreditType.getAllByPersonType(personType),
                CreditType::getMessageKey,
                CreditType::getCallbackCode
        );
    }

    private <E extends Enum<E>> KeyboardDef inlineEnumKeyboard(
            Collection<E> items,
            Function<E, String> messageKeyExtractor,
            Function<E, String> callbackExtractor
    ) {
        return new InlineKeyboardDef(
                items.stream()
                        .sorted(Comparator.comparingInt(Enum::ordinal)) // порядок как в enum
                        .map(item -> List.of(
                                new KeyboardButtonDef(
                                        messageKeyExtractor.apply(item),
                                        callbackExtractor.apply(item)
                                )
                        ))
                        .toList()
        );
    }
}