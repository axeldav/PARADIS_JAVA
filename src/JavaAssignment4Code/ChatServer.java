// Peter Idestam-Almquist, 2021-03-07.
// Server, multi-threaded, accepting several simultaneous clients.

//axda2670
//First start the ChatServer
//Then start the Clients, with the first message being their names

package JavaAssignment4Code;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class ChatServer implements Runnable {
    private final static int PORT = 8000;
    private final static int MAX_CLIENTS = 5;
    private final static Executor executor = Executors.newFixedThreadPool(MAX_CLIENTS);

    // We need a list to hold the clients
    // Should not be updated often, but iterated often
    final static CopyOnWriteArrayList<PrintWriter> clientSocketWriters = new CopyOnWriteArrayList<PrintWriter>();

    // List of Client-names also
    // i dont use this
    final static CopyOnWriteArrayList<String> clientNames = new CopyOnWriteArrayList<>();

    // So here we have just two names and we should have lists here
    private final Socket clientSocket;
    private String clientName = "";

    // Här verkar det som att en chatserver har endast en clientSocket, men den ska ha en lista av clientSockets
    private ChatServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        SocketAddress remoteSocketAddress = clientSocket.getRemoteSocketAddress();
        SocketAddress localSocketAddress = clientSocket.getLocalSocketAddress();
        System.out.println("Accepted client " + remoteSocketAddress + " (" + localSocketAddress + ").");


        PrintWriter socketWriter = null;
        BufferedReader socketReader = null;
        try {

            socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            socketReader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            String threadInfo = " (" + Thread.currentThread().getName() + ").";
            String inputLine = socketReader.readLine();
            System.out.println("Received: \"" + inputLine + "\" from "
                    + remoteSocketAddress + threadInfo);

            // First message is client name.
            clientName = inputLine;


            /**
             *  I THINK WE MUST ALSO HAVE ONE WHILE LOOP FOR EVERY CLIENT-SERVER CONNECTION
             *  OR COULD WE HAVE LOOP OF SOCKETS?
             *  in 'socketReader.readLine()', its looking at a specific socket
             *  What is the socketWriter
             */
            // ====================== while loop where we listen to client =======================================================================
            while (inputLine != null) {

                for (PrintWriter p: clientSocketWriters) {
                    p.println(clientName + ": " + inputLine);
                }
                //socketWriter.println(inputLine);
                System.out.println("Sent: \"" + inputLine + "\" to " + clientName + " " + remoteSocketAddress + threadInfo);

                inputLine = socketReader.readLine();
                System.out.println("Received: \"" + inputLine + "\" from " + clientName + " " + remoteSocketAddress + threadInfo);

            }
            // ============================== end of while loop =====================================================================================
            System.out.println("Closing connection " + remoteSocketAddress
                    + " (" + localSocketAddress + ").");
        } // try

        catch (Exception exception) {
            System.out.println(exception);
        }
        finally {
            try {
                if (socketWriter != null)
                    socketWriter.close();
                if (socketReader != null)
                    socketReader.close();
                if (clientSocket != null)
                    clientSocket.close();
            }
            catch (Exception exception) {
                System.out.println(exception);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("EchoServer started.");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            SocketAddress serverSocketAddress = serverSocket.getLocalSocketAddress();
            System.out.println("Listening (" + serverSocketAddress + ").");

            // this while-loop is constantly waiting for new clients to connect
            // When a client connects to the server,
            // the server creates a new socket object for that client connection.
            while (true) {

                System.out.println("waiting here for a client to connect");
                clientSocket = serverSocket.accept(); // this is our connection between the server and the client.
                System.out.println("a client connected :)");

                //add to list of writers for all the Sockets
                clientSocketWriters.add(new PrintWriter(clientSocket.getOutputStream(), true));

                // Creates a ChatServer
                // This could be wrong, and we should have a list of sockets in this Server instead
                executor.execute(new ChatServer(clientSocket));


            } // while
        } // main
        catch (Exception exception) {
            System.out.println(exception);
        }
        finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            }
            catch (Exception exception) {
                System.out.println(exception);
            }
        }
    }
}