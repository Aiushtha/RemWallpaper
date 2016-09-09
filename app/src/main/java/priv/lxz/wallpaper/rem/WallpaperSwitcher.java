package priv.lxz.wallpaper.rem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.greenrobot.event.EventBus;
import share.DataShare;

public class WallpaperSwitcher extends WallpaperService {

    private int mWidth;
    private int mHeight;
    public Handler handler = new Handler();
    GifCanvas gitCanvas;
    private boolean isVisible;
    private boolean isDestop;
    private boolean isPause;

    ReentrantLock lock;
    private Condition condition;
    Thread thread;


    //系统状态栏高度
    private int system_bar_height;
    private GifShowOption option;
    private Wallpaper wallpaper;
    private DataShare dataShare;


    private MediaPlayer player;

    private boolean isLog=false;


    public void onEventMainThread(AppMessageEvent event) {

        option = (GifShowOption) event.getObj();
        wallpaper.effect(option);

    }


    public void showLog(String str)
    {
       if(isLog) {
           Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG);
       }
    }
    @Override
    public Engine onCreateEngine() {
        // TODO Auto-generated method stub
        showLog("onCreate");
        initOption();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        gitCanvas = new GifCanvas(getResources(), R.raw.rem);
        player = MediaPlayer.create(this, R.raw.music_b);
        player.setLooping(false);

        if(condition!=null) {
            isDestop = true;
            //当重新选择壁纸时销毁线程
            {
                lock.lock();
                condition.signalAll();
                lock.unlock();
                //直到线程被销毁
                while (thread != null) {
                }
                isDestop = false;
            }

        }


        lock = new ReentrantLock();
        condition = lock.newCondition();

        system_bar_height = getStatusHeight(getBaseContext());


        return wallpaper = new Wallpaper();

    }

    private void initOption() {
        dataShare = DataShare.getInstance(getBaseContext());
        option = new GifShowOption();
        option = dataShare.getData(GifShowOption.class);
        if (option == null) option = GifShowOption.getDefault();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestop = true;
        lock.lock();
        condition.signalAll();
        lock.unlock();
        EventBus.getDefault().unregister(this);
        player.release();
        lock=null;
        condition=null;
        showLog("Destroy");
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    class Wallpaper extends Engine implements GestureDetector.OnGestureListener {

        private GestureDetector gestureScanner;

        public Wallpaper() {

            thread = new Thread() {
                @Override
                public void run() {
                    while (!isDestop) {
                        try {
                            if (isVisible && gitCanvas.getNumberOfRemainingPlay() > 0) {
                                Canvas canvas = null;
                                try {
                                    canvas = getSurfaceHolder().lockCanvas();
                                    if (canvas != null) {
                                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                        gitCanvas.drawMovieFrame(canvas);
                                        setCanvasARGB(canvas);
                                    }
                                } finally {
                                    if (canvas != null && !isDestop)
                                        getSurfaceHolder().unlockCanvasAndPost(canvas);
                                }
                                ;
                            } else {
                                if (!isDestop) {
                                    show();
                                    lock.lock();
                                    try {
                                        condition.await();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    lock.unlock();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    thread = null;
                }

            };

            thread.start();
        }

        public void checkPlayMusic() {
            if (gitCanvas.getNumberOfRemainingPlay() <= 0) {
                if (option.isPlaySound) {
                    player.start();
                }
            }
        }

        private void tiggerCheck(int mode) {

            switch (mode) {
                case GifShowOption.TRIGGER_MODE_CHECK_SINGLE:
                    if (option.trigger_mode == GifShowOption.TRIGGER_MODE_CHECK_SINGLE) {
                        checkPlayMusic();
                        gitCanvas.resetPlayNumbierOfOne();
                        play();
                    }
                    break;
                case GifShowOption.TRIGGER_MODE_CHECK_LONG:
                    if (option.trigger_mode == GifShowOption.TRIGGER_MODE_CHECK_SINGLE) {
                        checkPlayMusic();
                        gitCanvas.resetPlayNumbierOfOne();
                        play();
                    }
                    break;
                case GifShowOption.TRIGGER_MODE_CHECK_DOUBLE:
                    if (option.trigger_mode == GifShowOption.TRIGGER_MODE_CHECK_DOUBLE) {
                        checkPlayMusic();
                        gitCanvas.resetPlayNumbierOfOne();
                        play();
                    }
                    break;
            }
        }


        /**
         * 计算比例
         */
        private void setCanvasScale(GifCanvas gitCanvas, int mWidth, int mHeight, Movie movie) {
            if (option != null) {
                if (option.isAdaptiveHeight) {
                    gitCanvas.setScale(1.0f * mHeight / gitCanvas.getMovie().height());
                } else if (option.isAdaptiveWidth) {
                    gitCanvas.setScale(1.0f * mWidth / gitCanvas.getMovie().width());
                } else if (option.isAdaptiveCustomRatio) {
                    gitCanvas.setScale(option.scale);
                }
            }
        }

        private void setCanvasARGB(Canvas canvas) {
            if (option != null) {
                canvas.drawARGB((int) (255 - option.brightness * 0.01f * 255), 0, 0, 0);
            }

        }

        private void effect(GifShowOption option) {
            if (getSurfaceHolder() != null) {
                if (mHeight != 0 && mWidth != 0) {
                    if (option.isAdaptiveHeight) {
                        gitCanvas.setScale(1.0f * mHeight / gitCanvas.getMovie().height());
                    } else if (option.isAdaptiveWidth) {
                        gitCanvas.setScale(1.0f * mWidth / gitCanvas.getMovie().width());
                    } else if (option.isAdaptiveCustomRatio) {
                        gitCanvas.setScale(option.scale);
                    }
                }
            }
            float offectTop = option.upperOffset;
            float offectLeft = option.leftOffset;

            if (option.isCalculationBarheight) {
                gitCanvas.setMoveTop(offectTop + system_bar_height);
            } else {
                gitCanvas.setMoveTop(offectTop);
            }
            gitCanvas.setMoveLeft(offectLeft);

            show();

        }


        @Override
        public void onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub
            super.onTouchEvent(event);
            gestureScanner.onTouchEvent(event);
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);

            gestureScanner = new GestureDetector(this);
        }


        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }

        boolean isFristChang = false;

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            if (!isFristChang) {
                mHeight = height;
                mWidth = width;
                setCanvasScale(gitCanvas, mWidth, mHeight, gitCanvas.getMovie());
                ;
                show();
            }
            isFristChang = true;

            super.onSurfaceChanged(holder, format, width, height);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            isVisible = visible;
            if (visible) {
                play();
            } else {
            }
        }

        void show() {
            Canvas canvas = null;
            try {
                canvas = getSurfaceHolder().lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    gitCanvas.drawMovieFrame(canvas, 0);
                    setCanvasARGB(canvas);

                }
            } finally {
                try {
                    if (canvas != null && !isDestop) getSurfaceHolder().unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                }
            }
        }


        public int isDoubleClick = 0;

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            tiggerCheck(GifShowOption.TRIGGER_MODE_CHECK_SINGLE);

            isDoubleClick++;
            if (isDoubleClick >= 2) {
                tiggerCheck(GifShowOption.TRIGGER_MODE_CHECK_DOUBLE);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isDoubleClick = 0;
                }
            }, 250);


            return false;
        }


        public void play() {

            lock.lock();
            condition.signalAll();
            lock.unlock();
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            gitCanvas.resetPlayNumbierOfOne();
            play();
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    }

}