package com.forway.common.remote.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConsoleControl implements Runnable {
    private static final String QUIT_CMD = "quit";
    /** socket对应的输入流 */
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private boolean running;
    private SimpleDateFormat dataDateFormat = null;
    private Socket socket;
    private SocketAddress socketAddr = null;

    /**
     * Create ConsoleControl instance
     * @param socket
     * @throws IOException
     */
    public ConsoleControl(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        dataDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        this.socketAddr = this.socket.getRemoteSocketAddress();
    }

    @Override
    public void run() {
        running = true;
        Map<String, String> args = null;
        ConsoleTask consoleTask = null;
        try {
            // 判断是否为合法客户端IP
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            String ipaddr = inetSocketAddress.getAddress().getHostAddress();
            if ( !ConsoleConfig.containsIp(ipaddr) ) {
                throw new ConsoleException("[ERROR] The ip address invalid. [ipaddr=" + ipaddr + "]");
            }
            // 判断是否达到最大连接数, 如果达到最大连接数，则断开连接
            if ( !ConsoleConfig.addClientCount(inetSocketAddress) ) {
                throw new ConsoleException("[ERROR] More than the client limit number.");
            }
            // 输出欢迎信息
            String inputString = null;
            while (running) {
                // 如果为第一次，则显示欢迎信息
                if ( inputString == null ) {
                    inputString = "welcome";
                }
                // 读取command
                else {
                    inputString = bufferedReader.readLine();
                    if ( inputString == null ) {
                        throw new SocketException("Socket closed.");
                    }
                    inputString = inputString.trim();
                }
                // 如果没有输入任何东西
                if ( inputString.length() == 0 ) {
                    continue;
                }
                String[] inStrings = inputString.split(" ");
                String command = inStrings[0];
                // 如果为退出
                if ( QUIT_CMD.equalsIgnoreCase(command) ) {
                    throw new ConsoleException("bye-bye");
                }
                // 是否为有效命令
                consoleTask = ConsoleConfig.getConsoleTask(command);
                if ( consoleTask == null ) {
                    write("The command invalid. [command=" + command + "]");
                    continue;
                }
                // 如果有参数，则进行参数构造
                if ( inStrings.length > 1 ) {
                    inStrings = inStrings[1].split(",");
                    String[] arginfo = null;
                    args = new HashMap<String, String>(inStrings.length);
                    for (String tmpArgs : inStrings) {
                        arginfo = tmpArgs.split("=");
                        args.put(arginfo[0], arginfo.length > 1 ? arginfo[1] : null);
                    }
                }
                // 取得command所带参数
                String result = consoleTask.doTask(args);
                args = null;
                if ( result != null ) {
                    write(result);
                }
            }
        }
        catch (ConsoleException e) {
            try {
                write(e.getMessage());
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            // socket已经关闭，针对此异常不做任何处理
        }
        finally {
            ConsoleConfig.minusClientCount(socketAddr);
            try {
                socket.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void write(String out) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ConsoleConfig.LINE_SEPARATOR);
        stringBuilder.append(out);
        stringBuilder.append(ConsoleConfig.LINE_SEPARATOR);
        stringBuilder.append(ConsoleConfig.LINE_SEPARATOR);
        stringBuilder.append("The last time the response message for ");
        stringBuilder.append(dataDateFormat.format(new Date()));
        stringBuilder.append(ConsoleConfig.LINE_SEPARATOR);
        stringBuilder.append("------------------------------------");
        stringBuilder.append(ConsoleConfig.LINE_SEPARATOR);
        stringBuilder.append(ConsoleConfig.LINE_SEPARATOR);
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.flush();
    }
}
