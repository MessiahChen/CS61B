package capers;

import java.io.File;
import java.io.Serializable;

public class Dog implements Serializable {

    public Dog (String name, String breed, int age) {
        _age = age;
        _breed = breed;
        _name = name;
    }

    /**
     * Increases a dogs age and celebrates!
     */
    public void haveBirthday() {
        _age += 1;

        // celebratory message
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    @Override
    public String toString() {
        return String.format("Woof! My name is %s and I am a %s! I am %d years old! Woof!", _name, _breed, _age);
    }

    private int _age;
    private String _breed;
    private String _name;
}
