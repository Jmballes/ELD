package com.objects;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.draw.MultiDrawable;
import com.draw.Zone;
import com.paintview.IProcess;

public class Courtain extends MultiDrawable {


	static final boolean COURTAIN_OPENING = false;
	static final boolean COURTAIN_CLOSING = true;
	public static final int COLOR_BLACK= 0XFF000000;
	public static final int COLOR_WHITE= 0XFFFFFFFF;
	boolean state;
	int color;
	static final int REQUEST_SQUARES_HORIZONTAL = 20;

	static int squareCourtainWidth;

	static int numSquaresHorizontal;
	static int numSquaresVertical;

	static double cortinilla[][];
	boolean finished = false;

	public boolean processCortinilla() {
		double value = squareCourtainWidth * COURTAIN_PROCESS_VEL;

		for (int j = 0; j < numSquaresVertical; j++) {
			for (int i = 0; i < numSquaresHorizontal; i++) {
				if (cortinilla[i][j] > 0) {
					if (cortinilla[i][j] < squareCourtainWidth) {
						cortinilla[i][j] += value;
						if (cortinilla[i][j] > squareCourtainWidth) {
							cortinilla[i][j] = squareCourtainWidth;
						}
					} else {
						cortinilla[i][j] = squareCourtainWidth;
					}
				} else {
					cortinilla[i][j] += value;
				}
			}
		}
		return finished;
	}

	static final int FINALGAME_CURTAIN = 0;
	static final int FINALGAME_CHARACTER_RUNNING = 0;
	static final int appState = 0;
	static final int subappState = 0;
	static final int timeBetweenSubstates = 0;

	@Override
	public void drawDebug(Canvas canvas, Paint paint) {
	
	}
	@Override
	public void draw(Canvas canvas, Paint paint, Zone zone) {

		paint.setColor(color);
		paint.setStyle(Style.FILL);

		int x = 0;
		int y = 0;

		boolean tmpfinished = true;
		for (int j = 0; j < numSquaresVertical; j++) {
			x = 0;
			for (int i = 0; i < numSquaresHorizontal; i++) {
				if (state == COURTAIN_OPENING) {
					if (cortinilla[i][j] > 0) {
						int tmpx = (int) (x + ((cortinilla[i][j]) / 2));
						int tmpy = (int) (y + ((cortinilla[i][j]) / 2));
						int tmpright = (int) (tmpx + squareCourtainWidth - cortinilla[i][j]);
						int tmpbottom = (int) (tmpy + squareCourtainWidth - cortinilla[i][j]);
						canvas.drawRect(tmpx, tmpy, tmpright, tmpbottom, paint);
						if (cortinilla[i][j] < squareCourtainWidth) {
							tmpfinished = false;
						}
					} else {
						canvas.drawRect(x, y, x + squareCourtainWidth, y
								+ squareCourtainWidth, paint);
						tmpfinished = false;
					}

				}else{
					if (cortinilla[i][j] > 0) {
						int tmpx= (int) ((x+squareCourtainWidth/2) - (cortinilla[i][j]) / 2);
						int tmpy= (int) ((y+squareCourtainWidth/2) - (cortinilla[i][j]) / 2);

						int tmpright = (int) (tmpx +  cortinilla[i][j]);
						int tmpbottom = (int) (tmpy +  cortinilla[i][j]);
						canvas.drawRect(tmpx, tmpy, tmpright, tmpbottom, paint);
						if (cortinilla[i][j] < squareCourtainWidth) {
							tmpfinished = false;
						}
					} else {
//						canvas.drawRect(x, y, x + squareCourtainWidth, y
//								+ squareCourtainWidth, paint);
						tmpfinished = false;
					}
				}
				//
				x += squareCourtainWidth;
			}

			y += squareCourtainWidth;
		}
		y = 0;
		this.finished = tmpfinished;

	}

	@Override
	public void load(Resources resources, IProcess process) {


	}

	@Override
	public void init(List<MultiDrawable> listDrawable) {


	}

	static final double COURTAIN_START_SIZE_PERCENTAGE_BEGINNING = 1d;
	static final double COURTAIN_START_SIZE_DIFFERENCE = 0.5d;
	static final double COURTAIN_PROCESS_VEL = 0.1d;

	public void setInvertHorizontal(int x, int y, int width, int height,
			double value) {
		cortinilla[width - x][y] = value;
	}

	public void setInvertVertical(int x, int y, int width, int height,
			double value) {
		cortinilla[x][height - y] = value;
	}

	public void setInvertHV(int x, int y, int width, int height, double value) {
		cortinilla[width - x][height - y] = value;
	}

	public void initCortinilla(boolean showback, int canvasWidth,
			int canvasHeight,int color) {
		int inicio = 1;
		int i;
		state = showback;
		finished=false;
		this.color=color;
		Courtain.squareCourtainWidth = canvasWidth / REQUEST_SQUARES_HORIZONTAL;
		int requestSquaresVertical = canvasHeight / squareCourtainWidth;

		Courtain.numSquaresHorizontal = squareCourtainWidth
				* REQUEST_SQUARES_HORIZONTAL == canvasWidth ? REQUEST_SQUARES_HORIZONTAL
				: REQUEST_SQUARES_HORIZONTAL + 1;
		Courtain.numSquaresVertical = squareCourtainWidth
				* requestSquaresVertical == canvasWidth ? requestSquaresVertical
				: requestSquaresVertical + 1;

		cortinilla = new double[numSquaresHorizontal][numSquaresVertical];

		int mitadv = numSquaresVertical % 2 == 0 ? numSquaresVertical / 2
				: (numSquaresVertical / 2) + 1;
		int mitadh = numSquaresHorizontal % 2 == 0 ? numSquaresHorizontal / 2
				: (numSquaresHorizontal / 2) + 1;
		int numeroDiagonales = (mitadv) + (mitadh);

		double increment = squareCourtainWidth * COURTAIN_START_SIZE_DIFFERENCE;
		double value = (-squareCourtainWidth * COURTAIN_START_SIZE_PERCENTAGE_BEGINNING)
				* numeroDiagonales;
		if (showback){
			 increment = -squareCourtainWidth * COURTAIN_START_SIZE_DIFFERENCE;
			 value = (-squareCourtainWidth * COURTAIN_START_SIZE_PERCENTAGE_BEGINNING)
					;
		}
		for (int j = 0; j < numeroDiagonales; j++) {
			inicio = j >= mitadv ? j - mitadv + 1 : 0;
			i = inicio;
			for (i = inicio; j - i >= 0 && i < mitadh; i++) {
				cortinilla[i][j - i] = cortinilla[i][j - i] + value;
				setInvertHorizontal(i, j - i, numSquaresHorizontal - 1,
						numSquaresVertical - 1, value);
				setInvertVertical(i, j - i, numSquaresHorizontal - 1,
						numSquaresVertical - 1, value);
				setInvertHV(i, j - i, numSquaresHorizontal - 1,
						numSquaresVertical - 1, value);

			}
			value += increment;

		}

	}
}
