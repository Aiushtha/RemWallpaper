package priv.lxz.wallpaper.rem;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Movie;

/**
 * Created by Lin on 16/8/23.
 */

public class GifCanvas {
    private final int mMovieResourceId;
    private final Resources res;
    private Movie movie;

    private long mMovieStart;
    private int mCurrentAnimationTime;

    private static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;


    //尝试播放次
    private long numberOfPlayCount = 0;
    private long numberOfRemainingPlay = 0;





    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float mLeft = 0;
    private float mTop = 0;

    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale = 3.0f;

    public GifCanvas(Resources res, int mMovieResourceId) {
        this.mMovieResourceId = mMovieResourceId;
        this.res = res;
        movie = Movie.decodeStream(res.openRawResource(mMovieResourceId));


    }

    /**
     * Calculate current animation time
     */
    public void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = movie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIE_VIEW_DURATION;
        }
        numberOfRemainingPlay = numberOfPlayCount - (now - mMovieStart) / dur;
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }


    /**
     * Draw current GIF frame
     */
    public void drawMovieFrame(Canvas canvas) {

        if (numberOfRemainingPlay > 0) {
            updateAnimationTime();
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
            movie.setTime(mCurrentAnimationTime);
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.scale(mScale, mScale);
            movie.draw(canvas, mLeft / mScale, mTop / mScale);
            canvas.restore();
        } else {
            movie.setTime(movie.duration());
        }
    }


    public GifCanvas setNumberOfRemainingPlay(long numberOfRemainingPlay) {
        this.numberOfRemainingPlay = numberOfRemainingPlay;
        return this;
    }

    /**
     * Draw current GIF frame
     */
    public void drawMovieFrame(Canvas canvas, int dur) {
        movie.setTime(0);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        movie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();


    }

    public void resetPlayNumbierOfOne() {
        // TODO Auto-generated method stub
        if (numberOfRemainingPlay <= 0) {
            mMovieStart = 0;
            numberOfPlayCount = 1;
            numberOfRemainingPlay = 1;
        }
    }

    public long getNumberOfRemainingPlay() {
        return numberOfRemainingPlay;
    }


    public float getMoveLeft() {
        return mLeft;
    }

    public GifCanvas setMoveLeft(float mLeft) {
        this.mLeft = mLeft;
        return this;
    }

    public float getMoveTop() {
        return mTop;
    }

    public GifCanvas setMoveTop(float mTop) {
        this.mTop = mTop;
        return this;
    }

    public Movie getMovie() {
        return movie;
    }

    public GifCanvas setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    public float getScale() {
        return mScale;
    }

    public GifCanvas setScale(float mScale) {
        this.mScale = mScale;
        return this;
    }
}
