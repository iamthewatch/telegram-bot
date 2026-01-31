package kz.iamthewatch.springbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CONFIRM_NO;
import static kz.iamthewatch.springbot.utils.MessageConstants.CONSULTATION_CONFIRM_YES;

@Getter
@RequiredArgsConstructor
public enum ConfirmationStatus {

    ACCEPTED("confirmation_accepted", CONSULTATION_CONFIRM_YES),
    REJECTED("confirmation_rejected", CONSULTATION_CONFIRM_NO);

    private final String callbackCode;
    private final String messageKey;

    public static Set<ConfirmationStatus> getAll() {
        return Arrays.stream(values())
                .collect(Collectors.toSet());
    }

    public static boolean isCallbackCommand(String callback) {
        return BY_CALLBACK.containsKey(callback);
    }

    public static Optional<ConfirmationStatus> tryFromCallback(String callbackCode) {
        return Optional.ofNullable(BY_CALLBACK.get(callbackCode));
    }

    private static final Map<String, ConfirmationStatus> BY_CALLBACK =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            ConfirmationStatus::getCallbackCode,
                            Function.identity()
                    ));
}