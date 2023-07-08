package project.cs.gui;

import edu.virginia.cs.wordle.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordleController {
    Wordle wordle = new WordleImplementation();
    @FXML
    GridPane wordleGuesses = new GridPane();
    @FXML
    GridPane bottomHalf = new GridPane();

    private TextField[][] fields = new TextField[6][5];
    TextArea textArea = new TextArea();
    Button playAgain = new Button();
    Button quit = new Button();
    public boolean fakeWord = false;
    public boolean hasEnded = false;
    EventHandler<MouseEvent> handler = MouseEvent::consume;
    @FXML
    public void initialize(){
        createGrid();
    }

    public void createGrid(){
        bottomHalf.setHgap(3);
        bottomHalf.setVgap(3);
        wordleGuesses.setHgap(3);
        wordleGuesses.setVgap(3);
        wordleGuesses.setPadding(new Insets(40));
        wordleGuesses.setAlignment(Pos.CENTER);
        wordleGuesses.addEventFilter(KeyEvent.ANY, this::handleTyping);

        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                fields[i][j] = new TextField();
                fields[i][j].setPrefWidth(30.0);
                fields[i][j].setStyle("-fx-background-color: white;");
                fields[i][j].setStyle("-fx-border-color: grey;");
                fields[i][j].addEventFilter(MouseEvent.ANY, handler);
                wordleGuesses.add(fields[i][j], j, i);
            }
        }
    }

    public void getAndHandleUserGuess(Wordle wordle, int row){
        bottomHalf.getChildren().remove(textArea);
        bottomHalf.add(textArea, 0, 0);
        textArea.addEventFilter(MouseEvent.ANY, handler);
        String guess = ((TextField) getNodeFromGridPane(wordleGuesses, 0, row)).getText() +
                ((TextField) getNodeFromGridPane(wordleGuesses, 1, row)).getText() +
                ((TextField) getNodeFromGridPane(wordleGuesses, 2, row)).getText() +
                ((TextField) getNodeFromGridPane(wordleGuesses, 3, row)).getText() +
                ((TextField) getNodeFromGridPane(wordleGuesses, 4, row)).getText();

        try {
            LetterResult[] result = wordle.submitGuess(guess); //makes a result array each with colors denoting letter status
            int greenCounter = 0;
            for(int col = 0; col < 5; col++){
                TextField field = fields[row][col];
                if(result[col].equals(LetterResult.YELLOW)){
                    field.setStyle("-fx-text-fill: #FFFFFF; -fx-control-inner-background: #FABD02;-fx-border-color: black;");

                }
                if(result[col].equals(LetterResult.GRAY)){
                    field.setStyle("-fx-text-fill: #FFFFFF; -fx-control-inner-background: #C0C0C0;-fx-border-color: black;");

                }
                if(result[col].equals(LetterResult.GREEN)){
                    greenCounter++;
                    field.setStyle("-fx-text-fill: #FFFFFF; -fx-control-inner-background: #3CB043;-fx-border-color: black;");

                }

            }
            if(row==5 || greenCounter==5) {
                textArea.setEditable(true);
                textArea.setPrefSize(240, 15);
                textArea.setStyle("-fx-text-fill: #000000;");
                bottomHalf.add(playAgain, 0, 2);
                playAgain.setPrefSize(240, 10);
                playAgain.setText("Play Again");
                bottomHalf.add(quit, 0, 3);
                quit.setPrefSize(240, 10);
                quit.setText("Quit");
                if(greenCounter == 5) {
                    textArea.setText("You Won!");
                    hasEnded = true;
                } else {
                    textArea.setText("You Lost! The correct word was: " + wordle.getAnswer());
                    hasEnded = true;
                }
                textArea.setEditable(false);
            }
        }
        catch (IllegalWordException e) {
            textArea.setEditable(true);
            textArea.setText(guess + " is not a valid word!");
            fakeWord = true;
            for(int i=4; i>=0; i--){
                fields[row][i].clear();
            }
            textArea.setEditable(false);
        }
        EventHandler<ActionEvent> eventPlayAgain = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Stage stage = new Stage();
                WordleApplication newGame = new WordleApplication();
                try {
                    Stage now = (Stage) playAgain.getScene().getWindow();
                    now.close();
                    newGame.start(stage);
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        EventHandler<ActionEvent> eventQuit = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Platform.exit();
            }
        };
        playAgain.setOnAction(eventPlayAgain);
        quit.setOnAction(eventQuit);

    }

    private void handleTyping(KeyEvent event){
        Node source = (Node) event.getSource();
        Node focused = source.getScene().getFocusOwner();
        if(focused.getParent() == source){
            int row = GridPane.getRowIndex(focused);
            int col = GridPane.getColumnIndex(focused);
            if(event.getCode().equals(KeyCode.BACK_SPACE)){
                if(event.getEventType() == KeyEvent.KEY_RELEASED) {
                    if (fields[row][col].getText().length() == 1) {
                        fields[row][Math.max(0, col)].clear();
                    }
                    else {
                        fields[row][Math.max(0, col -1)].clear();
                        fields[row][Math.max(0, col - 1)].requestFocus();
                    }
                }
            }
            else if(event.getCode().equals(KeyCode.ENTER)) {
                if (col == 4 && fields[row][col].getText().length() == 1 && event.getEventType() == KeyEvent.KEY_RELEASED) {
                    getAndHandleUserGuess(wordle, row);
                    if(hasEnded){
                        playAgain.requestFocus();
                    }
                    else if(fakeWord){
                        fields[row][0].requestFocus();
                        fakeWord = false;
                    }
                    else if (row != 5) {
                        fields[Math.min(5, row + 1)][0].requestFocus();
                    }
                }
                else{}
            }
            else if(event.getCode().isLetterKey()){
                if(fields[row][col].getText().length() == 1){
                    fields[row][Math.min(4, col+1)].requestFocus();
                    fields[row][Math.min(4, col+1)].positionCaret(1);
                }
                else if(fields[row][col].getText().length() == 1 && col==4){
                    fields[row][col].deleteNextChar();
                    fields[row][col].positionCaret(1);
                }
                else{
                    fields[row][col].setText(event.getText());
                    fields[row][col].positionCaret(1);
                    fields[row][col].setFont(Font.font ("Arial Black", 10));
                    String s = fields[row][col].getText().substring(0, 1);
                    fields[row][col].setText(s.toUpperCase());
                    fields[row][col].setStyle("-fx-border-color: black;");
                }
            }
            event.consume();
        }
    }

    //Citation: https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}