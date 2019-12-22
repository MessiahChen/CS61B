package enigma;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Mingjie Chen
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        this._alphabet = alphabet;
        this._cycles = cycles;
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    public void addCycle(String cycle) {
        this._cycles += cycle;
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return this._alphabet.size();
    }

    /**
     * Split the _cycles by parentheses.
     * Throw enigma exception if input format is not valid
     *
     * @param cycles cycles of this rotor
     * @return ArrayList<String>
     */
    static ArrayList<String> splitCycles(String cycles) {
        ArrayList<String> res = new ArrayList<>();
        if (cycles.contains(" ")) {
            cycles = cycles.replaceAll(" ", "");
        }
        if (!Pattern.matches("(\\(([A-Z]*)|([a-z]*)|([0-9]*)\\))*", cycles)) {
            throw new EnigmaException("WRONG CYCLES FORMAT");
        } else {
            String[] split0 = cycles.split("\\)\\(");
            for (String s : split0) {
                s = s.replaceAll("\\(", "");
                s = s.replaceAll("\\)", "");
                res.add(s);
            }
        }
        return res;
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        p = wrap(p);
        char in = _alphabet.toChar(p);
        char out = permute(in);
        int res = _alphabet.toInt(out);
        return res;
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        c = wrap(c);
        char in = _alphabet.toChar(c);
        char out = invert(in);
        int res = _alphabet.toInt(out);
        return res;
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        ArrayList<String> cycles = splitCycles(_cycles);
        for (String cycle : cycles) {
            if (cycle.contains(String.valueOf(p))) {
                int index = cycle.indexOf(String.valueOf(p));
                if (index != cycle.length() - 1) {
                    p = cycle.charAt(index + 1);
                } else {
                    p = cycle.charAt(0);
                }
            }
        }
        return p;
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        ArrayList<String> cycles = splitCycles(_cycles);
        for (String cycle : cycles) {
            if (cycle.contains(String.valueOf(c))) {
                int index = cycle.indexOf(String.valueOf(c));
                if (index != 0) {
                    c = cycle.charAt(index - 1);
                } else {
                    c = cycle.charAt(cycle.length() - 1);
                }
            }
        }
        return c;
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        ArrayList<String> arrlist = splitCycles(_cycles);
        for (String s : arrlist) {
            if (s.length() == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;
    /**
     * The cycles as a String.
     */
    private String _cycles;
}
