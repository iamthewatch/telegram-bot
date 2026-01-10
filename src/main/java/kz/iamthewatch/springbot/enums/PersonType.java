package kz.iamthewatch.springbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PersonType {
    PERSON_FL("person_fl"),
    PERSON_UL("person_ul");

    private final String value;
}
