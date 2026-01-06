package kz.iamthewatch.springbot.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
public final class UpdateUtils {

    public static Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    public static String getCallbackData(Update update) {
        return update.getCallbackQuery().getData();
    }

    public static String getUsername(Update update) {
        return update.getCallbackQuery().getFrom().getUserName();
    }

    public static String getFirstname(Update update) {
        return update.getCallbackQuery().getFrom().getFirstName();
    }

    public static String getLastname(Update update) {
        return update.getCallbackQuery().getFrom().getLastName();
    }
}