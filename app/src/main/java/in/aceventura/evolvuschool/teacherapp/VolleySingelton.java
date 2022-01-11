package in.aceventura.evolvuschool.teacherapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingelton {
	private static VolleySingelton volleySingelton;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	private VolleySingelton(Context context) {
		requestQueue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(requestQueue, new BitmapLruCache());
	}
	
	public static VolleySingelton getInstance(Context context){
		if ( volleySingelton == null )
			volleySingelton = new VolleySingelton(context);
		return volleySingelton;
	}
	
	public RequestQueue getRequestQueue(){
		return requestQueue;
	}
	
	public ImageLoader getImageLoader(){
		return imageLoader;
	}
		
}
