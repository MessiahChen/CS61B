package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Mingjie Chen
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _plugboard = null;
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * @return All the available rotors the machine has
     */
    Collection<Rotor> allRotors() {
        return _allRotors;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        for (String name : rotors) {
            for (Rotor r : _allRotors) {
                if (name.equals(r.name())) {
                    _rotors.add(r);
                }
            }
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        for (int i = 1; i < _rotors.size(); i++) {
            _rotors.get(i).set(setting.charAt(i - 1));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        boolean[] toAdvance = new boolean[_rotors.size() - 1];

        for (int i = _rotors.size() - 2; i > 0; i--) {
            if (_rotors.get(i).rotates() && _rotors.get(i + 1).atNotch()) {
                toAdvance[i] = true;
                if (i != _rotors.size() - 2) {
                    toAdvance[i + 1] = true;
                }
            }
        }
        _rotors.get(_rotors.size() - 1).advance();
        for (int i = _rotors.size() - 2; i > 0; i--) {
            if (toAdvance[i]) {
                _rotors.get(i).advance();
            }
        }

        int out = c;
        if (_plugboard != null) {
            out = _plugboard.permute(c);
        }
        for (int j = _rotors.size() - 1; j >= 0; j--) {
            out = _rotors.get(j).convertForward(out);
        }
        for (int k = 1; k < _rotors.size(); k++) {
            out = _rotors.get(k).convertBackward(out);
        }
        if (_plugboard != null) {
            out = _plugboard.permute(out);
        }

        return out;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        char[] inChars = msg.toCharArray();
        char[] outChars = new char[inChars.length];

        for (int i = 0; i < inChars.length; i++) {
            int tmp = _alphabet.toInt(inChars[i]);
            tmp = convert(tmp);
            outChars[i] = _alphabet.toChar(tmp);
        }

        String res = "";
        for (char c : outChars) {
            res += c;
        }

        return res;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * Number of rotors in the machine.
     */
    private int _numRotors;
    /**
     * Number of pawls in the machine.
     */
    private int _pawls;
    /**
     * Collection of all the available rotors.
     */
    private Collection<Rotor> _allRotors;
    /**
     * The plugboard.
     */
    private Permutation _plugboard;
    /**
     * The rotors in the machine stored in ArrayList.
     */
    private ArrayList<Rotor> _rotors = new ArrayList<>();
}
