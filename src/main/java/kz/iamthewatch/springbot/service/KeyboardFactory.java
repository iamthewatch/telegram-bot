package kz.iamthewatch.springbot.service;

import kz.iamthewatch.springbot.dto.keyboard.KeyboardDef;
import kz.iamthewatch.springbot.enums.PersonType;

public interface KeyboardFactory {
    KeyboardDef mainMenu();
    KeyboardDef language();
    KeyboardDef confirmation();
    KeyboardDef personType();
    KeyboardDef creditTypes(PersonType personType);
}