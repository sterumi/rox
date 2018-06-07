package rox.main.minecraftserver;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class MCInputHandler extends Thread {

    private Object[] objects;

    public MCInputHandler(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public void run() {
        try {
            String input;
            while ((input = ((BufferedReader) objects[2]).readLine()) != null) {
                String[] args = input.split("[§ ]+");
                if (input.startsWith("§")) {

                } else {
                    ((PrintWriter) objects[3]).println("§INVALID_COMMAND_STRUCTURE");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
