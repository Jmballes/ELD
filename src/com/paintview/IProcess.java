package com.paintview;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.draw.MultiDrawable;

public interface IProcess {
	//Variables


	
	//Constants
	public static final int CONTROL_TOUCH_MODE = 0;
	public static final int CONTROL_MOVE_MODE = 1;
	
	public static final int JUEGO_ESTADO_INICIO = 0;
	public static final int JUEGO_ESTADO_COURTAIN = 1;
	public static final int JUEGO_ESTADO_NUEVA_RONDA = 2;
	public static final int JUEGO_ESTADO_INTRO = 3;
	public static final int JUEGO_ESTADO_RESET = 4;
	public static final int JUEGO_ESTADO_PLAY = 5;
	public static final int JUEGO_ESTADO_DERROTA = 6;
	public static final int JUEGO_ESTADO_VICTORIA = 7;
	public static final int JUEGO_ESTADO_CALCULANDO_RESULTADO = 8;
	public static final int JUEGO_ESTADO_MOSTRANDO_RESULTADO = 9;
	public static final int JUEGO_ESTADO_PERFECT = 10;
	public static final int JUEGO_ESTADO_PAUSA = 11;
	public static final int JUEGO_ESTADO_PARTIDATERMINADA = 12;
	public static final int JUEGO_ESTADO_COURTAIN_CLOSING_INIT = 13;
	public static final int JUEGO_ESTADO_COURTAIN_CLOSING = 14;
	public static final int JUEGO_ESTADO_COURTAIN_CLOSED = 15;
	
	public static final int PRESENTATION_STATE_INIT = 20;
	
	public static final int PRESENTATION_STATE_LOGO_COMPANY = PRESENTATION_STATE_INIT+1;
		public static final int SUBLOGO_COMPANY_FADEIN = 0;
		public static final int SUBLOGO_COMPANY_STILL = 1;
		public static final int SUBLOGO_COMPANY_FADEOUT = 2;
	public static final int PRESENTATION_STATE_MENU_INIT = PRESENTATION_STATE_LOGO_COMPANY+1;
	public static final int PRESENTATION_STATE_COURTAIN_OPENING_MENU = PRESENTATION_STATE_MENU_INIT+1;
	public static final int PRESENTATION_STATE_MENU =PRESENTATION_STATE_COURTAIN_OPENING_MENU+1;
	public static final int PRESENTATION_STATE_COURTAIN_CLOSING_INIT =PRESENTATION_STATE_MENU+1;
	public static final int PRESENTATION_STATE_COURTAIN_CLOSING =PRESENTATION_STATE_COURTAIN_CLOSING_INIT+1;
	public static final int PRESENTATION_STATE_COURTAIN_CLOSED =PRESENTATION_STATE_COURTAIN_CLOSING+1;
	public static final int PRESENTATION_STATE_PAUSE =PRESENTATION_STATE_COURTAIN_CLOSED+1;
	
	public static final int MESSAGE_SENT_OPENING_PRESENTATION_FINISHED=0;
	public static final int MESSAGE_SENT_CLOSING_PRESENTATION_FINISHED=1;
	
	
	public static final int MESSAGE_SENT_CLOSING_GAME=2;
	//Methods
	int getState();
	void setState(int state);
	 List<MultiDrawable> getListDrawable();
	void setListDrawable(List<MultiDrawable> listDrawable);

	void process(int timeProcess);
	void onStop();
	void onSensorChanged(int sensor, float[] values);
	boolean onTouchEvent(MotionEvent event);
	void setPause();
	void resumePause();
	void setMode(int mode);
	String getInfo(String typeInfo);
	int getValue(String typeInfo);
	MultiDrawable getMultiDrawable(List<MultiDrawable> list, String name);
	Object getAtributte(String Attribute);
	int requestSound(int s);
	void pausarSonido(int lastSound);
	void doDraw(Canvas canvas);
	void destroy();
	JuegoView getView();
	Context getContext();
	void surfaceCreated();
	void setAvgFps(String string);
}
