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
public enum PersonType {
    PERSON_FL("person_fl"),
    PERSON_UL("person_ul");

    private final String code;

    public static boolean isPersonTypeCommand(String code) {
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