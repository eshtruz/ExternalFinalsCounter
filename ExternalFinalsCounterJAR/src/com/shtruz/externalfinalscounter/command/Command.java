package com.shtruz.externalfinalscounter.command;

import com.shtruz.externalfinalscounter.ExternalFinalsCounter;

public interface Command {
    String getName();

    void execute(ExternalFinalsCounter externalFinalsCounter, String[] args);
}
