package rox.main.blscripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class BlScriptLoader {

    private long count;

    public void loadFile(String filePath){

        new Thread(() ->{
            try{
                File file = new File(filePath);

                switch(file.getName().split(".")[1]){
                    case "bl":



                        FileInputStream fs = new FileInputStream(file);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fs));
                        Stream<String> lines = reader.lines();
                        count = lines.count();

                        lines.forEach(s -> {
                            String[] commands = s.split(" ");

                            switch(commands[0]){
                                case "print":

                                    break;

                                case "safef":

                                    break;
                            }
                        });

                        break;
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public long count(){
        return count;
    }

}
