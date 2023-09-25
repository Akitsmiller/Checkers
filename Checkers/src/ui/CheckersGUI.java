/**
 * 
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class CheckersGUI extends JFrame {
	//ID for JFrame class verifies that sender and receiver of objects 
    //Loaded classes for objects compatible to serialization
    private static final long serialVersionUID = 1L;
    //Constant for the size of the board
    private static final int BOARD_SIZE = 8;
    //Flag to indicate turns
    private boolean isBlackTurn = false;
    //Board buttons
    private JButton[][] boardButtons;
    //Icons
    private Icon blackCheckerIcon;
    //Icons
    private Icon redCheckerIcon;
    //Selected button
    private JButton selectedButton = null;
    
    //Main method
    public static void main(String[] args) {
    	//SwingUtilities.invokeLater used to ensure GUI creation is executed
        SwingUtilities.invokeLater(new Runnable() {
        	//The run method will be executed on the Event Dispatch Thread
            public void run() {
            	//Creating a new instance CheckersGUI class
                new CheckersGUI();
            }
        });
    }

    //Constructor
    public CheckersGUI() {
    	//Setting the title
        setTitle("Checkers Game");
        //Setting operation to exit
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Setting layout
        setLayout(new BorderLayout());
        //Icons black and red
        blackCheckerIcon = createIcon(Color.BLACK);
        redCheckerIcon = createIcon(Color.RED);
        //Set up the menu
        initializeMenu();
        //Set up bard buttons
        initializeBoardButtons();
        //Initialize board with checkers
        initializeBoardWithCheckers();
        //Resize the frame
        pack();
        //set location of frame to center of screen
        setLocationRelativeTo(null);
        //Make frame visible
        setVisible(true);
    }

    //Method for Menu
    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        //Adding menu items for selecting the opponent (human or computer)
        JMenu opponentMenu = new JMenu("Select Opponent");
        JMenuItem humanOpponentItem = new JMenuItem("Human");
        JMenuItem computerOpponentItem = new JMenuItem("Computer");

        //Attaching event handlers for the new menu items
        humanOpponentItem.addActionListener(e -> setOpponent(true));
        computerOpponentItem.addActionListener(e -> setOpponent(false));

        //Adding items to the opponent menu
        opponentMenu.add(humanOpponentItem);
        opponentMenu.add(computerOpponentItem);

        //Adding action listeners for the new and exit game options
        newGameItem.addActionListener(e -> newGame());
        exitItem.addActionListener(e -> System.exit(0));

        //Adding menu items to the game menu
        gameMenu.add(newGameItem);
        gameMenu.add(exitItem);

        //Adding the game and opponent menus to the menu bar
        menuBar.add(gameMenu);
        menuBar.add(opponentMenu); 

        //Setting the menu bar for the frame
        setJMenuBar(menuBar);
    }

    //Method for Board
    private void initializeBoardButtons() {
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setPreferredSize(new Dimension(50, 50));
                
                if ((row + col) % 2 == 0) {
                    boardButtons[row][col].setBackground(Color.BLACK);
                } else {
                    boardButtons[row][col].setBackground(Color.WHITE);
                }

                final int finalRow = row;
                final int finalCol = col;
                boardButtons[row][col].addActionListener(e -> handleBoardButtonClick(finalRow, finalCol));
                boardPanel.add(boardButtons[row][col]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    //Method for Icons
    private Icon createIcon(Color color) {
    	//Image with transparency
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        //Create graphics 
        Graphics g = image.getGraphics();
        //Set color
        g.setColor(color);
        //Draw circle
        g.fillOval(5, 5, 20, 20);
        //Return icon
        return new ImageIcon(image);
    }

    //Method for Board with Checkers
    private void initializeBoardWithCheckers() {
    	//Black checkers in starting position 
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
            	//Placing black checkers
                if ((row + col) % 2 != 0) {
                    boardButtons[row][col].setIcon(blackCheckerIcon);
                }
            }
        }
        //Red checkers in starting position
        for (int row = 5; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                //Placing  checkers
            	if ((row + col) % 2 != 0) {
                    boardButtons[row][col].setIcon(redCheckerIcon);
                }
            }
        }
    }
    
    private boolean playAgainstHuman = true;

    //Method to Select opponent
    private void setOpponent(boolean playAgainstHuman) {
        this.playAgainstHuman = playAgainstHuman;
        newGame();  //Start new game each time opponent is changed
    }
    
    //New game Method
    private void newGame() {
    	//Clear the board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boardButtons[row][col].setIcon(null);
            }
        }
        //Initialize board
        initializeBoardWithCheckers();
        //Notify new game
        System.out.println("New game started");
    }

    //Method for selecting buttons
    private Point getSelectedButtonPosition() {
    	//Get selected button
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
            	//Check if button is selected 
                if (boardButtons[r][c] == selectedButton) {
                    return new Point(r, c);
                }
            }
        }
        return null;
    }

    //Method for handing button clicks
    private void handleBoardButtonClick(int row, int col) {
        //Check if button is selected 
        if (selectedButton != null) {
            // Get selected button
            Point selectedPoint = getSelectedButtonPosition();
            int selectedRow = selectedPoint.x;
            int selectedCol = selectedPoint.y;
            //Check player's turn
            if ((isBlackTurn && selectedButton.getIcon() == blackCheckerIcon) || 
                    (!isBlackTurn && selectedButton.getIcon() == redCheckerIcon)) {
                //Check if capture is possible
                if (Math.abs(selectedRow - row) == 2 && Math.abs(selectedCol - col) == 2) {
                    //Find position of captured piece
                    int middleRow = (selectedRow + row) / 2;
                    int middleCol = (selectedCol + col) / 2;
                    //Check if capture is valid
                    if (boardButtons[middleRow][middleCol].getIcon() != null && 
                            boardButtons[middleRow][middleCol].getIcon() != selectedButton.getIcon() && 
                            boardButtons[row][col].getIcon() == null) {
                        //Apply capture
                        boardButtons[row][col].setIcon(selectedButton.getIcon());
                        boardButtons[selectedRow][selectedCol].setIcon(null);
                        boardButtons[middleRow][middleCol].setIcon(null);
                        //Reset selected button and switch turns
                        selectedButton = null;
                        isBlackTurn = !isBlackTurn;
                        //Check if it's computer's turn to play
                        if (!playAgainstHuman && !isBlackTurn) {
                            makeComputerMove();
                        }
                        //Check if game is over
                        checkGameOver();
                        return;
                    }
                    //Check normal move
                } else if (Math.abs(selectedRow - row) == 1 && Math.abs(selectedCol - col) == 1 && 
                        boardButtons[row][col].getIcon() == null) {
                    //Apply move
                    boardButtons[row][col].setIcon(selectedButton.getIcon());
                    boardButtons[selectedRow][selectedCol].setIcon(null);
                    //Reset selected button and switch turns
                    selectedButton = null;
                    isBlackTurn = !isBlackTurn;
                    //Check if it's computer's turn to play
                    if (!playAgainstHuman && !isBlackTurn) {
                        makeComputerMove();
                    }
                }
            }
        }
        //Check if button is selected
        if (boardButtons[row][col].getIcon() != null) {
            selectedButton = boardButtons[row][col];
        }
    }

    //Method for checking valid moves
    private boolean hasValidMoves() {
        Icon currentPlayerIcon = isBlackTurn ? blackCheckerIcon : redCheckerIcon;
        Icon opponentIcon = isBlackTurn ? redCheckerIcon : blackCheckerIcon;

        for (int row = 0; row < boardButtons.length; row++) {
            for (int col = 0; col < boardButtons[row].length; col++) {
                if (boardButtons[row][col].getIcon() == currentPlayerIcon) {
                    //Check moves and captures
                    for (int dRow = -2; dRow <= 2; dRow++) {
                        for (int dCol = -2; dCol <= 2; dCol++) {
                            if (Math.abs(dRow) == Math.abs(dCol)) {
                                int newRow = row + dRow;
                                int newCol = col + dCol;

                                if (newRow >= 0 && newRow < boardButtons.length && newCol >= 0 && newCol < boardButtons[0].length) {
                                    //Check for valid capture
                                    if (Math.abs(dRow) == 2) {
                                        int middleRow = row + dRow / 2;
                                        int middleCol = col + dCol / 2;
                                        if (boardButtons[middleRow][middleCol].getIcon() == opponentIcon && boardButtons[newRow][newCol].getIcon() == null) {
                                            return true;
                                        }
                                    }
                                    //Check for valid move
                                    else if (Math.abs(dRow) == 1 && boardButtons[newRow][newCol].getIcon() == null) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
    
    //Method to end game
    private void checkGameOver() {
        int blackCheckersCount = 0;
        int redCheckersCount = 0;

        //Count checkers for each player
        for (int row = 0; row < boardButtons.length; row++) {
            for (int col = 0; col < boardButtons[row].length; col++) {
                if (boardButtons[row][col].getIcon() == blackCheckerIcon) {
                    blackCheckersCount++;
                } else if (boardButtons[row][col].getIcon() == redCheckerIcon) {
                    redCheckersCount++;
                }
            }
        }

        boolean hasValidMoves = hasValidMoves();

        //Determine outcome based pieces and valid moves
        if (blackCheckersCount == 0 || redCheckersCount == 0 || !hasValidMoves) {
            String message;
            if (blackCheckersCount == 0 || (!hasValidMoves && !isBlackTurn)) {
                message = "Red wins!";
            } else if (redCheckersCount == 0 || (!hasValidMoves && isBlackTurn)) {
                message = "Black wins!";
            } else {
                message = "It's a draw!";
            }

            JOptionPane.showMessageDialog(this, message);
            resetGame(); //Reset the game 
        }
    }
    
    //Method resets game
    private void resetGame() {
		newGame();
	}

    //Method for computer moves
    private void makeComputerMove() {
        // List to store all valid moves
    	List<Move> validMoves = new ArrayList<>();
        // Loop through all squares on the board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // If the square contains a computer's checker
                if (boardButtons[row][col].getIcon() == redCheckerIcon) {
                    // Loop to check all possible moves for the current checker
                    for (int dRow = -2; dRow <= 2; dRow++) {
                        for (int dCol = -2; dCol <= 2; dCol++) {
                            // If the move is a valid move or a valid capture
                            if (Math.abs(dRow) == Math.abs(dCol)) {
                                int newRow = row + dRow;
                                int newCol = col + dCol;
                                // Checking bounds and if the destination square is empty
                                if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE && boardButtons[newRow][newCol].getIcon() == null) {
                                    // If the move is a valid capture
                                    if (Math.abs(dRow) == 2) {
                                        int middleRow = row + dRow / 2;
                                        int middleCol = col + dCol / 2;
                                        if (boardButtons[middleRow][middleCol].getIcon() == blackCheckerIcon) {
                                            validMoves.add(new Move(row, col, newRow, newCol));
                                        }
                                    }
                                    // If the move is a simple one-step move
                                    else if (Math.abs(dRow) == 1) {
                                        validMoves.add(new Move(row, col, newRow, newCol));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // If there are any valid moves
        if (!validMoves.isEmpty()) {
            // Randomly select a move from the list of valid moves
            Random rand = new Random();
            Move selectedMove = validMoves.get(rand.nextInt(validMoves.size()));            
            // Make the selected move
            boardButtons[selectedMove.newRow][selectedMove.newCol].setIcon(redCheckerIcon);
            boardButtons[selectedMove.row][selectedMove.col].setIcon(null);            
            // If it was a capture, remove the captured piece
            if (Math.abs(selectedMove.newRow - selectedMove.row) == 2) {
                int middleRow = (selectedMove.newRow + selectedMove.row) / 2;
                int middleCol = (selectedMove.newCol + selectedMove.col) / 2;
                boardButtons[middleRow][middleCol].setIcon(null);
            }
            // Switch turns
            isBlackTurn = !isBlackTurn;
        }
        // After making a move, check if the game is over
        checkGameOver();
    }

    //Class to represent a move
    class Move {
        int row, col, newRow, newCol;

        Move(int row, int col, int newRow, int newCol) {
            this.row = row;
            this.col = col;
            this.newRow = newRow;
            this.newCol = newCol;
        }
    }
}
