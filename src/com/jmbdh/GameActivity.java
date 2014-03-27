package com.jmbdh;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CheckBox;

import com.newproyectjmb.R;
import com.paintview.IProcess;
import com.paintview.JuegoView;
import com.paintview.JuegoView.JuegoThread;



public class GameActivity extends FragmentActivity implements IActivityHandler{


    /** Manejador de nuestro Thread. */
    private JuegoThread mJuegoThread;

    /** El objeto View en el que el juego se ejecuta*/
    public JuegoView mJuegoView;


    private int modeJuego=-1;
    
    SensorManager mSensorManager;
    private AnalyticsHelper helper;

    /**
     * Invocado cuando la Activity es creada
     * 
     * @param savedInstanceState un Bundle contiene un estado guardado de una 
     *        ejecución previa, o null si es una ejecución nueva
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper=new AnalyticsHelper(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Obtenemos la información extra de nuestro Intent
        Bundle extras = getIntent().getExtras();
        //Nos centramos en el campo modo de juego
       	modeJuego = extras.getInt("modojuego");
       	//Establecemos nuestro layout
        setContentView(R.layout.layout_juego);
        //Obtenemos el identificador de nuestro elemento principal (y unico) de nuestro layout
        mJuegoView = (JuegoView) findViewById(R.id.juego);
        mJuegoView.init(this,"ProcessGame");
        
        mJuegoView.getThread().setActivity(this);
        //Establecemos el modo de juego
        mJuegoView.setTotalDePatosPorMiniFase(modeJuego);
        //Obtenemos el hilo de nuestro juego
        mJuegoThread = mJuegoView.getThread();
        mJuegoThread.setActivity(this);
        System.out.println("Activty create");
        //Solicitamos un manejador del sensor de nuestro dispositivo.
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        

    }

    public final Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                
                // It is time to bump the value!
                case 0: {
                	showDialog(3);
                } break;
                
                case IProcess.MESSAGE_SENT_CLOSING_GAME:{
                	//mJuegoView.process.setState(IProcess.PRESENTATION_STATE_COURTAIN_CLOSING_INIT);
                	
                	mJuegoView.destroy();
                	mJuegoView=null;
                	
                	Intent intent = new Intent();
                	intent.putExtra("modojuego", 1);
                	intent.setClass(GameActivity.this, MenuActivity.class);
                	startActivity(intent);
                	finish();
                }break;
                default:
                	
                    super.handleMessage(msg);
            }
            System.out.println("Enviando mensajes");
        }
    };
    @Override
    protected void onStop() {

    	super.onStop();
    	//Dejamos de obtener eventos de nuestro sensor
    	mSensorManager.unregisterListener(mJuegoView);
    	//Paramos cualquier sonido en caso de que se este reproducciendo
    	if (mJuegoView!=null && mJuegoView.process!=null){
    	mJuegoView.process.onStop();
    	}
    }
    protected void onDestroy(){
    	
    	super.onDestroy();
    	mSensorManager.unregisterListener(mJuegoView);
    }
    @Override
    protected void onResume(){
    	super.onResume();
    	//Solicitamos obtener eventos de nuestro sensor
        mSensorManager.registerListener(mJuegoView,
                SensorManager.SENSOR_ORIENTATION,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onPause(){
    	super.onPause();
    	//Dejamos de obtener eventos de nuestro sensor
        mSensorManager.unregisterListener(mJuegoView);
    }
    private static final int MENU_CONTINUAR = 1;
    private static final int MENU_OPCIONES = 2;
    private static final int MENU_SALIR = 3;

    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        menu.add(0, MENU_CONTINUAR, 0, R.string.menu_continuar);
//        menu.add(0, MENU_OPCIONES, 0, R.string.menu_opciones);
//        menu.add(0, MENU_SALIR, 0, R.string.menu_salir);
//    	mMenu = menu;
        
        // Inflate the currently selected menu XML resource.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ingame, menu);
       
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
    	mJuegoView.process.setPause();
    	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuIngameContinue:
            	mJuegoView.process.resumePause();
                return true;
            case R.id.menuIngameBack:
            	mJuegoView.process.resumePause();
            	
            	mJuegoView.process.setState(IProcess.JUEGO_ESTADO_COURTAIN_CLOSING_INIT);
                return true;
            case R.id.menuIngameOptions:
            	DialogFragment helpFragment=OptionsDialogFragment.newInstance();
				helpFragment.show( getSupportFragmentManager(), "dialog");
                return true;

        }
        return false;
    }
    
    
	private CheckBox sonido;
	private CheckBox vibracion;
	//private Spinner modo;
	SharedPreferences app_preferences;
	private final static byte SOUND_ON=0;
	private final static byte SOUND_OFF=1;
	private final static byte VIBRATE_ON=2;
	private final static byte VIBRATE_OFF=3;
	private static final byte [] value_debug_mode={SOUND_ON,SOUND_OFF,SOUND_ON,SOUND_OFF,
	};

	private static byte index_debug_mode_user=0;

    protected Dialog onCreateDialog(int id) {
        switch (id) {

        case 3:
        	return new AlertDialog.Builder(GameActivity.this)
            .setIcon(R.drawable.icon)
            .setTitle(R.string.dialog_sms_titulo)
            .setMessage(getResources().getString(R.string.gameover_question))
            .setPositiveButton(R.string.dialog_salir_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//		            Intent intent = new Intent();
//		            intent.putExtra("puntuacion", mJuegoThread.getScore());
//		            intent.setClass(JuegoActivity.this, EnviarSMS.class);
//		            startActivity(intent);
//		            finish();
		            Intent sendIntent = new Intent();
		            sendIntent.setAction(Intent.ACTION_SEND);
		            sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.Message_Shared) +" " +  mJuegoView.process.getInfo("SCORE") + " "+getResources().getString(R.string.Message_Shared2));
		            sendIntent.setType("text/plain");
		            startActivity(sendIntent);
		            finish();
                }
            })
            .setNegativeButton(R.string.dialog_salir_cancelar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	finish();
                }
            })
            .create();

//        case 1:
//        	//Lectura de preferencias guardadas
//        	app_preferences = PreferenceManager.getDefaultSharedPreferences(this); 
//            boolean sonido_boolean = app_preferences.getBoolean("preference_sonido", false);
//            boolean vibracion_boolean = app_preferences.getBoolean("preference_vibracion", false);
//            //String control = app_preferences.getString("preference_controles", "no valor");
//            
//            //Conseguimos los identificadores de nuestra interfaz guardada en XML
//            LayoutInflater factory = LayoutInflater.from(this);
//            final View textEntryView = factory.inflate(R.layout.layout_opciones_pausa, null);
//            sonido=(CheckBox)textEntryView.findViewById(R.id.check2);
//            vibracion=(CheckBox)textEntryView.findViewById(R.id.check3);
////            modo=(Spinner)textEntryView.findViewById(R.id.controles);
//            
//            //Establecemos las preferencias cargadas, a nuestra interfaz
//            this.sonido.setChecked(sonido_boolean);
//            this.vibracion.setChecked(vibracion_boolean);
//            //this.modo.setSelection(control.equals("tm")?0:1);
//            
//            this.sonido.setOnCheckedChangeListener(new OnCheckedChangeListener()
//            {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView,
//						boolean isChecked) {
//					byte value=isChecked?SOUND_ON:SOUND_OFF;
//					checkDebugMode(value);
//				}
//            });
//            this.vibracion.setOnCheckedChangeListener(new OnCheckedChangeListener()
//            {
//
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView,
//						boolean isChecked) {
//					byte value=isChecked?VIBRATE_ON:VIBRATE_OFF;
//					checkDebugMode(value);
//				}
//            });
//            //Creamos el dialogo
//            return new AlertDialog.Builder(this)
//            .setIcon(R.drawable.icon)
//            .setTitle(R.string.opciones)
//            .setView(textEntryView)
//            .setPositiveButton(R.string.dialog_salir_ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    	SharedPreferences.Editor editor = app_preferences.edit();
//                        editor.putBoolean("preference_sonido", sonido.isChecked());
//                        editor.putBoolean("preference_vibracion", vibracion.isChecked());
//                        //editor.putString("preference_controles", modo.getSelectedItemId()==0?"tm":"mm");
//                        editor.commit();
//                        mJuegoView.readPreferences();
//
//                    }
//             })
//            .setNegativeButton(R.string.dialog_salir_cancelar, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                    }
//            })
//            .create();
        }
        return null;
    }
    public void checkDebugMode(byte value){
    	if (value_debug_mode[index_debug_mode_user]==value){
			index_debug_mode_user++;
		}else{
			index_debug_mode_user=0;
		}
		if (index_debug_mode_user>=value_debug_mode.length){
			mJuegoView.debug_mode=!mJuegoView.debug_mode;
			index_debug_mode_user=0;
		}
		
    }
	@Override
	public Handler getHandler() {
		return mHandler;
	}
	@Override
	public void showMenu() {
		openOptionsMenu();
		
	}




}