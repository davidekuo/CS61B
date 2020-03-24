import java.math.*;

public class TestPlanet {

	public static void main(String[] args) {
		checkPlanets();
	}

	private static boolean approxEqual(double actual, double expected, double eps) {
		return Math.abs(expected - actual) <= eps * Math.max(expected, actual);
	}

	private static void checkEquals(double actual, double expected, String label, double eps) {
		if (approxEqual(actual, expected, eps)) {
			System.out.println("PASS: " + label + ": Expected " + expected + " and you gave " + actual);
		} else {
			System.out.println("FAIL: " + label + ": Expected " + expected + " and you gave " + actual);
		}
	}

	private static void checkPlanets() {
		System.out.println("Checking gravitational force between planets...");

		Planet sun = new Planet(1e12, 2e11, 0, 0, 2e30, "sun.gif");
		Planet saturn = new Planet(2.3e12, 9.5e11, 0, 0, 6e26, "saturn.gif");

		checkEquals(saturn.calcForceExertedBy(sun), 3.6e22, "gravitational force", 0.1);
	}

}