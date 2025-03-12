package com.mycompany.multithreadingdemo;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class WordCountComparison {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Hardcoded text to avoid file-related issues
        String fileContent = "This is a sample text. It contains multiple lines.\n"
                           + "Multithreading is fun! Let's count words efficiently."
                            + "This is a sample text. It contains multiple lines.\n"
                           + "Multithreading is fun! Let's count words efficiently.";
        
        // Single-threaded execution
        long startTime = System.currentTimeMillis();
        Map<String, Integer> singleThreadResult = countWordsSingleThread(fileContent);
        long singleThreadTime = System.currentTimeMillis() - startTime;
        int totalWordsSingle = computeTotalWords(singleThreadResult);

        System.out.println("Single-threaded execution time: " + singleThreadTime + " ms");
        System.out.println("Single-threaded word count: " + singleThreadResult);
        System.out.println("Total words (single-threaded): " + totalWordsSingle);
        
        // Multi-threaded execution
        startTime = System.currentTimeMillis();
        Map<String, Integer> multiThreadResult = countWordsMultiThread(fileContent, 4);
        long multiThreadTime = System.currentTimeMillis() - startTime;
        int totalWordsMulti = computeTotalWords(multiThreadResult);

        System.out.println("\nMulti-threaded execution time: " + multiThreadTime + " ms");
        System.out.println("Multi-threaded word count: " + multiThreadResult);
        System.out.println("Total words (multi-threaded): " + totalWordsMulti);
    }
    
    // Single-threaded word count
    private static Map<String, Integer> countWordsSingleThread(String text) {
        Map<String, Integer> wordCount = new HashMap<>();
        String[] lines = text.split("\n");  // Simulating lines from a file
        for (String line : lines) {
            processLine(line, wordCount);
        }
        return wordCount;
    }
    
    // Multi-threaded word count
    private static Map<String, Integer> countWordsMultiThread(String text, int threadCount) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();

        String[] lines = text.split("\n");
        for (String line : lines) {
            String finalLine = line;
            futures.add(executor.submit(() -> processLineThreaded(finalLine)));
        }
        
        Map<String, Integer> finalWordCount = new HashMap<>();
        for (Future<Map<String, Integer>> future : futures) {
            try {
                mergeWordCounts(finalWordCount, future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();  // Print error details
            }
        }
        
        executor.shutdown();
        return finalWordCount;
    }
    
    private static void processLine(String line, Map<String, Integer> wordCount) {
        String[] words = line.split("\\s+");
        for (String word : words) {
            word = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
            if (!word.isEmpty()) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
    }
    
    private static Map<String, Integer> processLineThreaded(String line) {
        Map<String, Integer> wordCount = new HashMap<>();
        processLine(line, wordCount);
        return wordCount;
    }
    
    private static void mergeWordCounts(Map<String, Integer> finalWordCount, Map<String, Integer> partialCount) {
        for (Map.Entry<String, Integer> entry : partialCount.entrySet()) {
            finalWordCount.put(entry.getKey(), finalWordCount.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
    }

    // Computes the total number of words (sum of all word occurrences)
    private static int computeTotalWords(Map<String, Integer> wordCount) {
        int total = 0;
        for (int count : wordCount.values()) {
            total += count;
        }
        return total;
    }
}
