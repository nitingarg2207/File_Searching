import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Stream;

public class HandlingUsingThreads {
    private static int lineNumbers = 1;

    public void FindLogs(String filePath, String searchPhrase){
        int numThreads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter("./output.log"))) {
            String line;
            int lineNumber = 0;

            Pattern messagePattern = Pattern.compile("\"message\":\".*?"+searchPhrase+".*?");

            List<List<String>> threadData = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                for (int i=0;i<numThreads;i++) {
                    threadData.add(new ArrayList<>());
                }
                threadData.get(lineNumber % numThreads).add(line);
                lineNumber++;
            }

            Thread[] threads = new Thread[numThreads];

            for (int i=0;i<numThreads;i++) {
                List<String> smallData = threadData.get(i);
                threads[i] = new Thread(() ->  processData(smallData, messagePattern, searchPhrase, writer));
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            executorService.shutdown();
        }
    }
    private static void processData(List<String> data, Pattern messagePattern, String searchPhrase, BufferedWriter writer) {
        for (String line : data) {
            Matcher matcher = messagePattern.matcher(line);
            if (matcher.find()) {
                try {
                    writer.write(("Line Number: " + lineNumbers+"\n"+"Details: "+line+"\n"));
                    lineNumbers++;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void findClasses(String filePath, HashMap<String,Integer> m){
        long noOfLines;
        int threadCount  = 4;

        try (Stream<String> fileStream = Files.lines(Paths.get(filePath));
        BufferedWriter writer = new BufferedWriter(new FileWriter("./output.log"))) {
            noOfLines = (int) fileStream.count();
            long noOfLinesPerThread = noOfLines/threadCount;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
                ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
                for (int i=0;i<threadCount-1;i++) {
                    processData(filePath,noOfLinesPerThread*i+1,noOfLinesPerThread*(i+1),writer);
                }
                processData(filePath,noOfLinesPerThread*(threadCount-1),noOfLines,writer);
                executorService.shutdown();
            } catch (IOException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void processData(String filePath, long start, long end, BufferedWriter writer){

        try(BufferedWriter out = new BufferedWriter(new FileWriter("./output.log"))) {
            try {
                Files.lines(Paths.get(filePath))
                        .skip(start - 1)
                        .limit(end - start + 1)
                        .forEach(line -> {
                            try {
//                                out.write(line);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        });
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
