package project1;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class SudokuPanel extends JPanel {
    private SudokuPuzzle puzzle;

    //Cột và hàng được chọn trên bảng Sudoku
    private int currentlySelectedColumn;
    private int currentlySelectedRow;

    //Chiều rộng và chiều cao được sử dụng trong panel.
    private int usedWidth;
    private int usedHeight;

    //Kích thước khung chữ được sử dụng để hiện thị số trên bảng Sudoku
    private int fontSize;

    private int wrongAttempts = 0;
    private static final int MAX_WRONG_ATTEMPTS = 5;
    private SudokuFrame frame;

    //Constructor
    public SudokuPanel(){
        this.setPreferredSize(new Dimension(540, 450));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.puzzle = puzzle;
        currentlySelectedColumn = -1;
        currentlySelectedRow = -1;
        usedHeight = 0;
        usedWidth = 0;
        fontSize = 26;
    }

    public SudokuPanel(SudokuFrame frame){
        this.frame = frame;
        this.setPreferredSize(new Dimension(540,450));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.puzzle = puzzle;
        currentlySelectedColumn = -1;
        currentlySelectedRow = -1;
        usedWidth = 0;
        usedHeight = 0;
        fontSize = 26;
    }

    public void newSudokuPuzzle(SudokuPuzzle puzzle){
        this.puzzle = puzzle;
    }

    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(new Color(1.0f,1.0f, 1.0f));

        //Tính toán kích thước mỗi ô Sudoku trên kích thước panel
        int slotWidth = this.getWidth()/puzzle.getNumColumns();
        int slotHeight = this.getHeight()/puzzle.getNumRows();

        //Tính toán kích thước thực tế được sử dụng để vẽ bảng Sudoku
        usedHeight = slotHeight*puzzle.getNumRows();
        usedWidth = slotWidth*puzzle.getNumColumns();

        //Vẽ hình chữ nhật màu đen với kích thước đã cho để tạo nền cho bảng
        graphics2D.fillRect(0,0,usedWidth, usedHeight);
        //Đặt màu thành màu đen
        graphics2D.setColor(new Color(0.0f, 0.0f, 0.0f));

        for(int x = 0; x <= usedWidth; x += slotWidth){
            if((x/slotWidth) % puzzle.getBoxWidth() == 0){
                graphics2D.setStroke(new BasicStroke(2));
                graphics2D.drawLine(x, 0, x, usedHeight);
            }else{
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.drawLine(x, 0, x, usedHeight);
            }
        }

        for(int y = 0; y <= usedHeight; y += slotHeight){
            if((y/slotHeight) % puzzle.getBoxHeight() == 0){
                graphics2D.setStroke(new BasicStroke(2));
                graphics2D.drawLine(0, y, usedWidth, y);
            }else{
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.drawLine(0, y, usedWidth, y);
            }
        }

        Font f = new Font("Arial", Font.PLAIN, fontSize);
        graphics2D.setFont(f);
        FontRenderContext fContext = graphics2D.getFontRenderContext();
        for(int row = 0; row < puzzle.getNumRows(); row++){
            for(int col = 0; col < puzzle.getNumColumns(); col++){
                if(!puzzle.isSlotAvailable(row,col)){
                    int textWidth = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
                    int textHeight = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();
                    graphics2D.drawString(puzzle.getValue(row, col), (col*slotWidth)+((slotWidth/2)-(textWidth/2)), (row*slotHeight)+((slotHeight/2)+(textHeight/2)));
                }
            }
        }
        if(currentlySelectedColumn != -1 && currentlySelectedRow != -1){
            graphics2D.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
            graphics2D.fillRect(currentlySelectedColumn * slotWidth, currentlySelectedRow * slotHeight, slotWidth, slotHeight);
        }

        //Thay đổi màu sắc của chữ số được điền vào ô trống
        for(int row = 0; row < puzzle.getNumRows(); row++){
            for(int col = 0; col < puzzle.getNumColumns(); col++){
                if(!puzzle.isSlotAvailable(row, col) && puzzle.isSlotMutable(row, col)){
                    // Nếu ô đã được điền đúng, đặt màu sắc tương ứng cho chữ số
                    graphics2D.setColor(Color.BLUE);
                } else {
                    // Nếu không, sử dụng màu sắc mặc định
                    graphics2D.setColor(Color.BLACK);
                }

                // Vẽ chuỗi tại vị trí tương ứng
                int textWidth = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getWidth();
                int textHeight = (int) f.getStringBounds(puzzle.getValue(row, col), fContext).getHeight();
                graphics2D.drawString(puzzle.getValue(row, col), (col*slotWidth)+((slotWidth/2)-(textWidth/2)), (row*slotHeight)+((slotHeight/2)+(textHeight/2)));
            }
        }

    }

    //Xóa một ô đã được điền số
    public void eraseCurrentCell() {
        if (currentlySelectedColumn != -1 && currentlySelectedRow != -1) {
            puzzle.makeSlotEmpty(currentlySelectedRow, currentlySelectedColumn);
            repaint();
        }
    }

    //Gợi ý cho ô được chọn
    public void showHintForCurrentCell() {
        if (currentlySelectedColumn != -1 && currentlySelectedRow != -1) {
            String hint = puzzle.getHint(currentlySelectedRow, currentlySelectedColumn);
            JOptionPane.showMessageDialog(null, "Hint: " + hint);
        }
    }

    public void messageFromNumActionListener(String buttonValue){
        if(currentlySelectedColumn != -1 && currentlySelectedRow != -1) {
            if (puzzle.isValidMove(currentlySelectedRow, currentlySelectedColumn, buttonValue)){
                puzzle.makeMove(currentlySelectedRow, currentlySelectedColumn, buttonValue, true);
                repaint();
            }else{
                wrongAttempts++;
                frame.updateWrongAttempts(wrongAttempts);
                if (wrongAttempts >= MAX_WRONG_ATTEMPTS) {
                    int o = JOptionPane.showConfirmDialog(this, "Bạn đã điền sai quá 5 lần. Bạn có muốn chơi lại không?", "Game Over", JOptionPane.YES_NO_OPTION);
                    // Bắt đầu trò chơi mới ở đây
                    if (o == JOptionPane.YES_OPTION) {
                        // Nếu người dùng chọn "Yes", bắt đầu một trò chơi mới
                        frame.startNewGame();
                    } else {
                        // Nếu người dùng chọn "No", thoát khỏi chương trình
                        System.exit(0);
                    }
                }
            }

            //Kiểm tra xem ô đã đầy chưa
            if (puzzle.boardFull()) {
                // Hiển thị hộp thoại thông báo
                int option = JOptionPane.showConfirmDialog(this, "Bảng đã đầy, bạn đã thắng. Bạn có muốn chơi lại không?", "Game Over", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Nếu người dùng chọn "Yes", bắt đầu một trò chơi mới
                    frame.startNewGame();
                } else {
                    // Nếu người dùng chọn "No", thoát khỏi chương trình
                    System.exit(0);
                }
            }
        }
    }

    public class NumActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event){
            messageFromNumActionListener(((JButton) event.getSource()).getText());
        }
    }

    public class SudokuPanelMouseAdapter extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent event){
            if(event.getButton() == MouseEvent.BUTTON1){
                int slotWidth = usedWidth/puzzle.getNumColumns();
                int slotHeight = usedHeight/ puzzle.getNumRows();
                currentlySelectedRow = event.getY()/slotHeight;
                currentlySelectedColumn = event.getX() /slotWidth;
                event.getComponent().repaint();
            }
        }
    }
}
