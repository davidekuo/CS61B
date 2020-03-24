import java.math.*;

public class Planet {

	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	private static final double G = 6.67e-11;

	public Planet(double xP, double yP, double xV, double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p) {
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName; 
	}

	public double calcDistance(Planet p) {
		double dx = p.xxPos - this.xxPos;
		double dy = p.yyPos - this.yyPos;
		double r = Math.sqrt(dx*dx + dy*dy);

		return r;
	}

	public double calcForceExertedBy(Planet p) {
		return G * this.mass * p.mass / (calcDistance(p) * calcDistance(p));
	}

	public double calcForceExertedByX(Planet p) {
		return calcForceExertedBy(p) * (p.xxPos - this.xxPos) / calcDistance(p);
	}

	public double calcForceExertedByY(Planet p) {
		return calcForceExertedBy(p) * (p.yyPos - this.yyPos) / calcDistance(p);
	}

	private boolean equals(Planet p) {
		if(this.xxPos == p.xxPos &&
			this.yyPos == p.yyPos &&
			this.xxVel == p.xxVel &&
			this.yyVel == p.yyVel &&
			this.mass == p.mass &&
			this.imgFileName == p.imgFileName) {
			return true;
		} else {
			return false;
		}
	}

	public double calcNetForceExertedByX(Planet[] planets) {
		double netForceExertedByX = 0;
		for(Planet p : planets) {
			if(!this.equals(p)) {
				netForceExertedByX += calcForceExertedByX(p);
			} 
		}
		
		return netForceExertedByX;
	}

	public double calcNetForceExertedByY(Planet[] planets) {
		double netForceExertedByY = 0;
		for(Planet p : planets) {
			if(!this.equals(p)) {
				netForceExertedByY += calcForceExertedByY(p);
			} 
		}
		
		return netForceExertedByY;
	}

	public void update(double dt, double fX, double fY) {
		// Calculate acceleration
		// acceleration = force / mass
		double aX = fX / mass;
		double aY = fY / mass;

		// Calculate new velocity
		// new velocity = old velocity + acceleration * time interval
		xxVel += aX * dt;
		yyVel += aY * dt;

		// Calculate new position
		// new position = old position + new velocity * time interval
		xxPos += xxVel * dt;
		yyPos += yyVel * dt;
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}


}