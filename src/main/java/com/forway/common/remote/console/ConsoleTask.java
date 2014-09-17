package com.forway.common.remote.console;

import java.util.Map;

public interface ConsoleTask {

    String doTask(Map<String, String> args);

    String description();

    Map<String, String> argsDesc();

    String[] getCommand();
}
