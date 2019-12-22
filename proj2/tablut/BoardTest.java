package tablut;

import org.junit.Test;

import static org.junit.Assert.*;
import static tablut.Move.mv;

public class BoardTest {

    Board board = new Board();

    @Test
    public void testInitial() {
        myPrint();
        assertEquals(true, board.toString() != null);
    }

    @Test
    public void testSimpleMove() {
        board.makeMove(Square.sq("b5"), Square.sq("b6"));
        myPrint();

        board.makeMove(Square.sq("d5"), Square.sq("d6"));
        myPrint();
        assertEquals(Square.sq("e5"), board.kingPosition());

        board.makeMove(Square.sq(0, 3), Square.sq(3, 3));
        myPrint();

        board.makeMove(mv(Square.sq(4 * 9 + 4), Square.sq("d", "5")));
        myPrint();
        assertEquals(Square.sq("d5"), board.kingPosition());

    }


    @Test(expected = AssertionError.class)
    public void testIllegal() {
        board.makeMove(Square.sq("d9"), Square.sq("e9"));
        myPrint();
    }

    @Test(expected = AssertionError.class)
    public void testIllegalThrone() {
        board.makeMove(Square.sq("d9"), Square.sq("c9"));
        board.makeMove(Square.sq("e4"), Square.sq("e5"));
    }

    @Test
    public void testUndo() {
        myPrint();
        board.makeMove(Square.sq("e8"), Square.sq("f8"));
        board.makeMove(Square.sq("e7"), Square.sq("f7"));
        myPrint();
        board.undo();
        myPrint();
        board.undo();
        myPrint();
    }

    public void myPrint() {
        System.out.println(board.toString());
    }

}
