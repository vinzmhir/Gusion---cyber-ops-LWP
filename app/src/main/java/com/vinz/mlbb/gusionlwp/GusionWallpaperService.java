package com.vinz.mlbb.gusionlwp;

import android.graphics.Movie;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import java.io.IOException;
import android.view.SurfaceHolder;
import android.os.Handler;
import android.graphics.Canvas;

public class GusionWallpaperService extends WallpaperService {

	public WallpaperService.Engine onCreateEngine() {
		try{
			Movie movie = Movie.decodeStream(
			getResources().getAssets().open("gusion.gif"));
			
			return new GIFWallpaperEngine(movie);
		}catch(IOException e){
			Log.d("GIF", "Could not load asset");
			return null;
		}
	}
	
	private class GIFWallpaperEngine extends WallpaperService.Engine {

        private final int frameDuration = 20;

        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;

        public GIFWallpaperEngine(Movie movie) {
            this.movie = movie;
            handler = new Handler();
        }
		
		public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder = surfaceHolder;
        }
		
		private Runnable drawGIF = new Runnable() {
            public void run() {
                draw();
            }
        };
		
		private void draw() {
			if (visible) {
				Canvas canvas = holder.lockCanvas();
                canvas.save();
				// Adjust size and position so that
				// the image looks good on your screen
				canvas.scale(1.9f, 1.9f);
				movie.draw(canvas, 0, 0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drawGIF);
                handler.postDelayed(drawGIF, frameDuration);
            }
        }
		
		public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawGIF);
            } else {
                handler.removeCallbacks(drawGIF);
            }
        }
		
		public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawGIF);
        }
	}
}
