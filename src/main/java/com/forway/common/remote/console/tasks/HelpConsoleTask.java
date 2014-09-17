package com.forway.common.remote.console.tasks;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.forway.common.remote.console.ConsoleConfig;
import com.forway.common.remote.console.ConsoleTask;

public class HelpConsoleTask implements ConsoleTask {

    @Override
    public String doTask(Map<String, String> args) {
        StringBuilder result = new StringBuilder();
        Iterator<ConsoleTask> configIterator = ConsoleConfig.getConsoleTasks().iterator();
        result.append("-----------Console useage-----------");
        result.append(ConsoleConfig.LINE_SEPARATOR);

        while (configIterator.hasNext()) {
            ConsoleTask consoleTask = (ConsoleTask) configIterator.next();
            result.append(Arrays.toString(consoleTask.getCommand()));
            result.append(" - ");
            result.append(consoleTask.description());
            result.append(ConsoleConfig.LINE_SEPARATOR);
            // 参数
            if ( consoleTask.argsDesc() != null && !consoleTask.argsDesc().isEmpty() ) {
                Set<Entry<String, String>> setArgs = consoleTask.argsDesc().entrySet();
                for (Entry<String, String> entry : setArgs) {
                    result.append("      [");
                    result.append(entry.getKey());
                    result.append(" - ");
                    result.append(entry.getValue());
                    result.append("]");
                    result.append(ConsoleConfig.LINE_SEPARATOR);
                }
            }
            result.append(ConsoleConfig.LINE_SEPARATOR);
        }
        return result.toString();
    }

    @Override
    public String description() {
        return "Display Console useage";
    }

    @Override
    public Map<String, String> argsDesc() {
        return null;
    }

    @Override
    public String[] getCommand() {
        return new String[] { "help", "useage" };
    }

}
