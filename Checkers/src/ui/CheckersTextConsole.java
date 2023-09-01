package ui;

import java.util.Scanner;

public class CheckersTextConsole {
	
	public static void displayBoard(char [][] board) {
		
		for (int y = 7; y >= 0; y--) {
            System.out.print(y + 1 + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print("|" + board[y][x]);
            }
            System.out.println("| " + (y + 1));
        }
        System.out.println("   a b c d e f g h");
    }

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
			//Initialize loop
        boolean gameInProgress = true;
        // Create loop
        while (gameInProgress) {
            while (gameInProgress) {
				//board display with empty spaces
				char [][] board = new char[8][8];
				for (int y = 0; y < 8; y++) {
					for (int x = 0; x < 8; x++) {
						board[y][x] = '-';
					}
				}
				//place pieces 
	            board[0][1] = 'o';
	            board[0][3] = 'o';
	            board[0][5] = 'o';
	            board[0][7] = 'o';

	            board[1][0] = 'o';
	            board[1][2] = 'o';
	            board[1][4] = 'o';
	            board[1][6] = 'o';

	            board[2][1] = 'o';
	            board[2][3] = 'o';
	            board[2][5] = 'o';
	            board[2][7] = 'o';

	            board[5][0] = 'x';
	            board[5][2] = 'x';
	            board[5][4] = 'x';
	            board[5][6] = 'x';

	            board[6][1] = 'x';
	            board[6][3] = 'x';
	            board[6][5] = 'x';
	            board[6][7] = 'x';

	            board[7][0] = 'x';
	            board[7][2] = 'x';
	            board[7][4] = 'x';
	            board[7][6] = 'x';

				//board display
				displayBoard(board);
		        
				while (true) {
		            // Get source and target positions from user input
		            System.out.print("Enter source position (e.g., 'b3'): ");
		            String sourcePosition = scanner.next().toLowerCase();
		            int sourceX = sourcePosition.charAt(0) - 'a';
		            int sourceY = Integer.parseInt(sourcePosition.substring(1)) - 1;

		            System.out.print("Enter target position (e.g., 'a4'): ");
		            String targetPosition = scanner.next().toLowerCase();
		            int targetX = targetPosition.charAt(0) - 'a';
		            int targetY = Integer.parseInt(targetPosition.substring(1)) - 1;

		            scanner.nextLine(); // Consume the newline character

		            // Validate and apply move
		            boolean validMove = validateAndApplyMove(board, sourceX, sourceY, targetX, targetY);

		            if (validMove) {
		                displayBoard(board);
		                if (checkGameEnd(board)) {
		                    break;
		                }
		            } else {
		                System.out.println("Invalid move. Please try again.");
		            }
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
		}

		private static boolean validateAndApplyMove(char[][] board, int sourceX, int sourceY, int targetX, int targetY) {
		    // Check if source and target positions are within the board boundaries
		    if (sourceX < 0 || sourceX >= 8 || sourceY < 0 || sourceY >= 8 ||
		        targetX < 0 || targetX >= 8 || targetY < 0 || targetY >= 8) {
		        return false;
		    }

		    // Check if the target position is unoccupied
		    if (board[targetY][targetX] != '-') {
		        return false;
		    }

		    // Check if the move is diagonal and one step forward
		    int dx = Math.abs(targetX - sourceX);
		    int dy = Math.abs(targetY - sourceY);
		    if (dx != 1 || dy != 1) {
		        return false;
		    }

		    // Determine the piece type (x or o)
		    char piece = board[sourceY][sourceX];
		    char king = Character.toUpperCase(piece);

		    // Check if the piece is allowed to move in the specified direction
		    if (piece == 'o' && targetY <= sourceY) {
		        return false;
		    } else if (piece == 'x' && targetY >= sourceY) {
		        return false;
		    }

		    // Check if the piece is making a capture move
		    if (dx == 2 && dy == 2) {
		        int capturedX = (sourceX + targetX) / 2;
		        int capturedY = (sourceY + targetY) / 2;

		        char capturedPiece = board[capturedY][capturedX];
		        if (capturedPiece == '-' || capturedPiece == piece || capturedPiece == king) {
		            return false;
		        }

		        // Apply the capture by removing the captured piece
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

		private static boolean checkGameEnd(char[][] board) {
		    int countX = 0;
		    int countO = 0;

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

		private static boolean canPieceMove(char[][] board, int x, int y) {
		    char piece = board[y][x];
		    int[] dx = {-1, 1};
		    int[] dy = {1, 1};

		    if (piece == 'x' || piece == 'X') {
		        dy[0] = -1; // For backward movement of X pieces
		    }

		    for (int i = 0; i < 2; i++) {
		        int targetX = x + dx[i];
		        int targetY = y + dy[i];

		        if (targetX >= 0 && targetX < 8 && targetY >= 0 && targetY < 8 && board[targetY][targetX] == '-') {
		            return true;
		        }
		    }

		    return false;
		}
		
}
