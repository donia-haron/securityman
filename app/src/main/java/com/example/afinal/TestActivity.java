package com.example.afinal;

import static androidx.camera.core.CameraX.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    public static final String lang = "eng";
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    InputStream trainDataInputStream;
    OutputStream trainDataOutputStream;
    AssetManager assetManager;
    String externalDataPath;
    String ImageString="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        } else {
            Log.i("nelsaaa", "nelaa");

        }

// 2. Obtain the python instance

//        PyObject sys = py.getModule("scan");
//        PyObject textOutputStream = sys.callAttr("fibonacci_of",5);
//        Log.i("outputtttttt",textOutputStream.toString());

        this.imageView = (ImageView) this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.button1);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

//        mTessOCR = new tessOCR();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");


            imageView.setImageBitmap(photo);
            Python py = Python.getInstance();
            ImageString=getStringImage(photo);
            Log.i("ImageeeeeeeeeeeStringggggg",ImageString);


//             PyObject sys = py.getModule("scan");
//            //call python method and pass imageString as a parameter
//            byte[] out = sys.callAttr("main",ImageString).toJava(byte[].class);;
//            short[] shortData = new short[out.length / 2];
//            ByteBuffer.wrap(out).order(ByteOrder.nativeOrder()).asShortBuffer().get(shortData);

            //  String result = URLDecoder.decode(imageUrls.get(j), "UTF-8");

//            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
//               // Or, to provide language hints to assist with language detection:
//// See https://cloud.google.com/vision/docs/languages for supported languages
//               FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
//                    .setLanguageHints(Arrays.asList("en","hi","ar"))
//                    .build();
//            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
//                    .getOnDeviceTextRecognizer();
//            Task<FirebaseVisionText> result =
//                    detector.processImage(image)
//                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//                                @Override
//                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
//                                    // Task completed successfully
//                                    // ...
//
//                                    for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
//                                        Rect boundingBox = block.getBoundingBox();
//                                        Point[] cornerPoints = block.getCornerPoints();
//                                        String text = block.getText();
//                                        Log.i("testx",text);
//                                    }
//                                }
//
//                            })
//                            .addOnFailureListener(
//                                    new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Task failed with an exception
//                                            // ...
//                                        }
//                                    });
//
//
//
//


//
//


//            File imageFile = new File("eurotext.tif");
//            ITesseract instance = new Tesseract();  // JNA Interface Mapping
//            // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//            instance.setDatapath("tessdata"); // path to tessdata directory
//
//            try {
//                String result = instance.doOCR(imageFile);
//                System.out.println(result);
//            } catch (TesseractException e) {
//                System.err.println(e.getMessage());
//            }


//            InputImage imagee = InputImage.fromBitmap(photo, 90);
//            // [START run_detector]
//            Task<Text> result =
//                    recognizer.process(imagee)
//                            .addOnSuccessListener(new OnSuccessListener<Text>() {
//                                @Override
//                                public void onSuccess(Text visionText) {
//
//                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
//                                        Rect boundingBox = block.getBoundingBox();
//                                        Point[] cornerPoints = block.getCornerPoints();
//                                        String text = block.getText();
//
//                                        Log.i("text",text);
//                                        //
//                                        for (Text.Line line: block.getLines()) {
//                                            // ...
//                                            for (Text.Element element: line.getElements()) {
//
//                                         Log.i("inn",element.toString());
//
//                                                // ...
//                                            }
//                                        }
//                                    }
//                                    // [END get_text]
//                                    // [END_EXCLUDE]
//                                }
//                            })
//                            .addOnFailureListener(
//                                    new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            // Task failed with an exception
//                                            // ...
//                                        }
//                                    });
//            // [END run_detector]
//

        }
    }
    public String getStringImage(Bitmap photo){
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,100,output);
        byte [] imageByte=output.toByteArray();
        String encodeImage=android.util.Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodeImage;
    }
}


