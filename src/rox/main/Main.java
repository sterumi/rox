package rox.main;

import rox.main.server.MainServer;

import java.util.Scanner;

public class Main {

    private static MainServer mainServer;

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {  mainServer = new MainServer(8981); mainServer.start(); }).start();

        System.out.println("Started ROX.");


        String input;
        while((input = scanner.nextLine()) != null){
            String[] scargs = input.split(" ");
            switch(scargs[0]){
                case "stop":
                    System.exit(0);
                    break;

                case "say":
                    System.out.println(input.substring(scargs[0].length() + 1));
                    break;
            }
        }

    }

}
