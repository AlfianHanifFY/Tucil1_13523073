package lib;

import java.util.*;
import java.io.*;

public class IO {
    public String fileName;
    public Scanner fileScanner;
    public static Scanner inputScanner = new Scanner(System.in);

    // Konstruktor
    public IO(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    // Open & Close File
    public void openFile() {
        try {
            File file = new File(getFileName());
            this.fileScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File " + getFileName() + "tidak ditemukan di directory test/input.");
            System.exit(0);
        }
    }

    public void closeFile() {
        if (fileScanner != null) {
            fileScanner.close();
        }
    }

    // Read file name from input
    public static String readFileName() {
        String fileName = "";
        while (true) {
            try {
                System.out.print("\nMasukan nama file: ");
                fileName = inputScanner.nextLine();
                File file = new File("test/input/" + fileName);
                Scanner tempScanner = new Scanner(file);
                tempScanner.close();
                break;
            } catch (FileNotFoundException e) {
                System.out.println("File " + fileName + " tidak ditemukan di directory test/input.");
            }
        }
        return "test/input/" + fileName;
    }

    // Count number of rows in the file
    public int[] getBoardInfo() {
        int[] info = new int[3];
        openFile();
        info[0] = fileScanner.nextInt();
        info[1] = fileScanner.nextInt();
        info[2] = fileScanner.nextInt();
        closeFile();
        return info;
    }

    public String getBoardType() {
        String type;
        openFile();
        fileScanner.nextLine();
        type = fileScanner.next();
        return type;
    }

    public Puzzle[] getPuzzle(int amount, int M, int N) {
        Puzzle[] resultPuzzles = new Puzzle[amount];
        openFile();
        fileScanner.nextLine(); // Skip first line (M N amount)
        fileScanner.nextLine(); // Skip board type

        int size = Math.max(M, N);

        String line = null;

        for (int i = 0; i < amount; i++) {
            if (line == null) {
                if (!fileScanner.hasNextLine()) {
                    System.out.println("[FAIL] File does not contain enough puzzle data!");
                    break;
                }
                line = fileScanner.nextLine();
            }

            char currentCharacter = line.trim().charAt(0);
            Puzzle currentPuzzle = new Puzzle(size, size);
            currentPuzzle.initiateShape();
            currentPuzzle.name = currentCharacter;

            int row = 0;
            do {
                currentPuzzle.insertShapeValue(line, size, row);
                row++;

                if (row > size) {
                    System.out.println("[FAIL] Puzzle exceeds allowed size!");
                    break;
                }

                if (fileScanner.hasNextLine()) {
                    String nextLine = fileScanner.nextLine();
                    String nextLineCC = nextLine.trim();
                    if (!nextLine.isEmpty() && nextLineCC.charAt(0) == currentCharacter) {
                        line = nextLine;
                    } else {
                        line = nextLine;
                        break;
                    }
                } else {
                    line = null;
                    break;
                }
            } while (true);

            currentPuzzle.compress();
            currentPuzzle.originShape = currentPuzzle.shape;
            resultPuzzles[i] = currentPuzzle;
        }

        closeFile();
        return resultPuzzles;
    }

}