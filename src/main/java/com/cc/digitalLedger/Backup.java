package com.cc.digitalLedger;

import java.io.*;
import java.util.List;
import java.util.Scanner;

//Not related to any api/db functionality
public class Backup<T> {
    String filename;

    public enum FileName {
        ledger, users
    }

    public Backup(FileName name) {
        this.filename = name.name() + ".ser";
    }

    public List<T> load() {
        try {
            return readBackup();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void backup(List<T> data) {
        try {
            updateBackup(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<T> readBackup() throws IOException, ClassNotFoundException {
        List<T> restoredData = null;
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            restoredData = (List<T>) ois.readObject();
            ois.close();
            fis.close();
        } catch (EOFException e) {
            e.printStackTrace();
            return null;
        }
        return restoredData;
    }

    private void updateBackup(List<T> data) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(data);
        oos.close();
        fos.close();
        System.out.println("Backup Successful!");
    }

}
