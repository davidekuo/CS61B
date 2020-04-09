// Discussion 4: Inheritance

/*
1. Creating Cats 
Given the Animal class, fill in the definition of the Cat class so that when greet()
is called, the label "Cat" (instead of "Animal") is printed to the screen. Assume
that a Cat will make a "Meow!" noise if the cat is 5 years or older and "MEOW!"
if the cat is less than 5 years old.
*/

public class Animal {
    protected String name, noise;
    protected int age;

    public Animal (String name, int age) {
        this.name = name;
        this.age = age;
        this.noise = "Huh?";
    }

    public String makeNoise() {
        if (age < 5) {
            return noise.toUpperCase();
        } else {
            return noise;
        }
    }

    public void greet() {
        System.out.println("Animal " + name + " says: " + makeNoise());
    }
}

public class Cat extends Animal {

    public Cat (String name, int age) {
        super(name, age);
        this.noise = "Meow!"; 
        // super.noise = "Meow!" OR noise = "Meow!" also work
    }

    @Override
    public void greet() {
        System.out.println("Cat " + name + " says: " + makeNoise());
    }

    public static void main (String[] args) {
        Cat katniss = new Cat ("Katniss", 3);
        Cat gilly = new Cat ("Gilly", 7);
        katniss.greet(); // expect "MEOW!"
        gilly.greet(); // expect "Meow!"
    }

}

/*
2. Raining Cats and Dogs
Assume that Animal and Cat are defined as above. What would Java print on each of the indicated lines?
*/

public class Dog extends Animal {
    public Dog(String name, int age) {
        super(name, age);
        noise = "Woof!";
    }

    @Override
    public void greet() {
        System.out.println("Dog " + name + " says: " + makeNoise());
    }

    public void playFetch() {
        System.out.println("Fetch, " + name + "!");
    }
}

public class TestAnimals {
    public static void main(String[] args) {
        Animal a = new Animal("Pluto", 10);
        Cat c = new Cat("Garfield", 6);
        Dog d = new Dog("Fido", 4);

        a.greet();              // Huh?
        c.greet();              // Meow!
        d.greet();              // WOOF!
        a = c;                  // Meow!
        ((Cat) a).greet();      // Meow!
        a.greet();              // Meow!
    }
}

// What would happen if we added the following to the end of main?
    a = new Dog("Spot", 10);
    d = a;
/* Why would this produce a compiler error and how could we fix it?
    d = a generates an error because Animal (a's static type) is NOT
    a Dog or subclass of Dog. This can be fixed by type-casting a:
        d = (Dog) a;



