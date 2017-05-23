package app;

import concurrentutiles.Channel;
import concurrentutiles.Dispecher;
import concurrentutiles.ThreadPool;
import netUtiles.Host;
import netUtiles.MessageHandlerFactory;
import netUtiles.Session;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by svetlana on 10.02.17.
 */
public class Server {

    private static final Object lock = new Object();    // монитор
    private static int maxSessionCount;     // максимальное количество одновременных сессий. Передается в аргументах main.
    private static volatile int sessionCount = 0;   // текущее количество сессий

    // декремент счетчика сессий. Вызывается при закрытии сессии.
    public static void closeSession(){
        synchronized (lock) {
            sessionCount--;
            lock.notify();       // оповещаем ждущий поток
            System.out.println("netUtiles.Session closed");
        }
    }

    public static void main(String[] args) {
        int port = 0;
        try {
            maxSessionCount = Integer.parseInt(args[1]);
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Usege integer for port");
        }

        Channel channel = new Channel(maxSessionCount);
        final MessageHandlerFactory messageHandlerFactory;
        try {
            Class classMHF = Class.forName("netUtiles.CommandLineMHF");
            try {
                messageHandlerFactory = (MessageHandlerFactory) classMHF.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        final Host host;
        try {
            host = new Host(port, channel, messageHandlerFactory);

            final ThreadPool threadPool = new ThreadPool(maxSessionCount);
            Dispecher dispecher = new Dispecher(channel, threadPool);
            host.start();
            dispecher.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}