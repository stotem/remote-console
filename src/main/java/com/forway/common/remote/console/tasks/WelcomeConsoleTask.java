package com.forway.common.remote.console.tasks;

import java.util.Map;

import com.forway.common.remote.console.ConsoleConfig;
import com.forway.common.remote.console.ConsoleTask;

public class WelcomeConsoleTask implements ConsoleTask {

    @Override
    public String doTask(Map<String, String> args) {
        StringBuilder welcome = new StringBuilder();
        welcome.append("Console version ");
        welcome.append(ConsoleConfig.VERSION);
        welcome.append(ConsoleConfig.LINE_SEPARATOR);
        welcome.append("Copyright 2011 by jianjun.wu (cqwujianjun@gmail.com)");
        return welcome.toString();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Map<String, String> argsDesc() {
        return null;
    }

    @Override
    public String[] getCommand() {
        return new String[] { "welcome" };
    }

}
