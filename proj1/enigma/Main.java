package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 *
 * @author Mingjie Chen
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        try {
            Machine m0 = readConfig();
            Machine machine = new Machine(_alphabet, m0.numRotors(),
                    m0.numPawls(), m0.allRotors());
            while (_input.hasNextLine()) {

                String line = _input.nextLine().trim();

                if (line.startsWith("*")) {
                    machine = new Machine(_alphabet, m0.numRotors(),
                            m0.numPawls(), m0.allRotors());
                    setUp(machine, line);
                } else if (line.length() == 0) {
                    _output.println();
                } else {
                    line = line.replaceAll(" ", "");
                    line = machine.convert(line);
                    printMessageLine(line);
                    if (_input.hasNextLine()) {
                        _output.println();
                    }
                }

            }
        } catch (NoSuchElementException | IndexOutOfBoundsException excep) {
            throw error("input file wrong");
        } catch (NegativeArraySizeException | NullPointerException excp) {
            throw error("input file wrong");
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            int numRotors = 0;
            int pawls = 0;
            ArrayList<Rotor> allRotors = new ArrayList<>();

            String alpha = _config.nextLine();
            alpha = alpha.replaceAll(" ", "");
            _alphabet = new Alphabet(alpha);

            numRotors = _config.nextInt();
            pawls = _config.nextInt();

            while (_config.hasNext()) {
                allRotors.add(readRotor(allRotors));
            }

            return new Machine(_alphabet, numRotors, pawls, allRotors);

        } catch (NoSuchElementException | IndexOutOfBoundsException
                | NegativeArraySizeException | NullPointerException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     *
     * @param allRotors All the rotors a machine has.
     */
    private Rotor readRotor(ArrayList<Rotor> allRotors) {
        try {

            String name = "";
            Permutation perm;
            String line;
            do {
                line = _config.nextLine().trim();
            } while (line.equals(""));

            if (!line.startsWith("(")) {
                String[] strings = line.split("\\s+");
                name = strings[0];
                String type = strings[1];
                String[] cycles = Arrays.copyOfRange(strings,
                        2, strings.length);
                String cycle = "";
                for (String c : cycles) {
                    cycle += c;
                }
                perm = new Permutation(cycle.replaceAll(" ", ""), _alphabet);

                char[] typ = type.toCharArray();
                if (typ[0] == 'M') {
                    char[] notches = Arrays.copyOfRange(typ, 1, typ.length);
                    String notch = "";
                    for (char n : notches) {
                        notch += n;
                    }
                    MovingRotor r = new MovingRotor(name, perm, notch);
                    return r;
                } else if (typ[0] == 'N') {
                    return new FixedRotor(name, perm);
                } else if (typ[0] == 'R') {
                    return new Reflector(name, perm);
                } else {
                    throw error("no such type of Rotor");
                }

            } else {
                Rotor r = allRotors.remove(allRotors.size() - 1);
                r.permutation().addCycle(line.replaceAll(" ", ""));
                return r;
            }

        } catch (NoSuchElementException | IndexOutOfBoundsException
                | NegativeArraySizeException | NullPointerException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        String[] input = settings.split("\\s+");
        String[] rotors = Arrays.copyOfRange(input, 1, M.numRotors() + 1);

        M.insertRotors(rotors);
        M.setRotors(input[M.numRotors() + 1]);

        if (input.length > M.numRotors() + 2) {
            String cycle = "";
            String[] cycles = Arrays.copyOfRange(input,
                    M.numRotors() + 2, input.length);
            for (String c : cycles) {
                cycle += c;
            }
            cycle = cycle.replaceAll(" ", "");
            Permutation p = new Permutation(cycle, _alphabet);
            M.setPlugboard(p);
        }
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        char[] out = msg.toCharArray();
        ArrayList<Character> output = new ArrayList<>();
        for (int i = 0; i < out.length; i++) {
            output.add(out[i]);
        }

        while (output.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                _output.print(output.remove(0));
            }
            if (output.size() >= 0) {
                _output.print(" ");
            }
        }

        if (output.size() > 0) {
            for (char c : output) {
                _output.print(c);
            }
        }
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;
}
