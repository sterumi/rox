package rox.main.server.dummy;
/*
Created by Bleikind
*/

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

class TestInputHandler extends Thread{

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter printWriter;

    TestInputHandler(Socket socket, BufferedReader reader, PrintWriter printWriter) {
        this.socket = socket;
        this.reader = reader;
        this.printWriter = printWriter;
    }

    @Override
    public void run(){
        String input;
        try{
            while((input = reader.readLine()) != null){
                if(input.startsWith("§")){
                    String[] args = input.substring(1).split("§");
                    if(args.length >= 3){
                        System.out.println("Answer: " + args[1] + " Value: " + args[2]);
                    }else{
                        System.out.println(input);
                    }
                }else{
                    System.out.println("Could not identify the response: " + input);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
