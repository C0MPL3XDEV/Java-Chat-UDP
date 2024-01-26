package com.complexdev.socketchatudp.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NetworkHandler extends Thread {
    DatagramSocket serverSocket;
    private ConcurrentLinkedDeque<String> messages = new ConcurrentLinkedDeque<>();

    public NetworkHandler(DatagramSocket server) {
        this.serverSocket = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] message = new byte[1024];
                DatagramPacket dataPacket = new DatagramPacket(message, message.length);
                serverSocket.receive(dataPacket);

                String dataReceived = new String(dataPacket.getData(), 0, dataPacket.getLength());
                messages.add(dataReceived);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String listPoll() {
       return this.messages.poll();
    }

    public String peek() {
        return this.messages.peek();
    }

    public void sendMsg(String message) throws IOException {
         byte[] buffer = message.getBytes();
         DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length, serverSocket.getRemoteSocketAddress());
         serverSocket.send(dataPacket);
    }
}
