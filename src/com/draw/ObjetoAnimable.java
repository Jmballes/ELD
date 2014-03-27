package com.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.objects.Diablo;

public class ObjetoAnimable {
	
	/**
	 * Actual animaci�n que se esta mostrando.
	 */
	
	public int actualAnimacion;
	
	/**
	 * Tiempo transcurrido de la animaci�n actual.
	 */
	public long tiempoAnimacion;
	
	
	/**
	 * 	Si una animaci�n es ciclica, a pesar de que el tiempo transcurrido haya superado el total de la animaci�n,
	 *  se reproducir� desde el inicio. 
	 */
	boolean ciclica;
	
	/**
	 * 	Indica si se tiene que hacer un espejado horizontal.
	 */
	boolean espejadoHorizontal;
	
	/**
	 * Indica el numero de animaciones que tiene, adem�s de los sprites a los que corresponde
	 */
	public final byte[] ANIMACIONES;
	
	/**
	 * Tama�o de un sprite
	 */
	public int TAMANIO_SPRITE;
	
	/**
	 * Tiempo que dura un Frame
	 */
	public final int TIEMPO_DURACION_SPRITES;
	public final int total_sprites;
	/**
	 * Bitmap que contendra todos nuestros frames.
	 */
	public final Bitmap sprite;
	
	/**
	 * Constructor que nos permitira iniciar varios atributos.
	 */
	public ObjetoAnimable(Bitmap sprite,byte[] anim,int time,int size,int total_sprites){
		this.sprite=sprite;
		this.ANIMACIONES=anim;
		this.TIEMPO_DURACION_SPRITES=time;
		this.TAMANIO_SPRITE=size;
		this.total_sprites=total_sprites;
	}
	public void setSize(int size){
		this.TAMANIO_SPRITE=size;
	}
	/**
	 * Funcion que actualiza el tiempo actual de la animaci�n
	 */
	public void actualizarTiempo(){
		//tiempoAnimacion+=JuegoThread.GAP_PROCESS;
	}
	/**
	 * Devuelve el tiempo que ha transcurrido en esta animaci�n.
	 * @return Tiempo actual
	 */
	public long getTiempoActual(){
		return (System.currentTimeMillis()-tiempoAnimacion);
	}

	/**
	 * Devuelve el tiempo que dura la animaci�n actual.
	 * @return	Tiempo que dura la animaci�n actual
	 */
	private long  getTiempoTotal(){
		return ANIMACIONES[actualAnimacion*2+1]*TIEMPO_DURACION_SPRITES;
	}
	
	/**
	 * Establece si la animaci�n se imprimira con efecto de espejado horizontal.
	 * @param value True si la animaci�n tendra espejado horizontal
	 */
	public void setEspejadoHorizontal(boolean value){
		espejadoHorizontal=value;
	}
	
	/**
	 * Calcula el sprite que debe pintar de una animaci�n.
	 * @param ciclica Establece si es ciclica la animaci�n
	 * @return Devuelve el sprite a pintar.
	 */
	public final int getSpriteAPintar(){

	    long calcularTiempoActualLimpio=getTiempoActual();
	    //System.out.println("tiempo:"+calcularTiempoActualLimpio);
	    if (ciclica){
	    	calcularTiempoActualLimpio = (calcularTiempoActualLimpio % getTiempoTotal());
	    }else if (calcularTiempoActualLimpio >= getTiempoTotal()){

	      calcularTiempoActualLimpio = getTiempoTotal()-1;
	    }
	    int sprite = (int)(calcularTiempoActualLimpio/TIEMPO_DURACION_SPRITES);
	    
	    return ANIMACIONES[actualAnimacion*2]+sprite;
	} 
	
	/**
	 * Pinta una animaci�n.
	 * @param canvas	Canvas sobre el que se pintar� la animaci�n
	 * @param x	Posici�n en el eje horizontal
	 * @param y	Posici�n en el eje vertical
	 * @param paint	Estilo con el que se pintara.
	 */
	
	public void pintarAnimacion(Canvas canvas,int x,int y, Paint paint) {
		int sprite=getSpriteAPintar();
		drawSprite(canvas,x,y, sprite, paint);
	}
	
	
	  
	  
	/**
	 * Pinta un sprite concreto.
	 * @param canvas	Canvas sobre el que se pintar� la animaci�n
	 * @param x	Posici�n en el eje horizontal
	 * @param y	Posici�n en el eje vertical
	 * @param indexsprite Sprite a pintar
	 * @param paint	Estilo con el que se pintara.
	 */
//	public void drawSprite(Canvas canvas,int x,int y,int indexsprite, Paint paint){
//		RectF sprite_destination = new RectF(0, 0, TAMANIO_SPRITE,TAMANIO_SPRITE);
//        
//        
//        Rect spriteorigen= new Rect(0,indexsprite*TAMANIO_SPRITE,TAMANIO_SPRITE,indexsprite*TAMANIO_SPRITE+TAMANIO_SPRITE);
//    	
//        if (espejadoHorizontal){
//            canvas.save();
//            canvas.scale(-1.0f, 1.0f);
//        	canvas.translate(-x-TAMANIO_SPRITE, y);
//        	canvas.drawBitmap(sprite, spriteorigen, sprite_destination, paint);
//            canvas.restore();
//        }else{
//        	sprite_destination.offset(x, y);
//        	canvas.drawBitmap(sprite, spriteorigen, sprite_destination, paint);
//        }
//
//	}
	public void drawSprite(Canvas canvas,int x,int y,int indexsprite, Paint paint){
		int height=sprite.getHeight()/total_sprites;
		RectF sprite_destination = new RectF(0, 0, sprite.getWidth(),height);
        
        
        Rect spriteorigen= new Rect(0,indexsprite*height,sprite.getWidth(),indexsprite*height+height);
    	
        if (espejadoHorizontal){
            canvas.save();
            canvas.scale(-1.0f, 1.0f);
        	canvas.translate(-x-sprite.getWidth(), y);
        	canvas.drawBitmap(sprite, spriteorigen, sprite_destination, paint);
            canvas.restore();
        }else{
        	sprite_destination.offset(x, y);
        	canvas.drawBitmap(sprite, spriteorigen, sprite_destination, paint);
        }
        //canvas.drawText(indexsprite+"", x,y, paint);
        
	}
	public Bitmap getBitmap(){
		return sprite;
	}
	/**
	 * Inicia una animaci�n en concreto.
	 * @param animacionAPintar Establece la animaci�n actual
	 * @param ciclica	Establece si ser� ciclica.
	 */
	public void initAnimation(int animacionAPintar,boolean ciclica){
		actualAnimacion=animacionAPintar;
		if (!ciclica){
		tiempoAnimacion=System.currentTimeMillis();
		}
		this.ciclica=ciclica;
	}

}
