package com.forway.common.remote.console.tasks;

import java.util.Map;

import com.forway.common.remote.console.ConsoleConfig;
import com.forway.common.remote.console.ConsoleTask;

public class ShowConsoleTask implements ConsoleTask {

    @Override
    public String doTask(Map<String, String> args) {
        StringBuilder result = new StringBuilder();
        result.append("-----Console system information-----");
        result.append(ConsoleConfig.LINE_SEPARATOR);
        result.append("version = " + ConsoleConfig.VERSION);
        result.append(ConsoleConfig.LINE_SEPARATOR);
        result.append("data_time_out = " + ConsoleConfig.getDataTimeout());
        result.append(ConsoleConfig.LINE_SEPARATOR);
        result.append("max_client_count = " + ConsoleConfig.getMaxClientCount());
        result.append(ConsoleConfig.LINE_SEPARATOR);
        result.append("client_ips = " + ConsoleConfig.clientToString());
        result.append(ConsoleConfig.LINE_SEPARATOR);
        result.append("online_client = " + ConsoleConfig.onLineClientString());
        return result.toString();
    }

    @Override
    public String description() {
        return "Display system information";
    }

    @Override
    public Map<String, String> argsDesc() {
        return null;
    }

    @Override
    public String[] getCommand() {
        // return new String[] { "show", "info" };
        return new String[] { "help" };
    }

}
