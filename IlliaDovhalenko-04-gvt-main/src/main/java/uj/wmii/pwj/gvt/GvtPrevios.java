//package uj.wmii.pwj.gvt;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//public class GvtPrevios {
////    private final ExitHandler exitHandler;
////    private final Path main_folder = Paths.get(".gvt");
////    private final Path current_version = Path.of(".gvt/.current_version");
////    private final Path last_version = Path.of(".gvt/.last_version");
////    public final String info = ".info";
////
////    public GvtPrevios(ExitHandler exitHandler) {
////        this.exitHandler = exitHandler;
////    }
////
////    public static void main(String... args) {
////        Gvt gvt = new Gvt(new ExitHandler());
////        gvt.mainInternal(args);
////    }
////
////    void mainInternal(String... args) {
////        if (args.length == 0) {
////            exitHandler.exit(1, "Please specify command.");
////        } else if (args[0].equals("init")) {
////            init();
////        } else if (!Files.exists(main_folder)) {
////            exitHandler.exit(-2, "Current directory is not initialized. Please use init command to initialize.");
////        } else {
////            switch (args[0]) {
////                case "add" -> add(args);
////                case "detach" -> detach(args);
////                case "checkout" -> checkout(args);
////                case "commit" -> commit(args);
////                case "history" -> history(args);
////                case "version" -> version(args);
////                default -> exitHandler.exit(1, "Unknown command " + args[0] + ".");
////            }
////        }
////    }
////
////    void system_problem() {
////        exitHandler.exit(-3, "Underlying system problem. See ERR for details.");
////    }
////
////    String is_a_message(String... args) {
////        for (int i = 1; i < args.length - 1; i++) {
////            if (args[i].equals("-m"))
////                return args[i + 1];
////        }
////        return null;
////    }
////
////    int is_a_number(String... args) {
////        for (int i = 1; i < args.length - 1; i++) {
////            if (args[i].equals("-last"))
////                return Integer.parseInt(args[i + 1]);
//        }
//        return -1;
//    }
//
//    void init() {
//        if (Files.exists(main_folder)) {
//            exitHandler.exit(10, "Current directory is already initialized.");
//            return;
//        }
//        try {
//            Files.createDirectory(main_folder);
//            Path version = Files.createDirectory(Path.of(main_folder.toString(), ".0"));
//            Path info_of_version = Path.of(version.toString(), info);
//            Files.createFile(info_of_version);
//            Files.writeString(info_of_version, "GVT initialized.");
//            Files.createFile(current_version);
//            Files.writeString(current_version, "0");
//            exitHandler.exit(0, "Current directory initialized successfully.");
//        } catch (IOException e) {
//            system_problem();
//        }
//    }
//
//    void add(String... args) {
//        if (args.length < 2) {
//            exitHandler.exit(20, "Please specify file to add.");
//        }
//        String name_of_file = args[1];
//        if (!Files.exists(Path.of(name_of_file))) {
//            exitHandler.exit(21, "File not found. File: " + name_of_file);
//            return;
//        }
//        try {
//            int version = Integer.parseInt(Files.readString(current_version).trim());
//            Path fileVersion = Path.of(main_folder.toString(), "."+ String.valueOf(version),name_of_file);
//            if (Files.exists(fileVersion)) {
//                exitHandler.exit(0, "File already added. File: " + name_of_file);
//                return;
//            }
//
//            Path new_version = Path.of(main_folder.toString(), "."+ String.valueOf(version + 1));
//            Files.createDirectory(new_version);
//            copy_dir(main_folder.toString()+"/."+ String.valueOf(version), new_version.toString());
//            // exitHandler.exit(21, "File not found. File: " + Path.of(new_version.toString(),"."+args[1]));
//            Files.copy(fileVersion, Path.of(new_version.toString(),args[1]));
//            String message = (args.length == 2) ? "File added successfully. File: " + name_of_file : args[2];
//            new_info_file(new_version, message);
//            update_version(version + 1);
//        } catch (IOException e) {
//            system_problem();
//        }
//    }
//
//    void update_version(int new_version) throws IOException {
//        Files.writeString(current_version, String.valueOf(new_version));
//        Files.writeString(last_version, String.valueOf(new_version));
//    }
//
//    void new_info_file(Path version, String message) throws IOException {
//        Path file = Path.of(version.toString(), info);
//        if (!Files.exists(file)) {
//            Files.createFile(file);
//        }
//        Files.writeString(file, message);
//    }
//
//    //    void copy_dir(Path source, Path destination) throws IOException {
////        exitHandler.exit(21, "File not found. File: ");
////        Files.walk(source).forEach(file->{
////            try {
////                if(!source.equals(file)){
////                    Files.copy(file, destination, StandardCopyOption.REPLACE_EXISTING);
////                }
////            }catch (IOException e){
////                e.printStackTrace();
////            }
////        });
////    }
//    public void copy_dir(String sourceDirectoryLocation, String destinationDirectoryLocation)
//            throws IOException {
//        //exitHandler.exit(21, "File not found. File: " +destinationDirectoryLocation);
//        Files.walk(Paths.get(sourceDirectoryLocation))
//                .forEach(source -> {
//                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
//                            .substring(sourceDirectoryLocation.length()));
//                    //exitHandler.exit(21, "File not found. File: " +destination);
//                    try {
//                        if(!sourceDirectoryLocation.equals(source.toString())) {
//                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
//
//    void detach(String... args) {
//        if (args.length < 2) {
//            exitHandler.exit(30, "Please specify file to detach.");
//            return;
//        }
//        try{
//            int last_vers= Integer.parseInt(Files.readString(last_version).trim());
//            Path version = Path.of(main_folder.toString(),".", String.valueOf(last_vers));
//            Path path_to_file=Paths.get(version.toString(), args[1]);
//            if(!Files.exists(path_to_file))
//                exitHandler.exit(0, "File is not added to gvt. File: "+args[1]);
//            Path new_version=Path.of(main_folder.toString(),".",String.valueOf(last_vers+1));
//            Files.createDirectory(new_version);
//            copy_dir(version.toString(), new_version.toString());
//            Files.delete(Paths.get(new_version.toString(), args[1]));
//            String information=(args.length==2)?"File detached successfully. File: "+ args[1] : args[2];
//            new_info_file(new_version, args[2]);
//            update_version(last_vers+1);
//            exitHandler.exit(0, "File detached successfully. File: "+args[1]);
//        }catch (Exception e){
//            exitHandler.exit(31, "File cannot be detached, see ERR for details. File: "+ args[1]);
//        }
//    }
//
//    void checkout(String... args) {
//        try {
//            int last_vers = Integer.parseInt(Files.readString(last_version).trim());
//            if (!Files.exists(Path.of(main_folder.toString(), "." , args[1]))) {
//                exitHandler.exit(60, "Invalid version number: " + args[1]);
//                return;
//            }
//            Path source = Path.of(main_folder.toString(), ".", args[1]);
//            Path target = Path.of(main_folder.toString(), String.valueOf(last_vers));
//            copy_dir(source.toString(), new File(".").toPath().toString());
//            exitHandler.exit(0, "Checkout successful for version: " + args[1]);
//        } catch (IOException e) {
//            system_problem();
//        }
//    }
//
//    void commit(String... args) {
//        if (args.length <= 1) {
//            exitHandler.exit(50, "Please specify file to commit.");
//        } else if (args[1]==null || !Files.exists(Path.of(args[1]))) {
//            exitHandler.exit(51, "File not found. File: " + args[1]);
//        }
//        try {
//            int last_vers = Integer.parseInt(Files.readString(last_version).trim());
//            if (!Files.exists(Path.of(main_folder.toString() , "." , String.valueOf(last_vers) , "." ,args[1]))) {
//                exitHandler.exit(0, "File is not added to gvt. File: " + args[1]);
//                return;
//            }
//        } catch (IOException e) {
//            exitHandler.exit(52, "File cannot be committed, see ERR for details. File: " + args[1]);
//        }
//    }
//
//    void history(String... args) {
//        int first_version=0;
//        try{
//            String last_vers_str=Files.readString(last_version);
//            int last_vers=Integer.parseInt(last_vers_str);
//            if(args.length!=1){
//                int number=Integer.parseInt(args[1]);
//                if(number>0 && number<last_vers){
//                    first_version=last_vers-number+1;
//                }
//            }
//            StringBuilder stringBuilder=new StringBuilder();
//            for(int i=last_vers; i>= first_version; i--){
//                stringBuilder.append(i).append(": ");
//                Path version_info=Path.of(main_folder.toString(),".",String.valueOf(i),".",info);
//                String information=Files.readString(version_info).split("\n")[0];
//                stringBuilder.append(information).append("\n");
//            }
//            exitHandler.exit(0, stringBuilder.toString());
//        }catch (IOException e){}
//    }
//
//    void version(String... args) {
////        int ver = 0;
////        try {
////            if (args.length < 2)
////                ver = Integer.parseInt(Files.readString(current_version));
////            else {
////                ver = Integer.parseInt(args[1]);
////            }
////        } catch (IOException e) {}
////        try {
////            if (!Files.exists(Path.of(main_folder.toString() + "/." + ver)))
////                exitHandler.exit(60, "Invalid version number: " + ver);
////            else {
////                String information = Files.readString(Path.of(main_folder.toString() + "/." + ver + "/." + info));
////                exitHandler.exit(0, "Version: " + ver + "\n" + information);
////            }
////        } catch (IOException e) {
////            exitHandler.exit(60, "Invalid version number: " + ver);
////        }
//        // exitHandler.exit(60, "Invalid version number: "+ Arrays.toString(args));
//        if (args.length < 2) {
//            Path current_file = Path.of(current_version.toString());
//            if (!Files.exists(current_file)) {
//                System.out.println(1);
//                exitHandler.exit(60, "Invalid version number: " + null);
//                return;
//            }
//            try {
//                String current_ver = Files.readString(current_file);
//                Path version = Path.of(main_folder.toString(), "."+current_ver);
//                //exitHandler.exit(60, "Invalid version number: " + Path.of(version.toString(), info));
//                String information = Files.readString(Path.of(version.toString(), info));
//                exitHandler.exit(0, "Version: " + current_ver + "\n" + information);
//                return;
//            } catch (IOException e) {
//            }
//        }
//        if (args.length == 2) {
//            Path version = Path.of(args[1]);
//            if (!Files.exists(version)) {
//                System.out.println(3);
//                exitHandler.exit(60, "Invalid version number: " + version);
//                return;
//            }
//            try {
//                Path info_file = Path.of(main_folder.toString(), version.toString(), info);
//                String information = Files.readString(info_file);
//                exitHandler.exit(0, "Version: " + version + "\n" + information);
//            } catch (IOException e) {
//            }
//        }
//    }
//}
