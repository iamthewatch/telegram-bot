package kz.iamthewatch.springbot.dto.keyboard;

import java.util.List;

public record InlineKeyboardDef(
        List<List<KeyboardButtonDef>> rows
) implements KeyboardDef {}