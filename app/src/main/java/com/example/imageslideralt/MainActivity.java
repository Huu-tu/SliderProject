package com.example.imageslideralt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.imageslideralt.Resouces.ImageAdapter;
import com.example.imageslideralt.Resouces.ImageEntity;
import com.example.imageslideralt.Ultis.ViewUtils;
import com.example.imageslideralt.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    private ArrayList<ImageEntity> imageEntities;
    FirebaseFirestore firestore;
    private SliderView sliderView;
    private LoaderManager loaderManager;
    EditText addUrl;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageEntities = new ArrayList<>();
//        sliderView = findViewById(R.id.slider);
        firestore = FirebaseFirestore.getInstance();

        loadData();

    }

    private void loadData() {
        firestore.collection("Images").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ImageEntity imageEntity = documentSnapshot.toObject(ImageEntity.class);
                    ImageEntity image = new ImageEntity();

                    image.setImageUrl(imageEntity.getImageUrl());
                    imageEntities.add(image);

                    imageAdapter = new ImageAdapter(MainActivity.this, imageEntities);
                    binding.slider.setSliderAdapter(imageAdapter);
                    binding.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    binding.slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    binding.slider.setScrollTimeInSec(3);
                    binding.slider.setAutoCycle(true);
                    binding.slider.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ViewUtils.showSnackbar(MainActivity.this, "Failed to load Data!");
            }
        });
    }

    public void submit(View view)
    {
        addUrl = findViewById(R.id.editUrl);
        String AddUrls = addUrl.getText().toString();

        if (TextUtils.isEmpty(AddUrls))
        {
            addUrl.setError("Required Field!");
        }
        else if (!Patterns.WEB_URL.matcher(addUrl.getText().toString()).matches())
        {
            addUrl.setError("Must be URL (See Hint)");
        }
        else
        {
            saveURl(AddUrls);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void saveURl(String Url)
    {
        CollectionReference collectionReference = firestore.collection("Images");
        ImageEntity imageEntity = new ImageEntity(Url);
        collectionReference.add(imageEntity).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                ViewUtils.showToast(MainActivity.this, "Image Added!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ViewUtils.showToast(MainActivity.this, "Image Add Failed!" + e);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.add_urls)
        {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}



//git hub token: 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