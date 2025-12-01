package kz.iamthewatch.springbot.enums;

public enum CommandName {
    ABOUT("ABOUT_COMMAND"),
    LANGUAGE("LANGUAGE_COMMAND");

    private final String name;

    CommandName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}