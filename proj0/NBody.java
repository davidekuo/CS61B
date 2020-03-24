public class NBody {

	public static double readRadius(String fileName) {
		In in = new In(fileName);
		int numPlanets = in.readInt();
		double radius = in.readDouble();

		return radius;
	}

	public static Planet[] readPlanets(String fileName) {
		In in = new In(fileName);
		int numPlanets = in.readInt();
		double radius = in.readDouble();
		
		Planet[] planets = new Planet[numPlanets];

		for(int i = 0; i < numPlanets; i++) {
			planets[i] = new Planet(in.readDouble(), // x position
				in.readDouble(), // y position
				in.readDouble(), // x velocity
				in.readDouble(), // y velocity
				in.readDouble(), // mass
				in.readString()); // image file name
		}

		return planets;
	}

	private static void drawUniverse(Planet[] planets) {
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg");
		for (Planet p : planets) {
			p.draw();
		}
		StdDraw.show();
		StdDraw.pause(10);
	}

	private static Planet[] updatePlanets(Planet[] planets, double dT) {
		double[] xForces = new double[planets.length];
		double[] yForces = new double[planets.length];
			
		for (int i = 0; i < planets.length; i++) {
			xForces[i] = planets[i].calcNetForceExertedByX(planets);
			yForces[i] = planets[i].calcNetForceExertedByY(planets);
		}

		for (int i = 0; i < planets.length; i++) {
			planets[i].update(dT, xForces[i], yForces[i]);
		}

		return planets;
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];

		double radius = readRadius(filename);
		Planet[] planets = readPlanets(filename);

		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-1 * radius, radius);
		
		drawUniverse(planets);

		for (int t = 0; t < T; t += dt) {
			planets = updatePlanets(planets, dt);
			drawUniverse(planets);
		}

		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
		}
	}
}