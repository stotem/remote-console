package com.forway.common.remote.console.tasks;

import java.util.Hashtable;
import java.util.Map;

import com.forway.common.remote.console.ConsoleConfig;
import com.forway.common.remote.console.ConsoleTask;

public class SetMaxClientTask implements ConsoleTask {

    private static final String MAX_CLIENT_COUNT = "max_client_count";

    @Override
    public String doTask(Map<String, String> args) {
        String result = null;
        String tmpClientCount = args == null ? null : args.get(MAX_CLIENT_COUNT);
        try {
            ConsoleConfig.setMaxClientCount(Integer.valueOf(tmpClientCount));
            result = "Operation success.[max_client_count=" + tmpClientCount + "]";
        }
        catch (NumberFormatException e) {
            result = "The max_time_client_count invalid. [max_client_count=" + tmpClientCount + "]";
        }
        return result;
    }

    @Override
    public String description() {
        return "The value of max_client_count set of system parameters";
    }

    @Override
    public Map<String, String> argsDesc() {
        Map<String, String> args = new Hashtable<String, String>();
        args.put(MAX_CLIENT_COUNT, "The client number online at the same time.");
        return args;
    }

    @Override
    public String[] getCommand() {
        return new String[] { "set_max_client_count" };
    }

}
