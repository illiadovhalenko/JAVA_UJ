package uj.wmii.pwj.w7.insurance;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

public class FloridaInsurance {
    static List<InsuranceEntry> list;
    static void countOfCountry() {
        try {
            Files.writeString(Paths.get("count.txt"), String.valueOf(list.stream().map(insuranceEntry -> insuranceEntry.county).distinct().count()));
        }catch (IOException e){
            System.out.println("Problem with count");
        }
    }
    static void sumOfInsurance(){
        try{
            Files.writeString(Paths.get("tiv2012.txt"), String.format( "%.2f", list.stream().map(insuranceEntry -> insuranceEntry.tiv_2012).reduce(0.0, Double::sum)).replace(",", "."));
        }catch (IOException e){
            System.out.println("Problem with sum");
        }
    }
    static void mostValuable(){
        try{
            String top10 = ("country,value\n")+list.stream().
                    collect(Collectors.groupingBy(InsuranceEntry::getCounty, Collectors.summingDouble(entry -> entry.getTiv_2012() - entry.getTiv_2011()))).
                    entrySet().stream().
                    sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                    limit(10).
                    map(entry -> entry.getKey() + "," +String.format("%.2f", entry.getValue()).replace(",", "."))
                    .collect(Collectors.joining("\n"));
            Files.writeString(Paths.get("most_valuable.txt"), top10);
        }catch(IOException e){
            System.out.println("Problem with most valuable");
        }
    }
    public static void main(String[] args)  {
        try {
            ZipFile zip = new ZipFile("FL_insurance.csv.zip");
            ZipEntry entry = zip.getEntry("FL_insurance.csv");
            InputStream inputStream = zip.getInputStream(entry);
            BufferedReader info = new BufferedReader(new InputStreamReader(inputStream));
            list = info.lines().skip(1).map(InsuranceEntry::new).toList();
            System.out.println(list.size());
            countOfCountry();
            sumOfInsurance();
            mostValuable();
            info.close();
            zip.close();
        }catch (Exception e){
            System.out.println("Problem with file");
        }
    }

}
