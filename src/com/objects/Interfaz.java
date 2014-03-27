package com.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.draw.MultiDrawable;
import com.draw.Zone;
import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.ProcessGame;

public class Interfaz extends MultiDrawable {
	
	private int BALA = 0;
	private int DUCK = 1;
	private int DUCKWHITE = 2;
	private int intraborder = 2;
	private int extraborder = 10;
	private int fontSize=0;
	private int borderPopup=0;
	private int img_points_width=0;
	int[] resourcesDrawable = {  R.drawable.bala,
			R.drawable.duck, R.drawable.duckwhite };
	private int POINTS_WIDTH = 30;

	public void drawDebug(Canvas canvas, Paint paint) {
		paint.setColor(Color.argb(0x99, 0xFF, 0x11, 0x11));


		paint.setColor(Color.argb(0x99, 0xFF, 0xFF, 0x11));
		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY()
				+ getHeight(), paint);
	}

	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
		Vector disparos=(Vector)process.getAtributte("disparos");
		List<Boolean> ducksFaileds= (List<Boolean>)process.getAtributte("ducksFaileds");
		
		Integer canvasHeight=(Integer)(process.getValue(ProcessGame.CANVAS_HEIGHT));

		//drawDebug(canvas, paint, zone);
		paint.setColor(0xFF000000);
		// canvas.drawRect(getX(), getY(),getX()+getWidth(), getY()+getHeight(),
		// paint);
		// Dibujamos las balas que nos quedan en esta minifase
		setX( ((canvas.getWidth() - getWidth()) / 2));
		// setX(0);
		setY( ((canvasHeight -getHeight())));
		
		drawCuadroCentral(canvas, (int) getX(), (int) getY(),
				(int) (getX() + getWidth()), (int) (getY() + getHeight()));
		// canvas.drawRect(getX(), getY(),getX()+getWidth(), getY()+getHeight(),
		// paint);

		setX(getX() + extraborder);
		int numbalas = 3 - disparos.size();
		// paint.setColor(0xFFFFFF00);
		// canvas.drawRect(getX(),
		// getY()-5,getX()+(listDrawable.get(BALA).getWidth()*3)+intraborder*2,getY()+7,paint);
		Bitmap bitmapBala = (Bitmap) listDrawable.get(BALA);
		int tmpBalaY= (int)(getY()
				+ ((getHeight() - bitmapBala.getHeight()) / 2));
		for (int i = 0; i < 3; i++) {
			paint.setColor(0xFFFFFFFF);
//			canvas.drawRect(getX(), getY() ,
//					getX() + (listDrawable.get(BALA).getWidth() * 3)
//							+ intraborder * 2, getY() +getHeight(), paint);
			if (i < numbalas) {
				canvas.drawBitmap(bitmapBala, getX(),tmpBalaY, paint);
			}
			setX(getX() + (bitmapBala.getWidth()) + intraborder);

		}
		setX(getX() - intraborder + (extraborder * 2));
		// paint.setColor(0xFF11FF00);
		// canvas.drawRect(getX(),
		// getY()-5,getX()+(listDrawable.get(DUCK).getWidth()*10)+intraborder*9,getY()+7,paint);
		// Dibujamos los patos acertados/fallados de esta ronda
		
		
		Bitmap bitmapMiniduck = (Bitmap) listDrawable.get(DUCK);
		int tmpMiniDuckY= (int)(getY()
				+ ((getHeight() - bitmapMiniduck.getHeight()) / 2));
		
		for (int i = 0; i < ducksFaileds.size(); i++) {
			if (pintadoEspecialSiEstaMostrandoResultados()) {
				pintandoLosPatosDeLaInterfaz(canvas, i,
						(int) getX(),  tmpMiniDuckY, paint);
			}
			setX(getX() + intraborder + (listDrawable.get(DUCK).getWidth()));
		}
		setX(getX() - intraborder + extraborder * 2 + img_points_width );
		// Mostramos la Ronda actual en la que se encuentra
		paint.setColor(0xFFCCF543);
		paint.setTextSize( fontSize);
		paint.setTextAlign(Paint.Align.RIGHT);
//		canvas.drawText("R=" + juegoview.thread.getRound(), getX(), getY(),
//				paint);

		// Mostramos la Puntuación actual
		//paint.setTextAlign(Paint.Align.RIGHT);
		
		String points=process.getInfo("SCORE") +"";
		
//		paint.setColor(0xFFFF00FF);
//		canvas.drawRect(0,getY(),300,getY()+getHeight(),paint);
//				tmpPointsY,getX()+10,tmpPointsY+11,paint);
		
		Rect bounds=new Rect();
		paint.getTextBounds("000000000000", 0, 1, bounds);
		int tmpPointsY= (int)(getY()+ (bounds.height()/2)+
				+ ((getHeight()) / 2));
		
		paint.setColor(0xFFFFFFFF);
		
		paint.setTextAlign(Align.RIGHT);
		String stringPoints=getStringofNumber(Integer.valueOf(points),process.getContext().getResources().getInteger(R.integer.MaxDigitsPoints));
		canvas.drawText(stringPoints, getX(), tmpPointsY,
				paint);

		pintarInformacionEnCiertosEstados(canvas, paint);
	}
	public String getStringofNumber(Integer value,int maxdigits){
		int actualDigits=String.valueOf(value).length();
		String aux=new String(new char[maxdigits-actualDigits]).replace('\0', '0');
		
		return aux + value;
	}
	public int measureHeight(String text,Paint paint) {
	    Rect result = new Rect();
	    // Measure the text rectangle to get the height
	    paint.getTextBounds(text, 0, text.length(), result);
	    return result.height();
	}
	private void pintarInformacionEnCiertosEstados(Canvas canvas,
			Paint paint) {
		// En caso de que acierte todos los patos de una ronda, mostraremos
		// una pantalla mostrando un logotipo
		if (process.getState() == IProcess.JUEGO_ESTADO_PERFECT) {
			String text=process.getContext().getResources().getString(R.string.interfacePerfect);
			drawTextInBackground(canvas, paint, text);
//			nt img_points_width =(int) paint.measureText("PERFECT 30.000");
//			Rect bounds=new Rect();
//			paint.getTextBounds("PERFECT 30.000", 0, 1, bounds);
//			drawCuadroCentral(canvas);
//			//drawCuadroCentral(canvas,img_points_width,bounds.height());
//			paint.setColor(0xFFCCF543);
//			paint.setTextAlign(Paint.Align.CENTER);
//			canvas.drawText("PERFECT", canvas.getWidth() / 2,
//					(canvas.getHeight() / 2) - 5, paint);
//			canvas.drawText("30.000", canvas.getWidth() / 2,
//					(canvas.getHeight() / 2) + 5, paint);
		} else if (process.getState() == IProcess.JUEGO_ESTADO_INTRO) {
			//drawCuadroCentral(canvas);
			String text=process.getContext().getResources().getString(R.string.interfaceRound)
					+ " " + process.getInfo("ROUND");
//			int img_points_width =(int) paint.measureText(text);
//			Rect bounds=new Rect();
//			paint.getTextBounds(text, 0, 1, bounds);
//			drawCentralCuadroCentral(canvas, img_points_width, bounds.height());
//			paint.setColor(0xFFCCF543);
//			paint.setTextAlign(Paint.Align.CENTER); 
			drawTextInBackground(canvas, paint, text);
//			canvas.drawText(
//					process.getContext().getResources().getString(R.string.juego_ronda)
//							+ " " + process.getInfo("ROUND"), canvas.getWidth() / 2,
//					(canvas.getHeight() / 2), paint);
//			canvas.drawRect(zone.getX(), zone.getY(),
//					zone.getX() + zone.getWidth(), zone.getY() + zone.getHeight(),
//					paint);
		} else if (process.getState() == IProcess.JUEGO_ESTADO_PAUSA) {
			// Si se encuentra en el estado de pausa, mostramos un letrero
			// que lo indique.
			String text=process.getContext().getResources().getString(R.string.interfacePause);
			drawTextInBackground(canvas, paint, text);
//			drawCuadroCentral(canvas);
//			paint.setColor(0xFFCCF543);
//			paint.setTextAlign(Paint.Align.CENTER);
//			canvas.drawText(
//					process.getContext().getResources().getString(R.string.menu_pausa),
//					canvas.getWidth() / 2, (canvas.getHeight() / 2), paint);
		} else if (process.getState() == IProcess.JUEGO_ESTADO_PARTIDATERMINADA) {
			String text=process.getContext().getResources().getString(R.string.interfaceGameOver);
			drawTextInBackground(canvas, paint, text);
			//			drawCuadroCentral(canvas);
//			paint.setColor(0xFFCCF543);
//			paint.setTextAlign(Paint.Align.CENTER);
//			canvas.drawText(
//					process.getContext().getResources().getString(
//							R.string.juego_gamover), canvas.getWidth() / 2,
//					(canvas.getHeight() / 2), paint);
		}

	}
	private int getTextHeight(Paint paint,String string){
		Rect bounds=new Rect();
		paint.getTextBounds(string, 0, 1, bounds);
		return bounds.height();
	}
	private int getTextWidth(Paint paint,String string){
		
		return (int) paint.measureText(string);
	}
	private void drawTextInBackground(Canvas canvas,Paint paint,String text){
		int width=canvas.getWidth();
		int height=canvas.getHeight();
		int backgroundWidth=getTextWidth(paint, text)+borderPopup*2;
		int backgroundHeight=getTextHeight(paint, text)+borderPopup*2;
		int left=(width-backgroundWidth)>>1;
		int top=(height-backgroundHeight)>>1;
		int right=left + backgroundWidth;
		int bot=top+backgroundHeight;
				
		drawCuadroCentral(canvas, left,top,right,bot);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText(text, width / 2,
				(canvas.getHeight() / 2)+getTextHeight(paint, text)/2, paint);
//		paint.setColor(0xFFFFFF00);
//		canvas.drawRect(0, height/2, width, (height/2+1), paint);
//		paint.setColor(0xFF0000FF);
//		canvas.drawRect(0, (height/2)-getTextHeight(paint, text)/2,
//				width/2,(height/2)+getTextHeight(paint, text)/2,paint);
		
	}
	private void drawCuadroCentral(Canvas canvas, int left, int top, int right,
			int bot) {
		float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
		RectF inset = new RectF(2, 2, 2, 2);

		ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR,
				inset, outerR));

		ShapeDrawable mDrawables2 = new ShapeDrawable(new RoundRectShape(
				outerR, null, outerR));
		mDrawables.getPaint().setColor(0xFFCCF543);

		mDrawables.setBounds(left, top, right, bot);
		mDrawables2.setBounds(left, top, right, bot);
		mDrawables2.draw(canvas);
		mDrawables.draw(canvas);
	}
	private void drawCentralCuadroCentral(Canvas canvas, int width,
			int height) {
		float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
		RectF inset = new RectF(2, 2, 2, 2);

		ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR,
				inset, outerR));

		ShapeDrawable mDrawables2 = new ShapeDrawable(new RoundRectShape(
				outerR, null, outerR));
		mDrawables.getPaint().setColor(0xFFCCF543);

		mDrawables.setBounds(canvas.getWidth()/2-width/2, canvas.getHeight()/2-height/2, canvas.getWidth()/2+width, canvas.getHeight()/2+height);
		mDrawables2.setBounds(canvas.getWidth()/2-width/2, canvas.getHeight()/2-height/2, canvas.getWidth()/2+width, canvas.getHeight()/2+height);
		mDrawables2.draw(canvas);
		mDrawables.draw(canvas);
	}
	private void drawCuadroCentral(Canvas canvas) {
		float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
		RectF inset = new RectF(2, 2, 2, 2);

		ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(
				outerR, inset, outerR));

		ShapeDrawable mDrawables2 = new ShapeDrawable(new RoundRectShape(
				outerR, null, outerR));
		mDrawables.getPaint().setColor(0xFFCCF543);

		mDrawables
				.setBounds((canvas.getWidth() / 2) - 60,
						(canvas.getHeight() / 2) - 25,
						(canvas.getWidth() / 2) + 60,
						(canvas.getHeight() / 2) + 25);
		mDrawables2
				.setBounds((canvas.getWidth() / 2) - 60,
						(canvas.getHeight() / 2) - 25,
						(canvas.getWidth() / 2) + 60,
						(canvas.getHeight() / 2) + 25);
		mDrawables2.draw(canvas);
		mDrawables.draw(canvas);
	}


	public void load(Resources resources, IProcess process) {
		this.process = process;
		fontSize=resources.getInteger(R.integer.font_size);
		borderPopup=(int)resources.getDimension(R.dimen.border_pop_up);
		listDrawable = new ArrayList<Bitmap>();

		for (int i = 0; i < resourcesDrawable.length; i++) {
			listDrawable.add(i, BitmapFactory.decodeResource(resources,
					resourcesDrawable[i]));
		}
		int img_bala_height = ((Bitmap) (listDrawable.get(BALA))).getHeight();
		int img_duck_height = ((Bitmap) (listDrawable.get(DUCK))).getHeight();
		int img_points_height = 15;
		int max_height = 15;

		int img_bala_width = ((Bitmap) (listDrawable.get(BALA))).getWidth();
		int img_duck_width = ((Bitmap) (listDrawable.get(DUCK))).getWidth();
		Paint paint=new Paint();
		paint.setTextSize(fontSize);
		 img_points_width =(int) paint.measureText("000000000000");
		Rect bounds=new Rect();
		paint.getTextBounds("000000000000", 0, 1, bounds);
		int iconHeightorfontHeight=img_duck_height>bounds.height()?img_duck_height:bounds.height();
		setHeight(iconHeightorfontHeight + 2 * extraborder);
		setWidth((img_bala_width * 3) + (img_duck_width * 10)
				+ img_points_width + (11 * intraborder) + (6 * extraborder));

		// imagenBala = BitmapFactory.decodeResource(resources,
		// R.drawable.bala);
		// imagenInterfazPatoAcertado = BitmapFactory.decodeResource(resources,
		// R.drawable.duck);
		// imagenInterfazPatoFallado= BitmapFactory.decodeResource(resources,
		// R.drawable.duckwhite);

	}


	

	@Override
	public void init(List<MultiDrawable> listDrawable) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Añade un efecto de parpadeo sobre los patos que se pintan sobre la
	 * interfaz cuando termina la ronda actual
	 * 
	 */

	public boolean pintadoEspecialSiEstaMostrandoResultados() {
		return ((process.getState() != process.JUEGO_ESTADO_MOSTRANDO_RESULTADO && process.getState() != process.JUEGO_ESTADO_PARTIDATERMINADA) || System
				.currentTimeMillis() % 200 > 100);
	}


	private boolean pintadoInterfazMientrasJuega() {
		return process.getState() == process.JUEGO_ESTADO_PLAY
				|| process.getState() == process.JUEGO_ESTADO_PAUSA;
	}
	private boolean patoAcertado(Diablo pato) {
		return pato.getEstadoActual() == Diablo.STATE_HITTED
				|| pato.getEstadoActual() == Diablo.STATE_CAYENDO
				|| pato.getEstadoActual() == Diablo.STATE_MUERTO;
	}

	private boolean patoFallado(Diablo pato) {
		return pato.getEstadoActual() == Diablo.STATE_HUYENDO
				|| pato.getEstadoActual() == Diablo.STATE_ESCAPO;
	}

	/**
	 * Añade un efecto de parpadeo sobre los patos que se pintan sobre la
	 * interfaz cuando termina la ronda actual
	 * 
	 */
	public void pintandoLosPatosDeLaInterfaz(Canvas canvas, int indicePato,
			int x, int y, Paint paint) {
		boolean esElPatoActual = false;
		ArrayList<Diablo> enemys = (ArrayList<Diablo>)process.getAtributte("enemys");
		ArrayList<Boolean>ducksFaileds = (ArrayList<Boolean>)process.getAtributte("ducksFaileds");
		
		if (pintadoInterfazMientrasJuega()) {
			for (Diablo e:enemys) {
				if (e.getID() == indicePato) { // Si es el Pato Actual
					if (e.getEstadoActual() == Diablo.STATE_MOVE) {
						// Si esta en movimiento, mostraremos el pato blanco
						// con un efecto
						// de parpadeo
						if (System.currentTimeMillis() % 500 > 250) {
							canvas.drawBitmap(listDrawable.get(DUCKWHITE), x,
									y, paint);
						}
					} else if (patoAcertado(e)) {
						// Si el pato esta muerto, o esta cayendo, usamos el
						// pato rojo
						canvas.drawBitmap(listDrawable.get(DUCK), x, y,
								paint);
					} else if (patoFallado(e)) {
						// Si el pato esta huyendo, lo pintamos blanco.
						canvas.drawBitmap(listDrawable.get(DUCKWHITE), x, y,
								paint);
					}
					esElPatoActual = true;
				}
			}
		}
		if (!esElPatoActual) {
			// Si No Es el Pato Actual, accedemos al array donde guardamos
			// el historial de los patos acertados
			// en esta ronda.
			canvas.drawBitmap(ducksFaileds.get(indicePato) ? listDrawable.get(DUCK)
							: listDrawable.get(DUCKWHITE), x, y, paint);
		}

	}

}
