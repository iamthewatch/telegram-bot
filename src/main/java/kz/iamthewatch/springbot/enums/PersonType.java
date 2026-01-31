package kz.iamthewatch.springbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kz.iamthewatch.springbot.utils.MessageConstants.PERSON_TYPE_FL;
import static kz.iamthewatch.springbot.utils.MessageConstants.PERSON_TYPE_UL;

@Getter
@AllArgsConstructor
public enum PersonType {
    PERSON_FL("person_fl", PERSON_TYPE_FL),
    PERSON_UL("person_ul", PERSON_TYPE_UL);

    private final String code;
    private final String messageKey;

    public static Set<PersonType> getAll() {
        return Arrays.stream(values())
                .collect(Collectors.toSet());
    }

    public static boolean isCallbackCommand(String code) {
        return BY_CALLBACK.containsKey(code);
    }

    public static Optional<PersonType> tryFromCallback(String code) {
        return Optional.ofNullable(BY_CALLBACK.get(code));
    }

    private static final Map<String, PersonType> BY_CALLBACK =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            PersonType::getCode,
                            Function.identity()
                    ));
}