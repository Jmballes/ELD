package com.objects;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.draw.MultiDrawable;
import com.draw.Zone;
import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.ProcessGame;

public class Logo extends MultiDrawable {
	
	private int logo = 0;

	private int transparent;
	int[] resourcesDrawable = { R.drawable.logo };
	public void process(int transparent){
		this.transparent=transparent;
				
	}
	public void drawDebug(Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(0x99, 0xFF, 0x11, 0x11));

//		canvas.drawRect(zone.getX(), zone.getY(),
//				zone.getX() + zone.getWidth(), zone.getY() + zone.getHeight(),
//				paint);

		paint.setColor(Color.argb(0x99, 0xFF, 0xFF, 0x11));
		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY()
				+ getHeight(), paint);
	}

	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
				//drawDebug(canvas, paint, zone);
		Bitmap bitmap = (Bitmap) listDrawable.get(logo);
		paint.setAlpha(transparent);

		canvas.drawBitmap((Bitmap) bitmap, getX(), getY(), paint);

	
		
		}

	public void load(Resources resources, IProcess process) {
		this.process = process;

		listDrawable = new ArrayList<Bitmap>();

		for (int i = 0; i < resourcesDrawable.length; i++) {
			listDrawable.add(i, BitmapFactory.decodeResource(resources,
					resourcesDrawable[i]));
		}

		
		
		Bitmap bitmap = (Bitmap) listDrawable.get(0);
		
		setHeight(bitmap.getHeight() );
		setWidth(bitmap.getWidth());

		
	}


	@Override
	public void init(List<MultiDrawable> listDrawable) {
		

			setY((process.getValue(ProcessGame.CANVAS_HEIGHT)-getHeight())/2);
			setX((process.getValue(ProcessGame.CANVAS_WIDTH)-getWidth())/2);
		
	}

}
