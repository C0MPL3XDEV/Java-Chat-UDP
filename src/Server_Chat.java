import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class Server_Chat {
    public static final int PORT = 8956;
    byte[] bufferIn = new byte[1024];
    byte[] bufferOut = new byte[1024];

    // Create a Map and HashMap (Data Structures) we use to trace the connected clients
    // every client will have unique key (IP + Port) The HashMap allows each key to be associated with a value,
    // which in our case is a DatagramPacket object representing the client (with this we get information about the client)
    private static final Map<String, DatagramPacket> clients = new HashMap<>();

    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            while (true) {
                receiveSendMsg(serverSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveSendMsg(DatagramSocket serverSocket) throws IOException {

        DatagramPacket receivedMsg = new DatagramPacket(bufferIn, bufferIn.length);
        serverSocket.receive(receivedMsg);

        String clientKEY = receivedMsg.getAddress().toString() + ":" + receivedMsg.getPort(); // Create the clientKey with IP + Port

        // Check if it's the first connection of the client to send the welcome msg checking that the client key is not in the hashmap
        if(!clients.containsKey(clientKEY)) {
            String welcomeMsg = "[SYSTEM]: Welcome To The UDP Chat :) type /help for get the list of the all commands";
            byte[] welcomeData = welcomeMsg.getBytes();
            DatagramPacket welcomePacket = new DatagramPacket(welcomeData, welcomeData.length, receivedMsg.getAddress(), receivedMsg.getPort());
            serverSocket.send(welcomePacket);

            clients.put(clientKEY, receivedMsg); // Put the info of the new client in the hashMap.
            System.out.println("[LOG]: New Client Connected: " + clientKEY);
        }

        String rMsg = new String(receivedMsg.getData()).trim();
        System.out.println("[LOG]: Message" + rMsg);

        broadCastMessage(serverSocket, clientKEY, rMsg);
    }

    private void broadCastMessage(DatagramSocket serverSocket, String senderKey, String message) {
        bufferOut = message.getBytes();

        for (Map.Entry<String, DatagramPacket> entry : clients.entrySet()) {
            String clientKey = entry.getKey();
            DatagramPacket clientPacket = entry.getValue();

        }
    }

    public static void main(String[] args) {
        Server_Chat serverChat = new Server_Chat();
        serverChat.run();
    }
}
