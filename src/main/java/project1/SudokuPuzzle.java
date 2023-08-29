package project1;

public class SudokuPuzzle {
    protected String [][] board;

    //Table to determine if a slot is mutable
    protected boolean [][] mutable;
    private final int Rows; //số hàng của bảng
    private final int Columns; //số cột của bảng

    //kích thước của các hộp con trong bảng
    private final int BoxWidth;
    private final int BoxHeight;

    //Mảng chứa giá trị hợp lệ
    private final String [] ValidValues;

    public SudokuPuzzle(int rows, int columns, int boxHeight, int boxWidth, String[] validValues){
        this.Rows = rows;
        this.Columns = columns;
        this.BoxHeight = boxHeight;
        this.BoxWidth = boxWidth;
        this.ValidValues = validValues;
        this.board = new String[Rows][Columns];
        this.mutable = new boolean[Rows][Columns];
        initializeBoard();
        initializeMutableSlots();
    }

    public SudokuPuzzle(SudokuPuzzle puzzle){
        this.Rows = puzzle.Rows;
        this.Columns = puzzle.Columns;
        this.BoxWidth = puzzle.BoxWidth;
        this.BoxHeight = puzzle.BoxHeight;
        this.ValidValues = puzzle.ValidValues;
        this.board = new String[Rows][Columns];
        for(int r = 0; r < Rows; r ++){
            for(int c = 0; r < Columns; c++){
                board[r][c] = puzzle.board[r][c];
            }
        }
        this.mutable = new boolean[Rows][Columns];
        for(int r = 0; r < Rows; r ++){
            for(int c = 0; r < Columns; c++){
                this.mutable[r][c] = puzzle.mutable[r][c];
            }
        }
    }

    public int getNumRows(){
        return this.Rows;
    }

    public int getNumColumns(){
        return this.Columns;
    }

    public int getBoxWidth(){
        return this.BoxWidth;
    }

    public int getBoxHeight(){
        return this.BoxHeight;
    }

    public String[] getValidValues(){
        return this.ValidValues;
    }

    //Thực hiện điền giá trị vào một ô trong bảng Sudoku
    // nếu giá trị hợp lệ và ô có thể điền (chưa có giá trị và ô có thể thay đổi)
    public void makeMove(int row, int col, String value, boolean isMutable){
        if(this.isValidValue(value) && this.isValidMove(row,col,value) && this.isSlotMutable(row, col)) {
            this.board[row][col] = value;
            this.mutable[row][col] = isMutable;
        }
    }

    //Kiểm tra tính hợp lệ của nước đi
    public boolean isValidMove(int row, int col, String value){
        if(this.inRange(row, col)){
            if(!this.numInCol(col, value) && !this.numInRow(row, value) && !this.numInBox(row, col, value)){
                return true;
            }
        }
        return false;
    }

    //Kiểm tra xem giá trị có xuất hiện trong cột, hàng hoặc hộp(Box)
    public boolean numInCol(int col, String value){
        if(col <= this.Columns){
            for(int r = 0; r < this.Rows; r++){
                if(this.board[r][col].equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean numInRow(int row, String value){
        if(row <= this.Rows){
            for(int c = 0; c < this.Columns; c++){
                if(this.board[row][c].equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean numInBox(int row, int col, String value){
        if(this.inRange(row, col)){
            int boxRow = row/this.BoxHeight;
            int boxCol = col/this.BoxWidth;

            int startingRow = (boxRow*this.BoxHeight);
            int startingCol = (boxCol*this.BoxWidth);

            for(int r = startingRow; r <= (startingRow+this.BoxHeight)-1; r++){
                for(int c = startingCol; c <= (startingCol+this.BoxWidth)-1; c++){
                    if(this.board[r][c].equals(value)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Kiểm tra xem tại vị trí (row, col) có thay đổi được không
    public boolean isSlotMutable(int row, int col){
        return this.mutable[row][col];
    }

    //Kiểm tra xem tại ô vị trí (row,col) có khả dụng để điền vào không
    public boolean isSlotAvailable(int row, int col){
        return (this.inRange(row, col) && this.board[row][col].equals("") && this.isSlotMutable(row, col));
    }

    //Lấy giá trị của ô tại vị trí (row, col)
    public String getValue(int row, int col){
        if(this.inRange(row, col)){
            return this.board[row][col];
        }
        return "";
    }

    //Lấy giá trị của cả bảng
    public String[][] getBoard(){
        return this.board;
    }

    //Kiểm tra xem một giá trị có hợp lệ trong ngữ cảnh của trò chơi không
    //Giá trị value cần kiểm tra, phải thỏa mãn tính hợp lệ vs các giá trị trong mảng ValidValues
    public boolean isValidValue(String value){
        for(String str : this.ValidValues){
            if(str.equals(value)) return true;
        }
        return false;
    }

    //Kiểm tra xem cặp tọa độ (row, col) có trong phạm vi của bảng không
    public boolean inRange(int row, int col){
        return row <= this.Rows &&  col <= this.Columns && row >= 0 && col >= 0;
    }

    //Kiểm tra xem bảng đã đầy(full) chưa
    public boolean boardFull(){
        for(int r = 0; r < this.Rows; r++){
            for(int c = 0; c < this.Columns; c++){
                if(this.board[r][c].equals("")) return false;
            }
        }
        return true;
    }

    //Phương thức xóa nước vừa đi
    public void makeSlotEmpty(int row, int col){
        this.board[row][col] = "";
    }

    //Trả về một biểu diễn chuỗi của bảng Sudoku
    //giúp hiện thị bảng trong định dạng văn bản
    @Override
    public String toString(){
        String str = "Game Board:\n";
        for(int row = 0;row < this.Rows; row++){
            for(int col=0; col < this.Columns; col++){
                str += this.board[row][col] + " ";
            }
            str += "\n";
        }
        return str+"\n";
    }

    //Khởi tạo thiết lập giá trị ban đầu cho bảng
    // và trạng thái thay đổi của các ô
    private void initializeBoard(){
        for(int row = 0; row < this.Rows; row++){
            for(int col = 0; col < this.Columns; col++){
                this.board[row][col] = "";
            }
        }
    }

    private void initializeMutableSlots(){
        for(int row = 0; row  < this.Rows; row++){
            for(int col = 0; col < this.Columns; col++){
                this.mutable[row][col] = true;
            }
        }
    }
}
