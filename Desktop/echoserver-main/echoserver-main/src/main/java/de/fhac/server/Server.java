package de.fhac.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5050);

            System.out.println("Server started and listening on port 5050...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientH(clientSocket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientH extends Thread {
    private final Socket clientSocket;

    public ClientH(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String clientMessage = inFromClient.readUTF();
                String echoMessage = "echo: " + clientMessage;

                outToClient.writeUTF(echoMessage);
                outToClient.flush();

                if (clientMessage.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
