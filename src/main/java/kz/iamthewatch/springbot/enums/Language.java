package kz.iamthewatch.springbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kz.iamthewatch.springbot.utils.MessageConstants.LANGUAGE_KZ;
import static kz.iamthewatch.springbot.utils.MessageConstants.LANGUAGE_RU;

@Getter
@AllArgsConstructor
public enum Language {
    LANG_RU("lang_ru", "ru", LANGUAGE_RU),
    LANG_KZ("lang_kk", "kk", LANGUAGE_KZ);

    private final String callbackCode;
    private final String locale;
    private final String messageKey;

    public static Set<Language> getAll() {
       return Arrays.stream(values())
                .collect(Collectors.toSet());
    }

    public static boolean isCallbackCommand(String callbackData) {
        return BY_CALLBACK.containsKey(callbackData);
    }

    public static Optional<Language> tryFromCallback(String callbackData) {
        return Optional.ofNullable(BY_CALLBACK.get(callbackData));
    }

    private static final Map<String, Language> BY_CALLBACK =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            Language::getCallbackCode,
                            Function.identity()
                    ));
}