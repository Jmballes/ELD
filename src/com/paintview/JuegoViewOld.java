/*
 * Copyright (C) 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.paintview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.hardware.SensorListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.jmbdh.IActivityHandler;

public class JuegoViewOld extends SurfaceView implements SurfaceHolder.Callback,
		SensorListener {

	/**
	 * Info About Device
	 */
	public int deviceWidth;
	public int deviceHeight;
	public float deviceDensity;
	public float deviceDensityDpi;
	public float deviceWidthPixels;
	public float deviceHeightPixels;
	public float deviceXdpi;
	public float deviceYdpi;
	public IProcess process;

	public boolean debug_mode = false;
	public boolean sound = false;
	public boolean vibrate = false;
	public int control = 0;

	public class JuegoThread extends Thread {
		public IActivityHandler j;

		public void setActivity(IActivityHandler j) {
			this.j = j;
		}

		Activity activity;

		/** Valor que contendra el Alto de nuestra pantalla */
		public int mCanvasHeight = -1;

		/** Valor que contendra el Ancho de nuestra pantalla */
		public int mCanvasWidth = -1;

		/** Vector que contendra los patos de una minifase. */

		/** Indicate whether the surface has been created & is ready to draw */
		private boolean mRun = false;

		/** Handle to the surface manager object we interact with */
		private SurfaceHolder mSurfaceHolder;

		public JuegoThread(SurfaceHolder surfaceHolder, Context context) {
			mSurfaceHolder = surfaceHolder;
			mContext = context;

		}

		public long lastTime;
		public int timeAccum;
		public static final int GAP_PROCESS = 10; // tiempo que pasa entre cada
													// proceso (ms)
		public static final int MAX_TIME_ALLOWED_BETWEEN_FRAMES = GAP_PROCESS * 2;

		long getTime() {
			long currentTime = System.currentTimeMillis();
			long ret = currentTime - lastTime;
			lastTime = currentTime;

			return (ret < MAX_TIME_ALLOWED_BETWEEN_FRAMES) ? ret
					: MAX_TIME_ALLOWED_BETWEEN_FRAMES;
		}

		public boolean paint = false;

		@Override
		public void run() {

			lastTime = System.currentTimeMillis();
			timeAccum = 0;
			mRun = true;

			while (mRun) {

				paint = false;
				timeAccum += getTime();
				Canvas canvas = null;

				try {
					canvas = mSurfaceHolder.lockCanvas(null);
					while (timeAccum >= GAP_PROCESS && mRun) {
						paint = true;
						process.process(GAP_PROCESS);
						timeAccum -= GAP_PROCESS;
					}

					if (paint) {
						doDraw(canvas);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
				} finally {
					if (canvas != null)
						mSurfaceHolder.unlockCanvasAndPost(canvas);
				}

				try {
					Thread.sleep(5);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}

		/**
		 * Establece si el thread se esta ejecutando o no.
		 */

		public void setRunning(boolean b) {
			mRun = b;
		}
		public boolean getRunning() {
			return mRun ;
		}
		private void doDraw(Canvas canvas) {
			if (process !=null && canvas!=null)process.doDraw(canvas);

		}

		public long timeGame = 0;


		public long getTiempoTranscurrido() {
			return System.currentTimeMillis() - timeGame;
		}

		/**
		 * Cada vez que nuestra aplicación detecte un cambio en el tamaño de
		 * pantalla (solo es posible al inicio de la aplicación, y cuando el
		 * usuario abre o cierra el teclado QWERTY) actualizaremos nuestras dos
		 * variables que guardan el ancho y alto de nuestro entorno de pintado.
		 * Además, añadiremos una restricción para que solo sea al inicio de la
		 * aplicación, impidiendo asi poder jugar en modo horizontal.
		 * 
		 * @param width
		 *            Ancho al que se inicializará nuestra pantalla
		 * @param height
		 *            Alto al que se inicializara nuestra pantalla
		 */
		public void estableceTamanio(int width, int height) {
			synchronized (mSurfaceHolder) {
				// Si no ha sido previamente inicializadas nuestras variables,
				// las inicializamos ahora.
				if (mCanvasWidth == -1) {
					mCanvasWidth = width;
					mCanvasHeight = height;
				}

			}
		}
	}

	/** Contexto de nuestra aplicación */
	public Context mContext;

	/** Thread de nuestra aplicación */
	public JuegoThread thread;

	public Activity activity;

	/**
	 * Establece el numero de patos que tendra una minifase
	 * 
	 * @param total
	 *            de patos
	 */
	public void setTotalDePatosPorMiniFase(int total) {
		process.setMode(total);
	}

	public void setState(int state) {
		process.setState(state);
	}

	public JuegoViewOld(Context context, AttributeSet attrs) {

		super(context, attrs);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// Aqui creamos el thread solo, lo iniciaremos en surfaceCreated()
		thread = new JuegoThread(holder, context);
		// paintGame=new PaintGame(this,thread,context);
		// process=new ProcessGame(this,thread,context,paintGame);
		// paintGame.setProcess(process);
		// Establecemos este View para que coja el foco, y pueda interactuar el
		// usuario con él.
		setFocusable(true);
		setFocusableInTouchMode(true);
		// Cargamos la preferencias del usuario
		readPreferences();

	}

	public void init(Activity activity, String processType) {
//		if ("ProcessGame".equals(processType)) {
//			process = new ProcessGame(this, thread, mContext);
//
//		} else if ("ProcessPresentation".equals(processType)) {
//			activity = activity;
//			process = new ProcessPresentation(activity, this, thread, mContext);
//
//		}
	}

	/**
	 * Cuando el usuario modifica sus preferencias, debemos actualizarlas en
	 * caliente durante la partida, además de al inicio de esta. Lo que haremos
	 * es cargar las opciones de sonido, vibración, y el modo de control que
	 * desea el jugador.
	 */
	public void readPreferences() {
		SharedPreferences app_preferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		sound = app_preferences.getBoolean("preference_sonido", false);
		vibrate = app_preferences.getBoolean("preference_vibracion", false);
		String valor_control = app_preferences.getString(
				"preference_controles", "no valor");
		control =  IProcess.CONTROL_TOUCH_MODE;
//		if (valor_control.equals("tm")) {
//			// Touch Mode
//			control = IProcess.CONTROL_TOUCH_MODE;
//		} else {
//			// Move Mode
//			control = IProcess.CONTROL_MOVE_MODE;
//		}

	}

	public JuegoThread getThread() {
		return thread;
	}

	/**
	 * Chequea si esta en el Modo Touch, y en ese caso, añade una solicitud de
	 * Disparo
	 * 
	 */
	public boolean onTouchEvent(MotionEvent event) {

		return process.onTouchEvent(event);

	}

	/**
	 * Chequea si esta en el Modo Move, y en ese caso, comprueba que haya sido
	 * pulsado el trackball, y en ese caso añadira una SolicitudDeDisparo
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		System.out.println("------Intentando añadir disparo");
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				&& control == IProcess.CONTROL_MOVE_MODE) {
			// thread.aniadirSolicitudDisparo(thread.realPointX,
			// thread.realPointY);
			System.out.println("Intentando añadir disparo");
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			thread.j.showMenu();
			return true;
		}
		return false;
	}

	/**
	 * MOVE MODE
	 * 
	 * Move Mode es un modo de control del juego que nos permitira, gracias al
	 * acelerometro, mediante el movimiento de nuestro telefono movil, poder
	 * desplazar la posición de nuestro cursor.
	 * 
	 */

	// public final int MAXIMO_VALOR_EN_HORIZONTAL_RECIBIDO = 90 ;
	// public final int MAXIMO_VALOR_EN_VERTICAL_RECIBIDO = 90 ;
	// public final int PORCENTAJE_FINAL_EN_HORIZONTAL = 150 ;
	// public final int PORCENTAJE_FINAL_EN_VERTICAL = 150 ;
	// public int getAyudaEnHorizontal(int valor){
	// return (valor*PORCENTAJE_FINAL_EN_HORIZONTAL)/100;
	// }
	// public int getAyudaEnVertical(int valor){
	// return (valor*PORCENTAJE_FINAL_EN_VERTICAL)/100;
	// }
	// public int getTotalMovementHorizontal(){
	// return thread.mCanvasWidth/2;
	// }
	// public int getTotalMovementVertical(){
	// return thread.mCanvasHeight/2;
	// }
	// public int correctvalueX(float v){
	// return
	// (int)((getTotalMovementHorizontal()*getAyudaEnHorizontal((int)v))/MAXIMO_VALOR_EN_HORIZONTAL_RECIBIDO);
	// }
	// public int correctvalueY(float v) {
	// return (int) ((getTotalMovementVertical() * getAyudaEnVertical((int) v))
	// / MAXIMO_VALOR_EN_VERTICAL_RECIBIDO);
	// }

	public void onSensorChanged(int sensor, float[] values) {
		if (process != null) {
			process.onSensorChanged(sensor, values);
		}
	}

	public void onAccuracyChanged(int sensor, int accuracy) {

	}

	/**
	 * Llamada de retorno invocada cuando la dimension de nuestra superficie
	 * cambia
	 * 
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		thread.estableceTamanio(width, height);

		WindowManager wm = (WindowManager) process.getView().mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point(); 
		try {
			display.getSize(size);
			deviceWidth = size.x;
			deviceHeight = size.y;
		 } catch (java.lang.NoSuchMethodError ignore) { // Older device
			 deviceWidth = display.getWidth();
			 deviceHeight = display.getHeight();
		    }
		
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		deviceDensity = metrics.density;
		deviceDensityDpi = metrics.densityDpi;
		deviceWidthPixels = metrics.widthPixels;
		deviceHeightPixels = metrics.heightPixels;
		deviceXdpi = metrics.xdpi;
		deviceYdpi = metrics.ydpi;

	}

	/**
	 * Llamada de retorno invocada cuando la Superficie es creada y lista para
	 * usarse
	 * 
	 */

	public void surfaceCreated(SurfaceHolder holder) {
		// Iniciamos aqui el Thread
		if (!thread.getRunning()) {
			thread.setRunning(true);
			thread.start();
		}
		if (process!=null){
			process.surfaceCreated();
		}
		
	}

	/**
	 * Llamada de retorno invocada cuando la Superficie es cerrada
	 * 
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (thread != null) {
			if (process!=null){
				process.setPause();
			}
			//thread.setRunning(false);

		}

//		// Indicamos al thread que se cierre, y esperamos hasta que se termine,
//		// sino esta podria recibir un evento y podria saltar un error
//		boolean retry = true;
//		// System.out.println("lalallaa");
//		thread.setRunning(false);
//		while (retry) {
//			try {
//				thread.join();
//				retry = false;
//			} catch (InterruptedException e) {
//			}
//		}
//		destroy();

	}

	public void destroy() {
		if (process != null) {
			process.destroy();
			process = null;
		}
		if (thread != null) {
			thread.setRunning(false);

		}
		System.gc();
	}

}
