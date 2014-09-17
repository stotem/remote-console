package com.forway.common.remote.console;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Console implements Runnable {
    private int port;
    private String ipAddress;
    private boolean running;

    /**
     * Create Console instance
     * @param port
     * @param tasks
     * @throws ConsoleException
     */
    public Console(int port, ConsoleTask... tasks) throws ConsoleException {
        this(null, port, tasks);
    }

    /**
     * Create Console instance
     * @param ipAddr
     * @param port
     * @param tasks
     * @throws ConsoleException
     */
    public Console(String ipAddr, int port, ConsoleTask... tasks) throws ConsoleException {
        this.ipAddress = ipAddr;
        this.port = port;
        // 注册自定义命令
        for (ConsoleTask task : tasks) {
            if ( !ConsoleConfig.regConsoleTask(task) ) {
                String[] command = task.getCommand();
                if ( command == null || command.length == 0 ) {
                    throw new ConsoleException("The command invalid.");
                }
                throw new ConsoleException("The command regedit fail. command=" + Arrays.toString(command));
            }
        }
    }

    public synchronized void start() {
        new Thread(this, "Console-" + ConsoleConfig.VERSION + "-" + port).start();
    }

    @Override
    public void run() {
        running = true;
        // 监听端口
        ServerSocket serverSocket = null;
        try {
            if ( ipAddress == null ) {
                serverSocket = new ServerSocket(port);
            }
            else {
                ConsoleConfig.putClientIp(ipAddress);
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(ipAddress, port));
            }
            while (running) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(ConsoleConfig.getDataTimeout());
                new Thread(new ConsoleControl(socket), socket.getRemoteSocketAddress().toString()).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if ( serverSocket != null ) {
                try {
                    serverSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
