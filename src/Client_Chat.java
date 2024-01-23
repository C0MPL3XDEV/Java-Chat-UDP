import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client_Chat {

    public static final int PORT = 8956;

    byte[] bufferIn = new byte[1024];
    byte[] bufferOut = new byte[1024];

    public static Scanner in = new Scanner(System.in);

    public void run() {
        try (DatagramSocket clientSocket = new DatagramSocket()) {

            InetAddress ipServer = InetAddress.getByName("127.0.0.1"); // Specify the IP address of the Server
            starter(clientSocket, ipServer);

            while (true) {
                sendReceiveMsg(clientSocket, ipServer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void starter(DatagramSocket clientSocket, InetAddress ipServer) throws IOException {
        String testMsg = ".";
        byte[] testMess = testMsg.getBytes();

        DatagramPacket testPacket = new DatagramPacket(testMess, testMess.length, ipServer, PORT);
        clientSocket.send(testPacket);
    }

    public void sendReceiveMsg (DatagramSocket clientSocket, InetAddress ipServer) throws IOException {
        DatagramPacket receiveMsg = new DatagramPacket(bufferIn, bufferIn.length);
        clientSocket.receive(receiveMsg);

        String dataReceived = new String(receiveMsg.getData()).trim();
        System.out.println(dataReceived);

        System.out.println(": ");
        String message = in.nextLine();

        if (message.contentEquals("/exit")) {
            System.out.println("GoodBye :(");
            System.exit(0);
        }

        bufferOut = message.getBytes();
        DatagramPacket sendMsgPacket = new DatagramPacket(bufferOut, bufferOut.length, ipServer, PORT);
        clientSocket.send(sendMsgPacket);

    }

    public static void main(String[] args) {
        Client_Chat clientChat = new Client_Chat();
        clientChat.run();
    }
}
