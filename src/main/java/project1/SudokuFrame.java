package project1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {
    private JPanel buttonSelectionPanel;
    private SudokuPanel sudokuPanel;
    private JLabel wrongAttemptsLabel;

    //Các biến để lưu trữ kích thước và mức độ trò chơi hiện tại
    private SudokuPuzzleType currentPuzzleType;
    private SudokuLevel currentLevel;

    //Số lần gợi ý
    private int hintsUsed = 0;
    private static final int MAX_HINTS = 3;

    public int calculateFontSize() {
        int baseFontSize;

        switch (currentPuzzleType) {
            case SIXBYSIX:
                baseFontSize = 30;
                return baseFontSize;
            case NINEBYNINE:
                baseFontSize = 26;
                return baseFontSize;
            case TWELVEBYTWELVE:
                baseFontSize = 20;
                return baseFontSize;
//            case SIXTEENBYSIXTEEN:
//                baseFontSize = 16;
//                return baseFontSize;
            default:
                throw new IllegalArgumentException("Invalid Puzzle Type: " + currentPuzzleType);
        }
    }

    public SudokuFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setMinimumSize(new Dimension(800, 600));

        currentPuzzleType = SudokuPuzzleType.NINEBYNINE;
        currentLevel = SudokuLevel.EASY;

        JMenuBar menuBar = new JMenuBar();
        JMenu newGame = new JMenu("New Game");
        JMenuItem sixBySixGame = new JMenuItem("6 By 6 Game");
        sixBySixGame.addActionListener(new NewGameListener(SudokuPuzzleType.SIXBYSIX, 30));
        JMenuItem nineByNineGame = new JMenuItem(("9 By 9 Game"));
        nineByNineGame.addActionListener(new NewGameListener(SudokuPuzzleType.NINEBYNINE, 26));
        JMenuItem twelveByTwelveGame = new JMenuItem("12 By 12 Game");
        twelveByTwelveGame.addActionListener(new NewGameListener(SudokuPuzzleType.TWELVEBYTWELVE,20));
//        JMenuItem sixteenBySixteenGame = new JMenuItem("16 By 16 Game");
//		sixteenBySixteenGame.addActionListener(new NewGameListener(SudokuPuzzleType.SIXTEENBYSIXTEEN,16));

        newGame.add(sixBySixGame);
        newGame.add(nineByNineGame);
        newGame.add(twelveByTwelveGame);
//        newGame.add(sixteenBySixteenGame);

        JMenu level = new JMenu("Level");
        JMenuItem easy = new JMenuItem("Easy");
        easy.addActionListener(new LevelListener(SudokuLevel.EASY));
        JMenuItem medium = new JMenuItem("Medium");
        medium.addActionListener(new LevelListener(SudokuLevel.MEDIUM));
        JMenuItem hard = new JMenuItem("Hard");
        hard.addActionListener(new LevelListener(SudokuLevel.HARD));
        JMenuItem expert = new JMenuItem("Expert");
        expert.addActionListener(new LevelListener(SudokuLevel.EXPERT));

        level.add(easy);
        level.add(medium);
        level.add(hard);
        level.add(expert);

        JMenuItem eraseCell = new JMenuItem("Erase");
        eraseCell.addActionListener(new EraseCellListener());

        JMenuItem hint = new JMenuItem("Hint");
        hint.addActionListener(new HintListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hintsUsed < MAX_HINTS) {
                    sudokuPanel.showHintForCurrentCell();
                    hintsUsed++;
                } else {
                    JOptionPane.showMessageDialog(null, "Bạn đã hết lượt gợi ý.");
                }
            }
        });

        JMenu file = new JMenu("Game");
        file.add(newGame);
        file.add(level);
        JMenu file1 = new JMenu("Function");
        file1.add(eraseCell);
        file1.add(hint);
        menuBar.add(file);
        menuBar.add(file1);
        this.setJMenuBar(menuBar);

        wrongAttemptsLabel = new JLabel("Số lần điền sai: 0");
        this.add(wrongAttemptsLabel, BorderLayout.SOUTH);

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800,600));

        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(800, 600));

        sudokuPanel = new SudokuPanel(this);

        windowPanel.add(sudokuPanel);
        windowPanel.add(buttonSelectionPanel);
        this.add(windowPanel);

        rebuildInterface(SudokuPuzzleType.NINEBYNINE, 26, SudokuLevel.EASY);
    }

    public void rebuildInterface(SudokuPuzzleType puzzleType, int fontSize, SudokuLevel level){
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().generateRandomSudoku(puzzleType, level);
        sudokuPanel.newSudokuPuzzle(generatedPuzzle);
        sudokuPanel.setFontSize(fontSize);
        buttonSelectionPanel.removeAll();

        for(String value: generatedPuzzle.getValidValues()){
            JButton button = new JButton(value);
            button.setPreferredSize(new Dimension(45, 45 ));
            button.addActionListener(sudokuPanel.new NumActionListener());
            buttonSelectionPanel.add(button);
        }

        sudokuPanel.repaint();
        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
    }

    public void updateWrongAttempts(int wrongAttempts){
        wrongAttemptsLabel.setText("Số lần điền sai: " + wrongAttempts);
    }

    public class NewGameListener implements ActionListener {
        private SudokuPuzzleType puzzleType;
        private int fontSize;

        public NewGameListener(SudokuPuzzleType puzzleType, int fontSize){
            this.puzzleType = puzzleType;
            this.fontSize = fontSize;
        }

        @Override
        public void actionPerformed(ActionEvent event){
            currentPuzzleType = puzzleType;
            currentLevel = SudokuLevel.EASY;
            rebuildInterface(currentPuzzleType, fontSize, currentLevel);
        }
    }

    public class LevelListener implements ActionListener{
        private SudokuLevel level;

        public LevelListener(SudokuLevel level){
            this.level = level;
        }

        @Override
        public void actionPerformed(ActionEvent event){
            int fontSize = calculateFontSize();
            currentLevel = level;
            rebuildInterface(currentPuzzleType, fontSize, currentLevel);
        }
    }

    public class EraseCellListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            sudokuPanel.eraseCurrentCell();
        }
    }

    public class HintListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            sudokuPanel.showHintForCurrentCell();
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuFrame frame = new SudokuFrame();
                frame.setVisible(true);
            }
        });
    }
}
