package org.example;




import java.io.*;
import java.nio.file.Files;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Hello world!
 *
 */
public class App {
    public static ArrayList<String> StartList = new ArrayList<String>();
    public static ArrayList<String> EndList = new ArrayList<String>();
    public static ArrayList<String> TimeList = new ArrayList<String>();
    public static ArrayList<String> AbbList = new ArrayList<String>();
    public static ArrayList<String> Final = new ArrayList<String>();
    public static void main(String[] args) {
        System.out.println("Hello World!");
        ReadFile("start.log");
        ReadFile("end.log");
        Calculate();
        Wrap("abbreviations.txt");
        Output();
    }

    public static void ReadFile(String filename) {
        try {
            File myObj = new File (filename);
            Scanner myReader = new Scanner(myObj);
            if(filename=="start.log")
            {
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    StartList.add(data);
                }
            } else {
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    EndList.add(data);
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        Collections.sort(StartList);
        Collections.sort(EndList);
//      for (String str : StartList) {
//            //System.out.println(str);
//            String id = str.substring(0,3);
//            String date = str.substring(3,13);
//            String time = str.substring(14);
//            System.out.println(id + " " + date + " " + time);
//            //StartMap.put(id);
//        }
        //System.out.println(StartMap.size());
    }
    public static void Calculate () {
        long[] Days = new long[StartList.size()];
        System.out.println(StartList);
        System.out.println(EndList);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.sss");
        for (int i = 0; i<StartList.size();i++) {
            LocalDate date_start = LocalDate.parse(StartList.get(i).substring(3,13));
            LocalDate date_end = LocalDate.parse(EndList.get(i).substring(3,13));
            Days[i] = ChronoUnit.DAYS.between(date_end,date_start);
            LocalTime lt_start = LocalTime.parse(StartList.get(i).substring(14));
            LocalTime lt_end = LocalTime.parse(EndList.get(i).substring(14));
            System.out.println(lt_start+" | "+lt_end);
            long diff_mill=0;
            if(Days[i]==1) {
                diff_mill = Duration.between(lt_start,lt_end).toMillis()+TimeUnit.DAYS.toMillis(1);
            } else {
                diff_mill = Duration.between(lt_start,lt_end).toMillis();
            }
            long minute = TimeUnit.MILLISECONDS.toMinutes(diff_mill);
            long second = TimeUnit.MILLISECONDS.toSeconds(diff_mill)-TimeUnit.MINUTES.toSeconds(minute);
            long milli = diff_mill - TimeUnit.SECONDS.toMillis(second) - TimeUnit.MINUTES.toMillis(minute);
            String out = String.valueOf(minute + ":"+"00".substring(String.valueOf(second).length())+ second
                    + "."+"000".substring(String.valueOf(milli).length()) + milli);
            System.out.println(out);
            TimeList.add(out);
        }
        System.out.println(Arrays.toString(Days));
        System.out.println(TimeList);
        //System.out.println(t[0].);

    }

    public static void Wrap(String filename) {
        try {
            File myObj = new File (filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                AbbList.add(data);
            }
            myReader.close();
            AbbList.sort(null);
            System.out.println(AbbList);
            System.out.println(TimeList);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        for (int i=0;i< AbbList.size();i++) {
            Final.add(TimeList.get(i)+" "+AbbList.get(i));
            System.out.println(Final.get(i));
        }
        Final.sort(null);
        System.out.println(Final);

    }
    public static void Output () {
        String toFile = "";
        for (String str : Final) {
            System.out.println(str);
            int tempInd = -1;
            int finInd = 0;
            //System.out.println(str.indexOf("_"));
            //System.out.println(str.lastIndexOf("_"));
            //System.out.println(str.substring(str.indexOf("_")+1,str.lastIndexOf("_")));
            toFile += Final.indexOf(str)+1 + ". ";
            toFile += str.substring(str.indexOf("_")+1,str.lastIndexOf("_"));
            toFile += "   |   ";
            toFile += str.substring(str.lastIndexOf("_")+1);
            toFile += "   |   ";
            toFile += str.substring(0,8);
            System.out.println();
            //System.out.println(str.substring(str.lastIndexOf("_")+1));
            if(Final.indexOf(str)==14) {

                toFile += "\n----------------------------------------------------------------";
            }
            toFile += "\n";

        }
        try (PrintStream out = new PrintStream(new FileOutputStream("output.txt"))) {
            out.print(toFile);
        } catch (FileNotFoundException e) {
            System.out.println("Failed to write");
        }

    }


}