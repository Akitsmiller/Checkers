package ui;

import java.util.Scanner;
import core.CheckersComputerPlayer;
import javax.swing.SwingUtilities;

public class CheckersTextConsole {
	
	//Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Would you like to play using the Console UI or GUI? Enter 'U' for UI or 'G' for GUI: ");
        String uiChoice = scanner.nextLine().toLowerCase();

        if (uiChoice.equals("u")) {
            initializeConsoleGame(scanner);
        } else if (uiChoice.equals("g")) {
        	SwingUtilities.invokeLater(new Runnable() {
        	    public void run() {
        	        new CheckersGUI();
        	    }
        	});
        } else {
            System.out.println("Invalid choice. Please restart and enter 'U' or 'G'.");
        }
        scanner.close();
    }

    //Method to initialize UI game play
    public static void initializeConsoleGame(Scanner scanner) {
        boolean gameInProgress = true;

        while (gameInProgress) {
            char[][] board = initializeBoard();
            displayBoard(board);

            System.out.print("Enter 'H' to play against a human, or 'C' to play against the computer: ");
            String playerChoice = scanner.nextLine().toLowerCase();

            if (playerChoice.equals("h")) {
                playAgainstPlayer(scanner);
            } else if (playerChoice.equals("c")) {
                playAgainstComputer(scanner);
            } else {
                System.out.println("Invalid choice. Please enter 'H' or 'C'.");
                continue;
            }

            System.out.print("Play again? y/n: ");
            String playAgain = scanner.nextLine().toLowerCase();
            if (!playAgain.equals("y")) {
                gameInProgress = false;
            }
        }

        System.out.println("Thank You for playing!");
    }

    //Method to initialize 2D game board 
    private static char[][] initializeBoard() {
        char[][] board = new char[8][8];
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if ((x + y) % 2 == 0) {
                    // Empty squares have '-'
                    board[y][x] = '-';
                } else {
                    // Set up 'o' and 'x' pieces based on the starting positions
                    if (y < 3) {
                        board[y][x] = 'o'; // 'o' represents player's pieces
                    } else if (y > 4) {
                        board[y][x] = 'x'; // 'x' represents player's pieces
                    } else {
                        board[y][x] = '-'; // Empty squares in the middle
                    }
                }
            }
        }
        //display board
        return board;
    }
	
	//Method to display game board 2D game board
    public static void displayBoard(char[][] board) {
    	//loop through the board
        for (int y = 7; y >= 0; y--) {
        	//print row number
            System.out.print(y + 1 + " ");
            // loop through each column
            for (int x = 0; x < 8; x++) {
            	// print board value and symbol
            	System.out.print("|" + board[y][x]);
            }
            // print closing symbol
            System.out.println("| " + (y + 1));
        }
        // print column headers
        System.out.println("   a b c d e f g h");
    }

    //Method to run game as human vs human
    private static void playAgainstPlayer(Scanner scanner) {
    	// flag to keep game on track
        boolean gameInProgress = true;
        char currentPlayer = 'o'; // 'o' starts first
        //outer loop to run game until game is over
        while (gameInProgress) {
            // Initialize the board
            char[][] board = initializeBoard();
            displayBoard(board);
            // inner loop for turns
            while (true) {
                // whose turn it is
                String currentPlayerSymbol = (currentPlayer == 'o') ? "O" : "X";
                // ask player for target
                System.out.print("Player " + currentPlayerSymbol + ", enter source position (e.g., 'b3'): ");
                // get source position
                String sourcePosition = scanner.nextLine().toLowerCase();
                // ask player for target
                System.out.print("Player " + currentPlayerSymbol + ", enter target position (e.g., 'a4'): ");
                // get target position
                String targetPosition = scanner.nextLine().toLowerCase();
                // covert source and target to coordinates and validate
                int targetX = targetPosition.charAt(0) - 'a';
                // convert target to coordinates and validate 
                int targetY = Integer.parseInt(targetPosition.substring(1)) - 1;
                // Validate the user's input
                if (sourcePosition.length() != 2 || sourcePosition.charAt(0) < 'a' || sourcePosition.charAt(0) > 'h' ||
                    sourcePosition.charAt(1) < '1' || sourcePosition.charAt(1) > '8' ||
                    targetPosition.length() != 2 || targetPosition.charAt(0) < 'a' || targetPosition.charAt(0) > 'h' ||
                    targetPosition.charAt(1) < '1' || targetPosition.charAt(1) > '8') {
                    System.out.println("Invalid positions. Please enter valid positions (e.g., 'b3' to 'a4').");
                    continue; // Restart the loop to get valid input
                }
                // Validate and apply the user's move
                int sourceX = sourcePosition.charAt(0) - 'a'; // Convert the source position to X-coordinate
                int sourceY = Integer.parseInt(sourcePosition.substring(1)) - 1; // Convert the source position to Y-coordinate
                boolean validMove = validateAndApplyMove(board, sourceX, sourceY, targetX, targetY);
                // if valid move update board
                if (validMove) {
                    displayBoard(board);
                    if (checkGameEnd(board)) {
                        break; // exit loop
                    }
                } else {
                    System.out.println("Invalid move. Please try again.");
                }
                // Switch players
                currentPlayer = (currentPlayer == 'o') ? 'x' : 'o';
            }
        }
    }
    
    //Method to run game as human vs computer
    private static void playAgainstComputer(Scanner scanner) {
        boolean gameInProgress = true; // flag to keep game on track
        char currentPlayer = 'o'; // Human player starts
        // outer loop runs until game is over
        while (gameInProgress) {
            // Initialize the board
            char[][] board = initializeBoard(); 
            displayBoard(board); // display board
            // inner loop for turns
            while (true) {
                if (currentPlayer == 'o') {
                    // Human player's turn
                    System.out.print("Enter source position (e.g., 'b3'): ");
                    String sourcePosition = scanner.nextLine().toLowerCase();
                    int sourceX = sourcePosition.charAt(0) - 'a';
                    int sourceY = Integer.parseInt(sourcePosition.substring(1)) - 1;

                    System.out.print("Enter target position (e.g., 'a4'): ");
                    String targetPosition = scanner.nextLine().toLowerCase();
                    int targetX = targetPosition.charAt(0) - 'a';
                    int targetY = Integer.parseInt(targetPosition.substring(1)) - 1;
                    // Validate the user's input
                    if (sourcePosition.length() != 2 || sourcePosition.charAt(0) < 'a' || sourcePosition.charAt(0) > 'h' || 
                        sourcePosition.charAt(1) < '1' || sourcePosition.charAt(1) > '8' || 
                        targetPosition.length() != 2 || targetPosition.charAt(0) < 'a' || targetPosition.charAt(0) > 'h' || 
                        targetPosition.charAt(1) < '1' || targetPosition.charAt(1) > '8') {
                        System.out.println("Invalid positions. Please enter valid positions (e.g., 'b3' to 'a4').");
                        continue; // Restart the loop to get valid input
                    }
                    // Validate and apply the user's move
                    boolean validMove = validateAndApplyMove(board, sourceX, sourceY, targetX, targetY);
                    // if valid move update board
                    if (validMove) {
                        displayBoard(board);
                        if (checkGameEnd(board)) {
                            break;
                        }
                    } else {
                        System.out.println("Invalid move. Please try again.");
                    }
                } else {
                    // Computer player's turn
                    int[] computerMove = CheckersComputerPlayer.generateComputerMove(board);
                    int sourceX = computerMove[0];
                    int sourceY = computerMove[1];
                    int targetX = computerMove[2];
                    int targetY = computerMove[3];
                    // Validate and apply the computer's move
                    boolean validMove = validateAndApplyMove(board, sourceX, sourceY, targetX, targetY);
                    
                    if (validMove) {
                        displayBoard(board);
                        if (checkGameEnd(board)) {
                            break;
                        }
                    } else {
                        System.out.println("Computer made an invalid move.");
                    }
                }
                // Switch players
                currentPlayer = (currentPlayer == 'o') ? 'x' : 'o';
            }
            // Ask to play again
            System.out.print("Play again? y/n: ");
            String playAgain = scanner.nextLine().toLowerCase();
            if (!playAgain.equals("y")) {
                gameInProgress = false;
            }
        }
        System.out.println("Thank You for playing!");
        scanner.close();
    }

    //Method to validate and apply move
    private static boolean validateAndApplyMove(char[][] board, int sourceX, int sourceY, int targetX, int targetY) {
        // Check if source and target positions are within the board boundaries
        if (sourceX < 0 || sourceX >= 8 || sourceY < 0 || sourceY >= 8 ||
            targetX < 0 || targetX >= 8 || targetY < 0 || targetY >= 8) {
            return false;
        }
        // Check if target position is unoccupied
        if (board[targetY][targetX] != '-') {
            return false;
        }
        // Check if move is diagonal and one step forward
        int dx = Math.abs(targetX - sourceX);
        int dy = Math.abs(targetY - sourceY);
        if (dx != 1 || dy != 1) {
            return false;
        }
        // Determine the piece type (x or o)
        char piece = board[sourceY][sourceX];
        char king = Character.toUpperCase(piece);
        // Check if piece is allowed to move in specified direction
        if (piece == 'o' && targetY <= sourceY) {
            return false;
        } else if (piece == 'x' && targetY >= sourceY) {
            return false;
        }
        // Check if piece makes a capture move
        if (dx == 2 && dy == 2) {
            int capturedX = (sourceX + targetX) / 2;
            int capturedY = (sourceY + targetY) / 2;
            char capturedPiece = board[capturedY][capturedX];
            if (capturedPiece == '-' || capturedPiece == piece || capturedPiece == king) {
                return false;
            }
            // Apply capture by removing captured piece
            board[capturedY][capturedX] = '-';
        }
        // Apply the move
        board[targetY][targetX] = piece;
        board[sourceY][sourceX] = '-';
        // Check if the piece becomes a king
        if ((piece == 'o' && targetY == 7) || (piece == 'x' && targetY == 0)) {
            board[targetY][targetX] = king;
        }
        return true;
    }
    
    //Method to check if piece can move
    private static boolean canPieceMove(char[][] board, int x, int y) {
        char piece = board[y][x];
        int[] dx = {-1, 1};
        int[] dy = {1, 1};
        // for movement of x
        if (piece == 'x' || piece == 'X') {
            dy[0] = -1; // For backward movement of X pieces
        }
        // for movement of o
        for (int i = 0; i < 2; i++) {
            int targetX = x + dx[i];
            int targetY = y + dy[i];
            // check if target is within the board boundaries
            if (targetX >= 0 && targetX < 8 && targetY >= 0 && targetY < 8 && board[targetY][targetX] == '-') {
                return true;
            }
        }
        return false;
    }
    
    //Method to check game end
    private static boolean checkGameEnd(char[][] board) {
        int countX = 0;
        int countO = 0;
        // Count number of pieces
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] == 'x' || board[y][x] == 'X') {
                    countX++;
                } else if (board[y][x] == 'o' || board[y][x] == 'O') {
                    countO++;
                }
            }
        }
        // Check if a player has no pieces left
        if (countX == 0) {
            System.out.println("Player O wins!");
            return true;
        } else if (countO == 0) {
            System.out.println("Player X wins!");
            return true;
        }
        // Check if both players are unable to move
        boolean xCanMove = false;
        boolean oCanMove = false;
        // Check if player can move
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board[y][x] == 'x' || board[y][x] == 'X') {
                    if (canPieceMove(board, x, y)) {
                        xCanMove = true;
                        break;
                    }
                } else if (board[y][x] == 'o' || board[y][x] == 'O') {
                    if (canPieceMove(board, x, y)) {
                        oCanMove = true;
                        break;
                    }
                }
            }
        }
        if (!xCanMove) {
            System.out.println("Player X cannot move. Player O wins!");
            return true;
        } else if (!oCanMove) {
            System.out.println("Player O cannot move. Player X wins!");
            return true;
        }

        return false;
    }
    }
