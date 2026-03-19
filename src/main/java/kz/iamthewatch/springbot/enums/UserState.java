package kz.iamthewatch.springbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserState {
    IDLE("IDLE"),
    AI_CHAT("AI_CHAT");

    private final String name;
}