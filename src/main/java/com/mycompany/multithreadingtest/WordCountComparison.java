
package com.mycompany.multithreadingtest;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;


public class WordCountComparison {
    public static void main(String[] args) throws IOException, InterruptedException {
        String filePath = "./src/sample.txt";

        // Single-threaded execution
        long startTime = System.currentTimeMillis();
        WordCountResult singleThreadResult = countWordsSingleThread(filePath);
        long singleThreadTime = System.currentTimeMillis() - startTime;
        System.out.println("Single-threaded execution time: " + singleThreadTime + " ms");
        System.out.println("Single-threaded total words: " + singleThreadResult.totalWords);
        System.out.println("Single-threaded word occurrences: " + singleThreadResult.wordCount);

        // Multi-threaded execution
        startTime = System.currentTimeMillis();
        WordCountResult multiThreadResult = countWordsMultiThread(filePath, 4);
        long multiThreadTime = System.currentTimeMillis() - startTime;
        System.out.println("Multi-threaded execution time: " + multiThreadTime + " ms");
        System.out.println("Multi-threaded total words: " + multiThreadResult.totalWords);
        System.out.println("Multi-threaded word occurrences: " + multiThreadResult.wordCount);
    }

    // Single-threaded word count
    private static WordCountResult countWordsSingleThread(String filePath) throws IOException {
        Map<String, LongAdder> wordCount = new ConcurrentHashMap<>();
        int totalWords = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalWords += processLine(line, wordCount);
            }
        }
        return new WordCountResult(wordCount, totalWords);
    }

    // Multi-threaded word count
    private static WordCountResult countWordsMultiThread(String filePath, int threadCount) throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        ConcurrentHashMap<String, LongAdder> wordCount = new ConcurrentHashMap<>();
        int totalWords = 0;

        List<Future<Integer>> futures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String finalLine = line;
                futures.add(executor.submit(() -> processLine(finalLine, wordCount)));
            }
        }

        for (Future<Integer> future : futures) {
            try {
                totalWords += future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return new WordCountResult(wordCount, totalWords);
    }

    private static int processLine(String line, Map<String, LongAdder> wordCount) {
        String[] words = line.split("\\s+");
        int count = 0;

        for (String word : words) {
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                wordCount.computeIfAbsent(word, k -> new LongAdder()).increment();
                count++;
            }
        }
        return count;
    }
}

class WordCountResult {
    Map<String, LongAdder> wordCount;
    int totalWords;

    public WordCountResult(Map<String, LongAdder> wordCount, int totalWords) {
        this.wordCount = wordCount;
        this.totalWords = totalWords;
    }
}
