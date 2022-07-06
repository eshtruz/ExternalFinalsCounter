package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

public class DisplayFinalsCounterCommand implements Command {
    @Override
    public String getName() {
        return "displayfinalscounter";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        externalFinalsCounter.getConfig().displayFinalsCounter = !externalFinalsCounter.getConfig().displayFinalsCounter;

        externalFinalsCounter.saveConfig();
    }
}
