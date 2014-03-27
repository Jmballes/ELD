package com.objects;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.draw.MultiDrawable;
import com.draw.Zone;
import com.paintview.IProcess;
import com.paintview.ProcessGame;

public class BackGround extends MultiDrawable {
	

	private int blue_height = 0;
	private int ground_height = 0;
	private int horizont_Y = 0;
	private int stalactite_bot= 0;


	public void drawDebug(Canvas canvas, Paint paint) {
		int textsize=15;
		paint.setColor(0xFFFF0000);
		paint.setTextSize(textsize);
		paint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText("W,H,D("+canvas.getWidth()+","+canvas.getHeight()+"):"+canvas.getDensity(),
				0,textsize, paint);
		canvas.drawText("Device.W,H("+process.getView().deviceWidth+","+process.getView().deviceHeight+"):",
				0,textsize*3, paint);
		canvas.drawText("Density:" +process.getView().deviceDensity +" DensityDpi:"+ process.getView().deviceDensityDpi,
				0,textsize*5, paint);
		canvas.drawText("WidthPixels:"+ process.getView().deviceWidthPixels+" HeightPixels:"+ process.getView().deviceHeightPixels,
				0,textsize*7, paint);
		canvas.drawText(
				" Xdpi"+ process.getView().deviceXdpi+" Ydpi:"+process.getView().deviceYdpi,
				0,textsize*9, paint);
		
		
//		paint.setColor(Color.argb(0x99, 0xFF, 0x11, 0x11));
//
//		canvas.drawRect(zone.getX(), zone.getY(),
//				zone.getX() + zone.getWidth(), zone.getY() + zone.getHeight(),
//				paint);
//		// Dibujamos las balas que nos quedan en esta minifase
//		setX(zone.getX() + ((zone.getWidth() - getWidth()) / 2));
//		// setX(0);
//		setY(zone.getY() + ((zone.getHeight() - getHeight()) / 2));
//
//		paint.setColor(Color.argb(0x99, 0xFF, 0xFF, 0x11));
//		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY()
//				+ getHeight(), paint);
	}

	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
		paint.setColor(0xFF5b241a);
		canvas.drawRect(0, 0, canvas.getWidth(), blue_height, paint);
		paint.setColor(0xFF7A3122);
		canvas.drawRect(0, blue_height, canvas.getWidth(), blue_height+ground_height, paint);

		//		paint.setColor(0xFF00FF00);
		//canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
		// drawDebug(canvas, paint, zone);
//		setX(zone.getX() + ((zone.getWidth() - getWidth()) / 2));
//		// setX(0);
//		setY(zone.getY() + ((zone.getHeight() - getHeight()) / 2));

		// paint.setColor(0x99000000);
		// canvas.drawRect(getX(), getY(),getX()+getWidth(), getY()+getHeight(),
		// paint);

		// Pintamos el fondo azul del cielo
//		paint.setColor(Color.argb(0xFF, 0x32, 0xD0, 0xFD));
//		canvas.drawRect(0, getY(), canvas.getWidth(), 30,
//				paint);
//		canvas.drawRect(0, getY(), canvas.getWidth(), getY() + blue_height,
//				paint);
//		
//		paint.setColor(Color.argb(0xFF, 0xAA, 0x7A, 0x00));
//		canvas.drawRect(0, getY() + blue_height, canvas.getWidth(), getY() + blue_height+ground_height,
//				paint);

		// paint.setColor(Color.argb(0xFF, 0xFF, 0xFF, 0xFF));
		// canvas.drawRect(0, getHorizont_Y(), canvas.getWidth(),
		// getHorizont_Y()+5,
		// paint);

	}

	public void load(Resources resources, IProcess process) {
		this.process = process;


		listDrawable = null;
//		int heightquarter = juegoview.thread.mCanvasHeight / 4;
//		setHeight(juegoview.thread.mCanvasHeight);
//		blue_height = (int) (heightquarter * 3);
//		ground_height = juegoview.thread.mCanvasHeight - blue_height;
		setWidth(process.getValue(ProcessGame.CANVAS_WIDTH));
//		horizont_Y = blue_height;

	}

	

	public int getHorizont_Y() {
		return horizont_Y;
	}
	public int getstalactiteBot_Y() {
		return stalactite_bot;
	}

	public void setHorizont_Y(int horizont_Y) {
		this.horizont_Y = horizont_Y;
	}

	@Override
	public void init(List<MultiDrawable> listDrawable) {
		Estalactitas estal = (Estalactitas) process.getMultiDrawable(
				listDrawable, "com.objects.Estalactitas");
		int mCanvasHeight =  process.getValue(ProcessGame.CANVAS_HEIGHT);
		
		int free_height=(int)(mCanvasHeight-estal.getHeight()*2);
		blue_height=mCanvasHeight/2;
		ground_height=blue_height;
//		if (estal.getHeight()>mCanvasHeight){
//			blue_height=mCanvasHeight;
//			ground_height=0;
//		}else{
//			blue_height= (int)(trees.getHeight()+(free_height/2));
//			ground_height=mCanvasHeight-blue_height;
//		}
		horizont_Y = blue_height+(ground_height/2);
		stalactite_bot = blue_height;
	}

}
