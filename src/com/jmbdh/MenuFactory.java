package com.jmbdh;

public abstract class MenuFactory {
 
 public abstract void init();
 public abstract void buttonsAppear();
 public abstract void buttonAppear(int indexButton);
 public abstract void startExit();
 public static MenuFactory newInstance(MenuActivity mA) {
	 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
         return new AdvanceMenuFactory(mA);
     }else{
         return new BaseMenuFactory(mA);
     }
	  }
}
