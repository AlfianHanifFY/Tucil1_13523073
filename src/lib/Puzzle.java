package lib;

public class Puzzle {
    boolean used;
    int row;
    int col;
    char[][] shape;
    char[][] originShape;
    int originCol;
    int originRow;
    char name;
    int rotateAmount;

    public Puzzle(int _row, int _col) {
        this.row = _row;
        this.col = _col;
        this.used = false;
        if (_row > _col) {
            this.shape = new char[_row][_row];
        } else {
            this.shape = new char[_col][_col];
        }
        this.rotateAmount = 0;
    }

    public void initiateShape() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                this.shape[i][j] = '%';
            }
        }
    }

    public void insertShapeValue(String line, int size, int row) {
        int i;
        for (i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                this.shape[row][i] = line.charAt(i);
            } else {
                this.shape[row][i] = '%';
            }
        }
    }

    public void show() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                System.out.printf("%s", this.shape[i][j]);
            }
            System.err.println();
        }
    }

    public void compress() {
        // get row and col
        int row = 0;
        int col = 0;
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (this.shape[i][j] != '%') {
                    if (i + 1 > row) {
                        row = i + 1;
                    }
                    if (j + 1 > col) {
                        col = j + 1;
                    }
                }
            }
        }

        // compress
        this.col = col;
        this.row = row;
        this.originCol = col;
        this.originRow = row;
    }

    public void reset() {
        this.col = this.originCol;
        this.row = this.originRow;
        this.shape = this.originShape;
        this.rotateAmount = 0;
        this.used = false;
    }

    public Boolean isOrigin() {
        if (this.col != this.originCol) {
            return false;
        }
        if (this.row != this.originRow) {
            return false;
        }

        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (this.originShape[i][j] != this.shape[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Puzzle transpose() {
        int m = this.col;
        int n = this.row;
        Puzzle res = new Puzzle(m, n);

        char[][] newShape = new char[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newShape[j][i] = this.shape[i][j];
            }
        }
        res.shape = newShape;
        return res;
    }

    public void swapCol(int col1, int col2) {
        char temp;
        int i;
        for (i = 0; i < this.row; i++) {
            temp = this.shape[i][col1];
            this.shape[i][col1] = this.shape[i][col2];
            this.shape[i][col2] = temp;
        }
    }

    public void rotate90() {
        Puzzle M = new Puzzle(this.row, this.col);
        M.shape = this.shape;

        Puzzle R = M.transpose();
        int m = R.col;

        for (int i = 0; i <= (m / 2) - 1; i++) {
            R.swapCol(i, m - i - 1);
        }
        this.row = R.row;
        this.col = R.col;
        this.shape = R.shape;
        this.rotateAmount += 1;
    }

    public void mirror() {
        int i;
        int j;
        char[][] temp = new char[this.row][this.col];
        for (i = 0; i < this.row; i++) {
            for (j = 0; j < this.col; j++) {
                temp[i][j] = this.shape[i][this.col - 1 - j];
            }
            for (j = 0; j < this.col; j++) {
                this.shape[i][j] = temp[i][j];
            }
        }
        this.rotateAmount += 1;
    }

    public boolean isMirrorOrigin() {
        Puzzle temp = new Puzzle(this.row, this.col);
        temp.originShape = this.shape;
        temp.originCol = this.col;
        temp.originRow = this.row;
        temp.shape = this.shape;

        temp.mirror();

        if (temp.isOrigin()) {
            return true;
        } else {
            return false;
        }
    }
}
