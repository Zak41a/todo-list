package de.fhac.client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            int serverPort = 5050;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the server IP address:");
            String serverIP = scanner.nextLine();

            System.out.println("Connecting to server at " + serverIP + " on port " + serverPort);
            Socket clientSocket = new Socket(serverIP, serverPort);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

            Thread readThread = new ReadFromServerThread(clientSocket);
            readThread.start();

            while (true) {
                System.out.println("Enter a message (or 'exit' to quit):");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                outToServer.writeUTF(message);
                outToServer.flush();
            }

            scanner.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ReadFromServerThread extends Thread {
        private final Socket clientSocket;

        public ReadFromServerThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    try {
                        String serverMessage = inFromServer.readUTF();
                        System.out.println("Server response: " + serverMessage);
                    } catch (EOFException e) {
                        // Server closed the connection
                        System.out.println("Server connection closed.");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
