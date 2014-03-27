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
import com.draw.Personaje;
import com.draw.Zone;
import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.JuegoView.JuegoThread;

public class Perro extends Personaje{
	int[] resourcesDrawable = { R.drawable.perro };
	private int index_Dog=0;
	
	int perro_size=0;
	public final static int TIME_FRAMES=300;
	public final static byte[] ANIMATIONS={0,3,
										3,2,
										5,1,
										6,2,
										8,2,
										10,1,
										11,1
										   
	};

	//public int kSpriteSize = 56;
	public int horizont_y=0;
	public int arbusto_top=0;
	public int arbusto_bottom=0;
	public int arbusto_behind_z=0;
	public int arbusto_onfront_z=0;
//	/**
//	 * Posici�n en el eje vertical de nuestro personaje durante la Intro
//	 */
//	public static final int POSICION_VERTICAL_DURANTE_LA_INTRO=308;
	/**
	 * Posici�n en el eje vertical de nuestro personaje de donde aparece cuando
	 * sale riendose, y a la que se dirige cuando desaparece.
	 */
//	public static final int POSICION_VERTICAL_RIENDO_OCULTO=308;
	/**
	 * Posici�n en el eje vertical de nuestro personaje cuando aparece riendose.
	 */
	//public static final int POSICION_VERTICAL_RIENDO_VISIBLE=255;


	/**
	 * Inicia nuestro ciclo de Intro
	 */
	public void iniciarEstadoDeIntro(int horizont_y,int arbusto_y,int arbusto_onfront_z,int arbusto_behind_z){
		//	Inicia los puntos a los que se dirigir� nuestro personaje
		posicionesDuranteIntro=new int[4];
		posicionesDuranteIntro[0]=(int) ((juegoThread.mCanvasWidth/2)-(getWidth()/2)-60*process.getView().deviceDensity);
		posicionesDuranteIntro[1]=(int) ((juegoThread.mCanvasWidth/2)-(getWidth()/2)-40*process.getView().deviceDensity);
		posicionesDuranteIntro[2]=(int) ((juegoThread.mCanvasWidth/2)-(getWidth()/2)-20*process.getView().deviceDensity);
		posicionesDuranteIntro[3]=(int) ((juegoThread.mCanvasWidth/2)-(getWidth()/2)-0);
		
		//Se inicia desde el punto 0
		actualPosicionEnIntro=0;
		
		//Iniciamos los valores
		setX(posicionesDuranteIntro[actualPosicionEnIntro]);
		setDestinyX(posicionesDuranteIntro[actualPosicionEnIntro+1]);
		setVx(0.5f*process.getView().deviceDensity);
		//setVy(2f);
		this.horizont_y=horizont_y;
		this.arbusto_behind_z=arbusto_behind_z;
		this.arbusto_onfront_z=arbusto_onfront_z;
		this.arbusto_top=(int) (arbusto_y+15-getWidth());
		this.arbusto_bottom=arbusto_y;
		setY(horizont_y-getWidth());
		setZ(arbusto_onfront_z);
		
		setDestinyY(getY());
		horizont_y=(int)getY();
		setEstado(ESTADO_INTRO_MOVIENDOSE);

		anim.initAnimation(0, true);

	}
	
	/**
	 * Inicia Ciclo de Victoria
	 */
	public void iniciarCicloVictoria(int patos,int x){
		setEstado(Perro.ESTADO_VICTORIA_APARECIENDO_DESAPARECIENDO);
		setX(x);
		setDestinyX(x);
		setVy(1.3f*process.getView().deviceDensity);
		setY(arbusto_bottom);
		setDestinyY(arbusto_top);
		anim.initAnimation(4+patos, true);
	}
	
	/**
	 * Inicia diclo de Derrota
	 */
	public void iniciarCicloDerrota(){
		setEstado(Perro.ESTADO_RISA_APARECIENDO_DESAPARECIENDO);
		setVy(1.3f*process.getView().deviceDensity);
		setY(arbusto_bottom);
		setDestinyY(arbusto_top);
		anim.initAnimation(4, true);
	}
	
	
	


	

	public final static byte ESTADO_INTRO_MOVIENDOSE=1;
	public final static byte ESTADO_INTRO_OLFATEAR=2;
	public final static byte ESTADO_INTRO_SORPRESA=3;
	public final static byte ESTADO_INTRO_SALTO_SUBIENDO=4;
	public final static byte ESTADO_INTRO_SALTO_CAYENDO=5;
	public final static byte ESTADO_RISA_APARECIENDO_DESAPARECIENDO=6;
	public final static byte ESTADO_RISA_VISIBLE=7;
	public final static byte ESTADO_VICTORIA_APARECIENDO_DESAPARECIENDO=8;
	public final static byte ESTADO_VICTORIA_VISIBLE=9;


	
	/**
	 * Referencia al Thread de nuestro juego
	 */
	JuegoThread juegoThread;
	
	/** Referencia a un objeto de tipo Animacion que nos permitira manejar las
	 * animaciones de nuestro personaje.
	 */
	ObjetoAnimable anim;
	
	/**
	 * Constructor para inicializar nustro personaje.
	 * @param bitmap	Bitmap que contendra los graficos de nuestro personaje
	 * @param juegoThread	Referencia al Thread de nuestro juego
	 */
	public Perro(Bitmap bitmap,JuegoThread juegoThread){
		this.juegoThread=juegoThread;
		anim= new ObjetoAnimable(bitmap,ANIMATIONS,TIME_FRAMES,(int) getWidth(),12);
	}
	
	

	
	/**
	 * Funci�n que pintara nuestro personaje en las coordenadas previamente procesadas
	 * @param canvas	Canvas sobre el que se pintar� nuestro personaje.
	 * @param paint	Estilo con el que se pintara.
	 */
	public void paint(Canvas canvas,Paint paint){
		
	}


	/**
	 * Posiciones por las que se movera nuestro personaje durante la Intro
	 */
	private int []posicionesDuranteIntro;
	/**
	 * Actual punto al que se dirige nuestro personaje durante la Intro
	 */
	private int actualPosicionEnIntro;
	
	

	
	
	/**
	 * Funci�n encargada de procesar nuestro personaje. Sus estados estan automatizados y dicha funci�n devolver�
	 * true, cuando su ciclo haya terminado. Podriamos considerar tres ciclos de estados para nuestro personaje:
	 * <p><b>Ciclo de Intro:</b> ESTADO_INTRO_INICIO, ESTADO_INTRO_MOVIENDOSE, 
	 *		ESTADO_INTRO_OLFATEAR, ESTADO_INTRO_SORPRESA, ESTADO_INTRO_SALTO_SUBIENDO, ESTADO_INTRO_SALTO_CAYENDO
	 * 
	 * <p><b>Ciclo de Risa:</b> ESTADO_RISA_APARECIENDO_DESAPARECIENDO, ESTADO_RISA_VISIBLE.
	 * <p><b>Ciclo de Victoria:</b> ESTADO_VICTORIA_APARECIENDO_DESAPARECIENDO, ESTADO_VICTORIA_VISIBLE.
	 * @return true si ha terminado su ciclo de estados.
	 */
	public boolean process(){
		anim.actualizarTiempo();
		//actualizarTiempoEntreEstados(JuegoThread.GAP_PROCESS);

		switch (getEstadoActual()) {

			case ESTADO_INTRO_MOVIENDOSE:{
				// 	En este estado nuestro personaje se movera a traves de unos puntos. Entre punto
				//	y punto nuestro personaje hara una animaci�n de olfateo.
				moverPersonaje();	
				
				//Si ha llegado a su destino
				if (getX()==getDestinyX()){
					//Preguntamos si es el penultimo punto, ya que el ultimo lo guardaremos para que nuestro
					//	personaje durante el salto avance un poco.
					if (actualPosicionEnIntro+2>=posicionesDuranteIntro.length){
						//	Si lo es, cambiamos a un estado de sorpresa, que mostrara su animaci�n.
						anim.initAnimation(2, false);
						setEstado(ESTADO_INTRO_SORPRESA);
						setDestinyY(Diablo.LIMIT_Y-60);
					}else{
						//	Si no es el ultimo punto, cambiamos a un estado en el que nuestro personaje
						//	mostrara una animaci�n en la que olfeatea el suelo. Tiempo m�s tarde, volver�
						//	a este estado para completar todos los puntos.
						anim.initAnimation(1, true);
						setEstado(ESTADO_INTRO_OLFATEAR);

						actualPosicionEnIntro++;
						setX(posicionesDuranteIntro[actualPosicionEnIntro]);
						setDestinyX(posicionesDuranteIntro[actualPosicionEnIntro+1]);
					}
				}
			break;
			}
			case ESTADO_INTRO_OLFATEAR:{
				//En este estado nuestro personaje olfatear� el suelo, y volvera al estado anterior.
				if (getTiempoEntreEstados()>1000){
					anim.initAnimation(0, true);
					setEstado(ESTADO_INTRO_MOVIENDOSE);
					

				}
			break;
			}
			case ESTADO_INTRO_SORPRESA:{
				//	En este estado simplemente mostraremos una animaci�n en la que nuestro personaje
				//	aparece sorprendido. Cuando pase un segundo, lo cambiaremos para que inicie
				//	el salto.
				if (getTiempoEntreEstados()>1000){
					anim.initAnimation(3, false);
					setEstado(ESTADO_INTRO_SALTO_SUBIENDO);
					setVy(2f*process.getView().deviceDensity);
					setDestinyY(arbusto_top);
				}
			break;
			}
			case ESTADO_INTRO_SALTO_SUBIENDO:{
				//	En este estado nuestro personaje iniciara su salto, ademas de moverse en el eje vertical,
				//	tambien lo hace un poco en el horizontal, a diferencia del estado siguiente que sera cuando
				//	esta cayendo.
				moverPersonaje();	
				if (getX()==getDestinyX() && getY()==getDestinyY()){
					setEstado(ESTADO_INTRO_SALTO_CAYENDO);
					setDestinyY(arbusto_bottom);
					setZ(arbusto_behind_z);
				}
			break;
			}
			case ESTADO_INTRO_SALTO_CAYENDO:{
				//	En este estado nuestro personaje hara la parte en la que cae en el salto. Por tanto
				// 	se puede dar por concluido este ciclo de Intro.
				moverPersonaje();	
				if (getX()==getDestinyX() && getY()==getDestinyY()){
					return true;
				}
			break;
			}
			case ESTADO_RISA_APARECIENDO_DESAPARECIENDO:{
				//	En este estado nuestro personaje aparecera y desaparecera en el ciclo en el que pierde
				//	una minifase. Cuando aparezca por completo, cambiara a un estado visible, en el que se
				//	mostrar� por encima del cesped durante un segundo, y volver� a este estado para desaparecer.
				//	Cuando desaparezca por completo, podemos dar por terminado este Ciclo.
				moverPersonaje();
				if (getY()==getDestinyY()){
					if (getDestinyY()==arbusto_top){
						setDestinyY(arbusto_bottom);
						setEstado(ESTADO_RISA_VISIBLE);

					}else{
						return true;
					}
				}
			break;
			}
			case ESTADO_RISA_VISIBLE:{
				//	Estado en el que se mostrar� a nuestro personaje riendose, y al cabo de un segundo
				//	volvera al estado anterior en el que desaparecera.
				if (getTiempoEntreEstados()>(process.getState()==process.JUEGO_ESTADO_PARTIDATERMINADA?3000:300)){
					setEstado(ESTADO_RISA_APARECIENDO_DESAPARECIENDO);
				}
			break;
			}
			case ESTADO_VICTORIA_APARECIENDO_DESAPARECIENDO:{
				//	En este estado nuestro personaje aparecera y desaparecera en el ciclo en el que gana
				//	una minifase. Cuando aparezca por completo, cambiara a un estado visible, en el que se
				//	mostrar� por encima del cesped durante un segundo, y volver� a este estado para desaparecer.
				//	Cuando desaparezca por completo, podemos dar por terminado este Ciclo.
				moverPersonaje();	
				if (getY()==getDestinyY()){
					if (getDestinyY()==arbusto_top){
						setDestinyY(arbusto_bottom);
						setEstado(ESTADO_VICTORIA_VISIBLE);
					}else{
						return true;
					}
				}
			break;
			}
			case ESTADO_VICTORIA_VISIBLE:{
				// 	Estado en el que se mostrar� a nuestro personaje con uno/dos pato/s en su mano
				// 	y al cabo de un segundo	volvera al estado anterior en el que desaparecera.
				
				if (getTiempoEntreEstados()>300){
					setEstado(ESTADO_VICTORIA_APARECIENDO_DESAPARECIENDO);

				}
			break;
			}

			
		}
		return false;
	}

	
	public void drawDebug(Canvas canvas, Paint paint) {
		
	}
	
		@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {
//			paint.setColor(Color.argb(0x99, 0xFF, 0x11, 0x11));
//
//			canvas.drawRect(getX(), getY(),
//					getX() + getWidth(), getY() + getHeight(),
//					paint);
			if (anim!=null){
				anim.pintarAnimacion(canvas,(int)getX(),(int)getY(),paint);
			}
			//canvas.drawText("Z:"+getZ(),(int)20,(int)20,paint);
			//canvas.drawBitmap(anim.getBitmap(), 0,0, paint);
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
		setHeight(bitmap.getWidth() );
		setWidth(bitmap.getWidth());
		anim= new ObjetoAnimable(listDrawable.get(index_Dog),ANIMATIONS,TIME_FRAMES,(int) getWidth(),12);

		

	
	}

	@Override
	public void init(List<MultiDrawable> listDrawable) {
//		kSpriteSize=1;
//		setWidth(kSpriteSize);
//		setHeight(kSpriteSize);
		
	}

}
