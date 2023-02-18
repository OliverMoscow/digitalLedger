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
        this.filename = name.name() + ".txt";
    }

    public List<T> load() {
        File backup = findBackup();
        try {
            return readBackup(backup);
        } catch (FileNotFoundException e) {
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

    private File findBackup() {
        try {
            File f = new File(filename);
            f.createNewFile();
            return f;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }
    private List<T> readBackup(File backup) throws FileNotFoundException {
        String content = readFileContent(backup);
        if(content != null && content != "") {
            return deserialize(content);
        } else {
            return null;
        }
    }

    private String readFileContent(File f) throws FileNotFoundException {
        Scanner myReader = new Scanner(f);
        String s = "";
        while (myReader.hasNextLine()) {
            s += myReader.nextLine();
        }
        myReader.close();
        return s;
    }

    private void updateBackup(List<T> data) throws IOException {
        String serialized = serialize(data);
        FileWriter myWriter = new FileWriter(filename,false);
        myWriter.write(serialized);
        myWriter.close();
        System.out.println("Backup Successful!");
    }

    private String serialize(List<T> transactions) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = new ObjectOutputStream(bo);
        so.writeObject(transactions);
        so.flush();
        return bo.toString();
    }

    private List<T> deserialize(String s) {
        byte b[] = s.getBytes();
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        ObjectInputStream si = null;
        try {
            si = new ObjectInputStream(bi);
            return (List<T>) si.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
