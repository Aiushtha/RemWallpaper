# RemWallpaper


.externalNativeBuild
://github.com/Aiushtha/RemWallpaper/tree/master

在别的地方看到类似的效果 自己仿做了一个 。
研究大致实现的原理 ，感觉可以做很多有趣的东西（大雾）
用最近大热的动漫人物作主题 
gif播放参考gitView核心代码--
[java] view plain copy print?在CODE上查看代码片派生到我的代码片
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
xternalNativeBuild
# Built application files
# Built application files
