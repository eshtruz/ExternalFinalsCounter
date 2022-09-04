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

        String output = "Finals have been reset";

        try {
            externalFinalsCounter.addChatComponentText(output);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
