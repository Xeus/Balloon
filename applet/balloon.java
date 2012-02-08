import processing.core.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class balloon extends PApplet {

// Ben Turner
// Nature of Code

// Experimenting with forces and physics.

Mover[] m = new Mover[5];

int numLoops = 0;
float ceilBumpTimeDecay = 0;
float ceilBumpVar = 0.0f;
PImage park;

public void setup() {
  size(500, 200);
  smooth();
  park = loadImage("park.jpg");
  for (int i=0; i < m.length; i++) {
    m[i] = new Mover();
  }
}

public void draw() {
  noStroke();
  fill(255, 200);
  //rect(0, 0, width, height);
  background(park);
  PVector wind = new PVector(0.01f, 0);
  PVector gravity = new PVector(0, 0);
  PVector helium = new PVector(0, -0.002f);

  for (int i=0; i < m.length; i++) {
    if (m[i].timeLoops != 0) {
      ceilBumpVar = m[i].timeLoops;
      ceilBumpTimeDecay = (ceilBumpVar * 0.001f);
      m[i].timeLoops--;
    }
    else {
      ceilBumpTimeDecay = 0.0f;
    }
    PVector ceilingBump = new PVector(0, ceilBumpTimeDecay);
    
    if (mousePressed) {
      m[i].applyForce(wind);
    }

    m[i].display();
    m[i].applyForce(gravity);
    m[i].applyForce(helium);

    m[i].update();
    m[i].checkEdges();
    m[i].timeLoops = m[i].ceilingCollision();
    if (m[i].timeLoops > 0) {
      m[i].applyForce(ceilingBump);
      m[i].timeLoops--;
    }
  }

  if (numLoops > 500) {
    numLoops = 0;
  }
  else {
    numLoops++;
  }
}



class Mover {

  PVector location;
  PVector velocity;
  PVector acceleration;
  float mass;
  float xoff;
  int timeLoops;
  float maxSpeed;
  int r,g,b;
  float randomNoise;

  Mover() {
    location = new PVector(random(width), height-20);
    velocity = new PVector(0,random(0,0.01f));
    acceleration = new PVector(0,random(0,0.3f));
    mass = 1;
    xoff = 0.0f;
    timeLoops = 0;
    maxSpeed = 8;
    randomNoise = random(0.0f,0.5f);
    r = round(random(255));
    g = round(random(255));
    b = round(random(255));
  }
  
  public void applyForce(PVector force) {
    PVector f = PVector.div(force,mass);
    acceleration.add(f);
  }
  
  public void update() {
    velocity.add(acceleration);
    velocity.limit(maxSpeed);
    location.add(velocity);
    acceleration.mult(0);
  }

  public void display() {
    stroke(0);
    fill(r,g,b);
    xoff = xoff + .01f;
    float x = location.x + noise(xoff+randomNoise) * 100;
    ellipse(x,location.y,12,19);
    line(x,location.y+10,x+3,location.y+18);
  }

  public void checkEdges() {

    if (location.x > width) {
      // location.x = 0;
      velocity.x = velocity.x * -1;
    } else if (location.x < 0) {
      // location.x = width;
      velocity.x = velocity.x * -1;
    }

    if (location.y > height) {
      velocity.y *= -1;
      location.y = height;
    }
    
    if (location.y < 0) {
      velocity.y *= -.5f;
      location.y = 0;
    }

  }
  
  public int ceilingCollision() {
    if (location.y < 0) {
      return timeLoops = 4;
    }
    else {
      return timeLoops = 0;
    }
  }

}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "balloon" });
  }
}
