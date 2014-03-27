package com.jmbdh;

import java.util.ArrayList;
import java.util.List;

import com.newproyectjmb.R;


import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BaseMenuFactory extends MenuFactory{
	MenuActivity menuActivity;
	
	
	public BaseMenuFactory(MenuActivity menuActivity) {
		super();
		this.menuActivity = menuActivity;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startExit() {
		menuActivity.nextOption();
		
	}
	public void setVisible(int visibility) {
////		ViewGroup hidecontainer = (LinearLayout) menuActivity.findViewById(R.id.hidecontainer);
//		
//		List<View> lViews = new ArrayList<View>();
//		int size = hidecontainer.getChildCount();
//		for (int i = 0; i < size; i++) {
//			//View view = hidecontainer.getChildAt(i);
//			lViews.add(hidecontainer.getChildAt(i));
//		}
//		for (View view : lViews) {
//			view.setVisibility(visibility);
//			view.getParent().requestTransparentRegion(menuActivity.g1);
//		}
		
		
	}
	@Override
	public void buttonsAppear() {
		setVisible(View.VISIBLE);
//		for (View view : lViews) {
//			view.setVisibility(View)
//		}
	}

	@Override
	public void buttonAppear(int indexButton) {
		// TODO Auto-generated method stub
		
	}

}
