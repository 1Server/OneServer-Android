package helpers;

import android.util.Log;

public class FFmpegLibrary {
	
	private static String LIBRARY_NAME = "videokit";
	
	private static native void run(String[] args);
	
	public static boolean loadLibrary() {
		try{
			Log.i("FFmpeg Library", "Attempting to load library: " + LIBRARY_NAME);
			System.loadLibrary(LIBRARY_NAME);
		}catch (Exception e){
			Log.i("FFmpeg Library", "Exception loading ffmpeg library: " + e.toString());
			return false;
		}
		
		return true;
	}

}
