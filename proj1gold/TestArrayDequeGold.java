import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    /*
    private String toString(StudentArrayDeque x) {
        String s = "";
        for (int i = 0; i < x.size(); i += 1) {
            s += x.get(i).toString() + " ";
        }
        return s;
    }

    private String toString(ArrayDequeSolution x) {
        String s = "";
        for (int i = 0; i < x.size(); i += 1) {
            s += x.get(i).toString() + " ";
        }
        return s;
    }
    */

    /*
    @Test
    public void testArrayDeque2() {
        StudentArrayDeque<Integer> test = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> soln = new ArrayDequeSolution<>();

        int startLength = 1;
        int numIter = 100;
        String trace = "";

        for (int i = 0; i < startLength; i += 1) {
                test.addLast(i);
                soln.addLast(i);
        }
        System.out.print("Start:\nsoln: " + toString(soln) + "\ntest: " + toString(test) + "\n\n");

        for (int i = 0; i < numIter; i += 1) {
            double rand = StdRandom.uniform();
            if(rand < 0.25) {
                test.addLast(i);
                soln.addLast(i);
                trace += "addLast(" + i + ")\n";
                System.out.println("addLast(" + i + "):");
            } else if(rand <0.5) {
                test.addFirst(i);
                soln.addFirst(i);
                trace += "addFirst(" + i + ")\n";
                System.out.println("addFirst(" + i + "):");
            } else if (rand < 0.75) {
                if (test.size() == 0) {
                    test.addLast(i);
                    soln.addLast(i);
                    trace += "addLast(" + i + ")\n";
                    System.out.println("addLast(" + i + "):");
                } else {
                    test.removeLast();
                    soln.removeLast();
                    trace += "removeLast()\n";
                    System.out.println("removeLast():");
                }
            } else {
                if (test.size() == 0) {
                    test.addFirst(i);
                    soln.addFirst(i);
                    trace += "addFirst(" + i + ")\n";
                    System.out.println("addFirst(" + i + "):");
                } else {
                    test.removeFirst();
                    soln.removeFirst();
                    trace += "removeFirst()\n";
                    System.out.println("removeFirst():");
                }
            }
            System.out.println("expect: " + toString(soln) + "\nactual: " + toString(test) + "\n");
            assertEquals(trace, toString(soln), toString(test));
        }
    }
    */

    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> test = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> soln = new ArrayDequeSolution<>();

        int numIter = 100;
        String trace = "";

        //System.out.println("Testing StudentArrayDeque");

        for (int i = 0; i < numIter; i += 1) {
            double rand = StdRandom.uniform();
            Integer s;
            Integer t;

            if (rand < 0.25) {
                test.addLast(i);
                t = test.get(test.size() - 1);

                soln.addLast(i);
                s = soln.get(soln.size() - 1);

                trace += "addLast(" + i + ")\n";
                //System.out.println("addLast(" + i + "):");
            } else if (rand < 0.5) {
                test.addFirst(i);
                t = test.get(0);

                soln.addFirst(i);
                s = test.get(0);

                trace += "addFirst(" + i + ")\n";
                //System.out.println("addFirst(" + i + "):");
            } else if (rand < 0.75) {
                if (test.size() == 0) {
                    test.addLast(i);
                    t = test.get(test.size() - 1);

                    soln.addLast(i);
                    s = soln.get(soln.size() - 1);

                    trace += "addLast(" + i + ")\n";
                    //System.out.println("addLast(" + i + "):");
                } else {
                    t = test.removeLast();
                    s = soln.removeLast();
                    trace += "removeLast()\n";
                    //System.out.println("removeLast():");
                }
            } else {
                if (test.size() == 0) {
                    test.addFirst(i);
                    t = test.get(0);

                    soln.addFirst(i);
                    s = test.get(0);

                    trace += "addFirst(" + i + ")\n";
                    //System.out.println("addFirst(" + i + "):");
                } else {
                    t = test.removeFirst();
                    s = soln.removeFirst();
                    trace += "removeFirst()\n";
                    //System.out.println("removeFirst():");
                }
            }
            //System.out.println("expect: " + s + "\nactual: " + t + "\n");
            assertEquals(trace, s, t);
        }
    }

}
