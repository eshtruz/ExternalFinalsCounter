package com.shtruz.externalfinalscounter.command.commands;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;
import com.shtruz.externalfinalscounter.command.Command;

public class FinalsInTabCommand implements Command {
    @Override
    public String getName() {
        return "finalsintab";
    }

    @Override
    public void execute(ExternalFinalsCounter externalFinalsCounter, String[] args) {
        externalFinalsCounter.getConfig().finalsInTab = !externalFinalsCounter.getConfig().finalsInTab;

        externalFinalsCounter.saveConfig();
    }
}
