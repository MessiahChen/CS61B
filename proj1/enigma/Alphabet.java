package enigma;

import java.util.ArrayList;

/**
 * An alphabet of encodable characters.  Provides a mapping from characters
 * to and from indices into the alphabet.
 *
 * @author Mingjie Chen
 */
class Alphabet {

    /**
     * A new alphabet containing CHARS.  Character number #k has index
     * K (numbering from 0). No character may be duplicated.
     */
    Alphabet(String chars) {
        char[] cArray = chars.toCharArray();
        for (char c : cArray) {
            alphaList().add(c);
        }
    }

    /**
     * A default alphabet of all upper-case characters.
     */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Get _alphaList.
     * @return ArrayList
     */
    ArrayList<Character> alphaList() {
        return _alphaList;
    }

    /**
     * Returns the size of the alphabet.
     */
    int size() {
        return alphaList().size();
    }

    /**
     * Returns true if preprocess(CH) is in this alphabet.
     */
    boolean contains(char ch) {
        return alphaList().contains(ch);
    }

    /**
     * Returns character number INDEX in the alphabet, where
     * 0 <= INDEX < size().
     */
    char toChar(int index) {
        return alphaList().get(index);
    }

    /**
     * Returns the index of character preprocess(CH), which must be in
     * the alphabet. This is the inverse of toChar().
     */
    int toInt(char ch) {
        return alphaList().indexOf(ch);
    }

    /**
     * ArrayList to store each sub-cycle.
     */
    private ArrayList<Character> _alphaList = new ArrayList<>();

}
