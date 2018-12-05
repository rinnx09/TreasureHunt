package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WefieActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private ImageView myImageView;
    private TextView text, faceDetected;
    private Button btn3,btngiveup;
    private Uri photoURI;
    private int face_needed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wefie);

        Intent intent = getIntent();
        face_needed = intent.getIntExtra(HomeActivity.MISSION_VALUE, 0);

        faceDetected = (TextView) findViewById(R.id.textViewfaceDetected);
        myImageView = (ImageView) findViewById(R.id.imgview);
        text = (TextView) findViewById(R.id.textViewfaceNeeded);
        text.setText("Mission: Taking photo with " + String.valueOf(face_needed) + " people");
        btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn3.getText().equals("Start") || btn3.getText().equals("Try again")) {
                    dispatchTakePictureIntent();
                } else if (btn3.getText().equals("Complete")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("RESULT", true);
                    returnIntent.putExtra("MARK", String.valueOf(faceDetected.getText().toString()));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

            }
        });

        btngiveup=(Button)findViewById(R.id.buttonGiveup1);
        btngiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("RESULT", false);
                returnIntent.putExtra("MARK", 0);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }

            myImageView.setImageBitmap(photo);
            myImageView.setVisibility(View.VISIBLE);
            Bitmap myBitmap = ((BitmapDrawable) myImageView.getDrawable()).getBitmap();


            Paint myRectPaint = new Paint();
            myRectPaint.setStrokeWidth(5);
            myRectPaint.setColor(Color.GREEN);
            myRectPaint.setStyle(Paint.Style.STROKE);

            Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(myBitmap, 0, 0, null);
            //create face detection
            FaceDetector faceDetector = new
                    FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                    .build();
            if (!faceDetector.isOperational()) {
                new AlertDialog.Builder(getApplicationContext()).setMessage("Could not set up the face detector!, try again later because it will auto download at first run.").show();
                return;
            }
            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Face> faces = faceDetector.detect(frame);
            //draw rectangle on face
            for (int i = 0; i < faces.size(); i++) {
                Face thisFace = faces.valueAt(i);
                float x1 = thisFace.getPosition().x;
                float y1 = thisFace.getPosition().y;
                float x2 = x1 + thisFace.getWidth();
                float y2 = y1 + thisFace.getHeight();
                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            }
            myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

            galleryAddPic();

            faceDetected.setText("Number of face detected: "+String.valueOf(faces.size()));
            if (faces.size() >= face_needed) {
                btn3.setText("Complete");
                btngiveup.setEnabled(false);
            } else {
                btn3.setText("Try again");
            }

        }

    }

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
                // new AlertDialog.Builder(getApplicationContext()).setMessage("Image File fail to create").show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
               /* Uri photoURI = FileProvider.getUriForFile(this,
                        "my.edu.tarc.facedetect",
                        photoFile);*/
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.rinnxyii.treasurehunt",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }
}
