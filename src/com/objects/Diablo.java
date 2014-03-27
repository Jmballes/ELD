package com.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.Vector;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.draw.MultiDrawable;
import com.draw.ObjetoAnimable;
import com.draw.Personaje;
import com.draw.Zone;
import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.JuegoView.JuegoThread;
import com.paintview.ProcessGame;

public class Diablo extends Personaje {
	int[] resourcesDrawable = { R.drawable.avatar };
	private int index_duck = 0;
	private int id;

	public final static byte STATE_MOVE = 0;
	public final static byte STATE_HITTED = 1;
	public final static byte STATE_CAYENDO = 2;
	public final static byte STATE_MUERTO = 3;
	public final static byte STATE_HUYENDO = 4;
	public final static byte STATE_ESCAPO = 5;
	public final static byte STATE_INTRO = 6;

	public final static int TIME_FRAMES = 100;
	// public final static byte[] ANIMATIONS={0,3,
	// 3,3,
	// 9,1,
	// 10,1,
	// 6,3
	//
	// };
	public final static byte[] ANIMATIONS = { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1

	};
	public final static int ANIM_MOVE_HORIZONTAL = 0;
	public final static int ANIM_MOVE_DIAGONAL = 1;
	public final static int ANIM_MOVE_GOLPEADO = 2;
	public final static int ANIM_MOVE_CAYENDO = 3;
	public final static int ANIM_MOVE_HUYENDO = 4;
	JuegoThread juegoThread;
	ObjetoAnimable anim;

	Vector vel;

	public Diablo(JuegoThread juegoThread, Context mContext) {
		this.juegoThread = juegoThread;

	}

	/**
	 * Inicia Ciclo de Vuelo en el que el pato se movera de un punto a otro
	 * 
	 * @param id
	 *            Identificador del pato
	 */
	public void iniciarCicloVolando(int id, int limity) {
		// Asignamos un identificador a nuestro personaje
		this.id = id;
		// Asinamos a nuestro personaje al estado de movimiento
		setEstado(STATE_MOVE);
		// Asignamos como posición en el eje horizontal el centro de la pantalla
		setX(juegoThread.mCanvasWidth / 2);
		// Y como posición en el eje vertical el limite donde se encuentra el
		// cesped
		BackGround backGround = (BackGround) process.getMultiDrawable(
				process.getListDrawable(), "com.objects.BackGround");

		limity = (int) (backGround.getHorizont_Y());
		LIMIT_Y = limity;
		setY(limity - getHeight());
		// A continuación, calcularemos el destino al que se dirigirá nuestro
		// personaje.
		if (process.getClass().getSimpleName().equals("ProcessGame")) {
			calcularNuevoDestino();

		}

	}

	public void iniciarCicloVolandoIntro(int id, int limity, int destinyX) {
		// Asignamos un identificador a nuestro personaje
		this.id = id;
		// Asinamos a nuestro personaje al estado de movimiento
		setEstado(STATE_MOVE);
		// Asignamos como posición en el eje horizontal el centro de la pantalla
		setX(juegoThread.mCanvasWidth / 2);
		// Y como posición en el eje vertical el limite donde se encuentra el
		// cesped
		BackGround backGround = (BackGround) process.getMultiDrawable(
				process.getListDrawable(), "com.objects.BackGround");

		limity = (int) (backGround.getHorizont_Y());
		LIMIT_Y = limity;
		setY(limity - getHeight());
		setDestinyTop(destinyX);

	}

	/**
	 * Inicia el ciclo en el que ha sido alcanzado por una bala.
	 */
	public void iniciarCicloDisparado() {
		setEstado(STATE_HITTED);
		anim.initAnimation(ANIM_MOVE_GOLPEADO, false);
		reiniciarTiempoEntreEstados();
	}

	/**
	 * Inicia el ciclo en el que empieza a huir el pato.
	 */
	public void iniciarCicloHuyendo() {
		setEstado(STATE_HUYENDO);
		anim.initAnimation(ANIM_MOVE_HUYENDO, true);
		setDestinyX(getX());
		setDestinyY(-getHeight());
		setVy(DPI_SPEED_ESCAPE * process.getView().deviceDensity);
	}

	public static int LIMIT_Y = 308;

	public static final int DISTANCIA_HACIA_SIGUIENTE_PUNTO = 50;
	public static final float VELOCIDAD_HASTA_SIGUIENTE_PUNTO = 1.5f;
	public static final float VELOCIDAD_HUYENDO_PRESENTATION = 2f;
	public final static float DPI_SPEED_ESCAPE = 2.5F;
	public final static float DPI_SPEED_FALLING = 2f;

	//
	/**
	 * Calcula el punto de destino del Pato. Cada vez que nuestro personaje
	 * llegue a su destino, habra que recalcular dicho valor El procedimiento
	 * sera: calcular un angulo de forma aletoria y con una distancia dada y a
	 * partir de la posición actual, se calcula el punto de destino.
	 */
	private void calcularNuevoDestino() {
		do {
			calcularDestinoAleatorio();
		} while (esElDestinoNoSeaValido());

		double dy = getDestinyY() - getY();
		double dx = getDestinyX() - getX();
		double tmpangle = Math.atan2(dy, dx);
		setVx((float) (Math
				.abs((Math.cos(tmpangle) * VELOCIDAD_HASTA_SIGUIENTE_PUNTO * process
						.getView().deviceDensity))));
		setVy((float) (Math
				.abs((Math.sin(tmpangle) * VELOCIDAD_HASTA_SIGUIENTE_PUNTO * process
						.getView().deviceDensity))));


		// setVx(Math.abs((getDestinyX()-getX())*VELOCIDAD_HASTA_SIGUIENTE_PUNTO*process.getView().deviceDensity));
		// setVy(Math.abs((getDestinyY()-getY())*VELOCIDAD_HASTA_SIGUIENTE_PUNTO*process.getView().deviceDensity));
		iniciarAnimacionVolando();
	}

	

	public void moveAdvanced() {

		moveNearingAngle();
		
		setX((float) (getX() + vel.getX()));
		setY((float) (getY() + vel.getY()));
	}

	public void drawsss(Canvas canvas, Paint paint, Vector p1, Vector p2,
			String s1, String s2) {

		double dy = p2.getY() - p1.getY();
		double dx = p2.getX() - p1.getX();
		Vector destiny = new Vector(dx, dy);

		double angleDestiny = destiny.getAngle();

		float linex = (float) (Math.cos(angleDestiny) * 30);
		float liney = (float) (Math.sin(angleDestiny) * 30);

		canvas.drawRect((float) p1.getX(), (float) p1.getY(),
				(float) p1.getX() + 5, (float) p1.getY() + 5, paint);
		canvas.drawText(s1, (float) p1.getX(), (float) p1.getY() - 20, paint);

		canvas.drawRect((float) p2.getX(), (float) p2.getY(),
				(float) p2.getX() + 5, (float) p2.getY() + 5, paint);
		canvas.drawText(s2, (float) p2.getX(), (float) p2.getY() - 20, paint);
		canvas.drawLine((float) p1.getX(), (float) p1.getY(), (float) p1.getX()
				+ linex, (float) p1.getY() + liney, paint);

	}

	public void moveNearingAngle() {
		double maxMagnitudes=5;
		if (vel == null) {
			vel = new Vector(0, 0);
		}
		// Vector tmpvel = vel;
		double dy = getDestinyY() - getY();
		double dx = getDestinyX() - getX();
		Vector destiny = new Vector(dx, dy);
		//destiny=destiny.VectorByAngle(destiny.getAngle(), maxMagnitudes);
		if (destiny.magnitude()>0.5){
			//int index=(int) ((System.currentTimeMillis()-timeNewPosition)/1000);
			destiny=destiny.VectorByAngle(destiny.getAngle(), 0.5 );
		}
		vel = vel.VectorByAngle(vel.getAngle(), vel.magnitude()* 0.98);
		Vector newVel=vel.add(destiny);

		double angleDestiny = destiny.getAngle();

		if (newVel.magnitude()>maxMagnitudes){
			vel=newVel.VectorByAngle(newVel.getAngle(), maxMagnitudes);
		}else{
			vel=newVel;
		}

		if (vel.getX() == 0 && vel.getY() == 0) {
			vel = vel.VectorByAngle(angleDestiny, 1);
		}
	}

	public double correctAngle(double angle) {
		if (angle < 0) {
			angle += Math.PI * 2;
		}
		if (angle > Math.PI * 2) {
			angle -= Math.PI * 2;
		}
		return angle;
	}
	
	public double correctAngleDeg(double angle) {
		if (angle < 0) {
			angle += 360;
		}
		if (angle > 360) {
			angle -= 360;
		}
		return angle;
	}

	public void moveRotatingAngle() {
		if (vel == null) {
			vel = new Vector(0, 0);
		}
		// Vector tmpvel = vel;
		double dy = getDestinyY() - getY();
		double dx = getDestinyX() - getX();
		Vector destiny = new Vector(dx, dy);

		double moduloVel = 4;// vel.magnitude();
		double angleDestiny = destiny.getAngle();
		double angleVel = vel.getAngle();

		angleVel = correctAngle(angleVel);
		angleDestiny = correctAngle(angleDestiny);

		double diffangles = angleDestiny - angleVel;
		diffangles = correctAngle(diffangles);

		double diffangles2 =   angleVel - angleDestiny;
		diffangles2 = correctAngle(diffangles2);
		double inc;
		if (getCircularNearMotion(vel.getAngle(),destiny.getAngle())==ANTI_CLOCKWISE){
			inc = -0.06;
		} else {
			inc = +0.06;
		}

		double newAngle = nearAngle(angleVel, angleDestiny, inc);
		double longitude = nearValue(moduloVel, moduloVel, 0.03);
		if (vel.getX() == 0 && vel.getY() == 0) {
			vel = vel.VectorByAngle(angleDestiny, 1);
		} else {
			vel = vel.VectorByAngle(newAngle, longitude);
		}
	}
	public final static int CLOCKWISE=0;
	public static final int ANTI_CLOCKWISE=1;
	public int getCircularNearMotion(Double origen, Double destiny){
		origen=correctAngle(origen);
		destiny=correctAngle(destiny);
		Double angle1=origen-destiny;
		Double angle2=destiny-origen;
		angle1=correctAngle(angle1);
		angle2=correctAngle(angle2);
		if (angle1<angle2){
			return ANTI_CLOCKWISE;
			
		}else{
			return CLOCKWISE;
		}
	}
	public double getAngleinDegrees(double angleInRadians) {
		return correctAngleDeg((angleInRadians / Math.PI) * 180.0);
	}

	private double nearValue(double origen, double destino, double inc) {
		double aux;

		if (origen > destino) {
			aux = origen - inc;
			if (aux < destino) {
				aux = destino;
			}
		} else {
			aux = origen + inc;
			if (aux > destino) {
				aux = destino;
			}
		}
		return aux;
	}
	private double nearAngle(double origen, double destino, double inc) {
		double aux =origen + inc;
		boolean origSmallerDestiny=origen<destino;
		if (inc>0) {

			if (origSmallerDestiny && aux > destino) {
				aux = destino;
			}
		} else {
			if (!origSmallerDestiny && aux < destino) {
				aux = destino;
			}
		}
		return aux;
	}

	public void setDestinyTop(int x) {
		setDestinyX((int) x);
		setDestinyY((int) -getWidth());
		double dy = getDestinyY() - getY();
		double dx = getDestinyX() - getX();
		double tmpangle = Math.atan2(dy, dx);
		setVx((float) (Math
				.abs((Math.cos(tmpangle) * VELOCIDAD_HUYENDO_PRESENTATION * process
						.getView().deviceDensity))));
		setVy((float) (Math
				.abs((Math.sin(tmpangle) * VELOCIDAD_HUYENDO_PRESENTATION * process
						.getView().deviceDensity))));
		iniciarAnimacionVolando();
		// calcularNuevoDestino();
	}
	Random generator = new Random(20);
	double randomGenerator(long seed) {
		
		double num = generator.nextDouble() * (1);

		return num;
	}

	private void calcularDestinoAleatorio() {
		// Calculamos un angulo aleatorio
//		double angle = randomGenerator(3) * Math.PI * 2;
//
//		// Calculamos la componente en el Eje X con el angulo y el modulo
//		// precalculado
//		double vectorX = Math.cos(angle) * DISTANCIA_HACIA_SIGUIENTE_PUNTO
//				* process.getView().deviceDensity;
//		// Calculamos la componente en el Eje Y con el angulo y el modulo
//		// precalculado
//		double vectorY = Math.sin(angle) * DISTANCIA_HACIA_SIGUIENTE_PUNTO
//				* process.getView().deviceDensity;
//		// Y añadimos a la posición actual el vector calculado

		setDestinyX((int) (randomGenerator(3)* juegoThread.mCanvasWidth));
		setDestinyY((int) (randomGenerator(3)* juegoThread.mCanvasHeight));

	}

	private boolean esElDestinoNoSeaValido() {
		return (//getDistance(getX(), getY(), getDestinyX(), getDestinyY()) < 100	|| 
				getDestinyX() < 0 || getDestinyY() < 0
				|| getDestinyX() > juegoThread.mCanvasWidth - getWidth() || getDestinyY() > LIMIT_Y
				- getWidth());
	}

	/**
	 * Calcula la distancia entre dos puntos.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	int getDistance(float x1, float y1, float x2, float y2) {
		return (int) Math.sqrt(Math.pow(x1 - x2, 2) * Math.pow(y1 - y2, 2));

	}

	/**
	 * Prepara la animación de vuelo de nuestro personaje
	 */
	void iniciarAnimacionVolando() {
		// Calcula que tipo de animación debe de usar, si la horizontal, o
		// diagonal
		anim.initAnimation(getDestinyY() - getY() > 0 ? ANIM_MOVE_HORIZONTAL
				: ANIM_MOVE_DIAGONAL, true);
		// Calcula el sentido de la animación y por tanto si tiene que hacer un
		// espejado de la animación.
		anim.setEspejadoHorizontal(getDestinyX() < getX());
	}

	/**
	 * Devuelve el identificador del personaje
	 * 
	 * @return identificador
	 */
	public int getID() {
		return id;
	}

	public void moverPersonajeA() {

	}

	public int ultimosonido;

	public boolean colission() {

		Vector pdest = new Vector(getDestinyX(), getDestinyY());
		Vector pact = new Vector(getX(), getY());
		Vector destsobreact = pact.subtract(pdest);
		return destsobreact.magnitude() <=4;

	}

	public void process() {
		// Actualizamos el tiempo de la animación
		anim.actualizarTiempo();
		// Actualizamos el tiempo
		// actualizarTiempoEntreEstados(JuegoThread.GAP_PROCESS);
		switch (getEstadoActual()) {
		case STATE_MOVE: {
			// Actualizamos la posición de nuestro personaje
			// moverPersonaje();
			moveAdvanced();
			// Si ha llegado a su destino
			if (colission()) {
				// Calculamos nuevo destino
				// if
				// (process.getState()==IProcess.PRESENTATION_STATE_DUCKS_FLYING){
				// setEstado(STATE_ESCAPO);
				// }else{
				calcularNuevoDestino();
				// }

			}
			break;
		}
		case STATE_HUYENDO: {
			// Actualizamos la posición de nuestro personaje
			moverPersonaje();
			// Si ha llegado a su destino
			if (llegoAlDestino()) {
				// Ha escapado
				setEstado(STATE_ESCAPO);
			}
			break;
		}
		case STATE_HITTED: {
			// Si pasa un segundo desde que es disparado
			if (getTiempoEntreEstados() > 500) {
				// Fijamos como destino el suelo
				setDestinyY(LIMIT_Y - getHeight());
				setDestinyX(getX());
				vel = new Vector(0, DPI_SPEED_FALLING
						* process.getView().deviceDensity);
				// A una velocidad
				setVy(DPI_SPEED_FALLING * process.getView().deviceDensity);
				setVx(0);
				// Con cierta animacion
				anim.initAnimation(ANIM_MOVE_CAYENDO, true);
				// Y cambiamos al estado cayendo
				setEstado(STATE_CAYENDO);
				ultimosonido = process.requestSound(ProcessGame.SONIDO_CAIDA);

			}
			break;
		}
		case STATE_CAYENDO: {
			// Actualizamos la posición de nuestro personaje
			moveAdvanced();
			// Si ha llegado a su destino
			if (colission()) {
				// Cambiamos al estado muerto
				setEstado(STATE_MUERTO);
				process.requestSound(ProcessGame.SONIDO_CAYO);
				process.pausarSonido(ultimosonido);
			}
			break;
		}
		case STATE_INTRO: {
			// Actualizamos la posición de nuestro personaje
			moverPersonaje();
			// Si ha llegado a su destino
			if (llegoAlDestino()) {
				// Cambiamos al estado muerto
				setX(-getSize());
			}
			break;
		}
		}

	}

	public void drawDebug(Canvas canvas, Paint paint) {
		canvas.drawRect(getX(), getY(), getX() + getWidth(), getY()
				+ getHeight(), paint);
		paint.setColor(0xFFFF0000);
		if (vel != null) {
			// Double v= vel.getAngle();
			// Vector tmpAngle = vel.multiply(20);
			// canvas.drawLine((int) tmpx, (int) tmpy,
			// (int) (tmpx + tmpAngle.getX()),
			// (int) (tmpy + tmpAngle.getY()), paint);

			double dy = getDestinyY() - getY();
			double dx = getDestinyX() - getX();
			Vector vdestinyactual = new Vector(dx, dy);
			Vector destiny = new Vector(getDestinyX(), getDestinyY());
			Vector actual = new Vector(getX(), getY());
			Vector velsobreAct = actual.add(vel);
			
			
			double angleVel = vel.getAngle();
			
			
			
			double veldeg=getAngleinDegrees(vel.getAngle());
			double destsobreActdeg=getAngleinDegrees(vdestinyactual.getAngle());

//			canvas.drawText("destsobreact:" ,(float)destsobreact.getX(), (float)destsobreact.getY(), paint);
//			canvas.drawText("destsobreact:" ,(float)destsobreact.getX(), (float)destsobreact.getY(), paint);
//			canvas.drawText("destsobreact:" ,(float)destsobreact.getX(), (float)destsobreact.getY(), paint);
//			canvas.drawText("destsobreact:" ,(float)destsobreact.getX(), (float)destsobreact.getY(), paint);
//			canvas.drawText("angleVel:" + angleVel, 0, 40, paint);
//			canvas.drawText("angleVel:" + angleVel, 0, 40, paint);
//			canvas.drawText("angleVel:" + angleVel, 0, 40, paint);
			

			
			paint.setTextSize(15);
			canvas.drawText(" Long: " + vel.magnitude(), 0, 20, paint);

			canvas.drawText("angleVel:" + angleVel, 0, 40, paint);
			canvas.drawText("x:" + getX(), 0, 80, paint);
			canvas.drawText("y:" + getY(), 0, 100, paint);
			canvas.drawText("vx:" + vel.getX(), 0, 120, paint);
			canvas.drawText("vy:" + vel.getY(), 0, 140, paint);
			paint.setColor(0xFF0000FF);
			drawsss(canvas, paint, actual, destiny, "", "");
			paint.setColor(0xFF000000);

			drawsss(canvas, paint, actual, velsobreAct, "", "");
			
			
			
			
//			canvas.drawText("Distance:" + destsobreact.magnitude(), 0, 200,
//					paint);
			
			canvas.drawText("angleVel:" + correctAngleDeg(veldeg), 0, 300, paint);
			canvas.drawText("angleDestiny:" + correctAngleDeg(destsobreActdeg), 0, 320, paint);
			canvas.drawText("vell-dest:" + correctAngleDeg(veldeg-destsobreActdeg), 0, 340, paint);
			canvas.drawText("dest-vel:" + correctAngleDeg(destsobreActdeg-veldeg), 0, 360, paint);
			
			
			canvas.drawText("dist:" + vdestinyactual.magnitude(), 200, 380, paint);
			
			if (getCircularNearMotion(vel.getAngle(),vdestinyactual.getAngle())==ANTI_CLOCKWISE){
				paint.setColor(0xFF00FF00);
				canvas.drawText("ANTI", (float) velsobreAct.getX(), (float) velsobreAct.getY()-20,
						paint);
//				canvas.drawRect((float) velsobreAct.getX(), (float) velsobreAct.getY(),
//						(float) velsobreAct.getX() + 5, (float) velsobreAct.getY() + 5, paint);
			}else{
				paint.setColor(0xFFFF0000);
				canvas.drawText("CLOCK", (float) velsobreAct.getX(), (float) velsobreAct.getY()+20,
						paint);
			}
			// float linex = (float) (getX() + Math.cos(angleDestiny) * 10);
			// float liney = (float) (getX() + Math.cos(angleDestiny) * 10);
			// canvas.drawLine(getX(), getY(), getX() + linex, getY() +
			// liney,
			// paint);
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
		// TODO Auto-generated method stub
		if (anim != null) {
			// Si esta nuestro personaje esta cayendo porque ha sido abatido
			if (getEstadoActual() == STATE_CAYENDO) {
				// Haremos un efecto intermitente de espejado, igual que en el
				// juego original.
				if (getTiempoEntreEstados() % 200 > 100) {
					anim.setEspejadoHorizontal(true);
				} else {
					anim.setEspejadoHorizontal(false);
				}
			}
			if (vel != null) {
				if (vel.getX() < 0) {
					anim.setEspejadoHorizontal(true);
				} else if (vel.getX() > 0) {
					anim.setEspejadoHorizontal(false);
				}
			}

			anim.pintarAnimacion(canvas, (int) getX(), (int) getY(), paint);
			
			int SHOOT_SIZE = 20;

			// canvas.drawLine(getDestinyX() - (SHOOT_SIZE/2), getDestinyY() -
			// (SHOOT_SIZE/2), getDestinyX() + (SHOOT_SIZE/2), getDestinyY()
			// +(SHOOT_SIZE/2), paint);
			// canvas.drawLine(getDestinyX() - (SHOOT_SIZE/2), getDestinyY() +
			// (SHOOT_SIZE/2), getDestinyX() + (SHOOT_SIZE/2), getDestinyY() -
			// (SHOOT_SIZE/2), paint);
			// paint.setColor(0xFF0000FF);
			// canvas.drawRect(getDestinyX(), getDestinyY(), getDestinyX() + 10,
			// getDestinyY() + 10, paint);
			// drawDestinyTOPato(canvas, paint);
			

			// Vector a = new Vector(600, 100);
			// Vector b = new Vector(300, 150);
			// drawsss(canvas, paint, a, b);
			// long sprite=(anim.getTiempoActual());
			// System.out.println("pato-->"+sprite);
		}

	}

	public void drawDestinyTOPato(Canvas canvas, Paint paint) {
		double dy = getY() - getDestinyY();
		double dx = getX() - getDestinyX();
		Vector destiny = new Vector(dx, dy);

		double angleDestiny = destiny.getAngle();

		float x = getDestinyX();
		float y = getDestinyY();
		float linex = (float) (getDestinyX() + Math.cos(angleDestiny) * 30);
		float liney = (float) (getDestinyY() + Math.cos(angleDestiny) * 30);
		canvas.drawLine(x, y, linex, liney, paint);

	}

	/**
	 * Función encargada de pintar a nuestro personaje
	 * 
	 * @param canvas
	 * @param paint
	 */
	public void paint(Canvas canvas, Paint paint) {

	}

	@Override
	public void load(Resources resources, IProcess process) {
		this.process = process;
		listDrawable = new ArrayList<Bitmap>();
		for (int i = 0; i < resourcesDrawable.length; i++) {
			listDrawable.add(i, BitmapFactory.decodeResource(resources,
					resourcesDrawable[i]));
		}
		Bitmap bitmap = (Bitmap) listDrawable.get(0);
		setHeight(bitmap.getWidth());
		setWidth(bitmap.getWidth());
		setSize(bitmap.getWidth());

		anim = new ObjetoAnimable(listDrawable.get(index_duck), ANIMATIONS,
				TIME_FRAMES, getSize(),1);

	}

	@Override
	public void init(List<MultiDrawable> listDrawable) {
		BackGround backGround = (BackGround) process.getMultiDrawable(
				listDrawable, "com.objects.BackGround");

		int limity = (int) (backGround.getHorizont_Y() - getHeight());
		LIMIT_Y = limity;
		setY(limity - getSize());
	}

}
