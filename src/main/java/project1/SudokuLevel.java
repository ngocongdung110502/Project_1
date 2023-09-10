package project1;

public enum SudokuLevel {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    EXPERT("Expert");

    private final String desc;

    SudokuLevel(String desc){
        this.desc = desc;
    }

    public String toString(){
        return desc;
    }
}
