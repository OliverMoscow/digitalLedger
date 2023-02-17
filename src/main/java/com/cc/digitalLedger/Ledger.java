package com.cc.digitalLedger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

//Not related to any api/db functionality
public class Ledger {
    public static List<Transaction> load() {
        File backup = findBackup();
        return readBackup(backup);
    }

    public static void backup(List<Transaction> transactions) {
        File backup = findBackup();
        updateBackup(backup, transactions);
    }

    private static File findBackup() {
        try {
            File f = new File("backup.txt");
            f.createNewFile();
            return f;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }
    private static List<Transaction> readBackup(File backup) {
        return  null;
    }

    private static void updateBackup(File backup, List<Transaction> transactions) {
        String serialized = serialize(transactions);
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static String serialize(List<Transaction> transactions) {
        return "";
    }

}
