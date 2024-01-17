import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){
        String fileName = "input.log";
        String filePath = System.getProperty("user.dir") + "/" + fileName;
        Scanner sc = new Scanner(System.in);
        String searchPhrase=sc.next();
        long startTime = System.currentTimeMillis();


//        Task 1: find all occurrences in message
        try {
            searchLogFile(filePath, searchPhrase);
//            HandlingUsingThreads handle = new HandlingUsingThreads();
//            handle.FindLogs(filePath, searchPhrase);
        } catch (Exception e) {
            System.out.println(e);
        }

        //        Task 2: find all caller_class_name and number of logs per class name
//        try {
//            HandlingUsingThreads handle = new HandlingUsingThreads();
//            HashMap<String,Integer> m = new HashMap<>();
//            handle.findClasses(filePath,m);
//        } catch (Exception e) {
//            System.out.println(e);
//        }


        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time Take: "+(int)(totalTime));
    }

    private static void searchLogFile(String filePath, String searchPhrase) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            FileWriter output_file = new FileWriter("./output.log");
            BufferedWriter out = new BufferedWriter(output_file);
            try{
                Pattern messagePattern = Pattern.compile("\"message\":.*?"+searchPhrase+".*?\"");
                int count=0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    Matcher matcher = messagePattern.matcher(line);
                    if (matcher.find()) {
                            count++;
                            out.write(("Line Number: " + lineNumber+"\n"+"Details: "+line+"\n"));
                    }
                }
                System.out.println("Count of Matches = "+count);
            } catch (Exception e){
                System.out.println(e);
            }
            finally {
                out.close();
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }
}