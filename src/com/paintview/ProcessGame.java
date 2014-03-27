package com.paintview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.draw.MultiDrawable;
import com.draw.ObjetoAnimable;
import com.draw.Zone;
import com.jmbdh.Efectos_Sonido;
import com.jmbdh.MultiComparator;
import com.jmbdh.Sonido_Musica;
import com.newproyectjmb.R;
import com.objects.Arbusto;
import com.objects.BackGround;
import com.objects.Courtain;
import com.objects.Fire;
import com.objects.Interfaz;
import com.objects.Diablo;
import com.objects.Perro;
import com.objects.Estalactitas;
import com.paintview.JuegoView.JuegoThread;

public class ProcessGame implements IProcess {
	public JuegoView juegoView;
	public JuegoThread juegoThread;
	private String avgFps;
	int state;
	int savedState;
	/** Contexto de nuestra aplicación */
	public Context mContext;
	int indicePatos = 0;

	Courtain courtain;
	Perro perro;
	// Serializable objects
	public List<Boolean> ducksFaileds = null;
	static final int TOTAL_DUCKS = 10;

	/** Total de patos que saldran en una minifase */
	public int totalDePatosPorMiniFase = -1;

	public float punteroX;
	public float punteroY;

	public ArrayList<Diablo> enemys;

	/** Vector que contendra los Disparos realizados en una minifase. */
	public Vector disparos = new Vector();

	/**
	 * Clase que almacena los valores necesarios cuando un Disparo se lanza.
	 * Entre ellos su posición x e y. Guardaremos un parametro visible que
	 * consultaremos para saber durante cuanto tiempo tendremos que mostrar
	 * nuestro elemento.
	 * 
	 */
	public class Disparo {
		public int x;
		public int y;
		int time;
		public boolean visible = true;

		public Disparo(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public ProcessGame(JuegoView juegoView2, JuegoThread thread, Context context) {
		this.juegoView = juegoView2;
		this.juegoThread = thread;
		this.mContext = context;

	}

	/**
	 * Esta función mantendra en pantalla durante 300 milisegundos un indicador
	 * de donde se hizo el ultimo disparo.
	 */
	public void procesDisparos(int value) {
		for (int i = 0; i < disparos.size(); i++) {
			Disparo e = (Disparo) disparos.elementAt(i);
			e.time += value;
			if (e.time > 300) {
				e.visible = false;
			}
		}
	}

	public final int[] VALORES_DE_BONIFICACIONES = { 1000, 500, 250 };

	/** Vector que contendra las Bonificaciones obtenidas en una minifase. */
	public Vector bonificaciones = new Vector();

	/**
	 * Clase que almacena los valores necesarios cuando una Bonificación es
	 * obtenida tras haber acertado sobre un pato. Guardaremos un parametro
	 * visible que consultaremos para saber durante cuanto tiempo tendremos que
	 * mostrar el valor que ha obtenido por acertar sobre el pato. Este valor
	 * dependera de la bala con cual se acierte: si es al primer intento,
	 * puntuará más.
	 * 
	 */
	public class Bonificacion {
		public int x;
		public int y;
		public int time;
		public int value;
		public boolean visible = true;

		public Bonificacion(int x, int y, int value) {
			this.x = x;
			this.y = y;
			this.value = value;
		}
	}

	/**
	 * Vector que contendra los eventos cuando el usuario toca sobre la
	 * pantalla.
	 */
	Vector eventosRecibidos = new Vector();

	/**
	 * Añade una solicitud de disparo, esta sera procesada en el thread, que
	 * determinara si se acepta el disparo, dependiendo de si tiene balas
	 * suficientes.
	 */

	public void aniadirSolicitudDisparo(int x, int y) {
		Disparo d = new Disparo(x, y);
		eventosRecibidos.addElement(d);
	}

	/**
	 * Procesa las solicitudes de disparo, y las va eliminando despues.
	 */

	public void procesarSolicitudesDeDisparo() {
		while (eventosRecibidos.size() > 0) {
			comprobandoDisparo((Disparo) (eventosRecibidos.elementAt(0)));
			eventosRecibidos.remove(0);
		}
	}

	/**
	 * Función encargada de procesar un disparo. Primero detectara si aun quedan
	 * balas, y si es asi, mostrará un efecto de sonido y otro de vibración (en
	 * caso de que esten activados). Tras ello, comprobará si ha colisionado con
	 * uno de nuestros patos, siempre que este en el estado de movimiento. Si ha
	 * colisionado, obtendremos una bonificacion e incrementaremos la puntuación
	 * actual, y actualizaremos la interfaz de patos acertados.
	 * 
	 * @param disparo
	 */
	public void procesandoDisparo(Disparo disparo) {
		boolean duck = false;
		// Si hay menos de 3 disparos
		if (disparos.size() < 3) {
			// Añadimos uno
			disparos.add(disparo);
			// Solicitamos efecto de sonido
			requestSound(SONIDO_DISPARO);
			// Solicitamos efecto de vibracion
			solicitarVibraccion();

			// Recorremos los patos para ver si han colisionado con nuestro
			// disparo
			for (Diablo pato : enemys) {
				// Si nuestro personaje esta moviendose
				if (pato.getEstadoActual() == Diablo.STATE_MOVE) {
					// Detectamos si nuestro disparo ha colisionado con el
					// pato
					if (acertoSobreElPato(pato, disparo)) {
						// Añadimos una Bonificación
						bonificaciones.add(new Bonificacion(disparo.x,
								disparo.y, VALORES_DE_BONIFICACIONES[disparos
										.size() - 1]));
						// Incrementamos la puntuación
						incScore(VALORES_DE_BONIFICACIONES[disparos.size() - 1]);
						// Iniciaremos el ciclo de ser disparado
						pato.iniciarCicloDisparado();
						// Actualizamos la interfaz.
						ducksFaileds.set(pato.getID(), true);
						if (patosTodaviaVolando() == 0) {
							efectos_de_musica.pararSonido(MUSICA_VOLAR);
						}
						duck = true;
						break;
					}
				}
			}

		}
		// Si era nuestro ultimo disparo y fallamos
		if (!duck && disparos.size() == 3) {
			// Hacemos que el resto de los patos huyan.
			hacerHuirATodosLosPatos();
		}
	}

	/**
	 * Si pasa un tiempo minimo, o si se queda sin balas, los patos huyen
	 */
	private void hacerHuirATodosLosPatos() {
		for (Diablo e : enemys) {
			if (e.getEstadoActual() == Diablo.STATE_MOVE) {
				e.iniciarCicloHuyendo();

			}
		}
	}

	/**
	 * Devuelve si el disparo colisiona con el pato. Tiene en cuenta que se
	 * calculara de forma distinta dependiendo el modo de control
	 * 
	 * @param pato
	 * @param disparo
	 * @return
	 */
	public boolean acertoSobreElPato(Diablo pato, Disparo disparo) {
		return juegoView.control == CONTROL_TOUCH_MODE
				&& estaPuntoEnCuadrado((int) pato.getX(), (int) pato.getY(),
						(int) pato.getX() + pato.getSize(), (int) pato.getY()
								+ pato.getSize(), disparo.x, disparo.y)
				|| juegoView.control == CONTROL_MOVE_MODE
				&& isIntersectingRect((int) pato.getX(), (int) pato.getY(),
						pato.getSize(), pato.getSize(), disparo.x, disparo.y,
						realPointSize, realPointSize);
	}

	/**
	 * Test for a collision between two rectangles using plane exclusion.
	 * 
	 * @return True if the rectangle for from the b coordinates intersects those
	 *         of a.
	 */
	public final boolean isIntersectingRect(int ax, int ay, int aw, int ah,
			int bx, int by, int bw, int bh) {
		if (by + bh < ay || // is the bottom b above the top of a?
				by > ay + ah || // is the top of b below bottom of a?
				bx + bw < ax || // is the right of b to the left of a?
				bx > ax + aw) // is the left of b to the right of a?
			return false;
		return true;
	}

	/**
	 * Las comprobaciones de disparos se haran si estamos en estado de Juego
	 * 
	 * @param disparo
	 */
	public void comprobandoDisparo(Disparo d) {
		switch (getState()) {
		case JUEGO_ESTADO_PLAY:
			procesandoDisparo(d);
			break;
		}

	}

	/**
	 * Deteca colisión entre una caja que se encuentra entre los puntos
	 * A1(ax,ay) y A2(ax2,ay2); y un punto B (bx,by).
	 * 
	 * @param ax
	 * @param ay
	 * @param ax2
	 * @param ay2
	 * @param bx
	 * @param by
	 * @return true si hay colisión.
	 */
	public final boolean estaPuntoEnCuadrado(int ax, int ay, int ax2, int ay2,
			int bx, int by) {
		return (bx > ax && bx < ax2 && by > ay && by < ay2);
	}

	/**
	 * Inicia la vibración del movil durante 500 milisegundos siempre que dicha
	 * opción este habilitada por el usuario.
	 */
	public void solicitarVibraccion() {
		if (juegoView.vibrate) {
			Vibrator vibrator = (Vibrator) mContext
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(500);
		}
	}

	/**
	 * Una ronda constara de 10 minifases en el modo de juego de un pato, y de 5
	 * minifases en el modo de juego dos patos. Entre minifases, habra que
	 * reiniciar una serie de parametros necesarios, ademas de crear lo/s pato/s
	 * necesarios. Tambien comprobaremos si se han completado todas las
	 * minifases, y en ese caso cambiaremos a un estado donde mostraremos el
	 * resultado de la ronda.
	 */
	public void resetvalues() {
		if (currentDuck == TOTAL_DUCKS - totalDePatosPorMiniFase) {
			setState(JUEGO_ESTADO_CALCULANDO_RESULTADO);
		} else {

			disparos = new Vector();
			bonificaciones = new Vector();

			for (Diablo pato : enemys) {
				BackGround background = (BackGround) getMultiDrawable(
						getListDrawable(), "com.objects.BackGround");
				pato.iniciarCicloVolando(indicePatos,
						background.getHorizont_Y());
				indicePatos++;

			}
			juegoThread.timeGame = 0;
			setState(JUEGO_ESTADO_PLAY);
			resetMusics();
			solicitarMusica(MUSICA_VOLAR);
			currentDuck += totalDePatosPorMiniFase;
		}
	}

	public MultiDrawable getMultiDrawable(List<MultiDrawable> list, String name) {
		for (MultiDrawable multidrawable : list) {
			if (multidrawable.getClass().getName().equals(name)) {
				return multidrawable;
			}
		}
		return null;
	}
	public MultiDrawable getMultiDrawable(List<MultiDrawable> list, String name,int indexItem) {
		int indexCurrentItem=0;
		for (MultiDrawable multidrawable : list) {
			if (multidrawable.getClass().getName().equals(name)) {
				if (indexCurrentItem==indexItem){
					return multidrawable;	
				}else{
					indexCurrentItem++;
				}
				
			}
		}
		return null;
	}

	public int currentDuck = -totalDePatosPorMiniFase;

	/**
	 * Esta función se encargará de indicar a los patos que se procesen, y en
	 * caso de que haya terminado la minifase, decidira si mostrará la
	 * indicación de victoria o derrota.
	 */
	public void processDucks() {
		for (Diablo e : enemys) {

			e.process();
			if (patosEnPantalla() == 0 && patosAcertados() == 0) {
				setState(JUEGO_ESTADO_DERROTA);
				perro.iniciarCicloDerrota();
				efectos_de_musica.pararSonido(MUSICA_VOLAR);
				requestSound(SONIDO_RISA);
				break;
			} else {
				if (patosAcertados() > 0 && patosEnPantalla() == 0) {
					perro.iniciarCicloVictoria(patosAcertados(), (int) e.getX());
					efectos_de_musica.pararSonido(MUSICA_VOLAR);
					setState(JUEGO_ESTADO_VICTORIA);
					requestSound(SONIDO_VICTORIA);
					break;
				}
			}
		}
	}

	public int patosAcertados() {
		int contador = 0;
		for (Diablo e : enemys) {

			if (e.getEstadoActual() == Diablo.STATE_MUERTO) {
				contador++;
			}
		}
		return contador;
	}

	public int patosEnPantalla() {
		int contador = 0;
		for (Diablo e : enemys) {
			if (e.getEstadoActual() != Diablo.STATE_ESCAPO
					&& e.getEstadoActual() != Diablo.STATE_MUERTO) {
				contador++;
			}
		}
		return contador;
	}

	public int patosTodaviaVolando() {
		int contador = 0;
		for (Diablo e : enemys) {
			if (e.getEstadoActual() == Diablo.STATE_MOVE) {
				contador++;
			}
		}
		return contador;
	}

	public int patosCayendo() {
		int contador = 0;
		for (Diablo e : enemys) {
			if (e.getEstadoActual() == Diablo.STATE_CAYENDO) {
				contador++;
			}
		}
		return contador;
	}

	/**
	 * Esta función mantendra en pantalla durante 300 milisegundos un indicador
	 * de la ultima bonificación que se consiguio al eliminar el ultimo pato.
	 */
	public void procesarBonificaciones() {
		for (int i = 0; i < bonificaciones.size(); i++) {
			Bonificacion e = (Bonificacion) bonificaciones.elementAt(i);
			e.time += juegoThread.GAP_PROCESS;
			if (e.time > 2000) {
				e.visible = false;
			}
		}
	}

	/**
	 * Este bloque de estructuras (atributos y metodos) nos permitiran saber en
	 * que ronda nos encontramos actualmente.
	 */
	public int round;

	public int getRound() {
		return round;
	}

	public void incRound() {
		round++;
	}

	public void setFirstRound() {
		round = 0;
	}

	/**
	 * Este bloque de estructuras (atributos y metodos) nos permitiran saber
	 * cual es nuestra actual puntuación.
	 */
	public int score;

	public int getScore() {
		return score;
	}

	public void incScore(int value) {
		score += value;
	}

	/**
	 * Este bloque de estructuras (atributos y metodos) nos permitiran
	 * reproducir sonidos en el momento que decidamos. Podremos pausar el sonido
	 * actual en caso de que el usuario entre en pausa.
	 * 
	 * En nuestro caso, dispondremos de varios sonidos asi que guardaremos un
	 * identificador en un array, por lo tanto a la hora de reproducir un
	 * sonido, tendremos que indicar cual es el sonido.
	 * 
	 */

	public int[] Sounds_effects = { R.raw.risa, R.raw.win, R.raw.disparo,
			R.raw.caida, R.raw.cayo, R.raw.wininterface };
	public int[] Sonidos_de_musica = { R.raw.inicio_ronda, R.raw.volando,
			R.raw.winronda, R.raw.perfect, R.raw.game_over };

	/**
	 * Objeto encargado de reproducir los sonidos que solicitemos
	 */
	public Efectos_Sonido efectos_de_sonido;

	Sonido_Musica efectos_de_musica;

	/**
	 * Identificadores que usaremos para solitiar el sonido adecuado al momento
	 * oportuno.
	 */

	public static final int SONIDO_RISA = 0;
	public static final int SONIDO_VICTORIA = 1;
	public static final int SONIDO_DISPARO = 2;
	public static final int SONIDO_CAIDA = 3;
	public static final int SONIDO_CAYO = 4;
	public static final int SONIDO_WININTERFACE = 5;

	public static final int MUSICA_INICIO = 0;
	public static final int MUSICA_VOLAR = 1;
	public static final int MUSICA_WINRONDA = 2;
	public static final int MUSICA_WINPERFECT = 3;
	public static final int MUSICA_PARTIDATERMINADA = 4;

	/**
	 * Para todos el sonido actual que se este reproduciendo.
	 */
	public void stopSound() {
		efectos_de_musica.pausarSonido();
	}

	/**
	 * Reproduce el sonido solicitado.
	 */
	public void resetMusics() {
		efectos_de_musica.prepareSounds();
	}

	public void solicitarMusica(int s) {
		if (juegoView.sound) {
			efectos_de_musica.playSonido(s);
		}
	}

	public int realPointX;
	public int realPointY;
	public static final int realPointSize = 20;

	public void actualizapuntero() {
		if (getModulo(punteroX, punteroY) > 5) {
			realPointX += punteroX / 8;
			realPointY -= punteroY / 8;
		}
		if (realPointX > juegoThread.mCanvasWidth - (realPointSize >> 1)) {
			realPointX = juegoThread.mCanvasWidth - (realPointSize >> 1);
		}
		if (realPointY > juegoThread.mCanvasHeight - (realPointSize >> 1)) {
			realPointY = juegoThread.mCanvasHeight - (realPointSize >> 1);
		}
		if (realPointX < (realPointSize >> 1)) {
			realPointX = (realPointSize >> 1);
		}
		if (realPointY < (realPointSize >> 1)) {
			realPointY = (realPointSize >> 1);
		}
	}

	public float getModulo(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	public void procesoJuego() {

	}

	int gameover = 0;

	/**
	 * Esta función devuelve si el usuario ha hecho un Perfect en esta Ronda.
	 * 
	 * @return True si ha hecho perfect.
	 */
	public int numeroDePatosAcertados() {
		int contador = 0;
		for (int i = 0; i < ducksFaileds.size(); i++) {
			if (ducksFaileds.get(i)) {
				contador++;
			}
		}
		return contador;
	}

	/**
	 * Durante la etapa en la que se calcula el resultado, va desplazando uno a
	 * uno los indicadores de la interfaz hasta que deja todos los objetivos
	 * fallados en el lado derecho.
	 * 
	 * @param array
	 *            que contiene los indicadores de la interfaz
	 * @param index
	 *            actual indice que calculará si tiene que ser desplazado
	 * @return True si ya estan todos los elementos colocados
	 */
	public boolean llevarAlFinal(int index) {
		int ultimaposicionlibre = ducksFaileds.size() - 1;
		for (int i = ducksFaileds.size() - 1; i >= 0; i--) {
			if (ducksFaileds.get(i)) {
				ultimaposicionlibre = i;
				break;
			}
		}
		if (index <= ultimaposicionlibre) {
			ducksFaileds.set(index, true);
			ducksFaileds.set(ultimaposicionlibre, false);

			return true;
		}
		return false;

	}

	/**
	 * Establece el estado de Pausa cuando el usuario pulsa Menu, o se produce
	 * un estado que interrumpa nuestra Activity
	 */
	public void estableceEstadoPausa() {

	}

	/**
	 * Reanuda la partida tras haberse pausado
	 * 
	 */
	public void vuelveAlJuegoTrasLaPausa() {

	}

	@Override
	public void process(int timeProcess) {
		juegoThread.timeGame += timeProcess;
		procesarSolicitudesDeDisparo();
		actualizapuntero();
		if (efectos_de_musica != null) {
			efectos_de_musica.prepareSounds();
		}
		switch (getState()) {
		case JUEGO_ESTADO_PAUSA:

			break;
		case JUEGO_ESTADO_INICIO:

			Resources resources = mContext.getResources();
			efectos_de_sonido = new Efectos_Sonido(mContext);
			efectos_de_sonido.init(Sounds_effects);
			efectos_de_musica = new Sonido_Musica(Sonidos_de_musica, mContext);
			Bitmap imagenPato = BitmapFactory.decodeResource(resources,
					R.drawable.avatar);

			Bitmap imagenPerro = BitmapFactory.decodeResource(resources,
					R.drawable.perro);
			listDrawable = new ArrayList<MultiDrawable>();
			Interfaz interfaz = new Interfaz();
			interfaz.load(resources, this);
			BackGround background = new BackGround();
			background.load(resources, this);
			Estalactitas estalactitas = new Estalactitas();
			estalactitas.load(resources, this);

			Arbusto arbusto = new Arbusto();
			arbusto.load(resources, this);
			Fire fire= new Fire();
			fire.load(resources,this);
			Fire fire2= new Fire();
			fire2.load(resources,this);

			perro = new Perro(imagenPerro, juegoThread);
			perro.load(resources, this);
			courtain = new Courtain();

			enemys = new ArrayList<Diablo>();
			listDrawable.add(background);
			listDrawable.add(fire2);
			listDrawable.add(estalactitas);

			for (int i = 0; i < totalDePatosPorMiniFase; i++) {
				Diablo pato = new Diablo(juegoThread, mContext);

				pato.load(resources, this);

				enemys.add(pato);
				listDrawable.add(pato);
			}
			listDrawable.add(arbusto);
			listDrawable.add(interfaz);
			listDrawable.add(perro);
			listDrawable.add(fire);
			listDrawable.add(courtain);
			int z = 0;
			for (MultiDrawable drawable : listDrawable) {
				drawable.init(listDrawable);
				z += 10;
			}
			fire2.setY(background.getstalactiteBot_Y()-fire2.getHeight());
			estalactitas.setStalactiteBotY((int)(background.getstalactiteBot_Y()-estalactitas.getHeight()));
			fire.setY(background.getHorizont_Y()-fire.getHeight());
			for (Diablo pato : enemys) {
				pato.iniciarCicloVolando(indicePatos,
						background.getHorizont_Y());
				indicePatos++;
			}
			for (MultiDrawable drawable : listDrawable) {
				drawable.setZ(z);
				z += 10;
			}
			// listDrawable.add(object)
			BackGround tmpbackground = (BackGround) getMultiDrawable(
					listDrawable, "com.objects.BackGround");
//			Estalactitas stalaco = (Estalactitas) getMultiDrawable(listDrawable,
//					"com.objects.Estalactitas");
			perro.iniciarEstadoDeIntro(tmpbackground.getHorizont_Y(),
					(int) (tmpbackground.getHorizont_Y()-fire.getHeight()), (int) fire.getZ() + 1,
					(int) fire.getZ() - 1);

			setFirstRound();
			ducksFaileds = new ArrayList<Boolean>(TOTAL_DUCKS);
			for (int i = 0; i < TOTAL_DUCKS; i++) {
				Boolean b = Boolean.valueOf(false);
				ducksFaileds.add(b);
			}
			setState(JUEGO_ESTADO_COURTAIN);
			// setState(JUEGO_ESTADO_RESET);

			courtain.initCortinilla(false, juegoThread.mCanvasWidth,
					juegoThread.mCanvasHeight, Courtain.COLOR_BLACK);
			break;

		case JUEGO_ESTADO_NUEVA_RONDA:
			indicePatos = 0;

			// listDrawable.add(object)
			BackGround tmpbackground2 = (BackGround) getMultiDrawable(
					listDrawable, "com.objects.BackGround");
			Fire firetmp = (Fire) getMultiDrawable(listDrawable,
					"com.objects.Fire",1);
			perro.iniciarEstadoDeIntro(tmpbackground2.getHorizont_Y(),
					(int) (tmpbackground2.getHorizont_Y()-firetmp.getHeight()), (int) firetmp.getZ() + 1,
					(int) firetmp.getZ() - 1);
			// perro.setY(-56);

			setState(JUEGO_ESTADO_INTRO);
			incRound();
			ducksFaileds = new ArrayList<Boolean>(TOTAL_DUCKS);
			for (int i = 0; i < TOTAL_DUCKS; i++) {
				Boolean b = Boolean.valueOf(false);
				ducksFaileds.add(b);
			}
			currentDuck = -totalDePatosPorMiniFase;
			break;
		case JUEGO_ESTADO_COURTAIN:
			if (courtain.processCortinilla()) {
				setState(JUEGO_ESTADO_NUEVA_RONDA);
				solicitarMusica(MUSICA_INICIO);
			}
			break;
		case JUEGO_ESTADO_MOSTRANDO_RESULTADO:
			if (juegoThread.timeGame > 5000) {
				juegoThread.timeGame = 0;
				if (numeroDePatosAcertados() == 10) {
					solicitarMusica(MUSICA_WINPERFECT);
					setState(JUEGO_ESTADO_PERFECT);
					incScore(30000);
				} else {
					setState(JUEGO_ESTADO_NUEVA_RONDA);
				}
			}
			break;
		case JUEGO_ESTADO_PERFECT:
			if (juegoThread.timeGame > 5000) {

				setState(JUEGO_ESTADO_NUEVA_RONDA);

			}
			break;
		case JUEGO_ESTADO_INTRO:
			if (perro.process()) {
				setState(JUEGO_ESTADO_RESET);
			}
			break;

		case JUEGO_ESTADO_RESET:
			resetvalues();
			break;

		case JUEGO_ESTADO_DERROTA:
			if (perro.process()) {
				resetvalues();
				juegoThread.timeGame = 0;
			}
			procesDisparos(JuegoThread.GAP_PROCESS);
			procesarBonificaciones();
			break;
		case JUEGO_ESTADO_VICTORIA:
			procesDisparos(JuegoThread.GAP_PROCESS);
			procesarBonificaciones();
			if (perro.process()) {
				resetvalues();
				juegoThread.timeGame = 0;
			}
			break;
		case JUEGO_ESTADO_PLAY: {
			if (juegoThread.timeGame > 10000) {
				hacerHuirATodosLosPatos();
				juegoThread.timeGame = 0;
			}
			processDucks();
			procesDisparos(JuegoThread.GAP_PROCESS);
			procesarBonificaciones();
			break;
		}
		case JUEGO_ESTADO_PARTIDATERMINADA: {
			perro.process();
			if (juegoThread.timeGame > 0) {

				if (gameover == 0) {
					juegoThread.j.getHandler().sendEmptyMessage(0);

					gameover = 1;
				}

			}
			break;
		}

		case JUEGO_ESTADO_CALCULANDO_RESULTADO:
			if (juegoThread.timeGame > 500) {
				boolean cambios = false;
				for (int i = 0; i < ducksFaileds.size(); i++) {
					if (!ducksFaileds.get(i)) {
						// cambios=true;
						juegoThread.timeGame = 0;
						if (llevarAlFinal(i)) {
							cambios = true;
							requestSound(SONIDO_WININTERFACE);
						}
						break;
					}
				}
				if (!cambios) {
					if (numeroDePatosAcertados() > 5) {
						solicitarMusica(MUSICA_WINRONDA);
						setState(JUEGO_ESTADO_MOSTRANDO_RESULTADO);
					} else {
						solicitarMusica(MUSICA_PARTIDATERMINADA);
						setState(JUEGO_ESTADO_PARTIDATERMINADA);
						juegoThread.timeGame = 0;
						perro.iniciarCicloDerrota();
					}
				}
			}
			break;
		case JUEGO_ESTADO_COURTAIN_CLOSING_INIT: {
			courtain.initCortinilla(true, juegoThread.mCanvasWidth,
					juegoThread.mCanvasHeight, Courtain.COLOR_BLACK);
			setState(JUEGO_ESTADO_COURTAIN_CLOSING);
		}
			break;
		case JUEGO_ESTADO_COURTAIN_CLOSING: {
			if (courtain.processCortinilla()) {
				juegoThread.j.getHandler().sendEmptyMessage(
						MESSAGE_SENT_CLOSING_GAME);
				setState(JUEGO_ESTADO_COURTAIN_CLOSED);
			}
		}
			break;

		}
	}

	@Override
	public void onStop() {
		efectos_de_musica.pausarSonido();
		// efectos_de_sonido.pararSonido();
	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		if (sensor == SensorManager.SENSOR_ORIENTATION) {
			punteroX = values[2];
			punteroY = values[1];
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (juegoView.control == CONTROL_TOUCH_MODE) {
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {

				aniadirSolicitudDisparo((int) event.getX(), (int) event.getY());
			}
		}

		return true;
	}

	@Override
	public void setPause() {
		if (getState() != JUEGO_ESTADO_PAUSA) {
			// efectos_de_sonido.pausarSonido();
			efectos_de_musica.pausarSonido();
			savedState = getState();
			setState(JUEGO_ESTADO_PAUSA);

		}
	}

	@Override
	public void resumePause() {
		efectos_de_musica.reanudarSonido();
		setState(savedState);

	}

	static final String TYPE_INFO_SCORE = "SCORE";
	static final String TYPE_INFO_ROUND = "ROUND";

	@Override
	public String getInfo(String typeInfo) {
		if (TYPE_INFO_SCORE.equals(typeInfo)) {
			return score + "";
		} else if (TYPE_INFO_ROUND.equals(typeInfo)) {
			return round + "";
		}
		return null;
	}

	public int getState() {
		return state;
	}

	@Override
	public void setMode(int mode) {
		totalDePatosPorMiniFase = mode;

	}

	public static final String CANVAS_WIDTH = "CANVAS_WIDTH";
	public static final String CANVAS_HEIGHT = "CANVAS_HEIGHT";

	@Override
	public Context getContext() {
		return mContext;
	}

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
		if ("disparos".equals(attribute)) {
			object = disparos;
		} else if ("ducksFaileds".equals(attribute)) {
			object = ducksFaileds;
		} else if ("enemys".equals(attribute)) {
			object = enemys;
		}

		return object;
	}

	@Override
	public int requestSound(int s) {
		int aux = -1;
		if (juegoView.sound) {
			// efectos_de_musica.pausarSonido();
			aux = efectos_de_sonido.playSonido(s);
		}
		return aux;
	}

	@Override
	public void setState(int state) {
		this.state = state;

	}

	/**
	 * Dibuja un cuadrado con reborde.
	 * 
	 * @param canvas
	 */
	private void drawCuadroCentral(Canvas canvas) {
		float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
		RectF inset = new RectF(2, 2, 2, 2);

		ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR,
				inset, outerR));

		ShapeDrawable mDrawables2 = new ShapeDrawable(new RoundRectShape(
				outerR, null, outerR));
		mDrawables.getPaint().setColor(0xFFCCF543);

		mDrawables.setBounds((canvas.getWidth() / 2) - 60,
				(canvas.getHeight() / 2) - 25, (canvas.getWidth() / 2) + 60,
				(canvas.getHeight() / 2) + 25);
		mDrawables2.setBounds((canvas.getWidth() / 2) - 60,
				(canvas.getHeight() / 2) - 25, (canvas.getWidth() / 2) + 60,
				(canvas.getHeight() / 2) + 25);
		mDrawables2.draw(canvas);
		mDrawables.draw(canvas);
	}

	// private void pintarInformacionEnCiertosEstados(Canvas canvas,
	// Paint paint) {
	// // En caso de que acierte todos los patos de una ronda, mostraremos
	// // una pantalla mostrando un logotipo
	// if (getState() == IProcess.JUEGO_ESTADO_PERFECT) {
	// drawCuadroCentral(canvas);
	//
	// paint.setColor(0xFFCCF543);
	// paint.setTextAlign(Paint.Align.CENTER);
	// canvas.drawText("PERFECT", canvas.getWidth() / 2,
	// (canvas.getHeight() / 2) - 5, paint);
	// canvas.drawText("30.000", canvas.getWidth() / 2,
	// (canvas.getHeight() / 2) + 5, paint);
	// } else if (getState() == IProcess.JUEGO_ESTADO_INTRO) {
	// drawCuadroCentral(canvas);
	//
	// paint.setColor(0xFFCCF543);
	// paint.setTextAlign(Paint.Align.CENTER);
	// canvas.drawText(
	// mContext.getResources().getString(R.string.juego_ronda)
	// + " " + getInfo("ROUND"), canvas.getWidth() / 2,
	// (canvas.getHeight() / 2), paint);
	//
	// } else if (getState() == IProcess.JUEGO_ESTADO_PAUSA) {
	// // Si se encuentra en el estado de pausa, mostramos un letrero
	// // que lo indique.
	// drawCuadroCentral(canvas);
	// paint.setColor(0xFFCCF543);
	// paint.setTextAlign(Paint.Align.CENTER);
	// canvas.drawText(
	// mContext.getResources().getString(R.string.menu_pausa),
	// canvas.getWidth() / 2, (canvas.getHeight() / 2), paint);
	// } else if (getState() == IProcess.JUEGO_ESTADO_PARTIDATERMINADA) {
	// drawCuadroCentral(canvas);
	// paint.setColor(0xFFCCF543);
	// paint.setTextAlign(Paint.Align.CENTER);
	// canvas.drawText(
	// mContext.getResources().getString(
	// R.string.juego_gamover), canvas.getWidth() / 2,
	// (canvas.getHeight() / 2), paint);
	// }
	//
	// }
	public static final int SHOOT_SIZE = 20;

	private void pintarDisparos(Canvas canvas, Paint paint) {
		paint.setColor(Color.WHITE);
		for (int i = 0; i < disparos.size(); i++) {
			Disparo d = (Disparo) disparos.elementAt(i);
			if (d.visible) {
				canvas.drawLine(d.x - (SHOOT_SIZE / 2), d.y - (SHOOT_SIZE / 2),
						d.x + (SHOOT_SIZE / 2), d.y + (SHOOT_SIZE / 2), paint);
				canvas.drawLine(d.x - (SHOOT_SIZE / 2), d.y + (SHOOT_SIZE / 2),
						d.x + (SHOOT_SIZE / 2), d.y - (SHOOT_SIZE / 2), paint);
			}
		}
	}

	private void pintarBonificaciones(Canvas canvas, Paint paint) {
		for (int i = 0; i < bonificaciones.size(); i++) {
			Bonificacion d = (Bonificacion) bonificaciones.elementAt(i);
			if (d.visible) {
				canvas.drawText(d.value + "", d.x, d.y, paint);
			}
		}
	}

	public List<MultiDrawable> listDrawable;

	private void displayFps(Canvas canvas, String fps) {

		if (canvas != null && fps != null) {

			Paint paint = new Paint();

			paint.setARGB(255, 0, 0, 0);
			paint.setTextSize(22);
			canvas.drawText(fps, juegoThread.mCanvasWidth - 100, 20, paint);
			canvas.drawText(juegoThread.timeGame+"", juegoThread.mCanvasWidth - 100, 40, paint);
		}

	}

	public void doDraw(Canvas canvas) {
		Paint paint = new Paint();

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
//			BackGround background = (BackGround) getMultiDrawable(
//					getListDrawable(), "com.objects.BackGround");
//			
//			Fire fire = (Fire) getMultiDrawable(
//					getListDrawable(), "com.objects.Fire");
//			fire.drawDebug(canvas, paint);
//			canvas.drawText(fire.getHeight()+"", 100, 100, paint);
//			paint.setColor(0xFFFFFF00);
//			canvas.drawRect(0, background.getHorizont_Y(), canvas.getWidth(), background.getHorizont_Y()+2, paint);
//			paint.setColor(0xFFFFFF00);
//			canvas.drawRect(0, background.getstalactiteBot_Y(), canvas.getWidth(), background.getstalactiteBot_Y()+2, paint);
			// Pintamos los Disparos
			pintarDisparos(canvas, paint);

			// Pintamos las Bonificaciones
			pintarBonificaciones(canvas, paint);

			// pintarInformacionEnCiertosEstados(canvas, paint);

			paintPuntero(canvas, paint);
			displayFps(canvas, avgFps);
			
		}

	}

	private void paintPuntero(Canvas canvas, Paint p) {
		// if (process.control == process.CONTROL_MOVE_MODE) {
		// // canvas.drawText(punteroX+":"+punteroY, 100, 100, p);
		// // canvas.drawLine(mCanvasWidth/2, mCanvasHeight/2,
		// // (mCanvasWidth/2)+punteroX, (mCanvasHeight/2)+punteroY, p);
		// if (process.getModulo(process.punteroX, process.punteroY) < 15) {
		// p.setColor(0xFFFF0000);
		// } else {
		// p.setColor(0xFF00FF00);
		// }
		//
		// Paint mipaint = new Paint();
		// mipaint.setAntiAlias(true);
		// mipaint.setStyle(Paint.Style.STROKE);
		// mipaint.setStrokeWidth(3);
		// mipaint.setColor(0xFF0000FF);
		// // canvas.drawRect(realPointX, realPointY, realPointX+20,
		// // realPointY+20, p);
		// RectF oval = new RectF(processGame.realPointX -
		// (processGame.realPointSize >> 1),
		// processGame.realPointY - (processGame.realPointSize >> 1),
		// processGame.realPointX
		// + (processGame.realPointSize >> 1), processGame.realPointY
		// + (processGame.realPointSize >> 1));
		// canvas.drawArc(oval, 0f, 360f, false, mipaint);
		// mipaint.setStrokeWidth(2);
		// mipaint.setColor(0x880000FF);
		// oval = new RectF(processGame.realPointX - 2, processGame.realPointY -
		// 2,
		// processGame.realPointX + 2, processGame.realPointY + 2);
		// canvas.drawArc(oval, 0f, 360f, false, mipaint);
		// }

	}

	@Override
	public List<MultiDrawable> getListDrawable() {

		return listDrawable;
	}

	@Override
	public void setListDrawable(List<MultiDrawable> listDrawable) {
		this.listDrawable = listDrawable;

	}

	@Override
	public void destroy() {

		for (MultiDrawable drawable : listDrawable) {
			drawable = null;
		}

		ducksFaileds = null;

		enemys = null;
		disparos = null;

	}

	@Override
	public void pausarSonido(int lastSound) {
		efectos_de_sonido.pausarSonido(lastSound);

	}

	@Override
	public JuegoView getView() {

		return juegoView;
	}

	@Override
	public void surfaceCreated() {
		if (getState() == JUEGO_ESTADO_PAUSA) {
			resumePause();
		}

	}

	@Override
	public void setAvgFps(String string) {
		this.avgFps = string;
	}

}
