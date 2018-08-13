package ecoloc.startapp.com.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ecoloc.startapp.com.CreatCarActivity;
import ecoloc.startapp.com.MainActivity;
import ecoloc.startapp.com.R;
import ecoloc.startapp.com.constractors.Ads;

public class UpdateActivity extends AppCompatActivity {


    private static final String AD_TITLE_KEY = "title";
    private static final String AD_VILLE = "ville";
    private static final String AD_PRICE_KEY = "price";
    private static final String AD_DESCRIPTION_KEY = "description";
    private static final String AD_TRANSMISSION_KEY = "transmission";
    private static final String AD_CAR_TYPE_KEY = "type";
    private static final String AD_CAR_MODEL_KEY = "model";
    private static final String AD_USER_ID = "UID";
    private static final String AD_FOLDER = "Folder";


    private static final String TAG = "firelog";
    private AutoCompleteTextView Ville;
    private EditText adTitle, PricePerDay, description;
    private Spinner CarModels, CarTypes;
    private RadioGroup TransmissionType;
    private Button updateAd;
    String id;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adTitle = findViewById(R.id.vehicleName);
        description = findViewById(R.id.description);
        Ville = findViewById(R.id.ville);
        PricePerDay = findViewById(R.id.pricePerDay);
        CarModels = findViewById(R.id.vehicleModelPicker);
        CarTypes = findViewById(R.id.vehicleTypePicker);
        TransmissionType = findViewById(R.id.transmission);
        updateAd = findViewById(R.id.update);
        mFirestore = FirebaseFirestore.getInstance();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        }

        loadAd();
        updateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCar();
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void updateCar() {
        String Title = adTitle.getText().toString().trim();
        String Description = description.getText().toString().trim();
        String VilleCar = Ville.getText().toString().trim();
        String Price = PricePerDay.getText().toString().trim();
        String Vehicule_Model = String.valueOf(CarModels.getSelectedItem());
        String Vehicule_Type = String.valueOf(CarTypes.getSelectedItem());

        // radiogroup transsmission type
        int selectedID = TransmissionType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
        String TransType = selectedRadioButton.getText().toString();


        if (VilleCar.isEmpty()) {
            Ville.setError("Champs obligatoire");
            Ville.requestFocus();
        }
        if (Description.isEmpty()) {
            description.setError("Champs obligatoire");
            description.requestFocus();
        }
        if (Title.isEmpty()) {
            adTitle.setError("Champs obligatoire");
            adTitle.requestFocus();
        }
        if (Price.isEmpty()) {
            PricePerDay.setError("Champs obligatoire");
            PricePerDay.requestFocus();
        }

        Map<String, Object> ad = new HashMap<>();

        ad.put(AD_TITLE_KEY, Title);
        ad.put(AD_VILLE, VilleCar);
        ad.put(AD_PRICE_KEY, Price);
        ad.put(AD_DESCRIPTION_KEY, Description);
        ad.put(AD_TRANSMISSION_KEY, TransType);
        ad.put(AD_CAR_MODEL_KEY, Vehicule_Model);
        ad.put(AD_CAR_TYPE_KEY, Vehicule_Type);
    /*
        ad.put(AD_FOLDER, uuid);
        ad.put(AD_USER_ID, uid);
        ad.put("link",downloadUri);
        ad.put("links",list);*/

     mFirestore.collection("ads").document(id).update(ad).addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void aVoid) {
             Toast.makeText(UpdateActivity.this, "updated", Toast.LENGTH_SHORT).show();
         }
     });
    }
    private void loadAd() {
        mFirestore.collection("ads").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Ads ads = documentSnapshot.toObject(Ads.class);

                    }

                });
    }
}
