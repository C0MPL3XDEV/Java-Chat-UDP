package com.complexdev.socketchatudp.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientHandler extends Thread {

    DatagramSocket clientSocket;
    ConcurrentLinkedDeque<String> messages = new ConcurrentLinkedDeque<>();

    public ClientHandler(DatagramSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] message = new byte[1024];
                DatagramPacket dataReceived = new DatagramPacket(message, message.length);
                clientSocket.receive(dataReceived);

                String dataReceive = new String(dataReceived.getData(), 0, dataReceived.getLength());
                messages.add(dataReceive);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String listPoll() {
        return this.messages.poll();
    }

    public void sendMessage(String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, clientSocket.getInetAddress(), clientSocket.getPort());
        clientSocket.send(sendPacket);
    }
}
