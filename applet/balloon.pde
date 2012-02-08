// Ben Turner
// Nature of Code

// Experimenting with forces and physics.

Mover[] m = new Mover[5];

int numLoops = 0;
float ceilBumpTimeDecay = 0;
float ceilBumpVar = 0.0;
PImage park;

void setup() {
  size(500, 200);
  smooth();
  park = loadImage("park.jpg");
  for (int i=0; i < m.length; i++) {
    m[i] = new Mover();
  }
}

void draw() {
  noStroke();
  fill(255, 200);
  //rect(0, 0, width, height);
  background(park);
  PVector wind = new PVector(0.01, 0);
  PVector gravity = new PVector(0, 0);
  PVector helium = new PVector(0, -0.002);

  for (int i=0; i < m.length; i++) {
    if (m[i].timeLoops != 0) {
      ceilBumpVar = m[i].timeLoops;
      ceilBumpTimeDecay = (ceilBumpVar * 0.001);
      m[i].timeLoops--;
    }
    else {
      ceilBumpTimeDecay = 0.0;
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



