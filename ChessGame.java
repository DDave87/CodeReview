public class ChessGame {
    private char[][] board;

    public ChessGame() {
        initializeBoard();
    }

    public void initializeBoard() {
        board = new char[8][8];
        // Initialize the board with pieces in their initial positions
        // 'W' for white pieces, 'B' for black pieces
        // 'P' for Pawn, 'R' for Rook, 'N' for Knight, 'B' for Bishop, 'Q' for Queen, 'K' for King
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = ' '; // Set all squares to empty
            }
        }
        // Initialize white pieces
        board[0][0] = 'R';
        board[0][1] = 'N';
        board[0][2] = 'B';
        board[0][3] = 'Q';
        board[0][4] = 'K';
        board[0][5] = 'B';
        board[0][6] = 'N';
        board[0][7] = 'R';
        for (int i = 0; i < 8; i++) {
            board[1][i] = 'P';
        }

        // Initialize black pieces
        board[7][0] = 'r';
        board[7][1] = 'n';
        board[7][2] = 'b';
        board[7][3] = 'q';
        board[7][4] = 'k';
        board[7][5] = 'b';
        board[7][6] = 'n';
        board[7][7] = 'r';
        for (int i = 0; i < 8; i++) {
            board[6][i] = 'p';
        }
    }

    public boolean isValidDiagonalMove(int fromRow, int fromCol, int toRow, int toCol, char targetPiece) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        if (rowDiff == colDiff) {
            int rowDirection = (toRow > fromRow) ? 1 : -1;
            int colDirection = (toCol > fromCol) ? 1 : -1;
            int row = fromRow + rowDirection;
            int col = fromCol + colDirection;

            while (row != toRow && col != toCol) {
                if (board[row][col] != ' ') {
                    return false; // Obstacle in the way
                }
                row += rowDirection;
                col += colDirection;
            }

            // Check the destination square for an enemy piece or empty
            return targetPiece == ' ' ||
                    (Character.isLowerCase(board[fromRow][fromCol]) &&
                            Character.isUpperCase(targetPiece)) ||
                    (Character.isUpperCase(board[fromRow][fromCol]) &&
                            Character.isLowerCase(targetPiece));
        }

        return false;
    }

    public boolean isValidBackMove(int fromRow, int fromCol, int toRow, int toCol, char targetPiece) {
        if (fromRow == toRow || fromCol == toCol) {
            int start = Math.min(fromRow, toRow);
            int end = Math.max(fromRow, toRow);
            int step = (toRow > fromRow || toCol > fromCol) ? 1 : -1;

            for (int i = start + step; i < end; i += step) {
                if (board[i][fromCol] != ' ') {
                    return false; // Obstacle in the way
                }
            }

            // Check the destination square for an enemy piece or empty
            return targetPiece == ' ' ||
                    (Character.isLowerCase(board[fromRow][fromCol]) &&
                            Character.isUpperCase(targetPiece)) ||
                    (Character.isUpperCase(board[fromRow][fromCol]) &&
                            Character.isLowerCase(targetPiece));
        }

        return false;
    }
    public boolean isValidPawnMove(int fromRow, int fromCol, int toRow, int toCol, char targetPiece) {
        char piece = board[fromRow][fromCol];
        int direction = (Character.isUpperCase(piece)) ? -1 : 1; // Direction for white or black pawn

        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);

        // Normal move (one step forward)
        if (colDiff == 0 && rowDiff == direction) {
            return targetPiece == ' '; // Destination square is empty
        }

        // Initial double step
        if (colDiff == 0 && rowDiff == 2 * direction && (fromRow == 1 || fromRow == 6) ) {
            if(board[toRow][toCol] == ' '){
            }
            return board[toRow][toCol] == ' ';
        }
        // Capturing diagonally
        if (colDiff == 1 && rowDiff == direction) {
            return Character.isLowerCase(targetPiece); // Destination has an enemy piece
        }
        // En passant capture
        if (colDiff == 1 && rowDiff == direction &&
                targetPiece == ' ' && board[fromRow][toCol] != ' ') {
            char lastMovedPiece = board[fromRow][toCol];
            if (lastMovedPiece == 'P' || lastMovedPiece == 'p') {
                // Add additional conditions for en passant
                return true;
            }
        }
        return false; // Invalid move
    }
    public boolean isValidMove(String move) {
        String[] parts = move.split(" ");
        String from = parts[0];
        String to = parts[1];

        int fromRow = 8 - (from.charAt(1) - '0');
        int fromCol = from.charAt(0) - 'a';

        int toRow = 8 - (to.charAt(1) - '0');
        int toCol = to.charAt(0) - 'a';

        char piece = board[fromRow][fromCol];
        char targetPiece = board[toRow][toCol];

        switch (piece) {
            case 'P':
            case 'p':
                return isValidPawnMove(fromRow, fromCol, toRow, toCol, targetPiece);
            case 'R':
            case 'r':
                return isValidBackMove(fromRow, fromCol, toRow, toCol, targetPiece) ||
                        isValidStraightMove(fromRow, fromCol, toRow, toCol, targetPiece);
            case 'N':
            case 'n':
                return isValidKnightMove(fromRow, fromCol, toRow, toCol, targetPiece);
            case 'B':
            case 'b':
                return isValidDiagonalMove(fromRow, fromCol, toRow, toCol, targetPiece);
            case 'Q':
            case 'q':
                return isValidDiagonalMove(fromRow, fromCol, toRow, toCol, targetPiece) ||
                        isValidBackMove(fromRow, fromCol, toRow, toCol, targetPiece) ||
                        isValidStraightMove(fromRow, fromCol, toRow, toCol, targetPiece);
            default:
                return false; // Invalid piece
        }
    }

    public void makeMove(String move) {
        String[] parts = move.split(" ");
        String from = parts[0];
        String to = parts[1];

        int fromRow = 8 - (from.charAt(1) - '0');
        int fromCol = from.charAt(0) - 'a';

        int toRow = 8 - (to.charAt(1) - '0');
        int toCol = to.charAt(0) - 'a';

        char piece = board[fromRow][fromCol];
        char targetPiece = board[toRow][toCol];

        boolean validMove = false;

        switch (piece) {
            case 'P':
            case 'p':
                validMove = isValidPawnMove(fromRow, fromCol, toRow, toCol, targetPiece);
                break;
            case 'R':
            case 'r':
                validMove = isValidBackMove(fromRow, fromCol, toRow, toCol, targetPiece) ||
                        isValidStraightMove(fromRow, fromCol, toRow, toCol, targetPiece);
                break;
            case 'N':
            case 'n':
                validMove = isValidKnightMove(fromRow, fromCol, toRow, toCol, targetPiece);
                break;
            case 'B':
            case 'b':
                validMove = isValidDiagonalMove(fromRow, fromCol, toRow, toCol, targetPiece);
                break;
            case 'Q':
            case 'q':
                validMove = isValidDiagonalMove(fromRow, fromCol, toRow, toCol, targetPiece) ||
                        isValidBackMove(fromRow, fromCol, toRow, toCol, targetPiece) ||
                        isValidStraightMove(fromRow, fromCol, toRow, toCol, targetPiece);
                break;
            default:
                validMove = false; // Invalid piece
        }

        if (validMove) {
            board[toRow][toCol] = piece;
            board[fromRow][fromCol] = ' ';
            // Other logic for special moves like castling, en passant, etc.
        }
    }

    public boolean isValidStraightMove(int fromRow, int fromCol, int toRow, int toCol, char targetPiece) {
        if (fromRow == toRow || fromCol == toCol) {
            int start = Math.min(fromRow, toRow);
            int end = Math.max(fromRow, toRow);
            int step = (toRow > fromRow || toCol > fromCol) ? 1 : -1;

            for (int i = start + step; i < end; i += step) {
                if (board[i][fromCol] != ' ') {
                    return false; // Obstacle in the way
                }
            }

            // Check the destination square for an enemy piece or empty
            return targetPiece == ' ' ||
                    (Character.isLowerCase(board[fromRow][fromCol]) &&
                            Character.isUpperCase(targetPiece)) ||
                    (Character.isUpperCase(board[fromRow][fromCol]) &&
                            Character.isLowerCase(targetPiece));
        }

        return false;
    }

    public boolean isValidKnightMove(int fromRow, int fromCol, int toRow, int toCol, char targetPiece) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }


    // Implement isValidPawnMove, isValidDiagonalMove, and isValidBackMove methods here

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();


        String[] moves = {"e2 e4", "e7 e5", "f1 c4", "b8 c6", "d1 h5", "g8 f6", "h5 f7", "f8 f7", "g7 f7", "h8 f7", "d8 f7", "c6 f7", "c4 f7", "h8 g8", "f2 f4", "e5 f4", "f7 e8"};

        for (String move : moves) {
            if (game.isValidMove(move)) {
                game.makeMove(move);
            } else {
                System.out.println("Invalid move: " + move);
            }
            game.printBoard();
        }
    }
}
