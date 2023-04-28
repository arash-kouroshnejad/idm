package ir.sharif.math.ap2023.hw5.Persistence;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHandler {
    RandomAccessFile file;
    private long offset;

    public static void generateEmptyFile(String PathName, long size) {
        try {
            RandomAccessFile file = new RandomAccessFile(PathName, "rw");
            file.setLength(size);
            file.close();
        } catch (Exception e) {
            System.out.println("Error creating or formatting " + PathName);
        }

    }

    public void init(String path, long offset) {
        try {
            file = new RandomAccessFile(path, "rws");
        } catch (FileNotFoundException e) {
            System.out.println("Could not read " + path);
        }
        setOffset(offset);
        this.offset = offset;
    }

    public void setOffset(long offset) { // sets the file pointer
        try {
            file.seek(offset);
            this.offset = offset;
        } catch (IOException e) {
            System.out.println("EOF occurred for offset : " + offset);
        }
    }

    public long getOffset() {
        return offset;
    }

    public void write(byte b) { // writer a single byte
        try {
            file.writeByte(b);
        } catch (Exception e) {
            try {
                if (file == null) {
                    System.out.println("file is not set!");
                }
                else {
                    System.out.println("Error writing byte! at : " + file.getFilePointer());
                }
            } catch (Exception ignored) {}
        }
    }


    public void close() {
        try {
            file.close();
        } catch (Exception e) {
            if (file == null) {
                System.out.println("File is null");
            }
            else {
                System.out.println("Error closing file!");
            }
        }
    }
}
