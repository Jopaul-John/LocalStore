package com.example.sinwan.shopkeeper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class photo extends AppCompatActivity implements OnClickListener,AdapterView.OnItemSelectedListener {
	private Button mTakePhoto,submit;
	private ImageView mImageView;
	private static final String TAG = "upload";
	EditText price,pname,spec,colour;
	String name,itemtype,filename,sid,phone,spname,sspec,scolour,sprice;
	int num;
	SharedPreferences preferences ;
	String PREFS_NAME ="com.example.sinwan.mini1",type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		if (preferences.getString("logged", "").toString().equals("logged")) {

			// type = intent.getStringExtra("TYPE");
			//  Uname = intent.getStringExtra("NAME");
			name=preferences.getString("username", "").toString();
			type=preferences.getString("type", "").toString();
			sid=preferences.getString("sid", "").toString();
			Toast.makeText(photo.this,name+phone+sid, Toast.LENGTH_SHORT).show();

		}

		submit = (Button)findViewById(R.id.button_submit);
		submit.setOnClickListener(this);
		pname = (EditText)findViewById(R.id.eName);
		spec = (EditText)findViewById(R.id.eSpec);
		colour = (EditText)findViewById(R.id.eColour);
		price = (EditText)findViewById(R.id.ePrice);
		mTakePhoto = (Button) findViewById(R.id.take_photo);
		mImageView = (ImageView) findViewById(R.id.imageview);
filename = System.currentTimeMillis()+".jpg";
		mTakePhoto.setOnClickListener(this);


		final Spinner spinner2 = (Spinner) findViewById(R.id.spinnerType);

		// Spinner click listener
		spinner2.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		List<String> categories2 = new ArrayList<String>();

		categories2.add("Electronics");
		categories2.add("Furniture");
		categories2.add("Stationary");
		categories2.add("Others");


		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);

		// Drop down layout style - list view with radio button
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinner2.setAdapter(dataAdapter2);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spinnerType) {
			switch (position) {
				case 0:
					itemtype = "Electronics";
					break;
				case 1:
					itemtype = "Furniture";
					break;
				case 2:
					itemtype = "Stationary";
					break;
				case 3:
					itemtype = "Others";
					break;
			}
		}
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }




	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		spname=pname.getText().toString();
		sspec=spec.getText().toString();
		scolour=colour.getText().toString();
		sprice=price.getText().toString();

		switch (id) {
		case R.id.take_photo:

			takePhoto();
			break;


		case R.id.button_submit:
		if((!spname.equals(""))||(!sspec.equals(""))||(!scolour.equals(""))||(!sprice.equals("")))
		{
			//Toast.makeText(photo.this,"Wood="+wood+"type="+itemtype, Toast.LENGTH_LONG).show();
			BackGround b = new BackGround();
			b.execute(spname,sspec,scolour,sprice,itemtype,name,filename,sid,phone);}
		else
			Toast.makeText(photo.this, "Please fill the fields", Toast.LENGTH_LONG).show();
		break;}

	}

	private void takePhoto() {
//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//		startActivityForResult(intent, 0);
		dispatchTakePictureIntent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onActivityResult: " + this);
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
			setPic();
//			Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//			if (bitmap != null) {
//				mImageView.setImageBitmap(bitmap);
//				try {
//					sendPhoto(bitmap);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
		}
	}
	
	private void sendPhoto(Bitmap bitmap) throws Exception {
		new UploadTask().execute(bitmap);
	}

	private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
		
		protected Void doInBackground(Bitmap... bitmaps) {
			if (bitmaps[0] == null)
				return null;
			setProgress(0);

			Bitmap bitmap = bitmaps[0];
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
			InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

			DefaultHttpClient httpclient = new DefaultHttpClient();
			try {
				HttpPost httppost = new HttpPost(
						"http://shopee.esy.es/savetofile.php"); // server

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("myFile",
						filename + ".jpg", in);
				httppost.setEntity(reqEntity);

				Log.i(TAG, "request " + httppost.getRequestLine());
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (response != null)
						Log.i(TAG, "response " + response.getStatusLine().toString());
				} finally {

				}
			} finally {

			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(photo.this, "uploaded successfully", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume: " + this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState");
	}
	
	String mCurrentPhotoPath;
	
	static final int REQUEST_TAKE_PHOTO = 1;
	File photoFile = null;

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File

	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	            		Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}

	/**
	 * http://developer.android.com/training/camera/photobasics.html
	 */
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
	    File dir = new File(storageDir);
	    if (!dir.exists())
	    	dir.mkdir();
	    
	    File image = new File(storageDir + "/" + imageFileName + ".jpg");

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    Log.i(TAG, "photo path = " + mCurrentPhotoPath);
	    return image;
	}
	
	private void setPic() {
		// Get the dimensions of the View
	    int targetW = mImageView.getWidth();
	    int targetH = mImageView.getHeight();

	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor << 1;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
	    
	    Matrix mtx = new Matrix();
	    mtx.postRotate(90);
	    // Rotating Bitmap
	    Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

	    if (rotatedBMP != bitmap)
	    	bitmap.recycle();
	    
	    mImageView.setImageBitmap(rotatedBMP);
	    
	    try {
			sendPhoto(rotatedBMP);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class BackGround extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
            String pname = params[0];
			String spec = params[1];
			String colour = params[2];
			String price = params[3];
			String type = params[4];
			String name = params[5];
			String fname = params[6];
			String sid= params[7];



			String data="done";
			int tmp;

			try {

				URL url = new URL("http://shopee.esy.es/BRAND/submit.php");
				String urlParams = "&pname="+pname+"&spec="+spec+"&colour="+colour+"&price="+price+"&type="+type+"&name="+name+"&filename="+fname+"&sid="+sid;

				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setDoOutput(true);
				OutputStream os = httpURLConnection.getOutputStream();
				os.write(urlParams.getBytes());
				os.flush();
				os.close();
				InputStream is = httpURLConnection.getInputStream();
				while((tmp=is.read())!=-1){
					data+= (char)tmp;
				}
				is.close();
				httpURLConnection.disconnect();

				return data;

			} catch (MalformedURLException e) {
				e.printStackTrace();
				return "Exception: "+e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				return "Exception: "+e.getMessage();
			}
		}

		@Override
		protected void onPostExecute(String s) {
			if(s.equals("done")){
				s="Data saved successfully.";
			}
			Toast.makeText(photo.this, s, Toast.LENGTH_LONG).show();
		}
	}

}


