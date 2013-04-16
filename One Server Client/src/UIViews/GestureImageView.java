package UIViews;

import java.net.URL;

import edu.msoe.oneserver.client.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * This class will draw a set image from a url.
 * @author kuszewskij
 *
 */
public class GestureImageView extends View{
	//The image that will be viewed.
	private Bitmap image;

	//The matrix that will handle scaling and translating the image.
	private Matrix matrix = new Matrix(); 

	//The progressbar that is used to display the progress of loading the image from a url
	private ProgressDialog progDialog;

	/**
	 * Default constructor for this class.
	 * @param context
	 */
	public GestureImageView(Context context) {
		super(context);
		this.setOnTouchListener(new GestureHandler());
	}

	public GestureImageView(Context context, AttributeSet attrs){
		super(context, attrs);
		this.setOnTouchListener(new GestureHandler());
	}

	public GestureImageView(Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
		this.setOnTouchListener(new GestureHandler());
	}

	/**
	 * This will scale the image to fit the screen and center it.
	 */
	private void initImage(){
		//see what the largest dimension is

		matrix = new Matrix();
		Point p = new Point();
		((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(p);

		float widthRatio = p.x/(float)(image.getWidth()*1.0f);
		float heightRatio = p.y/(float)(image.getHeight()*1.0f);
		float sizeRatio = 1f;
		if(widthRatio < 1 | heightRatio < 1){//if the image is too large in one dimension then scale it appropriately.
			if(widthRatio < heightRatio){
				sizeRatio = widthRatio;
			}else{
				sizeRatio = heightRatio;
			}
			matrix.postScale(sizeRatio, sizeRatio);
		}

		//translate image to center
		float xdiff = (p.x - (image.getWidth() * sizeRatio));
		float ydiff = (p.y - (image.getHeight()* sizeRatio));
		if(ydiff < 0) ydiff = 0;
		if(xdiff < 0) xdiff = 0;
		matrix.postTranslate(xdiff/2f, ydiff/2f);
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas){
		if(image != null){
			canvas.drawBitmap(image, matrix, null);//draw the image 
		}
	}

	/**
	 * This method will set the image to draw from a url.
	 * @param url
	 */
	public void loadImage(URL url){
		LoadURLToImage luti = new LoadURLToImage();
		progDialog = new ProgressDialog(this.getContext());
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setMessage("Loading");
		progDialog.show();
		luti.execute(url,this);
	}

	/**
	 * Sets an image and draws it
	 * @param image
	 */
	private void setImage(Bitmap image){
		progDialog.dismiss();
		this.image = image;
		if(image == null){
			this.image = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
			Toast.makeText(getContext(), "Could not load image from URL", Toast.LENGTH_LONG).show();
		}
		initImage();
	}

	/**
	 * Helper class that will load an image from a url and set it as the image that is being viewed.
	 * @author kuszewskij
	 *
	 */
	private class LoadURLToImage extends AsyncTask<Object, Void, Bitmap> {

		//the GestureImageView that started the task
		GestureImageView glv;

		@Override
		protected Bitmap doInBackground(Object... url) {

			Bitmap bmp = null;
			glv = (GestureImageView)url[1];

			try {
				bmp = BitmapFactory.decodeStream(((URL)url[0]).openConnection().getInputStream());
				image = bmp;
			} catch (Exception e) {
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap b){
			glv.setImage(b);
		}
	}


	/**
	 * Helper class used to respond to gestures. It will scale the image on a pinch event and translate the image on a drag event.
	 * @author kuszewskij
	 *
	 */
	private class GestureHandler implements OnTouchListener{
		// The 3 states (events) which the user is trying to perform
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		int mode = NONE; 

		// These matrices will be used to scale points of the image
		Matrix savedMatrix = new Matrix();

		// these PointF objects are used to record the point(s) the user is touching
		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;
		long lastClickTime = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			float scale;
			
			switch (event.getAction() & MotionEvent.ACTION_MASK) 
			{
			case MotionEvent.ACTION_DOWN:   // first finger down only
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = DRAG;
				
				if(System.currentTimeMillis() - lastClickTime < 500){
					savedMatrix = new Matrix();
					mode = NONE;
					initImage();
				}
				lastClickTime = System.currentTimeMillis();
				break;

			case MotionEvent.ACTION_UP: // first finger lifted

			case MotionEvent.ACTION_POINTER_UP: // second finger lifted
				mode = NONE;
				break;

			case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
				oldDist = spacing(event);
				if (oldDist > 5f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;

			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) 
				{ 
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
					invalidate();
				} 
				else if (mode == ZOOM) 
				{ 
					// pinch zooming
					float newDist = spacing(event);
					if (newDist > 5f) 
					{
						matrix.set(savedMatrix);
						scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}

					invalidate();
				}
				break;
			}
			return true; // indicate event was handled
		}

		/**
		 * Returns the distance between two touch events
		 * @param event
		 * @return
		 */
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}


		/**
		 * Returns the midpoint of a two touch event.
		 * @param point
		 * @param event
		 */
		private void midPoint(PointF point, MotionEvent event){
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	}
}
