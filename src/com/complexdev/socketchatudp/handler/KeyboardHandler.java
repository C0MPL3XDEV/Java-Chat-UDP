package com.complexdev.socketchatudp.handler;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

public class KeyboardHandler extends Thread {
    private ConcurrentLinkedDeque<String> messagesToSend = new ConcurrentLinkedDeque<>();
    private Scanner in = new Scanner(System.in);
    public KeyboardHandler() {

    }


    @Override
    public void run() {
        while (true) {
            try {
                System.out.print("[INPUT]: ");
                System.out.flush(); // Forces the string to be redirected without the end-of-line character
                String messageWrite = in.nextLine();
                messagesToSend.add(messageWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String listPoll() {
        return this.messagesToSend.poll();
    }

    public String peek() {
        return this.messagesToSend.peek();
    }
}
