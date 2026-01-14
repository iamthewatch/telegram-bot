package kz.iamthewatch.springbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ConfirmationStatus {

    ACCEPTED("confirmation_accepted"),
    REJECTED("confirmation_rejected");

    private final String callbackCode;

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