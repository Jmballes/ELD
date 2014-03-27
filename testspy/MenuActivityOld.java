package com.jmbdh;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.JuegoView;

public class MenuActivityOld extends FragmentActivity  implements IActivityHandler {
	/** Called when the activity is first created. */
	// public static final int MENU_JUGAR = 0;
	// public static final int MENU_OPCIONES = 1;
	MenuFactory menuFactory;
	CheckBox sonido;
	CheckBox vibracion;
	//Spinner modo;
	//List<ObjectAnimator> animStart;
	SharedPreferences app_preferences;
	JuegoView g1;
	public static final int DIALOG_OPCIONES = 2;
	public static final int DIALOG_AYUDA = 3;
	public static final int DIALOG_SALIR = 4;
	int mode_game = 0;
	static ImageButton bjugarx2;
	static ViewGroup viewGroup;
	static ImageButton bjugar;
	static ImageButton bexit;
	static ImageButton bayuda;
	static ImageButton boptions;
	//private LayoutTransition mTransitioner;
	//public List<View> lViews;
	//private List<Animator> lAnimator;
	public int optionPressed;
	//private AnalyticsHelper helper;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//heselper=new AnalyticsHelper(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.layout_principal);
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//		lViews = new ArrayList<View>();
//		viewGroup = (ViewGroup) findViewById(R.id.hidecontainer);
//		bjugar = (ImageButton) findViewById(R.id.jugar);
		g1 = (JuegoView) findViewById(R.id.glsurfaceview);
		g1.init(this, "ProcessPresentation");
//		Log.i(getClass().getPackage().toString(), "creating primera acitivty");
		g1.getThread().setActivity(this);
//		bjugar.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//setVisible(View.INVISIBLE);
//				g1.setState(IProcess.PRESENTATION_STATE_COURTAIN_CLOSING_INIT);
//				mode_game = 1;
//
//			}
//		});
//		bjugarx2 = (ImageButton) findViewById(R.id.jugarx2);
//
//		bjugarx2.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//setVisible(View.INVISIBLE);
//				g1.setState(IProcess.PRESENTATION_STATE_COURTAIN_CLOSING_INIT);
//				mode_game = 2;
//			}
//		});
//
//		bexit = (ImageButton) findViewById(R.id.salir);
//		bexit.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				DialogFragment exitFragment=ExitDialogFragment.newInstance();
//				
//				exitFragment.show(getSupportFragmentManager(), "exit");
//			}
//		});
//		bayuda = (ImageButton) findViewById(R.id.ayuda);
//		bayuda.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				DialogFragment helpFragment=AboutDialogFragment.newInstance();
//				helpFragment.show(getSupportFragmentManager(), "dialog");
//			}
//		});
//		boptions = (ImageButton) findViewById(R.id.opciones);
//		boptions.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				showDialog(DIALOG_OPCIONES);
//			}
//		});
//		lViews.add(bjugar);
//		lViews.add(bjugarx2);
//		lViews.add(boptions);
//		lViews.add(bayuda);
//		lViews.add(bexit);
		
		
//		int size = hidecontainer.getChildCount();
//		for (int i = 0; i < size; i++) {
//			View view = hidecontainer.getChildAt(i);
//			Log.i("Test", view.getClass() + "," + view.getId());
//		}
		//setupCustomAnimations();
		menuFactory = MenuFactory.newInstance(this);
		menuFactory.init();
	}
	public void introFinished(){
		menuFactory.buttonsAppear();
	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public void animStart() {
//		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//		animStart = new ArrayList<ObjectAnimator>();
//		for (View button : lViews) {
//			int w = button.getWidth();
//			int index = lViews.indexOf(button);
//			button.setVisibility(View.VISIBLE);
//			button.postInvalidate();
//			button.invalidate();
//			ObjectAnimator oA = ObjectAnimator.ofFloat(button, "x",
//					(index + 1) * w * -1,  w/5 ).setDuration(
//					(index + 1) * 500);
//			oA.setRepeatCount(0);
//			oA.setTarget(button);
//			oA.setRepeatMode(ObjectAnimator.REVERSE);
//			oA.setInterpolator(new AccelerateDecelerateInterpolator());
//			oA.start();
//			// oA.addUpdateListener(this);
//			oA.addListener(new AnimatorListenerAdapter() {
//
//				public void onAnimationEnd(Animator animation) {
//					View view = (View) ((ObjectAnimator) animation).getTarget();
//					int index = lViews.indexOf(view);
//					ObjectAnimator anim = ObjectAnimator.ofFloat(view, "x",
//							view.getX(), 0).setDuration(2000);
//					anim.setRepeatCount(ObjectAnimator.INFINITE);
//					anim.setRepeatMode(ObjectAnimator.REVERSE);
//					anim.setInterpolator(new AccelerateDecelerateInterpolator());
//					anim.start();
//					animStart.add(anim);
//
//					anim.setTarget(view);
//
//				}
//
//			});
//
//
//		}
//		}
//		setVisible(View.VISIBLE);
//	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void setVisibleAll(int visibility) {
		/**bjugar.setVisibility(visibility);
		bjugarx2.setVisibility(visibility);
		bexit.setVisibility(visibility);
		boptions.setVisibility(visibility);
		bayuda.setVisibility(visibility);
		viewGroup.setVisibility(visibility);
		bjugar.postInvalidate();
		bjugarx2.postInvalidate();
		bexit.postInvalidate();
		boptions.postInvalidate();
		bayuda.postInvalidate();
		viewGroup.postInvalidate();
		bjugar.getParent().requestTransparentRegion(g1);*/
	}
	public void setVisible(int visibility) {
//		showinfo();
//		viewGroup.setVisibility(visibility);
//		
//		//viewGroup.postInvalidate();
//		//bjugar.getParent().requestTransparentRegion(g1);
//		View viewparent=(View)viewGroup.getParent();
//		viewGroup.requestTransparentRegion(viewparent);
//		viewGroup.requestTransparentRegion(g1);
//		//viewparent.requestTransparentRegion(g1);
//		viewGroup.getParent().requestTransparentRegion(viewGroup.getRootView());
//		setVisibleAll(visibility);
//		showinfo();
//		//Log.d(tag, msg)
	}
	public void showinfo(){
		int size = viewGroup.getChildCount();
		View viewparent=(View)viewGroup.getParent();
		Log.d("Test", "---------------------------------------------------");
		Log.d("Test", viewparent.getClass() + "," + viewparent.getId()+","+viewparent.getVisibility());
		Log.d("Test", viewparent.getX()  + "," + viewparent.getY());
		Log.d("Test", viewGroup.getClass() + "," + viewGroup.getId()+","+viewGroup.getVisibility());
		Log.d("Test", viewGroup.getX()  + "," + viewGroup.getY());
		Log.d("Test", "---------------------------------------------------");
		for (int i = 0; i < size; i++) {
			View view = viewGroup.getChildAt(i);
			Log.d("Test", view.getClass() + "," + view.getId()+","+view.getVisibility());
			Log.d("Test", view.getX()  + "," + view.getY());
		}
	}
	public void startNewGame() {
		g1.destroy();
		g1 = null;

		Intent intent = new Intent();
		intent.putExtra("modojuego", mode_game);
		intent.setClass(MenuActivity.this, GameActivity.class);
		startActivity(intent);
		finish();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
//		case DIALOG_SALIR:
//			return new AlertDialog.Builder(MenuActivity.this)
//					.setIcon(R.drawable.icon)
//					.setTitle(R.string.dialog_salir_titulo)
//					.setMessage(R.string.dialog_salir_texto)
//					.setPositiveButton(R.string.dialog_salir_ok,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//									optionPressed = R.id.salir;
//									setAllViewsOut();
//									/* User clicked OK so do some stuff */
//								}
//							})
//					.setNegativeButton(R.string.dialog_salir_cancelar,
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int whichButton) {
//
//									/* User clicked Cancel so do some stuff */
//								}
//							}).create();
		case DIALOG_AYUDA:
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.layout_ayuda,
					null);
			return new AlertDialog.Builder(MenuActivity.this)
					.setIcon(R.drawable.icon).setTitle(R.string.ayuda)
					.setView(textEntryView)
					// .setPositiveButton(R.string.dialog_salir_ok, new
					// DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog, int
					// whichButton) {
					//
					// /* User clicked OK so do some stuff */
					// }
					// })
					// .setNegativeButton(R.string.dialog_salir_cancelar, new
					// DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog, int
					// whichButton) {
					//
					// /* User clicked cancel so do some stuff */
					// }
					// })
					.create();

		case DIALOG_OPCIONES:
			// Lectura de preferencias guardadas
			app_preferences = PreferenceManager
					.getDefaultSharedPreferences(this);
			boolean sonido_boolean = app_preferences.getBoolean(
					"preference_sonido", false);
			boolean vibracion_boolean = app_preferences.getBoolean(
					"preference_vibracion", false);
			String control = app_preferences.getString("preference_controles",
					"no valor");

			// Conseguimos los identificadores de nuestra interfaz guardada en
			// XML
			factory = LayoutInflater.from(this);
			final View textEntryView2 = factory.inflate(
					R.layout.layout_opciones_pausa, null);
			sonido = (CheckBox) textEntryView2.findViewById(R.id.check2);
			vibracion = (CheckBox) textEntryView2.findViewById(R.id.check3);
			

			// Establecemos las preferencias cargadas, a nuestra interfaz
			sonido.setChecked(sonido_boolean);
			vibracion.setChecked(vibracion_boolean);
			
			// Creamos el dialogo
			return new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon)
					.setTitle(R.string.opciones)
					.setView(textEntryView2)
					.setPositiveButton(R.string.dialog_salir_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									SharedPreferences.Editor editor = app_preferences
											.edit();
									editor.putBoolean("preference_sonido",
											sonido.isChecked());
									editor.putBoolean("preference_vibracion",
											vibracion.isChecked());
									
									editor.commit();

								}
							})
					.setNegativeButton(R.string.dialog_salir_cancelar,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create();

		}

		return null;
	}

	@Override
	public Handler getHandler() {
		return null;
	}

	@Override
	public void showMenu() {
		// TODO Auto-generated method stub

	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void setupCustomAnimations() {
//		for (View view : lViews) {
//			if (view.getId() == R.id.jugar || view.getId() == R.id.jugarx2) {
//				view.setOnClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						// Removing
//						optionPressed = v.getId();
//						ObjectAnimator animOut = ObjectAnimator.ofFloat(v,
//								"rotationX", 0f, 90f).setDuration(1000);
//
//						animOut.setRepeatCount(0);
//						animOut.setTarget(v);
//						animOut.start();
//
//						animOut.addListener(new AnimatorListenerAdapter() {
//							public void onAnimationEnd(Animator anim) {
//								setAllViewsOut();
//							}
//						});
//					}
//				});
//			}
//
//		}
//
//	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public void getAnimationOnClick(View view) {
//
//		optionPressed = view.getId();
//		ObjectAnimator animOut = ObjectAnimator
//				.ofFloat(view, "rotationX", 0f, 90f).setDuration(1000);
//
//		animOut.setRepeatCount(0);
//		animOut.setTarget(view);
//		animOut.start();
//
//		animOut.addListener(new AnimatorListenerAdapter() {
//			public void onAnimationEnd(Animator anim) {
//				
//				
//			}
//		});
//
//	}

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	public void setAllViewsOut() {
//		for (ObjectAnimator aS : animStart) {
//			aS.cancel();
//		}
//		for (View view : lViews) {
//			int w = view.getWidth();
//			int index = lViews.indexOf(view);
//
//			ObjectAnimator oA = ObjectAnimator.ofFloat(view, "x", view.getX(),
//					-w).setDuration((index + 1) * 500);
//			oA.setRepeatCount(0);
//			oA.setRepeatCount(0);
//			oA.setTarget(view);
//			oA.setRepeatMode(ObjectAnimator.REVERSE);
//			oA.setInterpolator(new AccelerateDecelerateInterpolator());
//			oA.start();
//
//			oA.addListener(new AnimatorListenerAdapter() {
//				public void onAnimationEnd(Animator anim) {
//					View view = (View) ((ObjectAnimator) anim).getTarget();
//					int index = lViews.indexOf(view);
//					if (index==lViews.size()-1){
//					//if (areAllViewsHiden()) {
//						nextOption();
//					}
//
//				}
//			});
//		}
//
//	}

//	private boolean areAllViewsHiden() {
//		boolean result = true;
//		
//		for (View view : lViews) {
//			if (view.getX() > -view.getWidth()) {
//				result = false;
//				break;
//			}
//		}
//		return result;
//	}

	public void nextOption() {
		switch (optionPressed) {
		case R.id.jugar:
			g1.setState(IProcess.PRESENTATION_STATE_COURTAIN_CLOSING_INIT);
			mode_game = 1;
			break;
		case R.id.jugarx2:
			g1.setState(IProcess.PRESENTATION_STATE_COURTAIN_CLOSING_INIT);
			mode_game = 2;
			break;
		case R.id.salir:
			finish();
			// showDialog(DIALOG_SALIR);
			break;
		default:
			break;
		}
	}

	public void doExitOkClick() {
		menuFactory.startExit();
		
	}

	public void doExitCancelClick() {
		// TODO Auto-generated method stub
		
	}

}