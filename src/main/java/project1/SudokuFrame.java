package project1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {
    private JPanel buttonSelectionPanel;
    private SudokuPanel sudokuPanel;

    public SudokuFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setMinimumSize(new Dimension(800, 600));

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("Game");
        JMenu newGame = new JMenu("New Game");
        JMenuItem sixBySixGame = new JMenuItem("6 By 6 Game");
        sixBySixGame.addActionListener(new NewGameListener(SudokuPuzzleType.SIXBYSIX, 30));
        JMenuItem nineByNineGame = new JMenuItem(("9 By 9 Game"));
        nineByNineGame.addActionListener(new NewGameListener(SudokuPuzzleType.NINEBYNINE, 26));
        JMenuItem twelveByTwelveGame = new JMenuItem("12 By 12 Game");
        twelveByTwelveGame.addActionListener(new NewGameListener(SudokuPuzzleType.TWELVEBYTWELVE,20));
        JMenuItem sixteenBySixteenGame = new JMenuItem("16 By 16 Game");
		sixteenBySixteenGame.addActionListener(new NewGameListener(SudokuPuzzleType.SIXTEENBYSIXTEEN,16));

        newGame.add(sixBySixGame);
        newGame.add(nineByNineGame);
        newGame.add(twelveByTwelveGame);
        newGame.add(sixteenBySixteenGame);

        JMenu level = new JMenu("Level");
        JMenuItem easy = new JMenuItem("Easy");


        file.add(newGame);
        menuBar.add(file);
        this.setJMenuBar(menuBar);

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(800,600));

        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(800, 600));

        sudokuPanel = new SudokuPanel();

        windowPanel.add(sudokuPanel);
        windowPanel.add(buttonSelectionPanel);
        this.add(windowPanel);

        rebuildInterface(SudokuPuzzleType.NINEBYNINE, 26);
    }

    public void rebuildInterface(SudokuPuzzleType puzzleType, int fontSize){
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().generateRandomSudoku(puzzleType);
        sudokuPanel.newSudokuPuzzle(generatedPuzzle);
        sudokuPanel.setFontSize(fontSize);
        buttonSelectionPanel.removeAll();

        for(String value: generatedPuzzle.getValidValues()){
            JButton button = new JButton(value);
            button.setPreferredSize(new Dimension(40, 40 ));
            button.addActionListener(sudokuPanel.new NumActionListener());
            buttonSelectionPanel.add(button);
        }

        sudokuPanel.repaint();
        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
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
            rebuildInterface(puzzleType, fontSize);
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
