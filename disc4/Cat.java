public class Cat extends Animal {

    public Cat (String name, int age) {
        super(name, age);
        super.noise = "Meow!";
    }
    
    @Override
    public void greet() {
        System.out.println("Cat " + name + " says: " + makeNoise());
    }   

    public static void main (String args[]) {
        Cat katniss = new Cat ("Katniss", 3);
        katniss.greet();

        Cat gilly = new Cat("Gilly", 7);
        gilly.greet();
    }
}
