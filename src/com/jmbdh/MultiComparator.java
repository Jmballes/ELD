package com.jmbdh;

import java.util.Comparator;

import com.draw.MultiDrawable;

public class MultiComparator  implements Comparator<MultiDrawable>{

	@Override
	public int compare(MultiDrawable arg0, MultiDrawable arg1) {
		// TODO Auto-generated method stub
		return arg0.getZ()-arg1.getZ();
	}

}
