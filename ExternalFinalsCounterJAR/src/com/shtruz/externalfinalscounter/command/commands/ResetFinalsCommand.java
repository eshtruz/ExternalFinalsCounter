package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

import java.lang.reflect.InvocationTargetException;

public class ResetFinalsCommand implements Command {
    @Override
    public String getName() {
        return "resetfinals";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        externalFinalsCounter.getChatMessageParser().reset();

        try {
            externalFinalsCounter.addChatComponentText("Reset finals");
        } catch (IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException
                 | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
