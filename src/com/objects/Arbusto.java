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

public class Arbusto extends MultiDrawable {
	
	private int arbusto = 0;


	int[] resourcesDrawable = { R.drawable.arbusto };

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
		Bitmap bitmap = (Bitmap) listDrawable.get(arbusto);
		for (int i=(int)getX();i<canvas.getWidth();i+=bitmap.getWidth()){
		canvas.drawBitmap((Bitmap) bitmap, i, getY(), paint);
		}
//		paint.setColor(Color.argb(0xFF, 0xAA, 0x7A, 0x00));
//		canvas.drawRect(0, getY() + bitmap.getHeight(), canvas.getWidth(), getY() + bitmap.getHeight() + (canvas.getHeight()-(getY() + bitmap.getHeight())),
//				paint);
//		
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
		
			BackGround backGround = (BackGround) process.getMultiDrawable(
					listDrawable, "com.objects.BackGround");
			
			//int mCanvasHeight =  process.getValue(ProcessGame.CANVAS_HEIGHT);
			setY(backGround.getHorizont_Y()-getHeight());
		
		
	}

}
