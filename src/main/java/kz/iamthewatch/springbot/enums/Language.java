package kz.iamthewatch.springbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Language {
    LANG_RU("lang_ru", "ru"),
    LANG_KZ("lang_kk", "kk");

    private final String callbackCode;
    private final String locale;

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