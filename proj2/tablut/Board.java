package tablut;

import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Move.mv;

import java.util.Stack;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.Formatter;
import java.util.Collections;


/**
 * The state of a Tablut Game.
 *
 * @author Mingjie Chen
 */
class Board {

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 9;

    /**
     * The throne (or castle) square and its four surrounding squares..
     */
    static final Square THRONE = sq(4, 4),
            NTHRONE = sq(4, 5),
            STHRONE = sq(4, 3),
            WTHRONE = sq(3, 4),
            ETHRONE = sq(5, 4);

    /**
     * Initial positions of attackers.
     */
    static final Square[] INITIAL_ATTACKERS = {
            sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
            sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
            sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
            sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /**
     * Initial positions of defenders of the king.
     */
    static final Square[] INITIAL_DEFENDERS
            = {NTHRONE, ETHRONE, STHRONE, WTHRONE,
            sq(4, 6), sq(4, 2), sq(2, 4), sq(6, 4)};

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    @SuppressWarnings("unchecked")
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
        _kingSq = model.kingPosition();
        _turn = model.turn();
        HashMap<Square, Piece> newPairs =
                (HashMap<Square, Piece>) model.allPairs().clone();
        _allPairs = newPairs;
        _winner = model.winner();
        _moveLim = model.moveLim();
        Stack<HashMap<Square, Piece>> records =
                (Stack<HashMap<Square, Piece>>) model.records().clone();
        _records = records;
        Stack<String> states = (Stack<String>) model.states().clone();
        _states = states;
        _moveCount = model.moveCount();
        _repeated = model.repeatedPosition();
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        _allPairs = new HashMap<Square, Piece>();
        _records = new Stack<HashMap<Square, Piece>>();
        _states = new Stack<String>();

        _winner = null;
        _turn = BLACK;
        _kingSq = THRONE;
        _repeated = false;
        _moveCount = 0;
        _moveLim = (int) Float.POSITIVE_INFINITY;

        _allPairs.put(THRONE, KING);

        for (Square att : INITIAL_ATTACKERS) {
            _allPairs.put(att, BLACK);
        }
        for (Square def : INITIAL_DEFENDERS) {
            _allPairs.put(def, WHITE);
        }

        for (Square sq : SQUARE_LIST) {
            _allPairs.putIfAbsent(sq, EMPTY);
        }

        _states.push(encodedBoard());
    }

    /**
     * Set the move limit to LIM.  It is an error if 2*LIM <= moveCount().
     *
     * @param n move limit.
     */
    void setMoveLimit(int n) {
        if (2 * n > _moveCount) {
            _moveLim = 2 * n;
        } else {
            throw new NumberFormatException();
        }
    }

    /**
     * Return a Piece representing whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Set turn.
     *
     * @param p side.
     */
    void setTrun(Piece p) {
        _turn = p;
    }

    /**
     * Return the winner in the current position, or null if no winner yet.
     */
    Piece winner() {
        return _winner;
    }

    /**
     * Return moveLim.
     */
    int moveLim() {
        return _moveLim;
    }

    /**
     * Return the HashMap of all pairs.
     */
    HashMap<Square, Piece> allPairs() {
        return _allPairs;
    }

    /**
     * Return _records.
     */
    Stack<HashMap<Square, Piece>> records() {
        return _records;
    }

    /**
     * Return _states.
     */
    Stack<String> states() {
        return _states;
    }

    /**
     * Returns true iff this is a win due to a repeated position.
     */
    boolean repeatedPosition() {
        return _repeated;
    }

    /**
     * Record current position and set winner() next mover if the current
     * position is a repeat.
     */
    private void checkRepeated() {
        String s = _states.pop();
        for (String tmp : _states) {
            if (tmp.equals(s)) {
                _repeated = true;
                if (turn() == WHITE) {
                    _winner = WHITE;
                } else {
                    _winner = BLACK;
                }
            }
        }
        _states.push(s);
    }

    /**
     * Return whether the Throne is hostile.
     */
    private boolean isThroneHostile() {
        int cnt = 0;
        if (get(THRONE) != EMPTY) {
            Square[] near = {NTHRONE, WTHRONE, ETHRONE, STHRONE};
            for (Square sq : near) {
                if (get(sq) == BLACK) {
                    cnt++;
                }
            }
            if (cnt == 3) {
                return true;
            }
            if (cnt == 4) {
                _winner = BLACK;
            }
            return false;
        }
        return true;
    }

    /**
     * Check whether King touch the edge.
     */
    public void checkKingPosition() {
        if (kingPosition().row() == 0
                || kingPosition().col() == 0
                || kingPosition().row() == 8
                || kingPosition().col() == 8) {
            _winner = WHITE;
        }
    }


    /**
     * If the King is captured, the BLACK wins.
     */
    private void checkKingCaptured() {
        int row = kingPosition().row();
        int col = kingPosition().col();

        ArrayList<Square> near = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            if (exists(col + i, row)) {
                near.add(sq(col + i, row));
            }
        }
        for (int j = -1; j <= 1; j += 2) {
            if (exists(col, row + j)) {
                near.add(sq(col, row + j));
            }
        }
        boolean captured = true;

        if (kingPosition() == THRONE
                || kingPosition() == NTHRONE
                || kingPosition() == ETHRONE
                || kingPosition() == WTHRONE
                || kingPosition() == STHRONE) {
            for (Square sq : near) {
                if (get(sq) == WHITE) {
                    captured = false;
                } else if (get(sq) == EMPTY) {
                    if (sq != THRONE) {
                        captured = false;
                    } else {
                        if (!isThroneHostile()) {
                            captured = false;
                        }
                    }
                }
            }
        } else {
            checkCaptured(kingPosition());
            if (get(kingPosition()) == KING) {
                captured = false;
            }
        }

        if (captured) {
            revPut(EMPTY, kingPosition());
            _winner = BLACK;
        }
    }

    /**
     * Check whether captured.
     *
     * @param sq Square to be captured.
     */
    private void checkCaptured(Square sq) {
        Square up = null, down = null, left = null, right = null;
        int col = sq.col();
        int row = sq.row();
        Square throne = null;
        Square antiThrone = null;
        if (exists(col, row + 1)) {
            up = sq(col, row + 1);
        }
        if (exists(col, row - 1)) {
            down = sq(col, row - 1);
        }
        if (exists(col - 1, row)) {
            left = sq(col - 1, row);
        }
        if (exists(col + 1, row)) {
            right = sq(col + 1, row);
        }
        if (up == THRONE) {
            throne = up;
            antiThrone = down;
        } else if (down == THRONE) {
            throne = down;
            antiThrone = up;
        } else if (left == THRONE) {
            throne = left;
            antiThrone = right;
        } else if (right == THRONE) {
            throne = right;
            antiThrone = left;
        }
        if (up != null && down != null
                && get(up).opponent() == get(down).opponent()
                && (get(up) == get(sq).opponent()
                || (get(sq) == BLACK && (get(up) == KING
                || get(down) == KING)))) {
            capture(up, down);
        }
        if (left != null && right != null
                && get(left).opponent() == get(right).opponent()
                && (get(left) == get(sq).opponent()
                || (get(sq) == BLACK && (get(left) == KING
                || get(right) == KING)))) {
            capture(left, right);
        }
        if (throne != null) {
            if (get(THRONE) == KING) {
                if (isThroneHostile() && get(sq) == WHITE
                        && get(antiThrone) == BLACK) {
                    capture(throne, antiThrone);
                } else if (get(sq) == BLACK && get(antiThrone) == WHITE) {
                    capture(throne, antiThrone);
                }
            } else if (get(THRONE) == EMPTY) {
                if (get(antiThrone) == get(sq).opponent()) {
                    capture(throne, antiThrone);
                }
            }
        }
    }

    /**
     * Make Capture.
     *
     * @param sq Square to be captured.
     */
    private void makeCapture(Square sq) {
        int row = sq.row();
        int col = sq.col();

        ArrayList<Square> near = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2) {
            if (exists(col + i, row)) {
                near.add(sq(col + i, row));
            }
        }
        for (int j = -1; j <= 1; j += 2) {
            if (exists(col, row + j)) {
                near.add(sq(col, row + j));
            }
        }

        for (Square s : near) {
            if (get(s) == get(sq).opponent()
                    || (get(s) == BLACK && get(sq) == KING)) {
                checkCaptured(s);
            } else if (get(s) == KING) {
                checkKingCaptured();
            }
        }

    }

    /**
     * Return the number of moves since the initial position that have not been
     * undone.
     */
    int moveCount() {
        return _moveCount;
    }

    /**
     * Return location of the king.
     */
    Square kingPosition() {
        return _kingSq;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return _allPairs.get(sq(col, row));
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        _allPairs.put(s, p);

        if (p == KING) {
            _kingSq = s;
        }
    }

    /**
     * Set square S to P and record for undoing.
     */
    @SuppressWarnings("unchecked")
    final void revPut(Piece p, Square s) {
        put(p, s);
        HashMap<Square, Piece> newPairs =
                (HashMap<Square, Piece>) _allPairs.clone();
        _records.push(newPairs);
        _states.push(encodedBoard());
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /**
     * Return true iff FROM - TO is an unblocked rook move on the current
     * board.  For this to be true, FROM-TO must be a rook move and the
     * squares along it, other than FROM, must be empty.
     */
    boolean isUnblockedMove(Square from, Square to) {
        if (!from.isRookMove(to)) {
            return false;
        }

        int dir = from.direction(to);

        if (dir == 0 || dir == 2) {
            int dis = to.row() - from.row();
            if (dis > 0) {
                for (int i = 1; i <= dis; i++) {
                    if (exists(from.col(), from.row() + i)
                            && _allPairs.get(sq(from.col(),
                            from.row() + i)) != EMPTY) {
                        return false;
                    }
                }
            } else {
                for (int i = -1; i >= dis; i--) {
                    if (exists(from.col(), from.row() + i)
                            && _allPairs.get(sq(from.col(),
                            from.row() + i)) != EMPTY) {
                        return false;
                    }
                }
            }
        } else {
            int dis = to.col() - from.col();
            if (dis > 0) {
                for (int i = 1; i <= dis; i++) {
                    if (exists(from.col() + i, from.row())
                            && _allPairs.get(sq(from.col() + i,
                            from.row())) != EMPTY) {
                        return false;
                    }
                }
            } else {
                for (int i = -1; i >= dis; i--) {
                    if (exists(from.col() + i, from.row())
                            && _allPairs.get(sq(from.col() + i,
                            from.row())) != EMPTY) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        return get(from).side() == _turn
                || (get(from) == KING && _turn == WHITE);
    }

    /**
     * Return true iff FROM-TO is a valid move.
     */
    boolean isLegal(Square from, Square to) {
        if (_allPairs.get(from) != KING && to == THRONE) {
            return false;
        } else {
            return isLegal(from) && isUnblockedMove(from, to);
        }
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /**
     * Move FROM-TO, assuming this is a legal move.
     */
    @SuppressWarnings("unchecked")
    void makeMove(Square from, Square to) {
        assert isLegal(from, to);
        if (_moveCount >= _moveLim) {
            _winner = turn().opponent();
        } else {
            HashMap<Square, Piece> newPairs =
                    (HashMap<Square, Piece>) _allPairs.clone();
            _records.push(newPairs);
            put(get(from), to);
            put(EMPTY, from);
            _moveCount++;
            makeCapture(to);
            _turn = _turn.opponent();
            _states.push(encodedBoard());
            checkRepeated();
            checkKingPosition();
        }
    }

    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /**
     * Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     * SQ0 and the necessary conditions are satisfied.
     */
    private void capture(Square sq0, Square sq2) {
        Square sq1 = sq0.between(sq2);
        revPut(EMPTY, sq1);
    }

    /**
     * Undo one move.  Has no effect on the initial board.
     */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            _winner = null;
            _turn = _turn.opponent();
        }
    }

    /**
     * Remove record of current position in the set of positions encountered,
     * unless it is a repeated position or we are at the first move.
     */
    private void undoPosition() {
        if (_moveCount != 0) {
            HashMap<Square, Piece> tmp = _allPairs;
            _allPairs = _records.pop();
            checkRepeated();
            if (!repeatedPosition()) {
                _repeated = false;
                _moveCount--;
            } else {
                _allPairs = tmp;
            }
        }
    }

    /**
     * Clear the undo stack and board-position counts. Does not modify the
     * current position or win status.
     */
    void clearUndo() {
        _records.clear();
        _moveCount = 0;
    }

    /**
     * Return a new mutable list of all legal moves on the current board for
     * SIDE (ignoring whose turn it is at the moment).
     */
    List<Move> legalMoves(Piece side) {
        HashSet<Square> pl = pieceLocations(side);
        ArrayList<Move> res = new ArrayList<>();

        Iterator<Square> ite = pl.iterator();
        while (ite.hasNext()) {
            Square from = ite.next();
            for (SqList toList : ROOK_SQUARES[from.index()]) {
                for (Square to : toList) {
                    if (isLegal(from, to)) {
                        res.add(mv(from, to));
                    }
                }
            }
        }
        Collections.shuffle(res);
        return res;
    }

    /**
     * Return true iff SIDE has a legal move.
     */
    boolean hasMove(Piece side) {
        return legalMoves(side).size() != 0;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /**
     * Return a text representation of this Board.  If COORDINATES, then row
     * and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /**
     * Return the locations of all pieces on SIDE.
     */
    public HashSet<Square> pieceLocations(Piece side) {
        assert side != EMPTY;

        HashSet<Square> pl = new HashSet<>();
        Iterator ite = _allPairs.keySet().iterator();

        while (ite.hasNext()) {
            Square key = (Square) ite.next();
            if (_allPairs.get(key).equals(side)
                    || (side == WHITE && _allPairs.get(key) == KING)) {
                pl.add(key);
            }
        }

        return pl;
    }

    /**
     * Return the contents of _board in the order of SQUARE_LIST as a sequence
     * of characters: the toString values of the current turn and Pieces.
     */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /**
     * Piece whose turn it is (WHITE or BLACK).
     */
    private Piece _turn;

    /**
     * Cached value of winner on this board, or null if it has not been
     * computed.
     */
    private Piece _winner = null;
    /**
     * Number of (still undone) moves since initial position.
     */
    private int _moveCount;
    /**
     * True when current board is a repeated position (ending the game).
     */
    private boolean _repeated;
    /**
     * Number of moves can take maxly.
     */
    private int _moveLim;
    /**
     * The position of the King.
     */
    private Square _kingSq;
    /**
     * HashMap to store square-piece pairs.
     */
    private HashMap<Square, Piece> _allPairs = new HashMap<>();
    /**
     * Stack to record former boards.
     */
    private Stack<HashMap<Square, Piece>> _records = new Stack<>();
    /**
     * Encoded state.
     */
    private Stack<String> _states = new Stack<>();
}
