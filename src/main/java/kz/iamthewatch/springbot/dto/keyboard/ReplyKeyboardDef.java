package kz.iamthewatch.springbot.dto.keyboard;

import java.util.List;

public record ReplyKeyboardDef(
        List<List<String>> rows,
        boolean resize,
        boolean oneTime
) implements KeyboardDef {}