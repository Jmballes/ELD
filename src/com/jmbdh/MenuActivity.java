package com.jmbdh;

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
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.JuegoView;

public class MenuActivity extends FragmentActivity implements IActivityHandler {
	/** Called when the activity is first created. */
	// public static final int MENU_JUGAR = 0;
	// public static final int MENU_OPCIONES = 1;
	MenuFactory menuFactory;
	CheckBox sonido;
	CheckBox vibracion;
	// Spinner modo;
	// List<ObjectAnimator> animStart;
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

	public int optionPressed;
	private ImageButton boptions;

	// private AnalyticsHelper helper;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// heselper=new AnalyticsHelper(this);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.layout_principal);
		// getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

		g1 = (JuegoView) findViewById(R.id.glsurfaceview);
		g1.init(this, "ProcessPresentation");

		g1.getThread().setActivity(this);
		bexit = (ImageButton) findViewById(R.id.salir);
		bexit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DialogFragment exitFragment = ExitDialogFragment.newInstance();

				exitFragment.show(getSupportFragmentManager(), "exit");
			}
		});
		bayuda = (ImageButton) findViewById(R.id.ayuda);
		bayuda.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// DialogFragment helpFragment=PuntuacionFragment.newInstance();
				// helpFragment.show(getSupportFragmentManager(), "dialog");
				//
//				Intent intent = new Intent(v.getContext(),
//						PuntuacionFragment.class);
//				v.getContext().startActivity(intent);
				
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, PuntuacionFragment.class);
				startActivity(intent);

			}
		});
		boptions = (ImageButton) findViewById(R.id.opciones);
		boptions.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DialogFragment helpFragment = OptionsDialogFragment
						.newInstance();
				helpFragment.show(getSupportFragmentManager(), "dialog");
			}
		});

		menuFactory = MenuFactory.newInstance(this);
		menuFactory.init();
	}

	public void introFinished(int num) {
		menuFactory.buttonAppear(num);
	}

	public int getNumButtons() {
		final ViewGroup linearContainer = (ViewGroup) findViewById(R.id.linearcontainer);

		return linearContainer.getChildCount();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void showinfo() {
		int size = viewGroup.getChildCount();
		View viewparent = (View) viewGroup.getParent();
		Log.d("Test", "---------------------------------------------------");
		Log.d("Test", viewparent.getClass() + "," + viewparent.getId() + ","
				+ viewparent.getVisibility());
		Log.d("Test", viewparent.getX() + "," + viewparent.getY());
		Log.d("Test", viewGroup.getClass() + "," + viewGroup.getId() + ","
				+ viewGroup.getVisibility());
		Log.d("Test", viewGroup.getX() + "," + viewGroup.getY());
		Log.d("Test", "---------------------------------------------------");
		for (int i = 0; i < size; i++) {
			View view = viewGroup.getChildAt(i);
			Log.d("Test",
					view.getClass() + "," + view.getId() + ","
							+ view.getVisibility());
			Log.d("Test", view.getX() + "," + view.getY());
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
		// case DIALOG_SALIR:
		// return new AlertDialog.Builder(MenuActivity.this)
		// .setIcon(R.drawable.icon)
		// .setTitle(R.string.dialog_salir_titulo)
		// .setMessage(R.string.dialog_salir_texto)
		// .setPositiveButton(R.string.dialog_salir_ok,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		// optionPressed = R.id.salir;
		// setAllViewsOut();
		// /* User clicked OK so do some stuff */
		// }
		// })
		// .setNegativeButton(R.string.dialog_salir_cancelar,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int whichButton) {
		//
		// /* User clicked Cancel so do some stuff */
		// }
		// }).create();
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

			// case DIALOG_OPCIONES:
			// // Lectura de preferencias guardadas
			// app_preferences = PreferenceManager
			// .getDefaultSharedPreferences(this);
			// boolean sonido_boolean = app_preferences.getBoolean(
			// "preference_sonido", false);
			// boolean vibracion_boolean = app_preferences.getBoolean(
			// "preference_vibracion", false);
			// String control =
			// app_preferences.getString("preference_controles",
			// "no valor");
			//
			// // Conseguimos los identificadores de nuestra interfaz guardada
			// en
			// // XML
			// factory = LayoutInflater.from(this);
			// final View textEntryView2 = factory.inflate(
			// R.layout.layout_opciones_pausa, null);
			// sonido = (CheckBox) textEntryView2.findViewById(R.id.check2);
			// vibracion = (CheckBox) textEntryView2.findViewById(R.id.check3);
			//
			//
			// // Establecemos las preferencias cargadas, a nuestra interfaz
			// sonido.setChecked(sonido_boolean);
			// vibracion.setChecked(vibracion_boolean);
			//
			// // Creamos el dialogo
			// return new AlertDialog.Builder(this)
			// .setIcon(R.drawable.icon)
			// .setTitle(R.string.opciones)
			// .setView(textEntryView2)
			// .setPositiveButton(R.string.dialog_salir_ok,
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			// SharedPreferences.Editor editor = app_preferences
			// .edit();
			// editor.putBoolean("preference_sonido",
			// sonido.isChecked());
			// editor.putBoolean("preference_vibracion",
			// vibracion.isChecked());
			//
			// editor.commit();
			//
			// }
			// })
			// .setNegativeButton(R.string.dialog_salir_cancelar,
			// new DialogInterface.OnClickListener() {
			// public void onClick(DialogInterface dialog,
			// int whichButton) {
			//
			// }
			// }).create();

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