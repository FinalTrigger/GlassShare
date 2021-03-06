package com.w9jds.glassshare;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity 
{

	public static final String CAMERA_IMAGE_BUCKET_NAME = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera";
	public static final String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);
	
	private ArrayList<Card> mlcCards = new ArrayList<Card>();
	private ArrayList<String> mlsPaths = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Card card1 = new Card(this);
//		card1.setText("This card has a footer.");
//		card1.setInfo("I'm the footer!");
//		View card1View = card1.toView();
//		
//		setContentView(card1View);
		
		mlsPaths = getCameraImages(this);
		
		for (int i = 0; i < mlsPaths.size(); i++)
		{
			Card newCard = new Card(this);
			newCard.setFullScreenImages(true);
			newCard.addImage(Uri.fromFile(new File(mlsPaths.get(i))));
			mlcCards.add(newCard);
		}
		
		CardScrollView csvCardsView = new CardScrollView(this);
		csaAdapter cvAdapter = new csaAdapter();
		csvCardsView.setAdapter(cvAdapter);
		csvCardsView.activate();
		setContentView(csvCardsView);
			
	}
	
	public static String getBucketId(String path) 
	{
	    return String.valueOf(path.toLowerCase().hashCode());
	}
	
	public static ArrayList<String> getCameraImages(Context context) 
	{
	    final String[] projection = { MediaStore.Images.Media.DATA };
	    final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
	    final String[] selectionArgs = { CAMERA_IMAGE_BUCKET_ID };
	    final Cursor cursor = context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
	    ArrayList<String> result = new ArrayList<String>(cursor.getCount());
	    
	    if (cursor.moveToFirst()) 
	    {
	        final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        do 
	        {
	            final String data = cursor.getString(dataColumn);
	            result.add(data);
	        } while (cursor.moveToNext());
	    }
	    
	    cursor.close();
	    return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    private class csaAdapter extends CardScrollAdapter 
    {
	    @Override
	    public int findIdPosition(Object id) 
	    {
	        return -1;
	    }

	    @Override
	    public int findItemPosition(Object item) 
	    {
	        return mlcCards.indexOf(item);
	    }

	    @Override
	    public int getCount() 
	    {
	        return mlcCards.size();
	    }

	    @Override
	    public Object getItem(int position) 
	    {
	        return mlcCards.get(position);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	        return mlcCards.get(position).toView();
	    }
}
	
}
