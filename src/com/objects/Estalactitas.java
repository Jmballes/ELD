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
import com.paintview.JuegoView;
import com.paintview.ProcessGame;

public class Estalactitas extends MultiDrawable {
	JuegoView juegoview;
	private int background = 0;
	private int stalactiteBotY=0;
	int[] resourcesDrawable = { R.drawable.fondo , R.drawable.stalactice_bot};

	public void drawDebug(Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(0x99, 0xFF, 0x11, 0x11));


		paint.setColor(Color.argb(0x99, 0xFF, 0xFF, 0x11));
		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY()
				+ getHeight(), paint);
	}

	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
				
		Bitmap bitmap = (Bitmap) listDrawable.get(background);
		for (int i=(int)getX();i<canvas.getWidth();i+=bitmap.getWidth()){
			canvas.drawBitmap((Bitmap) bitmap, i, getY(), paint);
		}
		Bitmap bitmapBot = (Bitmap) listDrawable.get(1);
		for (int i=(int)getX();i<canvas.getWidth();i+=bitmapBot.getWidth()){
			canvas.drawBitmap((Bitmap) bitmapBot, i, stalactiteBotY, paint);
		}
		
		}
	
	public int getStalactiteBotY() {
		return stalactiteBotY;
	}

	public void setStalactiteBotY(int stalactiteBotY) {
		this.stalactiteBotY = stalactiteBotY;
	}

	public void load(Resources resources,IProcess process) {
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
		int mCanvasHeight =  process.getValue(ProcessGame.CANVAS_HEIGHT);
		setY(0);
	}

}
