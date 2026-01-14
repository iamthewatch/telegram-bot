package kz.iamthewatch.springbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kz.iamthewatch.springbot.utils.MessageConstants.CREDIT_TYPE_CAR;
import static kz.iamthewatch.springbot.utils.MessageConstants.CREDIT_TYPE_CONSUMER;
import static kz.iamthewatch.springbot.utils.MessageConstants.CREDIT_TYPE_MORTGAGE;
import static kz.iamthewatch.springbot.utils.MessageConstants.CREDIT_TYPE_OTHER;

@Getter
@RequiredArgsConstructor
public enum CreditType {

    CONSUMER(
            "credit_consumer",
            CREDIT_TYPE_CONSUMER,
            EnumSet.of(PersonType.PERSON_FL)
    ),
    MORTGAGE(
            "credit_mortgage",
            CREDIT_TYPE_MORTGAGE,
            EnumSet.of(PersonType.PERSON_FL)
    ),
    CAR(
            "credit_car",
            CREDIT_TYPE_CAR,
            EnumSet.of(PersonType.PERSON_FL)
    ),
    OTHER(
            "credit_other",
            CREDIT_TYPE_OTHER,
            EnumSet.of(PersonType.PERSON_FL, PersonType.PERSON_UL)
    );

    private final String callbackCode;
    private final String messageKey;
    private final Set<PersonType> supportedPersonTypes;

    public static boolean isCallbackCommand(String callbackData) {
        return BY_CALLBACK.containsKey(callbackData);
    }

    public static Optional<CreditType> tryFromCallback(String callbackData) {
        return Optional.ofNullable(BY_CALLBACK.get(callbackData));
    }

    public static Set<CreditType> getAllByPersonType(PersonType personType) {
        return Arrays.stream(values())
                .filter(ct -> ct.isSupportedPerson(personType))
                .collect(Collectors.toSet());
    }

    public boolean isSupportedPerson(PersonType personType) {
        return supportedPersonTypes.contains(personType);
    }

    private static final Map<String, CreditType> BY_CALLBACK =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            CreditType::getCallbackCode,
                            Function.identity()
                    ));
}