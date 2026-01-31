package kz.iamthewatch.springbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserState {
    IDLE("IDLE"),
    WAITING_FOR_ANSWER("WAITING_FOR_ANSWER");

    private final String name;
}