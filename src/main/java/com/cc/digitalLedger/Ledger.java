package com.cc.digitalLedger;

import java.io.*;
import java.util.List;
import java.util.Scanner;

//Not related to any api/db functionality
public class Ledger {
    public static List<Transaction> load() {
        File backup = findBackup();
        try {
            return readBackup(backup);
        } catch (IOException e) {
           e.printStackTrace();
           return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void backup(List<Transaction> transactions) {
        File backup = findBackup();
        try {
            updateBackup(backup, transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private static List<Transaction> readBackup(File backup) throws IOException, ClassNotFoundException {
        String content = readFileContent(backup);
        return deserialize(content);
    }

    private static String readFileContent(File backup) throws FileNotFoundException {
        File myObj = new File("filename.txt");
        Scanner myReader = new Scanner(myObj);
        String s = "";
        while (myReader.hasNextLine()) {
            s += myReader.nextLine();
        }
        myReader.close();
        return s;
    }

    private static void updateBackup(File backup, List<Transaction> transactions) throws IOException {
        String serialized = serialize(transactions);
        FileWriter myWriter = new FileWriter("backup.txt");
        myWriter.write(serialized);
        myWriter.close();
        System.out.println("Backup Successful!");
    }

    private static String serialize(List<Transaction> transactions) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(transactions);
        so.flush();
        return bo.toString();
    }

    private static List<Transaction> deserialize(String s) throws IOException, ClassNotFoundException {
        byte b[] = s.getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = new ObjectInputStream(bi);
        return (List<Transaction>) si.readObject();
    }

}
