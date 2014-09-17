package com.forway.common.remote.console.tasks;

import java.util.Hashtable;
import java.util.Map;

import com.forway.common.remote.console.ConsoleConfig;
import com.forway.common.remote.console.ConsoleTask;

public class RemoveClientIPAddressTask implements ConsoleTask {

    private static final String CLIENT_IP = "client_ip";

    @Override
    public String doTask(Map<String, String> args) {
        String result = null;
        String tmpClientIP = args == null ? null : args.get(CLIENT_IP);
        if ( tmpClientIP == null ) {
            result = "The client_ip is null.";
        }
        else {
            ConsoleConfig.removeClientIp(tmpClientIP.trim());
            result = "Operation success.[client_ip=" + tmpClientIP + "]";
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
        args.put(CLIENT_IP, "Need to move out of the client IP address.");
        return args;
    }

    @Override
    public String[] getCommand() {
        return new String[] { "remove_client_ip" };
    }

}
