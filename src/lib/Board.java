package lib;

public class Board {
    char[][] map;
    int M;
    int N;
    int P;
    String type;

    public Board(int _M, int _N, int _P, String _type) {
        this.M = _M;
        this.N = _N;
        this.P = _P;
        this.type = _type;
        this.map = new char[_M][_N];
    }

    public void initiateMap() {
        int i;
        int j;
        for (i = 0; i < this.M; i++) {
            for (j = 0; j < this.N; j++) {
                this.map[i][j] = '%';
            }
        }
    }

    public void clearPuzzle(char puzzleName) {
        int i;
        int j;
        for (i = 0; i < this.M; i++) {
            for (j = 0; j < this.N; j++) {
                if (this.map[i][j] == puzzleName) {
                    this.map[i][j] = '%';
                }
            }
        }
    }

    public void show() {
        String[] colors = {
                "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m",
                "\u001B[91m", "\u001B[92m", "\u001B[93m", "\u001B[94m", "\u001B[95m", "\u001B[96m",
                "\u001B[97m", "\u001B[90m", "\u001B[37m", "\u001B[31m", "\u001B[32m", "\u001B[33m",
                "\u001B[34m", "\u001B[35m", "\u001B[36m", "\u001B[91m", "\u001B[92m", "\u001B[93m",
                "\u001B[94m", "\u001B[95m"
        };

        String reset = "\u001B[0m";

        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                char ch = this.map[i][j];
                if (ch >= 'A' && ch <= 'Z') {
                    int index = ch - 'A';
                    System.out.printf("%s%s%s ", colors[index], ch, reset);
                } else {
                    System.out.printf("%s ", ch);
                }
            }
            System.out.println();
        }
    }

    public int[] findRefCor() {
        int[] res = new int[2];
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.map[i][j] == '%') {
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        res[0] = -1;
        res[1] = -1;
        return res;
    }

    public Boolean addPuzzle(Puzzle puzzle) {
        int[] refCor = findRefCor();
        int x = refCor[1];
        int y = refCor[0];

        if (puzzle.used) {
            return false;
        }

        if (x == -1 && y == -1) {
            return false;
        }

        int i;
        int j;

        // find offset on puzzle shape
        int offset = 0;
        for (j = 0; j < puzzle.col; j++) {
            if (puzzle.shape[0][j] == '%') {
                offset++;
            } else {
                break;
            }
        }

        x -= offset;
        if (x < 0) {
            return false;
        }

        if (puzzle.row + y > this.M) {
            return false;
        }

        if (puzzle.col + x > this.N) {
            return false;
        }

        for (i = y; (i - y) < puzzle.row; i++) {
            for (j = x; (j - x) < puzzle.col; j++) {
                if (this.map[i][j] != '%' && puzzle.shape[i - y][j - x] != '%') {
                    clearPuzzle(puzzle.name);
                    return false;
                }
                if (puzzle.shape[i - y][j - x] != '%') {
                    this.map[i][j] = puzzle.shape[i - y][j - x];
                }
            }
        }
        puzzle.used = true;
        return true;
    }

    public Boolean isFull() {
        int i;
        int j;

        for (i = 0; i < this.M; i++) {
            for (j = 0; j < this.N; j++) {
                if (this.map[i][j] == '%') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFail(Puzzle[] puzzles) {
        boolean allUsed = true;
        for (int i = 0; i < this.P; i++) {
            if (puzzles[i].used == false) {
                allUsed = false;
            }
        }
        if (isFull() && allUsed) {
            return false;
        } else {
            return true;
        }
    }

    public void solve(Puzzle[] puzzles) {
        int id = 0;
        int[] placedPuzzle = new int[this.P];
        boolean status;
        boolean fail = false;
        int count = 0;

        do {
            status = false;

            // coba tambah puzzle
            for (int i = 0; i < puzzles.length; i++) {
                if (!puzzles[i].used) {
                    while (!addPuzzle(puzzles[i]) && !status) {
                        count++;

                        // ubah orientasi klo gagal
                        if (puzzles[i].rotateAmount == 4) {
                            puzzles[i].mirror();
                        } else if (puzzles[i].rotateAmount < 8) {
                            puzzles[i].rotate90();
                        } else {
                            puzzles[i].reset();
                            break;
                        }
                    }
                    // kalo puzzle berhasil stop nyari
                    if (puzzles[i].used) {
                        status = true;
                        placedPuzzle[id++] = i;
                        break;
                    }
                } else {
                    count++;
                }
            }

            // backtrack kalo gagal menambah puzzle
            while (!status) {
                // kondisi kalo puzzle pertama di backtrack

                // hapus puzzle terakhir
                if (id > 0) {
                    id--;
                }
                int tempId = placedPuzzle[id];
                count++;
                clearPuzzle(puzzles[tempId].name);
                puzzles[tempId].used = false;

                boolean newPlacementFound = false;

                // Coba orientasi lain sebelum ganti puzzle
                while (!newPlacementFound) {
                    count++;
                    if (puzzles[tempId].rotateAmount == 4) {
                        puzzles[tempId].mirror();
                    } else if (puzzles[tempId].rotateAmount < 8) {
                        puzzles[tempId].rotate90();
                    } else {
                        puzzles[tempId].reset();
                        if (id == 0 && tempId == this.P - 1) {
                            // here cok
                            fail = true;
                        }
                        break;
                    }

                    if (addPuzzle(puzzles[tempId])) {
                        placedPuzzle[id++] = tempId;
                        newPlacementFound = true;
                        status = true;
                        break;
                    }
                }

                // Kalo semua orientasi gagal, coba puzzle berikutnya
                if (!newPlacementFound) {
                    for (int newId = tempId + 1; newId < puzzles.length; newId++) {
                        if (!puzzles[newId].used) {
                            while (!addPuzzle(puzzles[newId]) && !newPlacementFound) {
                                count++;
                                if (puzzles[newId].rotateAmount == 4) {
                                    puzzles[newId].mirror();
                                } else if (puzzles[newId].rotateAmount < 8) {
                                    puzzles[newId].rotate90();
                                } else {
                                    puzzles[newId].reset();
                                    break;
                                }
                            }

                            if (puzzles[newId].used) {
                                placedPuzzle[id++] = newId;
                                newPlacementFound = true;
                                status = true;
                                break;
                            }
                        }
                    }
                }

            }
        } while (!isFull() && !fail);

        if (fail || isFail(puzzles)) {
            System.out.println("gagal");
        } else {
            show();
            System.out.println("sukses");
        }

        System.out.printf("Banyak kasus : %s \n", count);
    }

}
