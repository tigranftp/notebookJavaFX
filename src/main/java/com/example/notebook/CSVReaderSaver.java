package com.example.notebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVReaderSaver {
    public static ArrayList<LinkClass> readCSV() {
        ArrayList<LinkClass> res = new ArrayList<>();
        Scanner sc;
        try {
            sc = new Scanner(new File("links.csv"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        sc.useDelimiter("\n");
        System.out.print("Trying to read!\n");
        while (sc.hasNext())  //returns a boolean value
        {
            String[] curArr = sc.next().split(",");
            if (curArr.length != 3) {
                continue;
            }
            res.add(new LinkClass(curArr[0].strip(), curArr[1].strip(), curArr[2].strip()));
        }
        System.out.print("CSV reading done!\n");
        sc.close();
        return res;
    }
    public static String convertToCSV(LinkClass lc) {
        return lc.getLink() + ", " + lc.getDescription() + ", " + lc.getCategory();
    }
    public static void saveCSV(ArrayList<LinkClass> saveListOfLinks) {
        File csvOutputFile = new File("links.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            saveListOfLinks.stream()
                    .map(CSVReaderSaver::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
