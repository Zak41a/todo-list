package de.fhac.sabiri.Client;
import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Scanner;

public class Tls_Client {
    public static void main(String[] args) throws IOException {
    	String truststorePath = "C:/Users/Public/git/TLS/rn-fhac-de.jks";
        String truststorePassword = "UPWCUuR";
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(new FileInputStream(truststorePath), truststorePassword.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(truststore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 6000);
            var dataInputStream = new DataInputStream(sslSocket.getInputStream());
            var dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());
            System.out.print("Message Next: ");
            var scanner = new Scanner(System.in);
            
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                String response = dataInputStream.readUTF();
                System.out.println("Response from server: " + response);
                System.out.print("Message Next: ");
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
