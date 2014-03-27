package com.jmbdh;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.newproyectjmb.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AdvanceMenuFactory extends MenuFactory {
	MenuActivity menuActivity;

	List<ObjectAnimator> animStart;
	List<View> lViews;

	public AdvanceMenuFactory(MenuActivity menuActivity) {
		super();
		this.menuActivity = menuActivity;
	}

	@Override
	public void init() {
		initWithAnim();

	}

	public void initWithAnim() {
		final ViewGroup linearContainer = (ViewGroup) menuActivity
				.findViewById(R.id.linearcontainer);

		ObjectAnimator oa = ObjectAnimator.ofFloat(null, "x", -150f, 0f);
		oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				linearContainer.requestLayout();
			}
		});
		// linearContainer.requestLayout();
		linearContainer.setClipToPadding(false);
		Animator appearingAnimation = oa;
		appearingAnimation.setDuration(3000);
		appearingAnimation.setStartDelay(0);
		appearingAnimation.setInterpolator(new LinearInterpolator());

		appearingAnimation.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				//view.setTranslationX(0f);

				ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x",
						view.getX(), 100).setDuration(1500);
				animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					public void onAnimationUpdate(ValueAnimator animation) {
						linearContainer.requestLayout();
					}
				});
				animator.setRepeatCount(ObjectAnimator.INFINITE);
				animator.setRepeatMode(ObjectAnimator.REVERSE);
				animator.setInterpolator(new AccelerateDecelerateInterpolator());
				animator.start();
				if (animStart == null) {
					animStart = new ArrayList<ObjectAnimator>();
				}
				animStart.add(animator);

//				// anim.setTarget(view);
//				linearContainer.setClipChildren(false);
//				view.invalidate();
//				view.postInvalidate();
//				linearContainer.invalidate();
//				linearContainer.postInvalidate();
//				view.bringToFront();
//				linearContainer.getParent().requestTransparentRegion(
//						menuActivity.g1);
			}
		});

		LayoutTransition transitioner = new LayoutTransition();
		transitioner
				.setAnimator(LayoutTransition.APPEARING, appearingAnimation);
		transitioner.setDuration(LayoutTransition.APPEARING, 700);

		linearContainer.setLayoutTransition(transitioner);

		lViews = new ArrayList<View>();
		int size = linearContainer.getChildCount();
		for (int i = 0; i < size; i++) {
			// View view = hidecontainer.getChildAt(i);
			lViews.add(linearContainer.getChildAt(i));
		}
		for (View view : lViews) {
			if (view.getId() == R.id.jugar || view.getId() == R.id.jugarx2) {
				view.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (lastButtonIsOnViewParent()) {
							// Removing
							menuActivity.optionPressed = v.getId();
							ObjectAnimator animOut = ObjectAnimator.ofFloat(v,
									"rotationX", 0f, 90f).setDuration(1000);

							animOut.setRepeatCount(0);
							animOut.setTarget(v);
							animOut.start();

							animOut.addListener(new AnimatorListenerAdapter() {
								public void onAnimationEnd(Animator anim) {
									setAllViewsOut();
								}
							});
						}
					}
				});
			}
		}
		linearContainer.setClipChildren(false);

	}

	public void buttonsAppearWithAnim(int indexButton) {

		final ViewGroup linearContainer = (ViewGroup) menuActivity
				.findViewById(R.id.linearcontainer);
		linearContainer.setVisibility(View.VISIBLE);
		linearContainer.requestTransparentRegion(menuActivity.g1);
//
//		int count = linearContainer.getChildCount();
//		 for (int i = 0; i < count; i++) {
//		 View view = linearContainer.getChildAt(i);
//		 view.setVisibility(View.VISIBLE);
//		
//		 }
		 //View view = linearContainer.getChildAt(indexButton);
		if (indexButton<lViews.size())
		lViews.get(indexButton).setVisibility(View.VISIBLE);
//		final Thread mThread = new Thread() {
//			@Override
//			public void run() {
//				
//				while (true) {
//
//					// Update our shared state with the UI.
//					synchronized (this) {
//						// Our thread is stopped if the UI is not ready
//						// or it has completed its work.
//						int count = linearContainer.getChildCount();
//						for (int i = 0; i < count; i++) {
//							View view = linearContainer.getChildAt(i);
//							view.setVisibility(View.VISIBLE);
//							long lasttime = System.currentTimeMillis();
//							while (lasttime + 1000 <= System
//									.currentTimeMillis()) {
//
//								try {
//									sleep(100);
//								} catch (InterruptedException e) {
//								}
//							}
//						}
//						return;
//						
//					}
//				}
//			}
//		};
//		mThread.start();

		// linearContainer.invalidate();
		// linearContainer.postInvalidateOnAnimation();
		// linearContainer.postInvalidate();
		// transitioner.setAnimateParentHierarchy(true);

	}

	@Override
	public void startExit() {
		menuActivity.optionPressed = R.id.salir;
		setAllViewsOut();

	}

	public void setAllViewsOut() {
		for (ObjectAnimator aS : animStart) {
			aS.cancel();
		}
		for (View view : lViews) {
			int w = view.getWidth();
			int index = lViews.indexOf(view);

			ObjectAnimator oA = ObjectAnimator.ofFloat(view, "x", view.getX(),
					-w).setDuration((index + 1) * 500);
			oA.setRepeatCount(0);
			oA.setRepeatCount(0);
			oA.setTarget(view);
			oA.setRepeatMode(ObjectAnimator.REVERSE);
			oA.setInterpolator(new AccelerateDecelerateInterpolator());
			oA.start();

			oA.addListener(new AnimatorListenerAdapter() {
				public void onAnimationEnd(Animator anim) {
					View view = (View) ((ObjectAnimator) anim).getTarget();
					int index = lViews.indexOf(view);
					if (index == lViews.size() - 1) {

						menuActivity.nextOption();
					}

				}
			});
		}

	}

	public boolean lastButtonIsOnViewParent() {

		return (animStart.size() == lViews.size());

	}

	public void buttonAppear(int indexButton) {
		buttonsAppearWithAnim( indexButton);
	}
	public void buttonAppear() {
		//buttonsAppearWithAnim();
	}

	// public void buttonsAppear() {
	// animStart = new ArrayList<ObjectAnimator>();
	// menuActivity.setVisible(View.VISIBLE);
	// for (View button : lViews) {
	// int w = button.getWidth();
	// int index = lViews.indexOf(button);
	// // button.setVisibility(View.VISIBLE);
	// // button.postInvalidate();
	// // button.invalidate();
	// ObjectAnimator oA = ObjectAnimator.ofFloat(button, "x",
	// (index + 1) * w * -1, w / 5).setDuration((index + 1) * 500);
	// oA.setRepeatCount(0);
	// oA.setTarget(button);
	// oA.setRepeatMode(ObjectAnimator.REVERSE);
	// oA.setInterpolator(new AccelerateDecelerateInterpolator());
	// oA.start();
	// // oA.addUpdateListener(this);
	// oA.addListener(new AnimatorListenerAdapter() {
	//
	// public void onAnimationEnd(Animator animation) {
	// menuActivity.showinfo();
	// View view = (View) ((ObjectAnimator) animation).getTarget();
	// // int index = lViews.indexOf(view);
	// ObjectAnimator anim = ObjectAnimator.ofFloat(view, "x",
	// view.getX(), 0).setDuration(2000);
	// anim.setRepeatCount(ObjectAnimator.INFINITE);
	// anim.setRepeatMode(ObjectAnimator.REVERSE);
	// anim.setInterpolator(new AccelerateDecelerateInterpolator());
	// anim.start();
	// animStart.add(anim);
	//
	// anim.setTarget(view);
	//
	// }
	//
	// });
	//
	// }
	// menuActivity.setVisible(View.VISIBLE);
	//
	// }

	private void setupCustomAnimations() {
		// ViewGroup hidecontainer = (LinearLayout) menuActivity
		// .findViewById(R.id.hidecontainer);
		// if (android.os.Build.VERSION.SDK_INT >=
		// android.os.Build.VERSION_CODES.HONEYCOMB) {
		// mTransitioner = new LayoutTransition();
		// hidecontainer.setLayoutTransition(mTransitioner);
		// }
		// lViews = new ArrayList<View>();
		// int size = hidecontainer.getChildCount();
		// for (int i = 0; i < size; i++) {
		// // View view = hidecontainer.getChildAt(i);
		// lViews.add(hidecontainer.getChildAt(i));
		// }
		//
		// for (View view : lViews) {
		// if (view.getId() == R.id.jugar || view.getId() == R.id.jugarx2) {
		// view.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// if (lastButtonIsOnViewParent()) {
		// // Removing
		// menuActivity.optionPressed = v.getId();
		// ObjectAnimator animOut = ObjectAnimator.ofFloat(v,
		// "rotationX", 0f, 90f).setDuration(1000);
		//
		// animOut.setRepeatCount(0);
		// animOut.setTarget(v);
		// animOut.start();
		//
		// animOut.addListener(new AnimatorListenerAdapter() {
		// public void onAnimationEnd(Animator anim) {
		// setAllViewsOut();
		// }
		// });
		// }
		// }
		// });
		// }
		//
		// }

	}

	@Override
	public void buttonsAppear() {
		// TODO Auto-generated method stub
		
	}

}
