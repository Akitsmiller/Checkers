/**
 * 
 */
package core;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

	public class CheckersComputerPlayer {
		
	//Method generate computer move	
	public static int[] generateComputerMove(char[][] board) {
	    Random random = new Random();
	    int[] move = new int[4];

	    // Find all available pieces for the computer to move
	    List<int[]> computerPieces = new ArrayList<>();

	    for (int y = 0; y < 8; y++) {
	        for (int x = 0; x < 8; x++) {
	            if (board[y][x] == 'x' || board[y][x] == 'X') {
	                computerPieces.add(new int[]{x, y});
	            }
	        }
	    }
	    if (computerPieces.isEmpty()) {
	        // No computer pieces to move, return an empty move
	        return move;
	    }
	    boolean validMoveFound = false;

	    while (!validMoveFound && !computerPieces.isEmpty()) {
	        // Randomly select a piece to move
	        int[] selectedPiece = computerPieces.get(random.nextInt(computerPieces.size()));
	        int sourceX = selectedPiece[0];
	        int sourceY = selectedPiece[1];

	        // Find all valid target positions for the selected piece
	        List<int[]> validMoves = new ArrayList<>();

	        int[] dx = { -1, 1 };
	        int[] dy = { -1, 1 };

	        for (int dX : dx) {
	            for (int dY : dy) {
	                int targetX = sourceX + dX;
	                int targetY = sourceY + dY;
	                if (isValidMove(board, sourceX, sourceY, targetX, targetY)) {
	                    validMoves.add(new int[]{targetX, targetY});
	                }
	            }
	        }

	        if (!validMoves.isEmpty()) {
	            // Randomly select a valid move for the piece
	            int[] selectedMove = validMoves.get(random.nextInt(validMoves.size()));

	            move[0] = sourceX;
	            move[1] = sourceY;
	            move[2] = selectedMove[0];
	            move[3] = selectedMove[1];
	            
	            validMoveFound = true;
	        } else {
	            // Remove the piece from the list as it has no valid moves
	            computerPieces.remove(selectedPiece);
	        }
	    }
	    return move;
	}
	
	//Method to check if valid move
	private static boolean isValidMove(char[][] board, int sourceX, int sourceY, int targetX, int targetY) {
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

	    // Apply the move temporarily for validation
	    char temp = board[sourceY][sourceX];
	    board[sourceY][sourceX] = '-';
	    board[targetY][targetX] = temp;

	    // Check if the piece becomes a king
	    if ((piece == 'o' && targetY == 7) || (piece == 'x' && targetY == 0)) {
	        board[targetY][targetX] = king;
	    }

	    // Check if a capture move is valid
	    if (dx == 2 && dy == 2) {
	        int capturedX = (sourceX + targetX) / 2;
	        int capturedY = (sourceY + targetY) / 2;

	        char capturedPiece = board[capturedY][capturedX];
	        if (capturedPiece == '-' || capturedPiece == piece || capturedPiece == king) {
	            // Invalid capture move
	            board[sourceY][sourceX] = piece;
	            board[targetY][targetX] = '-';
	            return false;
	        }

	        // Apply the capture by removing the captured piece
	        board[capturedY][capturedX] = '-';
	    }

	    // Revert the temporary move
	    board[sourceY][sourceX] = piece;
	    board[targetY][targetX] = '-';

	    return true;
	}
	}