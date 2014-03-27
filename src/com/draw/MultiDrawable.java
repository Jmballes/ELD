package com.draw;

import java.util.Comparator;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.paintview.IProcess;

public abstract class MultiDrawable implements Comparator<MultiDrawable>{
	private int z;

	private float x;

	private float y;
	
	private float height;
	
	private float width;
	
	public List<Bitmap> listDrawable;

	public IProcess process;
	
	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	public static final int BOTTOM = 32;
	public static final int VCENTER =2;
	public static final int TOP = 16;
	public static final int RIGHT = 8;
	public static final int HCENTER = 1;
	public static final int LEFT = 4;
	public void setY(float y,int anchor){
		if ((anchor & BOTTOM)!=0) {
		      y = y - getHeight();
		    } else if ((anchor & VCENTER)!=0) {
		      y = y -(getHeight()/2);
		    } 
		    
		    if ((anchor & RIGHT)!=0) {
		      x = x - width;
		    } else if ((anchor & HCENTER)!=0) {
		      x = x - width/2;
		    }
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	public  void process(long time){
		
	}
	
	

	public abstract void draw(Canvas canvas,Paint paint,Zone zone);
	public abstract void drawDebug(Canvas canvas,Paint paint);
	public abstract void load(Resources resources, IProcess process);
	public abstract void init(List<MultiDrawable> listDrawable);
	// Overriding the compareTo method
//	   public int compareTo(MultiDrawable d){
//	      return (this.z).compareTo(d.getZ());
//	   }

	   // Overriding the compare method to sort the age 
	   public int compare(MultiDrawable d, MultiDrawable d1){
	      return d.z - d1.z;
	   }
}
