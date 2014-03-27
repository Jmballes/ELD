package com.paintview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.draw.MultiDrawable;
import com.draw.Zone;
import com.jmbdh.MenuActivity;
import com.jmbdh.MultiComparator;
import com.newproyectjmb.R;
import com.objects.Arbusto;
import com.objects.BackGround;
import com.objects.Courtain;
import com.objects.Logo;
import com.objects.Diablo;
import com.objects.Perro;
import com.objects.Estalactitas;
import com.paintview.JuegoView.JuegoThread;

public class ProcessPresentation implements IProcess {
	public JuegoView juegoView;
	public JuegoThread juegoThread;
	Activity activity;
	int state;
	int subState;
	int savedState;
	/** Contexto de nuestra aplicación */
	public Context mContext;
	int indicePatos = 0;

	Perro perro;
	BackGround background;
	Estalactitas estalactitas;

	Arbusto arbusto;

	Logo logo;
	public ArrayList<Diablo> ducks;
	Courtain courtain;
	private List<MultiDrawable> listDrawable;

	public static final int TOTAL_DUCKS = 10;

	public ProcessPresentation(Activity activity, JuegoView juegoView2,
			JuegoThread thread, Context context) {
		this.activity = activity;
		this.juegoView = juegoView2;
		this.juegoThread = thread;
		this.mContext = context;

		state = PRESENTATION_STATE_INIT;
	}

	public void resetvalues() {

		for (Diablo pato : ducks) {
			BackGround background = (BackGround) getMultiDrawable(
					getListDrawable(), "com.objects.BackGround");
			pato.iniciarCicloVolando(indicePatos, background.getHorizont_Y());
			indicePatos++;

		}
		juegoThread.timeGame = 0;
		setState(JUEGO_ESTADO_PLAY);

	}

	public MultiDrawable getMultiDrawable(List<MultiDrawable> list, String name) {
		for (MultiDrawable multidrawable : list) {
			if (multidrawable.getClass().getName().equals(name)) {
				return multidrawable;
			}
		}
		return null;
	}

	/**
	 * Esta función se encargará de indicar a los patos que se procesen, y en
	 * caso de que haya terminado la minifase, decidira si mostrará la
	 * indicación de victoria o derrota.
	 */
	public void processDucks() {
		for (Diablo e : ducks) {

			e.process();
			if (patosEnPantalla() == 0) {
				setState(JUEGO_ESTADO_DERROTA);
				perro.iniciarCicloDerrota();

				break;
			}

		}
	}

	public int patosEnPantalla() {
		int contador = 0;
		for (Diablo e : ducks) {
			if (e.getEstadoActual() != Diablo.STATE_ESCAPO
					&& e.getEstadoActual() != Diablo.STATE_MUERTO) {
				contador++;
			}
		}
		return contador;
	}

	@Override
	public void process(int timeProcess) {

		juegoThread.timeGame += timeProcess;
		switch (getState()) {
		case PRESENTATION_STATE_PAUSE:
			break;
		case PRESENTATION_STATE_INIT:

			Resources resources = mContext.getResources();

			Bitmap imagenPato = BitmapFactory.decodeResource(resources,
					R.drawable.avatar);

			Bitmap imagenPerro = BitmapFactory.decodeResource(resources,
					R.drawable.perro);

			background = new BackGround();
			background.load(resources, this);
			estalactitas = new Estalactitas();
			estalactitas.load(resources, this);

			arbusto = new Arbusto();
			arbusto.load(resources, this);

			logo = new Logo();
			logo.load(resources, this);

			ducks = new ArrayList<Diablo>();

			courtain = new Courtain();
			courtain.load(resources, this);

			listDrawable = new ArrayList<MultiDrawable>();

			listDrawable.add(logo);
			logo.init(listDrawable);
			setState(PRESENTATION_STATE_LOGO_COMPANY);
			setSubState(SUBLOGO_COMPANY_FADEIN);
			juegoThread.timeGame = 0;
			break;
		case PRESENTATION_STATE_MENU_INIT:
			courtain.initCortinilla(false, juegoThread.mCanvasWidth,
					juegoThread.mCanvasHeight, Courtain.COLOR_WHITE);
			listDrawable = new ArrayList<MultiDrawable>();
			listDrawable.add(background);
			listDrawable.add(estalactitas);
			listDrawable.add(logo);
			listDrawable.add(arbusto);
			// listDrawable.add(perro);
			listDrawable.add(courtain);
			int z = 0;
			for (MultiDrawable drawable : listDrawable) {
				drawable.init(listDrawable);
				z += 10;
			}
			int portion = juegoThread.mCanvasWidth / TOTAL_DUCKS;
			int xdestiny = 0;
			for (Diablo pato : ducks) {
				pato.iniciarCicloVolandoIntro(indicePatos,
						background.getHorizont_Y(), xdestiny);

				indicePatos++;
				xdestiny += portion;
			}
			for (MultiDrawable drawable : listDrawable) {
				drawable.setZ(z);
				z += 10;
			}
			// BackGround tmpbackground = (BackGround) getMultiDrawable(
			// getListDrawable(), "com.objects.BackGround");
			// Arbusto tmparbusto = (Arbusto) getMultiDrawable(listDrawable,
			// "com.objects.Arbusto");
			// perro.iniciarEstadoDeIntro(tmpbackground.getHorizont_Y(),
			// (int)
			// (tmparbusto.getY()),(int)tmparbusto.getZ()+1,(int)tmparbusto.getZ()-1);
			setState(PRESENTATION_STATE_COURTAIN_OPENING_MENU);
			break;
		// case PRESENTATION_STATE_COURTAIN_OPENING:
		// break;
		case PRESENTATION_STATE_LOGO_COMPANY:

			switch (subState) {
			case SUBLOGO_COMPANY_FADEIN:
				//
				int alphain = (int) ((juegoThread.timeGame * 255) / 2000);
				logo.process(alphain);
				if (juegoThread.timeGame > 2000) {
					logo.process(255);
					juegoThread.timeGame = 0;
					setSubState(SUBLOGO_COMPANY_STILL);
				}

				break;
			case SUBLOGO_COMPANY_STILL:
				
				if (juegoThread.timeGame > 5000) {
					juegoThread.timeGame = 0;
					setSubState(SUBLOGO_COMPANY_FADEOUT);
				}

				break;
			case SUBLOGO_COMPANY_FADEOUT:
				int alphaout = (int) (((2000 - juegoThread.timeGame) * 255) / 2000);
				logo.process(alphaout);
				if (juegoThread.timeGame > 2000) {
					logo.process(0);
					setState(PRESENTATION_STATE_MENU_INIT);
				}
				break;
			}

			break;
		case PRESENTATION_STATE_COURTAIN_OPENING_MENU:
			if (courtain.processCortinilla()) {
				setState(PRESENTATION_STATE_MENU);
				juegoThread.timeGame=0;
				subState = 0;
			}

			break;

		case PRESENTATION_STATE_MENU: {
			// int count=
			// (int)((System.currentTimeMillis()-juegoThread.timeGame) /1000);
			if (juegoThread.timeGame > 500) {
				juegoThread.timeGame = 0;
				subState++;
				MenuActivity primera = (MenuActivity) activity;
				final int var = subState - 1;
				if (subState >= 0) {

					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							MenuActivity primera = (MenuActivity) activity;
							// primera.setVisible(View.VISIBLE);
							primera.introFinished(var);

						}
					});
				}
			}

		}
			break;
		case PRESENTATION_STATE_COURTAIN_CLOSING_INIT: {
			courtain.initCortinilla(true, juegoThread.mCanvasWidth,
					juegoThread.mCanvasHeight, Courtain.COLOR_BLACK);
			setState(PRESENTATION_STATE_COURTAIN_CLOSING);
		}
			break;
		case PRESENTATION_STATE_COURTAIN_CLOSING: {
			if (courtain.processCortinilla()) {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						MenuActivity primera = (MenuActivity) activity;
						primera.startNewGame();
					}
				});
				// juegoThread.j.getHandler().sendEmptyMessage(MESSAGE_SENT_CLOSING_PRESENTATION_FINISHED);
				setState(PRESENTATION_STATE_COURTAIN_CLOSED);
			}
		}
			break;
		}

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	@Override
	public void setPause() {
		if (getState() != PRESENTATION_STATE_PAUSE) {
			// efectos_de_sonido.pausarSonido();

			savedState = getState();
			setState(PRESENTATION_STATE_PAUSE);

		}
	}

	@Override
	public void resumePause() {

		setState(savedState);


	}

	@Override
	public String getInfo(String typeInfo) {

		return null;
	}

	public int getState() {
		return state;
	}

	@Override
	public void setMode(int mode) {

	}

	public static final String CANVAS_WIDTH = "CANVAS_WIDTH";
	public static final String CANVAS_HEIGHT = "CANVAS_HEIGHT";

	@Override
	public int getValue(String typeInfo) {
		if (CANVAS_WIDTH.equals(typeInfo)) {
			return juegoThread.mCanvasWidth;
		} else if (CANVAS_HEIGHT.equals(typeInfo)) {
			return juegoThread.mCanvasHeight;
		}
		return 0;
	}

	@Override
	public Object getAtributte(String attribute) {
		Object object = null;

		return object;
	}

	@Override
	public int requestSound(int s) {
		int aux = -1;

		return aux;
	}

	@Override
	public void setState(int state) {
		this.state = state;

	}

	@Override
	public List<MultiDrawable> getListDrawable() {
		// TODO Auto-generated method stub
		return listDrawable;
	}

	@Override
	public void setListDrawable(List<MultiDrawable> listDrawable) {
		this.listDrawable = listDrawable;

	}

	@Override
	public void doDraw(Canvas canvas) {
		Paint paint = new Paint();

		if (getState() == IProcess.PRESENTATION_STATE_LOGO_COMPANY) {
			paint.setColor(0xFFFFFFFF);
			canvas.drawRect(0, 0, canvas.getWidth(), (int) canvas.getHeight(),
					paint);
		}
		if (getState() > IProcess.JUEGO_ESTADO_INICIO) {
			Collections.sort(listDrawable, new MultiComparator());
			for (MultiDrawable drawable : listDrawable) {
				Zone zone;
				zone = new Zone(0, 0, 0, canvas.getWidth(),
						(int) canvas.getHeight());

				drawable.draw(canvas, paint, zone);
				if (juegoView.debug_mode) {
					drawable.drawDebug(canvas, paint);
				}

			}
		}
	}

	@Override
	public void destroy() {

		for (MultiDrawable drawable : listDrawable) {
			drawable = null;
		}

	}

	public void pausarSonido(int lastSound) {

	}

	@Override
	public JuegoView getView() {

		return juegoView;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void surfaceCreated() {
		if (getState() == PRESENTATION_STATE_PAUSE) {
			resumePause();
		}

	}

	public int getSubState() {
		return subState;
	}

	public void setSubState(int subState) {
		this.subState = subState;
	}

	@Override
	public void setAvgFps(String string) {
		// TODO Auto-generated method stub
		
	}
}
