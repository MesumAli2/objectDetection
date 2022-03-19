package com.mesum.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.internal.TelemetryLogging.getClient
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetection.getClient
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.PredefinedCategory

private const val  REQUEST_CODE = 42
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getImage()


    }

    private fun analyzeImage( image: Bitmap) {
        //live detection and tracking
        val liveTracking = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableClassification()
            .build()

        val objectDetector = ObjectDetection.getClient(liveTracking)
        val image = InputImage.fromBitmap(image, 0)

        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects ->
                for (data in detectedObjects){
                    val boundingBox = data.boundingBox
                    val trackingID = data.trackingId
                    for (label in data.labels){

                        val text = label.text
                        if (PredefinedCategory.FOOD == text){
                          //  val lastView = findViewById<TextView>(R.id.textView4)
                           // lastView.text = text
                        }
                        val index = label.index
                        if (PredefinedCategory.FOOD_INDEX == index){
                            Toast.makeText(this, "${index.toString()}", Toast.LENGTH_SHORT).show()
                        }
                        val accuracytext = findViewById<TextView>(R.id.textView2)
                        accuracytext.text = text;

                        val confidence = findViewById<TextView>(R.id.textView3)
                        confidence.text = label.confidence.toString()
                    }

                }

            }


    }


    private fun getImage() {
        val button = findViewById<Button>(R.id.analyze_button)
        button.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, REQUEST_CODE )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val image = findViewById<ImageView>(R.id.AnalyzedImaged)
                .setImageBitmap(data?.extras?.get("data") as Bitmap)
            image
            analyzeImage(data.extras?.get("data") as Bitmap)


        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}