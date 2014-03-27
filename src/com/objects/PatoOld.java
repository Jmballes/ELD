package com.objects;

import java.util.ArrayList;
import java.util.List;

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

public class PatoOld extends Personaje{
	int[] resourcesDrawable = { R.drawable.avatar };
	private int index_duck=0;
	private int id;
	
	

	
	public final static byte STATE_MOVE=0;
	public final static byte STATE_HITTED=1;
	public final static byte STATE_CAYENDO=2;
	public final static byte STATE_MUERTO=3;
	public final static byte STATE_HUYENDO=4;
	public final static byte STATE_ESCAPO=5;
	public final static byte STATE_INTRO=6;
	
	public final static int TIME_FRAMES=100;
//	public final static byte[] ANIMATIONS={0,3,
//										3,3,
//										9,1,
//										10,1,
//										6,3
//										   
//	};
	public final static byte[] ANIMATIONS={0,1,
		0,1,
		0,1,
		0,1,
		0,1
		   
};
	public final static int ANIM_MOVE_HORIZONTAL=0;
	public final static int ANIM_MOVE_DIAGONAL=1;
	public final static int ANIM_MOVE_GOLPEADO=2;
	public final static int ANIM_MOVE_CAYENDO=3;
	public final static int ANIM_MOVE_HUYENDO=4;
	JuegoThread juegoThread;
	ObjetoAnimable anim;
	public PatoOld(JuegoThread juegoThread,Context mContext){
		this.juegoThread=juegoThread;
		

	}
	/**
	 * Inicia Ciclo de Vuelo en el que el pato se movera de un punto a otro 
	 * @param id Identificador del pato
	 */
	public void iniciarCicloVolando(int id,int limity){
		//Asignamos un identificador a nuestro personaje
		this.id=id;
		//Asinamos a nuestro personaje al estado de movimiento
		setEstado(STATE_MOVE);
		//Asignamos como posici�n en el eje horizontal el centro de la pantalla
		setX(juegoThread.mCanvasWidth/2);
		//Y como posici�n en el eje vertical el limite donde se encuentra el cesped
		BackGround backGround = (BackGround) process.getMultiDrawable(
				process.getListDrawable(), "com.objects.BackGround");
		
		limity=(int)(backGround.getHorizont_Y());
		LIMIT_Y=limity;
		setY(limity-getHeight());
		//A continuaci�n, calcularemos el destino al que se dirigir� nuestro personaje.
		if (process.getClass().getSimpleName().equals("ProcessGame")){
			calcularNuevoDestino();
		}
		
	}
	
	public void iniciarCicloVolandoIntro(int id,int limity,int destinyX){
		//Asignamos un identificador a nuestro personaje
		this.id=id;
		//Asinamos a nuestro personaje al estado de movimiento
		setEstado(STATE_MOVE);
		//Asignamos como posici�n en el eje horizontal el centro de la pantalla
		setX(juegoThread.mCanvasWidth/2);
		//Y como posici�n en el eje vertical el limite donde se encuentra el cesped
		BackGround backGround = (BackGround) process.getMultiDrawable(
				process.getListDrawable(), "com.objects.BackGround");
		
		limity=(int)(backGround.getHorizont_Y());
		LIMIT_Y=limity;
		setY(limity-getHeight());
		setDestinyTop(destinyX);
		
	}
	/**
	 * Inicia el ciclo en el que ha sido alcanzado por una bala.
	 */
	public void iniciarCicloDisparado(){
		setEstado(STATE_HITTED);
		anim.initAnimation(ANIM_MOVE_GOLPEADO,false);
		reiniciarTiempoEntreEstados();
	}
	/**
	 * Inicia el ciclo en el que empieza a huir el pato.
	 */
	public void iniciarCicloHuyendo(){
		setEstado(STATE_HUYENDO);
		anim.initAnimation(ANIM_MOVE_HUYENDO, true);
		setDestinyX(getX());
 		setDestinyY(-getHeight());
 		setVy(DPI_SPEED_ESCAPE*process.getView().deviceDensity);
	}

	
	public static  int LIMIT_Y=308;

	public static final int DISTANCIA_HACIA_SIGUIENTE_PUNTO=100;
	public static final float VELOCIDAD_HASTA_SIGUIENTE_PUNTO=1.5f;
	public static final float VELOCIDAD_HUYENDO_PRESENTATION=2f;
	public final static float DPI_SPEED_ESCAPE=2.5F;
	public final static float DPI_SPEED_FALLING=2f;
//	
	/**
	 * Calcula el punto de destino del Pato. Cada vez que nuestro personaje llegue a su destino, habra que recalcular dicho valor
	 * El procedimiento sera: calcular un angulo de forma aletoria y con una distancia dada y a partir de la posici�n actual,
	 * se calcula el punto de destino.
	 */
	private void calcularNuevoDestino(){
		do{
			calcularDestinoAleatorio();
		}while (esElDestinoNoSeaValido());

		
		double dy=getDestinyY()-getY();   
		double dx=getDestinyX()-getX();
		double tmpangle=Math.atan2(dy,dx );
		setVx((float) (Math.abs((Math.cos(tmpangle)*VELOCIDAD_HASTA_SIGUIENTE_PUNTO*process.getView().deviceDensity))));
		setVy((float) (Math.abs((Math.sin(tmpangle)*VELOCIDAD_HASTA_SIGUIENTE_PUNTO*process.getView().deviceDensity))));
		

//		setVx(Math.abs((getDestinyX()-getX())*VELOCIDAD_HASTA_SIGUIENTE_PUNTO*process.getView().deviceDensity));
//		setVy(Math.abs((getDestinyY()-getY())*VELOCIDAD_HASTA_SIGUIENTE_PUNTO*process.getView().deviceDensity));
		iniciarAnimacionVolando();
	}
	public void setDestinyTop(int x){
		setDestinyX((int)x);
		setDestinyY((int)-getWidth());
		double dy=getDestinyY()-getY();   
		double dx=getDestinyX()-getX();
		double tmpangle=Math.atan2(dy,dx );
		setVx((float) (Math.abs((Math.cos(tmpangle)*VELOCIDAD_HUYENDO_PRESENTATION*process.getView().deviceDensity))));
		setVy((float) (Math.abs((Math.sin(tmpangle)*VELOCIDAD_HUYENDO_PRESENTATION*process.getView().deviceDensity))));
		iniciarAnimacionVolando();
		//calcularNuevoDestino();
	}
	
	private void calcularDestinoAleatorio(){
		//Calculamos un angulo aleatorio
		double angle=Math.random()*Math.PI*2;
		
		//Calculamos la componente en el Eje X con el angulo y el modulo precalculado
		double vectorX=Math.cos(angle)*DISTANCIA_HACIA_SIGUIENTE_PUNTO*process.getView().deviceDensity;
		//Calculamos la componente en el Eje Y con el angulo y el modulo precalculado
		double vectorY=Math.sin(angle)*DISTANCIA_HACIA_SIGUIENTE_PUNTO*process.getView().deviceDensity;
		//Y a�adimos a la posici�n actual el vector calculado
		

		setDestinyX((int)(getX()+vectorX));
		setDestinyY((int)(getY()+vectorY));

	}
	private boolean esElDestinoNoSeaValido(){
		return (getDistance(getX(),getY(),getDestinyX(),getDestinyY())<100 ||
		getDestinyX()<0 || getDestinyY()<0 || getDestinyX()>juegoThread.mCanvasWidth-getWidth() || getDestinyY()>LIMIT_Y-getWidth());
	}
	/**
	 * Calcula la distancia entre dos puntos.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	int getDistance(float x1,float y1,float x2,float y2){
		return (int)Math.sqrt(Math.pow(x1-x2, 2)*Math.pow(y1-y2, 2));
		
	}
	/**
	 * Prepara la animaci�n de vuelo de nuestro personaje
	 */
	void iniciarAnimacionVolando(){
		//Calcula que tipo de animaci�n debe de usar, si la horizontal, o diagonal
		anim.initAnimation(getDestinyY()-getY()>0?ANIM_MOVE_HORIZONTAL:ANIM_MOVE_DIAGONAL,true);
		//Calcula el sentido de la animaci�n y por tanto si tiene que hacer un espejado de la animaci�n.
		anim.setEspejadoHorizontal(getDestinyX()<getX());
	}

		/**
	 * Devuelve el identificador del personaje
	 * @return identificador
	 */
	public int getID(){
		return id;
	}





	public int ultimosonido;
	public void process(){
		//Actualizamos el tiempo de la animaci�n
		anim.actualizarTiempo();
		//Actualizamos el tiempo 
		//actualizarTiempoEntreEstados(JuegoThread.GAP_PROCESS);
		switch (getEstadoActual()) {
			case STATE_MOVE:{
				//Actualizamos la posici�n de nuestro personaje
				moverPersonaje();
				//Si ha llegado a su destino
				if (llegoAlDestino()){
					//Calculamos nuevo destino
//					if (process.getState()==IProcess.PRESENTATION_STATE_DUCKS_FLYING){
//						setEstado(STATE_ESCAPO);
//					}else{
						calcularNuevoDestino();
					//}
		        	
				}
			break;
			}
			case STATE_HUYENDO:{
				//Actualizamos la posici�n de nuestro personaje
				moverPersonaje();
				//Si ha llegado a su destino
				if (llegoAlDestino()){
					//Ha escapado
					setEstado(STATE_ESCAPO);
				}
			break;
			}
			case STATE_HITTED:{
				//Si pasa un segundo desde que es disparado
				if (getTiempoEntreEstados()>500){
					//Fijamos como destino el suelo
					setDestinyY(LIMIT_Y-getHeight());
					setDestinyX(getX());
					//A una velocidad
					setVy(DPI_SPEED_FALLING*process.getView().deviceDensity);
					setVx(0);
					//Con cierta animacion
					anim.initAnimation(ANIM_MOVE_CAYENDO,true);
					//Y cambiamos al estado cayendo
					setEstado(STATE_CAYENDO);
					ultimosonido=process.requestSound(ProcessGame.SONIDO_CAIDA);

				}
			break;
			}
		    case STATE_CAYENDO:{
				//Actualizamos la posici�n de nuestro personaje
				moverPersonaje();
				//Si ha llegado a su destino
				if (llegoAlDestino()){
					//Cambiamos al estado muerto
					setEstado(STATE_MUERTO);
					process.requestSound(ProcessGame.SONIDO_CAYO);
					process.pausarSonido(ultimosonido);
				}
		      break;
			}
		    case STATE_INTRO:{
				//Actualizamos la posici�n de nuestro personaje
				moverPersonaje();
				//Si ha llegado a su destino
				if (llegoAlDestino()){
					//Cambiamos al estado muerto
					setX(-getSize());
				}
		      break;
			}
		}

	}
	public void drawDebug(Canvas canvas, Paint paint) {
		canvas.drawRect(getX(),getY(),getX()+getWidth(),getY()+getHeight(), paint);
	}
	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
		// TODO Auto-generated method stub
	    if (anim != null) {
	    	//Si esta nuestro personaje esta cayendo porque ha sido abatido
	    	if (getEstadoActual()==STATE_CAYENDO){
	    		//Haremos un efecto intermitente de espejado, igual que en el juego original.
		    	if (getTiempoEntreEstados()%200>100){
					anim.setEspejadoHorizontal(true);
				}else{
					anim.setEspejadoHorizontal(false);
				}
	    	}
	    	anim.pintarAnimacion(canvas,(int)getX(),(int)getY(),paint);
	    	//long sprite=(anim.getTiempoActual());
	    	//System.out.println("pato-->"+sprite);
	    }
		
	}
	/**
	 * Funci�n encargada de pintar a nuestro personaje
	 * @param canvas
	 * @param paint
	 */
	public void paint(Canvas canvas,Paint paint){
		


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
		
		anim= new ObjetoAnimable(listDrawable.get(index_duck),ANIMATIONS,TIME_FRAMES,getSize(),11);
		
	}
	@Override
	public void init(List<MultiDrawable> listDrawable) {
		BackGround backGround = (BackGround) process.getMultiDrawable(
				listDrawable, "com.objects.BackGround");
		
		int limity=(int)(backGround.getHorizont_Y()-getHeight());
		LIMIT_Y=limity;
		setY(limity-getSize());
	}

}
