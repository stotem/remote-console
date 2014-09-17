package com.forway.common.remote.console.tasks;

import java.util.Hashtable;
import java.util.Map;

import com.forway.common.remote.console.ConsoleConfig;
import com.forway.common.remote.console.ConsoleTask;

public class SetSocketTimeOutTask implements ConsoleTask {

    private static final String DATA_TIME_OUT = "data_time_out";

    @Override
    public String doTask(Map<String, String> args) {
        String result = null;
        String tmpTimeOut = args == null ? null : args.get(DATA_TIME_OUT);
        try {
            ConsoleConfig.setDataTimeout(Integer.valueOf(tmpTimeOut));
            result = "Operation success.[data_time_out=" + tmpTimeOut + "]" + ConsoleConfig.LINE_SEPARATOR
                    + "Reconnect take effect!";
        }
        catch (NumberFormatException e) {
            result = "The data_time_out invalid. [data_time_out=" + tmpTimeOut + "]";
        }
        return result;
    }

    @Override
    public String description() {
        return "The value of data_time_out set of system parameters";
    }

    @Override
    public Map<String, String> argsDesc() {
        Map<String, String> args = new Hashtable<String, String>();
        args.put(DATA_TIME_OUT, "Socket free time to maintain. unit:milliseconds");
        return args;
    }

    @Override
    public String[] getCommand() {
        return new String[] { "set_socket_time_out" };
    }

}
