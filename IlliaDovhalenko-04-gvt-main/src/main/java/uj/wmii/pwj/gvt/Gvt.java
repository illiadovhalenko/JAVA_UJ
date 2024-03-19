package uj.wmii.pwj.gvt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Gvt {
    private final ExitHandler exitHandler;
    private final String main_folder = ".gvt";
    private final String current_version = ".gvt/current_version";
    private final String last_version = ".gvt/last_version";
    public final String info = ".info";

    public Gvt(ExitHandler exitHandler) {
        this.exitHandler = exitHandler;
    }

    public static void main(String... args) {
        Gvt gvt = new Gvt(new ExitHandler());
        gvt.mainInternal(args);
    }

    void system_problem() {
        exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
    }

    int get_current_version() {
        try {
            return Integer.parseInt(Files.readString(Path.of(current_version)).trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void mainInternal(String... args) {
        if (args.length == 0) {
            exitHandler.exit(1, "Please specify command.");
        } else if (args[0].equals("init")) {
            init();
        } else if (!Files.exists(Path.of(main_folder))|| !Files.isDirectory(Path.of(main_folder))) {
            exitHandler.exit(-2, "Current directory is not initialized. Please use init command to initialize.");
        } else {
            switch (args[0]) {
                case "add" -> add(args);
                case "detach" -> detach(args);
                case "checkout" -> checkout(args);
                case "commit" -> commit(args);
                case "history" -> history(args);
                case "version" -> version(args);
                default -> exitHandler.exit(1, "Unknown command " + args[0] + ".");
            }
        }
    }

    void init() {
        if (Files.exists(Path.of(main_folder))) {
            exitHandler.exit(10, "Current directory is already initialized.");
            return;
        }
        try {
            new File(main_folder).mkdir();
            Path version = Files.createDirectory(Path.of(main_folder, "v0"));
            Path info_of_version = Path.of(version.toString(), info);
            Files.createFile(info_of_version);
            Files.writeString(info_of_version, "GVT initialized.");
            Files.createFile(Path.of(current_version));
            Files.writeString(Path.of(current_version), "0");
            exitHandler.exit(0, "Current directory initialized successfully.");
        } catch (IOException e) {
            system_problem();
        }
    }

    void new_info_file(Path version, String message) throws IOException {
        Path file = Path.of(version.toString(), info);
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        Files.writeString(file, message);
    }

    void update_version(int new_version) throws IOException {
        Files.writeString(Path.of(current_version), String.valueOf(new_version));
        Files.writeString(Path.of(last_version), String.valueOf(new_version));
    }

    void add(String... args) {
        if (args.length < 2) {
            exitHandler.exit(20, "Please specify file to add.");
        }
        File addedFile = new File(args[1]);
        if (!addedFile.exists()) {
            exitHandler.exit(21, "File not found. File: " + args[1]);
            return;
        }
        try {
            int cur_version = get_current_version();
            Path path_to_cur_version = Path.of(main_folder, "v"+String.valueOf(cur_version));
            Path path_to_file_in_cur_version = Path.of(path_to_cur_version.toString(), args[1]);
//            List<String> results = new ArrayList<String>();
//            File[] files = new File(path_to_cur_version.toString()).listFiles();
////If this pathname does not denote a directory, then listFiles() returns null.
//
//            for (File file : files) {
//                if (file.isFile()) {
//                    results.add(file.getName());
//                }
//            }
////            exitHandler.exit(0, "File already added. File: " + results);
            if (Files.exists(path_to_file_in_cur_version)) {
                exitHandler.exit(0, "File already added. File: " + args[1]);
                return;
            }
            Path path_to_new_version = Path.of(main_folder, "v"+String.valueOf(cur_version + 1));
            Files.createDirectory(path_to_new_version);
            //copy_dir(path_to_cur_version.toString(), path_to_new_version.toString());
            copyAllFilesWithoutOne(path_to_cur_version.toString()+"/", path_to_new_version.toString()+"/", info);
            Files.copy(Paths.get(args[1]), Paths.get(path_to_new_version.toString(), args[1]), REPLACE_EXISTING);
            String message = (args.length == 2) ? "File added successfully. File: " + args[1] : args[3];
            new_info_file(path_to_new_version, message);
            update_version(cur_version + 1);
            exitHandler.exit(0, "File added successfully. File: " + args[1]);
        } catch (IOException e) {
            system_problem();
        }
    }
    void copyAllFilesWithoutOne(String source, String target, String excludedFile) throws IOException {

        File sourceDir = new File(source.substring(0, source.length() - 1));
        File[] files = sourceDir.listFiles();

        if (files != null) {

            for (File file : files) {
                if (!file.getName().equals(excludedFile)) {
                    Files.copy(file.toPath(), Path.of(target+"/" + file.getName()), REPLACE_EXISTING);
                }
            }

        }
    }
    void detach(String... args) {
        if (args.length < 2) {
            exitHandler.exit(30, "Please specify file to detach.");
            return;
        }
        try{
           int last_version_int=Integer.parseInt(Files.readString(Path.of(last_version)));
           Path path_to_last_version=Path.of(main_folder, "v"+String.valueOf(last_version_int));
           Path path_file_in_last_version=Path.of(path_to_last_version.toString(), args[1]);
           if(!Files.exists(path_file_in_last_version)){
               exitHandler.exit(0, "File is not added to gvt. File: " + args[1]);
               return;
           }
           Path path_to_new_version=Path.of(main_folder, "v"+String.valueOf(last_version_int+1)+"/");
           Files.createDirectory(path_to_new_version);
           copyAllFilesWithoutOne(path_file_in_last_version.toString(), path_to_new_version.toString(), args[1]);
           //Files.delete(Path.of(path_to_new_version.toString(), args[1]));
           String message = (args.length == 2)?  "File detached successfully. File: "+ args[1] : args[3];
           new_info_file(path_to_new_version, message);
           update_version(last_version_int+1);
           exitHandler.exit(0,"File detached successfully. File: " + args[1]);
        }catch (Exception e){
            exitHandler.exit(31, "File cannot be detached, see ERR for details. File: "+ args[1]);
        }
    }
    void checkout(String... args) {
        try {
            if(args.length<2){
                exitHandler.exit(60,"Invalid version number: ");
                return;
            }
            int last_vers = Integer.parseInt(Files.readString(Path.of(last_version)).trim());
            int number=Integer.parseInt(args[1]);
            if(number>last_vers ||number<0){
                exitHandler.exit(60,"Invalid version number: " + args[1]);
                return;
            }
            Path source = Path.of(main_folder.toString(),  "v"+args[1]);
            copyAllFilesWithoutOne(source.toString()+"/", ".", info);
            exitHandler.exit(0, "Checkout successful for version: " + args[1]);
        } catch (IOException e) {
            system_problem();
        }
    }
    void commit(String... args) {
        //exitHandler.exit(50, "Please specify file to commit."+args.length);
        if (args.length <= 1) {
            exitHandler.exit(50, "Please specify file to commit.");
            return;
        } else if (!Files.exists(Path.of(args[1]))) {
            exitHandler.exit(51, "File not found. File: " + args[1]);
            return;
        }
        try {
            String last_vers = Files.readString(Path.of(last_version)).trim();
            Path path_to_last_version=Path.of(main_folder.toString(),"v"+last_vers);
            int last_vers_int=Integer.parseInt(last_vers);
            if (!Files.exists(Path.of(path_to_last_version.toString(), args[1]))) {
                exitHandler.exit(0, "File is not added to gvt. File: " + args[1]);
                return;
            }
            Path path_to_new_version = Path.of(main_folder.toString(), "v"+String.valueOf(last_vers_int+1));
            Files.createDirectory(path_to_new_version);
            copyAllFilesWithoutOne(path_to_last_version.toString(), path_to_new_version.toString(), args[1]);
            Files.copy(Paths.get(args[1]), Paths.get(path_to_new_version.toString()+"/"+args[1]), REPLACE_EXISTING);
            String message = (args.length == 2)?  "File committed successfully. File: "+ args[1] : args[3];
            new_info_file(path_to_new_version, message);
            //exitHandler.exit(1000,"kkkkk "+ args[1]);
            update_version(last_vers_int+1);
            exitHandler.exit(0,"File committed successfully. File: "+ args[1]);
        } catch (IOException e) {
            exitHandler.exit(52, "File cannot be committed, see ERR for details. File: " + args[1]);
        }
    }
    void history(String... args){
        int first_version=0;
        try{
            String last_vers_str=Files.readString(Path.of(last_version));
            int last_vers=Integer.parseInt(last_vers_str);
            if(args.length>=2){
                int number=Integer.parseInt(args[2]);
                if(number>0 && number<last_vers){
                    first_version=last_vers-number+1;
                }
            }
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=last_vers; i>= first_version; i--){
                stringBuilder.append(i).append(": ");
                Path version_info=Path.of(main_folder.toString(),"v"+String.valueOf(i), info);
                String information=Files.readString(version_info).split("\n")[0];
                stringBuilder.append(information).append("\n");
            }
            exitHandler.exit(0, stringBuilder.toString());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    void version(String... args){
        if (args.length < 2) {
            Path current_file = Path.of(current_version.toString());
            if (!Files.exists(current_file)) {
                //System.out.println(1);
                exitHandler.exit(60, "Invalid version number: " + null);
                return;
            }
            try {
                String current_ver = Files.readString(current_file);
                Path version = Path.of(main_folder.toString(), "v"+current_ver);
                //exitHandler.exit(60, "Invalid version number: " + Path.of(version.toString(), info));
                String information = Files.readString(Path.of(version.toString(), info));
                exitHandler.exit(0, "Version: " + current_ver + "\n" + information);
                return;
            } catch (IOException e) {
            }
        }
        if (args.length >= 2) {
            Path version = Path.of(args[1]);
            if (!Files.exists(version)) {
                //System.out.println(3);
                exitHandler.exit(60, "Invalid version number: " + version);
                return;
            }
            try {
                Path info_file = Path.of(main_folder.toString(),"v"+ version.toString(), info);
                String information = Files.readString(info_file);
                exitHandler.exit(0, "Version: " + version + "\n" + information);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

