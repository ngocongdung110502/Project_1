package project1;

import java.security.PublicKey;
import java.util.*;

public class SudokuGenerator {

    //Tạo ra một trò chơi Sudoku ngẫu nhiên dựa trên loại trò chơi đã cho
    public SudokuPuzzle generateRandomSudoku(SudokuPuzzleType puzzleType){
        SudokuPuzzle puzzle = new SudokuPuzzle(puzzleType.getRows(), puzzleType.getColumns(), puzzleType.getBoxWidth(), puzzleType.getBoxHeight(), puzzleType.getValidValues());
        SudokuPuzzle copy = new SudokuPuzzle(puzzle); //tạo một bản sao của bản gốc

        Random randomGenerator = new Random();

        //Sử dụng một danh sách để theo dõi các giá trị hợp lệ chưa được sử dụng
        //với mỗi hàng chọn ngẫu nhiên 1 giá trị trong danh sách để thêm nó vào bản sao(copy)
        List<String> notUsedValidValues = new ArrayList<String>(Arrays.asList(copy.getValidValues()));
        for(int r = 0; r < copy.getNumRows(); r++){
            int randomValue = randomGenerator.nextInt(notUsedValidValues.size());
            copy.makeMove(r, 0, notUsedValidValues.get(randomValue), true);
            notUsedValidValues.remove(randomValue);
        }

        backtrackSudokuSolver(0,0, copy);

        //Xác định số lượng cần giữ lại trong trò chơi cần được giải quyết để tạo mức độ (level)
        //Số lược giữ lại được tính bằng cách nhân tỉ lệ 0.2222 với tổng số ô
        int numberOfValuesToKeep = (int)(0.3*(copy.getNumRows()*copy.getNumRows()));

        for(int i = 0; i < numberOfValuesToKeep;){
            int randomRow = randomGenerator.nextInt(puzzle.getNumRows());
            int randomColumn = randomGenerator.nextInt(puzzle.getNumColumns());

            if(puzzle.isSlotAvailable(randomRow, randomColumn)){
                puzzle.makeMove(randomRow, randomColumn, copy.getValue(randomRow, randomColumn), false);
                i++;
            }
        }

        return puzzle;
    }

    private boolean backtrackSudokuSolver(int r, int c, SudokuPuzzle puzzle){
        //Nếu tọa độ không hợp lệ trả về false
        if(!puzzle.inRange(r,c)){
            return false;
        }

        //Nếu vị trị hiện tại trống
        if(puzzle.isSlotAvailable(r,c)){
            for(int i = 0; i < puzzle.getValidValues().length; i++){
                if(!puzzle.numInRow(r, puzzle.getValidValues()[i]) && !puzzle.numInCol(c, puzzle.getValidValues()[i]) && !puzzle.numInBox(r, c, puzzle.getValidValues()[i])){

                    //thưc hiện điền giá trị vào ô
                    puzzle.makeMove(r, c, puzzle.getValidValues()[i], true);

                    //nếu trò chơi được giải quyết thì trả về true
                    if(puzzle.boardFull()){
                        return true;
                    }

                    if(r == puzzle.getNumRows() - 1){
                        if(backtrackSudokuSolver(0, c+1, puzzle)) return true;
                    }else{
                        if(backtrackSudokuSolver(r+1, c, puzzle)) return true;
                    }
                }
            }
        }

        //Nếu vị trí hiện tại không trống
        else{
            //chuyển sang vị trí tiếp theo
            if(r == puzzle.getNumRows()-1){
                return backtrackSudokuSolver(0, c+1, puzzle);
            }else{
                return backtrackSudokuSolver(r+1, c, puzzle);
            }
        }

        //undo move
        puzzle.makeSlotEmpty(r, c);

        //backtrack
        return false;
    }
}
