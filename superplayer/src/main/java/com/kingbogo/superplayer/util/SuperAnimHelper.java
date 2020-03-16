package com.kingbogo.superplayer.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/9/12
 */
public final class SuperAnimHelper {

    private SuperAnimHelper() {
    }

    // ------------------------------------------------------------------------ @ Top

    public static void showTop(final View view,
                               final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    public static void hideTop(final View view,
                               final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    // ------------------------------------------------------------------------ @ Bottom

    public static void showBottom(final View view,
                                  final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    public static void hideBottom(final View view,
                                  final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    // ------------------------------------------------------------------------ @ Left

    public static void showLeft(final View view,
                                final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    public static void hideLeft(final View view,
                                final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    // ------------------------------------------------------------------------ @ Right

    public static void showRight(final View view,
                                 final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    public static void hideRight(final View view,
                                 final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 1f,
                    Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(300);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setFillAfter(false);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(translateAnimation);
        }
    }

    // ------------------------------------------------------------------------ @ center

    public static void showCenter(final View view,
                                  final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    0f, 1f,
                    0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setFillAfter(false);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(scaleAnimation);
        }
    }

    public static void hideCenter(final View view,
                                  final SuperAnimActionListener animActionListener) {
        if (view.getAnimation() == null) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    1f, 0f,
                    1f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(300);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setFillAfter(false);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animActionListener != null) {
                        animActionListener.onSuperAnimEnd();
                    }
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }
            });
            view.startAnimation(scaleAnimation);
        }
    }

    // ------------------------------------------------------------------------ @ Listener

    public interface SuperAnimActionListener {
        /**
         * 动画结束
         */
        void onSuperAnimEnd();
    }

}
