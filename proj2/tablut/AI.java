package tablut;

import java.util.List;

import static java.lang.Math.*;

/** import static tablut.Square.sq;
 * import static tablut.Board.THRONE;
 */
import static tablut.Piece.*;

/**
 * A Player that automatically generates moves.
 *
 * @author Mingjie Chen
 */
class AI extends Player {

    /**
     * A position-score magnitude indicating a win (for white if positive,
     * black if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /**
     * A position-score magnitude indicating a forced win in a subsequent
     * move.  This differs from WINNING_VALUE to avoid putting off wins.
     */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;

    /**
     * A new AI with no piece or controller (intended to produce
     * a template).
     */
    AI() {
        this(null, null);
    }

    /**
     * A new AI playing PIECE under control of CONTROLLER.
     */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {

        String s = findSillyMove().toString();
        return s;
    }

    @Override
    boolean isManual() {
        return false;
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board();
        b.copy(board());
        _lastFoundMove = null;
        if (_myPiece == BLACK) {
            findMove(b, maxDepth(b), true, 1, INFTY, -INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, INFTY, -INFTY);
        }
        return _lastFoundMove;
    }

    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastMoveFound.
     * <p>
     * Return a best move for minimizing player from POSN, searching
     * to depth DEPTH. Any move with value <= ALPHA is also
     * "good enough".
     */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (board.winner() != null) {
            return -sense * INFTY;
        }
        if (depth == 0) {
            return simpleFindMove(board, sense, alpha, beta);
        }

        int bestSoFar = -sense * INFTY;
        for (Move M : board.legalMoves(board.turn())) {
            Board next = new Board();
            next.copy(board);
            next.makeMove(M);
            int response = findMove(next, depth - 1,
                    false, -sense, alpha, beta);
            if (sense == 1) {
                if (response >= bestSoFar) {
                    bestSoFar = response;
                    if (saveMove) {
                        _lastFoundMove = M;
                    }
                    alpha = max(alpha, response);
                }
            } else if (sense == -1) {
                if (response <= bestSoFar) {
                    bestSoFar = response;
                    if (saveMove) {
                        _lastFoundMove = M;
                    }
                    beta = min(beta, response);
                }
            }
            if (beta <= alpha) {
                break;
            }
        }
        return bestSoFar;
    }

    /**
     * @param board board
     * @param sense sense
     * @param alpha alpha
     * @param beta  beta
     * @return score
     */
    private int simpleFindMove(Board board, int sense, int alpha, int beta) {

        if (board.winner() == BLACK) {
            return INFTY;
        } else if (board.winner() == WHITE) {
            return -INFTY;
        }

        int bestSoFar = -sense * INFTY;
        for (Move M : board.legalMoves(board.turn())) {
            Board next = new Board();
            next.copy(board);
            next.makeMove(M);
            if (sense == 1) {
                if (staticScore(next) >= bestSoFar) {
                    bestSoFar = staticScore(next);
                    alpha = max(alpha, staticScore(next));
                }
            } else if (sense == -1) {
                if (staticScore(next) <= bestSoFar) {
                    bestSoFar = staticScore(next);
                    beta = min(beta, staticScore(next));
                }
            }
            if (beta <= alpha) {
                break;
            }
        }
        return bestSoFar;
    }

    /**
     * Find the silly move without game tree.
     *
     * @return Move silly move.
     */
    private Move findSillyMove() {
        List<Move> moves = board().legalMoves(board().turn());
        int bestScore = staticScore(board());
        Move res = moves.get(0);
        for (Move m : moves) {
            Board b = new Board();
            b.copy(board());
            b.makeMove(m);
            if (_myPiece == BLACK
                    && staticScore(b) > bestScore) {
                res = m;
                bestScore = staticScore(b);
            } else if (_myPiece == WHITE
                    && staticScore(b) < bestScore) {
                res = m;
                bestScore = staticScore(b);
            }
        }
        return res;
    }

    /**
     * Return a heuristically determined maximum search depth
     * based on characteristics of BOARD.
     */
    private static int maxDepth(Board board) {
        return min(4, board.moveLim());
    }

    /**
     * Return a heuristic value for BOARD.
     * White wants to minimize and BLACK wants to maximize it.
     */
    private int staticScore(Board board) {
        Square king = board.kingPosition();
        int kingLive = 0;
        if (board.get(board.kingPosition()) == EMPTY) {
            kingLive = 1000;
        }
        return min(min(king.col(), king.row()),
                min(8 - king.col(), 8 - king.row()))
                + board.pieceLocations(BLACK).size()
                - 2 * board.pieceLocations(WHITE).size()
                + kingLive;
    }

}
