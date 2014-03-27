package utils;

public class Vector {

	 
	 public static Vector ZERO = new Vector(0, 0);
	 
	 private double x = 0.0d;
	 private double y = 0.0d;
	 
	 public Vector(double x, double y) {
	  this.x = x;
	  this.y = y;
	 }
	 public Vector VectorByAngle (double angle, double longitude){
		 return new Vector(Math.cos(angle)*longitude,Math.sin(angle)*longitude);
	 }
//	 setVx((float) (Math.abs((Math.cos(tmpangle)*VELOCIDAD_HUYENDO_PRESENTATION*process.getView().deviceDensity))));
//		setVy((float) (Math.abs((Math.sin(tmpangle)*VELOCIDAD_HUYENDO_PRESENTATION*process.getView().deviceDensity))));
		
	 
	 public Vector add( double c ) {
	  return new Vector(this.x+c,this.y+c);
	 }
	 
	 public Vector add( double x, double y ) {
	  return new Vector(this.x+x,this.y+y);
	 }
	 
	 public Vector add( Vector v ) {
	  return new Vector(this.x+v.x,this.y+v.y);
	 }
	 
	 public Vector cross( Vector v ) {
	  return new Vector(this.y - v.y, this.x - v.x );
	 }
	 
	 public Vector divide( double c ) {
	  return new Vector(this.x/c,this.y/c);
	 }
	 
	 public Vector divide( double x, double y ) {
	  return new Vector(this.x/x,this.y/y);
	 }
	 
	 public Vector divide( Vector v ) {
	  return new Vector(this.x/v.x,this.y/v.y);
	 }
	 
	 public double dot( Vector v ) {
	  return this.x * v.x  + this.y * v.y;
	 }
	 
	 @Override
	 public boolean equals(Object o) {
	  if(o instanceof Vector) {
	   Vector v = (Vector)o;
	   return x == v.x && y == v.y;
	  }
	  return false;
	 }
	 
	 public double getX() {
	  return x;
	 }
	 
	 public double getY() {
	  return y;
	 }
	 
	 public double magnitude() {
	  return Math.sqrt(this.x * this.x + this.y * this.y);
	 }
	 
	 public Vector multiply( double c ) {
	  return new Vector(this.x*c,this.y*c);
	 }
	 
	 public Vector multiply( double x, double y ) {
	  return new Vector(this.x*x,this.y*y);
	 }
	 
	 public Vector multiply( Vector v ) {
	  return new Vector(this.x*v.x,this.y*v.y);
	 }
	 
	 public Vector negative() {
	  return this.multiply(-1);
	 }
	 
	 public Vector normalize() {
	  double mag = magnitude();
	  if(mag != 0) {
	   return new Vector(x/mag, y/mag);
	  }else {
	   return new Vector(x,y);
	  }
	 }
	 
	 public void set(int i, int j) {
	  this.x = i;
	  this.y = j;
	 }
	  
	 public void set(double x, double y) {
	  this.x = x;
	  this.y = y;
	 }
	 
	 public void set(Vector v) {
	  x = v.x;
	  y = v.y;
	 }
	 
	 public void setX( double x ) {
	  this.x = x;
	 }
	 
	 public void setY( double y ) {
	  this.y = y;
	 }
	 
	 public Vector subtract( double c ) {
	  return new Vector(this.x-c,this.y-c);
	 }
	 
	 public Vector subtract( double x, double y ) {
	  return new Vector(this.x-x,this.y-y);
	 }
	 
	 public Vector subtract( Vector v ) {
	  return new Vector(this.x-v.x,this.y-v.y);
	 }
	 
	 @Override
	 public String toString() {
	  return String.format("(%f,%f)", x, y);
	 }
	 
	 public static Vector valueOf(float x, float y) {
	  return new Vector(x, y);
	 }
	 
	 public static Vector valueOf(double x, double y) {
	  return new Vector(x, y);
	 }
	 public Vector rotate(double angle,double axisX,double axisY)
	   {
	      double tY=y-axisX,tX=x-axisY;
	      double cosa=Math.cos(angle);
	      double sina=Math.sin(angle);
	      x=tX*cosa + tY*sina + axisX;
	      y=-tX*sina + tY*cosa + axisY;
	      return new Vector(tX*cosa + tY*sina + axisX,-tX*sina + tY*cosa + axisY);
	   }
	 public double getAngle(){
		 return Math.atan2(y, x);
	 }
	 
}
