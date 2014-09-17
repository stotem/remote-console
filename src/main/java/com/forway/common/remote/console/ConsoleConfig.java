package com.forway.common.remote.console;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.forway.common.remote.console.tasks.AddClientIPAddressTask;
import com.forway.common.remote.console.tasks.HelpConsoleTask;
import com.forway.common.remote.console.tasks.RemoveClientIPAddressTask;
import com.forway.common.remote.console.tasks.SetMaxClientTask;
import com.forway.common.remote.console.tasks.SetSocketTimeOutTask;
import com.forway.common.remote.console.tasks.ShowConsoleTask;
import com.forway.common.remote.console.tasks.WelcomeConsoleTask;

public class ConsoleConfig {
    // 当前版本
    public static final String VERSION = "0.2 beta";
    // 换行符
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    // 超时时间，默认1分钟
    private static int dataTimeout = 60 * 1000;
    private static Map<String, ConsoleTask> consoleTasks = null;
    private static int clientCount = 0;
    private static int maxClientCount = 2;
    private static Set<String> clientIp = null;
    private static Set<SocketAddress> onLineClient = null;

    static {
        // 初始化客户端IP列表
        clientIp = Collections.synchronizedSet(new HashSet<String>());
        clientIp.add("127.0.0.1");
        // 初始化在线客户端IP列表
        onLineClient = Collections.synchronizedSet(new HashSet<SocketAddress>());
        // 初始化命令
        consoleTasks = new Hashtable<String, ConsoleTask>();
        regConsoleTask(new HelpConsoleTask());
        regConsoleTask(new WelcomeConsoleTask());
        regConsoleTask(new ShowConsoleTask());
        regConsoleTask(new SetSocketTimeOutTask());
        regConsoleTask(new SetMaxClientTask());
        regConsoleTask(new AddClientIPAddressTask());
        regConsoleTask(new RemoveClientIPAddressTask());
    }

    public static Collection<ConsoleTask> getConsoleTasks() {
        return Collections.synchronizedSet(new HashSet<ConsoleTask>(consoleTasks.values()));
    }

    static boolean containsIp(String ip) {
        return clientIp.contains(ip);
    }

    /**
     * get clientCount value
     * @return the clientCount
     */
    public static int getClientCount() {
        return clientCount;
    }

    /**
     * add clientCount value
     * @param socketAddr
     * @param clientCount
     *        the clientCount to set
     */
    static synchronized boolean addClientCount(SocketAddress socketAddr) {
        if ( maxClientCount <= clientCount ) {
            return false;
        }
        onLineClient.add(socketAddr);
        clientCount++;
        return true;
    }

    /**
     * get maxClientCount value
     * @return the maxClientCount
     */
    public static int getMaxClientCount() {
        return maxClientCount;
    }

    /**
     * set maxClientCount value
     * @param maxClientCount
     *        the maxClientCount to set
     */
    public static void setMaxClientCount(int maxClientCount) {
        ConsoleConfig.maxClientCount = maxClientCount;
    }

    /**
     * add clientCount value
     * @param clientCount
     *        the clientCount to set
     */
    static synchronized void minusClientCount(SocketAddress socketAddress) {
        onLineClient.remove(socketAddress);
        clientCount--;
    }

    public static void putClientIp(String ip) {
        clientIp.add(ip);
    }

    public static void removeClientIp(String ip) {
        clientIp.remove(ip);
    }

    public static String clientToString() {
        StringBuilder result = new StringBuilder();
        for (String client : clientIp) {
            result.append("," + client);
        }
        return result.substring(1);
    }

    public static void setDataTimeout(int timeout) {
        dataTimeout = timeout;
    }

    public static ConsoleTask getConsoleTask(String command) {
        return consoleTasks.get(command);
    }

    /**
     * get dataTimeout value
     * @return the dataTimeout
     */
    public static int getDataTimeout() {
        return dataTimeout;
    }

    public static boolean regConsoleTask(ConsoleTask task) {
        String[] command = task.getCommand();
        if ( command == null ) {
            return false;
        }
        boolean added = false;
        for (String cmd : command) {
            if ( !consoleTasks.containsKey(cmd) ) {
                consoleTasks.put(cmd, task);
                added = true;
            }
        }
        return added;
    }

    public static String onLineClientString() {
        StringBuilder result = new StringBuilder();
        InetSocketAddress inetSocketAddress = null;
        for (SocketAddress socketAddress : onLineClient) {
            inetSocketAddress = (InetSocketAddress) socketAddress;
            result.append(", ");
            result.append(inetSocketAddress.getAddress().getHostAddress());
            result.append(":");
            result.append(inetSocketAddress.getPort());
        }
        return result.substring(1);
    }

}
