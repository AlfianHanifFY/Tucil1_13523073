package lib;

import java.util.*;

import javax.imageio.ImageIO;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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
            System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : File " + getFileName()
                    + " tidak ditemukan di directory...");
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
                System.out.print("\u001B[38;5;214m[INPUT]\u001B[0m" + " : ");
                fileName = inputScanner.nextLine();
                File file = new File("test/input/" + fileName);
                Scanner tempScanner = new Scanner(file);
                tempScanner.close();
                break;
            } catch (FileNotFoundException e) {
                System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : File " + fileName
                        + " tidak ditemukan di directory test/input.");
            }
        }
        return "test/input/" + fileName;
    }

    // Count number of rows in the file
    public int[] getBoardInfo() {
        int[] info = new int[3];
        boolean stat = true;
        info[0] = -99999;
        info[1] = -99999;
        info[2] = -99999;
        openFile();
        if (fileScanner.hasNextInt()) {
            info[0] = fileScanner.nextInt();
        }
        if (fileScanner.hasNextInt()) {
            info[1] = fileScanner.nextInt();
        }
        if (fileScanner.hasNextInt()) {
            info[2] = fileScanner.nextInt();
        }
        closeFile();
        for (int i = 0; i < 3; i++) {
            if (info[i] < 0) {
                stat = false;
            }
        }
        if (stat) {
            System.out.println("\u001B[32m[SUCCESS]\u001B[0m"
                    + " : Konfigurasi (M/N/P) pada board valid...");
        } else {
            System.out.println("\u001B[31m[ERROR]\u001B[0m" + " : Konfigurasi (M/N/P) pada board tidak valid...");
        }
        return info;
    }

    public String getBoardType() {
        String type = "";
        openFile();
        if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
        }
        if (fileScanner.hasNext()) {
            type = fileScanner.next();
        }
        if (type.equals("DEFAULT")) {
            System.out.println("\u001B[32m[SUCCESS]\u001B[0m"
                    + " : Konfigurasi tipe board valid...");
        } else {
            System.out.println("\u001B[31m[ERROR]\u001B[0m" + " : Konfigurasi tipe board tidak tersedia...");
        }
        return type;
    }

    public Puzzle[] getPuzzle(int amount, int M, int N) {
        Puzzle[] resultPuzzles = new Puzzle[amount];
        Puzzle[] failPuzzles = new Puzzle[0];
        boolean valid = true;
        openFile();

        // Skip baris pertama (info board)
        if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
        }
        // Skip baris kedua (tipe board)
        if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
        }

        int size = Math.max(M * N, M * N);

        String line = null;

        for (int i = 0; i < amount; i++) {
            if (line == null) {
                if (!fileScanner.hasNextLine()) {
                    System.out.println("\u001B[31m[ERROR]\u001B[0m" + " : Konfigurasi Puzzle tidak valid...");
                    valid = false;
                    return failPuzzles;
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
                    System.out.println("\u001B[31m[ERROR]\u001B[0m" + " : Konfigurasi Puzzle tidak valid...");
                    valid = false;
                    return resultPuzzles;
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
        if (valid) {
            System.out.println("\u001B[32m[SUCCESS]\u001B[0m"
                    + " : Konfigurasi Puzzle selesai...");
        }
        return resultPuzzles;
    }

    public void saveBoardTxt(Board board, long attempt, long duration, long status) {
        Scanner scanner = new Scanner(System.in);
        String directory = "test/output/txt";
        File folder = new File(directory);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filename;
        File file;

        while (true) {
            System.out.println("\n\u001B[34m[INFO]\u001B[0m" + " : Masukan nama file !");
            System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : Pastikan menambahkan .txt (XXX.txt) !");
            System.out.print("\u001B[38;5;214m[INPUT]\u001B[0m" + " : ");
            filename = scanner.nextLine();
            file = new File(directory, filename);

            if (!file.exists()) {
                break;
            } else {
                System.out.println("\u001B[31m[ERROR]\u001B[0m" + " : File sudah tersedia ! ubah nama file !...");
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < board.M; i++) {
                for (int j = 0; j < board.N; j++) {
                    writer.write(board.map[i][j]);
                    writer.write(" ");
                }
                writer.write("\n");
            }
            writer.write("\n");
            if (status == 1) {
                writer.write("Status : Sukses\n");
            } else {
                writer.write("Status : Gagal\n");
            }
            writer.write("Percobaan : ");
            writer.write(String.valueOf(attempt));
            writer.write("\n");
            writer.write("Durasi : ");
            writer.write(String.valueOf(duration));
            writer.write("ms");
            writer.write("\n");

            System.out.println("\u001B[32m[SUCCESS]\u001B[0m"
                    + " : File disimpan pada test/output/txt/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveBoardImage(Board board, long attempt, long duration, long status) {
        char[][] map = board.map;
        Scanner scanner = new Scanner(System.in);
        int cellSize = 50;
        int padding = 50;
        int textHeight = 100;

        int width = (board.N * cellSize) + (2 * padding);
        int height = (board.M * cellSize) + (2 * padding) + textHeight;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        Color[] colors = {
                Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.CYAN,
                Color.PINK, Color.ORANGE, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.GRAY, Color.WHITE,
                new Color(128, 0, 0), new Color(0, 128, 0), new Color(0, 0, 128), new Color(128, 128, 0),
                new Color(128, 0, 128), new Color(0, 128, 128), new Color(192, 192, 192), new Color(255, 165, 0)
        };

        for (int i = 0; i < board.M; i++) {
            for (int j = 0; j < board.N; j++) {
                char ch = map[i][j];

                if (ch >= 'A' && ch <= 'Z') {
                    g2d.setColor(colors[(ch - 'A') % colors.length]);
                } else {
                    g2d.setColor(Color.WHITE);
                }

                int x = padding + (j * cellSize);
                int y = padding + (i * cellSize);

                g2d.fillRect(x, y, cellSize, cellSize);

                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, cellSize, cellSize);

                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                g2d.drawString(String.valueOf(ch), x + 15, y + 35);
            }
        }

        // font
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();

        int lineSpacing = 25; // Jarak antar teks
        int baseY = height - textHeight + (fm.getAscent() / 2) + 10; // Awal posisi teks

        // teks Status
        String statText = (status == 1) ? "Status: Sukses " : "Status: Gagal";
        int textWidth = fm.stringWidth(statText);
        int textX = (width - textWidth) / 2;
        g2d.drawString(statText, textX, baseY);

        // teks Percobaan
        String attemptText = "Percobaan: " + attempt;
        textWidth = fm.stringWidth(attemptText);
        textX = (width - textWidth) / 2;
        g2d.drawString(attemptText, textX, baseY + lineSpacing);

        // teks Durasi
        String durationText = "Durasi: " + duration + "ms";
        textWidth = fm.stringWidth(durationText);
        textX = (width - textWidth) / 2;
        g2d.drawString(durationText, textX, baseY + (2 * lineSpacing));

        g2d.dispose();

        // validasi
        File directory = new File("test/output/image");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save as PNG
        File outputFile;
        while (true) {
            System.out.println("\n\u001B[34m[INFO]\u001B[0m" + " : Masukan nama file !");
            System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : Pastikan menambahkan .png (XXX.png) !");
            System.out.print("\u001B[38;5;214m[INPUT]\u001B[0m" + " : ");
            String filename = scanner.nextLine();
            outputFile = new File(directory, filename);

            if (!outputFile.exists()) {
                break;
            } else {
                System.out.println("\u001B[31m[ERROR]\u001B[0m" + " : File sudah tersedia ! ubah nama file !...");
            }
        }
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("\u001B[32m[SUCCESS]\u001B[0m"
                    + " : File disimpan pada " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
