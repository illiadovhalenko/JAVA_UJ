package src;

import java.io.File;
import java.util.Scanner;

public class App{
    private static final int timeOut = 1000;
    private static int mapSize = 10;
    public static void main(String[] args){
        StartArguments arg = parseArgs(args);
        File mapFile = new File(arg.mapFile());
        try(Scanner scanner = new) {

        }

    }
    private static StartArguments parseArgs(String[] args){
        GameMode mode = null;
        String server = null;
        String map = null;
        int port = 0;
        for(int i=0; i<args.length; i++){
            String option = args[i];
            try {
                switch (option){
                    case "-mode"->{
                        if(args[i+1].equals("server") || args[i+1].equals("client")){
                            mode = GameMode.valueOf(args[++i].toUpperCase());
                        }else {
                            System.err.println("Uncorrect GameMode");
                            System.exit(1);
                        }
                    }
                    case "-server"->{
                            server = args[++i];
                    }
                    case "-port"->{
                        try {
                            port = Integer.parseInt(args[++i]);
                        }catch (NumberFormatException e){
                            System.err.println("Wrong port number: "+ args[i]);
                            System.exit(1);
                        }
                    }
                    case "-map"->{
                        map = args[++i];
                    }
                    default -> {
                        System.err.println("Error with "+ args[i]);
                        System.exit(1);
                    }
                }
            }catch (ArrayIndexOutOfBoundsException e){
                System.err.println("Unknown argument for" + option);
                System.exit(1);
            }
        }
        return new StartArguments(mode, server, port, map);
    }
}