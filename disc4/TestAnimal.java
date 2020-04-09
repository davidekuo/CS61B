public class TestAnimal {
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
