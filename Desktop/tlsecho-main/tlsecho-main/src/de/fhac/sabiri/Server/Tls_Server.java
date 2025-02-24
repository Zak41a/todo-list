package de.fhac.sabiri.Server;
import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

public class Tls_Server {
	    public static void main(String[] args) throws IOException {
	    	String keystorePath = "C:/Users/Public/git/TLS/rn-fhac-de.jks";
	        String keystorePassword = "UPWCUuR";
	        
	        try {
	            SSLContext sslContext = SSLContext.getInstance("TLS");
	            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	            keyStore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
	            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
	            keyManagerFactory.init(keyStore, keystorePassword.toCharArray());
	            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	            trustManagerFactory.init(keyStore);
	            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
	            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
	            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(6000);
	            
	            System.out.println("port 6000");

	            while (true) {
	                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
	                System.out.println("Connection accepted client");
	                var dataInputStream = new DataInputStream(sslSocket.getInputStream());
	                var dataOutputStream = new DataOutputStream(sslSocket.getOutputStream());
	                while (true) {
	                    String clientMessage = dataInputStream.readUTF();
	                    System.out.println("Message received client: " + clientMessage);
	                    String upperCaseMessage = clientMessage.toUpperCase();
	                    dataOutputStream.writeUTF(upperCaseMessage);
	                    dataOutputStream.flush();
	                    System.out.println("Sent response to client: " + upperCaseMessage);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

