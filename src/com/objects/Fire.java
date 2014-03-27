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
import com.draw.ObjetoAnimable;
import com.draw.Zone;
import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.ProcessGame;

public class Fire extends MultiDrawable {
	
	private int arbusto = 0;
	public final static int TIME_FRAMES=100;
	public final static byte[] ANIMATIONS={0,3
		   
	};
	ObjetoAnimable anim;
	int[] resourcesDrawable = { R.drawable.fire };

	public void drawDebug(Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(0x99, 0xFF, 0x11, 0x11));

//		canvas.drawRect(zone.getX(), zone.getY(),
//				zone.getX() + zone.getWidth(), zone.getY() + zone.getHeight(),
//				paint);

		paint.setColor(Color.argb(0x99, 0xFF, 0xFF, 0x11));
		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY()
				+ getHeight(), paint);
	}
	public void process(long time){
		anim.actualizarTiempo();
	}
	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
		if (anim!=null){
			for (int i=(int)getX();i<canvas.getWidth();i+=anim.TAMANIO_SPRITE){

				anim.pintarAnimacion(canvas,i, (int) getY(),paint);
				}
			
		}
		}

	public void load(Resources resources, IProcess process) {
		this.process = process;

		listDrawable = new ArrayList<Bitmap>();
		for (int i = 0; i < resourcesDrawable.length; i++) {
			listDrawable.add(i, BitmapFactory.decodeResource(resources,
					resourcesDrawable[i]));
		}
		Bitmap bitmap = (Bitmap) listDrawable.get(0);
		setHeight(bitmap.getHeight()/3 );
		setWidth(bitmap.getWidth());
		anim= new ObjetoAnimable(listDrawable.get(0),ANIMATIONS,TIME_FRAMES,(int) getWidth(),3);
		anim.initAnimation(0, true);
	}


	@Override
	public void init(List<MultiDrawable> listDrawable) {
		
			BackGround backGround = (BackGround) process.getMultiDrawable(
					listDrawable, "com.objects.BackGround");
			
			//int mCanvasHeight =  process.getValue(ProcessGame.CANVAS_HEIGHT);
			setY(backGround.getHorizont_Y()-getHeight());
		
		
	}

}
