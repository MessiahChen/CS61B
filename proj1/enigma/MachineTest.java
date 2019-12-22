package enigma;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Machine class.
 * Only test for basic fuctions,
 * deep test will be made in Main (file input/output)
 *
 * @author Mingjie Chen
 */
public class MachineTest {

    private Rotor setRotor(String name, HashMap<String, String> rotors,
                           String notches) {
        return new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
    }

    @Test
    public void testAll() {
        ArrayList<Rotor> allRotors = new ArrayList<>();
        allRotors.add(setRotor("B", NAVALA, ""));
        allRotors.add(setRotor("Beta", NAVALA, ""));
        allRotors.add(setRotor("I", NAVALA, ""));
        Machine M = new Machine(UPPER, 3, 1, allRotors);
        assertEquals(M.allRotors().size(), 3);

        String[] rotors = {"B", "Beta", "I"};
        M.insertRotors(rotors);
        assertEquals(M.convert("HELLO"), "FJGAN");

        ArrayList<Rotor> allRotors0 = new ArrayList<>();
        allRotors0.add(setRotor("B", NAVALA, ""));
        allRotors0.add(setRotor("Beta", NAVALA, ""));
        allRotors0.add(setRotor("I", NAVALA, ""));
        Machine M0 = new Machine(UPPER, 3, 1, allRotors0);
        M0.insertRotors(rotors);
        assertEquals(M0.convert("FJGAN"), "HELLO");
    }

}
