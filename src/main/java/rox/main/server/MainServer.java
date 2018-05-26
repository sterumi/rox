package rox.main.server;

import rox.main.server.database.MainDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {

    private ServerSocket serverSocket;

    private int port;

    private boolean waitingConnection = true;

    private boolean isActiv = false;

    private ConcurrentHashMap<String, Object[]> clients = new ConcurrentHashMap<>();

    private MainDatabase database;

    private Thread acceptThread;

    public MainServer(int port){
        this.port = port;
    }

    public void start(){
        try {
            database = new MainDatabase("localhost", 3306, "root", "", "rox");
            serverSocket = new ServerSocket(port);
            isActiv = true;
            acceptThread = new AcceptThread();
            acceptThread.start();
            System.out.println("Main Server started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            serverSocket.close();
            acceptThread.interrupt();
            clients.forEach(((s, objects) -> {
                Thread thread = (Thread)objects[2];
                thread.interrupt();
            }));
            clients.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isActiv(){
        return isActiv;
    }

    public boolean isWaitingConnection(){
        return waitingConnection;
    }

    class AcceptThread extends Thread{

        @Override
        public void run(){
            try {
                Socket socket;
                while((socket = serverSocket.accept()) != null){
                    waitingConnection = false;

                    // <name>§<password>

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                    String[] input = reader.readLine().split("§");

                    if(database.Query("SELECT * FROM users WHERE username='" + input[0] + "' AND password='" + input[1] + "'") != null){
                        Object[] objects = new Object[8];
                        objects[0] = input[0]; objects[1] = socket;
                        Thread thread1 = new ClientThread(objects);
                        objects[2] = thread1;
                        thread1.start();


                        clients.put(input[0], objects);
                        writer.println("§SERVER_CONNECTED");
                    }else{
                        writer.println("§SERVER_WRONG_LOGIN");
                        socket.close();
                    }


                    waitingConnection = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    class ClientThread extends Thread{

        private Object[] objects;

        ClientThread(Object[] objects){
            this.objects = objects;
        }


        @Override
        public void run() {
            Socket socket = (Socket) objects[1];
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                String input;
                while((input = reader.readLine()) != null){
                    String[] args = input.split(" ");
                    if(args[0].startsWith("§")){
                        switch(args[0]){
                            case "§DISCONNECT":
                                socket.close();
                                Thread thread = (Thread)objects[2];
                                thread.interrupt();
                                clients.remove(objects[0]);
                                break;
                            case "§INFO":
                                writer.println("MainServer§" + clients.size());
                                break;
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
