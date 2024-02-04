package com.complexdev.socketchatudp;

import com.complexdev.socketchatudp.handler.KeyboardHandler;
import com.complexdev.socketchatudp.handler.NetworkHandler;

import java.net.*;

public class Client_Chat  {

    public static final int PORT = 8956;
    public static InetAddress IPServer;

    static {
        try {
            IPServer = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try  {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.connect(new InetSocketAddress(IPServer, PORT));

            NetworkHandler networkHandler = new NetworkHandler(clientSocket);
            KeyboardHandler keyboardHandler = new KeyboardHandler();

            networkHandler.start();
            keyboardHandler.start();

            while (keyboardHandler.peek() == null) {
                
            }

            String name = keyboardHandler.listPoll();
            networkHandler.sendMsg(name);

            while (true) {
                String message;
                while (networkHandler.peek() != null) {
                    message = networkHandler.listPoll();
                    System.out.println("[MESSAGE]: " + message);
                    System.out.flush();
                }
                while (keyboardHandler.peek() != null ) {
                    message = keyboardHandler.listPoll();
                    System.out.println(message);
                    networkHandler.sendMsg(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}