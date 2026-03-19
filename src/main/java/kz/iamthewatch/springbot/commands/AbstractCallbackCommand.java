package kz.iamthewatch.springbot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import static kz.iamthewatch.springbot.utils.UpdateUtils.getCallbackData;

public abstract class AbstractCallbackCommand implements Command {

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery() && matches(getCallbackData(update));
    }

    protected abstract boolean matches(String callbackData);
}
