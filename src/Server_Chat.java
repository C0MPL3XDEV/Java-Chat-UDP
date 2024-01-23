import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server_Chat implements Runnable {

    public static final int PORT = 4567;
    byte[] bufferIn = new byte[1024];
    byte[] bufferOut = new byte[1024];

    Map<String, DatagramPacket> clients = new HashMap<>();

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            System.out.println("[LOG]: Server Started AT: " + PORT);

            Thread receiveSendThread = new Thread(() -> {
                try {
                    while (true) {
                        receiveMessage(serverSocket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiveSendThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(DatagramSocket serverSocket) throws IOException {
        DatagramPacket messageReceived = new DatagramPacket(bufferIn, bufferIn.length);
        serverSocket.receive(messageReceived);

        String clientKEY = messageReceived.getAddress().toString() + ":" + messageReceived.getPort();

        if (!clients.containsKey(clientKEY)) {
            String welcomeMessage = "[SYSTEM]: Welcome into the Chat - Type /help for commands";
            byte[] welcomeBuffer = welcomeMessage.getBytes();

            DatagramPacket welcomePacket = new DatagramPacket(welcomeBuffer, welcomeBuffer.length, messageReceived.getAddress(), messageReceived.getPort());
            serverSocket.send(welcomePacket);

            clients.put(clientKEY, messageReceived);
            System.out.println("[LOG]: New Client Connected: " + clientKEY);
        }

        String receivedMsg = new String(messageReceived.getData()).trim();
        System.out.println("[LOG]: Message From: " + clientKEY + receivedMsg);

        broadCastMessage(serverSocket, receivedMsg);
    }

    public void broadCastMessage (DatagramSocket serverSocket, String message) {
        bufferOut = message.getBytes(StandardCharsets.UTF_8);

        for (Map.Entry<String, DatagramPacket> entry : clients.entrySet()) {
            DatagramPacket clientPacket = entry.getValue();

            DatagramPacket dataPacket = new DatagramPacket(bufferOut, bufferOut.length, clientPacket.getAddress(), clientPacket.getPort());
            try {
                serverSocket.send(dataPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server_Chat serverChat = new Server_Chat();
        serverChat.run();
    }
}
