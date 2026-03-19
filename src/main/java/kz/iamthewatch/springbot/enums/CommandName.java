package kz.iamthewatch.springbot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommandName {
    ABOUT("ABOUT_COMMAND"),
    LANGUAGE("LANGUAGE_COMMAND"),
    START("START_COMMAND"),
    ASK_QUESTION("ASK_QUESTION_COMMAND"),
    EXIT_AI_CONSULTATION("EXIT_AI_CONSULTATION_COMMAND"),
    QUESTION_HANDLER("QUESTION_HANDLER_COMMAND"),
    CONSULTATION_REQUEST("CONSULTATION_REQUEST_COMMAND");

    private final String name;
}