package ecoloc.startapp.com;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ecoloc.startapp.com.adapters.ModelOnItemSelectedListener;
import ecoloc.startapp.com.adapters.UploadListAdapter;
import ecoloc.startapp.com.constractors.ImagesUrl;

import static java.lang.String.valueOf;

public class CreatCarActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    private static final String TAG = "CreatCarActivity";
    private static final String AD_TITLE_KEY = "title";
    private static final String AD_VILLE = "ville";
    private static final String AD_PRICE_KEY = "price";
    private static final String AD_DESCRIPTION_KEY = "description";
    private static final String AD_TRANSMISSION_KEY = "transmission";
    private static final String AD_CAR_TYPE_KEY = "type";
    private static final String AD_CAR_MODEL_KEY = "model";
    private static final String AD_USER_ID = "UID";
    private static final String AD_FOLDER = "Folder";

    private AutoCompleteTextView Ville;
    private EditText adTitle, PricePerDay, description;
    private Spinner CarModels, CarTypes;
    private RadioGroup TransmissionType;
    private Button SubmitAd, mSelectBtn;
    private RecyclerView mUploadList;
    private StorageReference mStorage;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    String uuid;
    ArrayList<String> list;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String url,downloadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_car);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uuid = UUID.randomUUID().toString();

        //find veiw by id
        adTitle = findViewById(R.id.vehicleName);
        description = findViewById(R.id.description);
        Ville = findViewById(R.id.ville);
        PricePerDay = findViewById(R.id.pricePerDay);
        CarModels = findViewById(R.id.vehicleModelPicker);
        CarTypes = findViewById(R.id.vehicleTypePicker);
        TransmissionType = findViewById(R.id.transmission);
        SubmitAd = findViewById(R.id.submit);
        mSelectBtn = findViewById(R.id.img_upload);
        mUploadList = findViewById(R.id.upload_list);

        CarModels.setOnItemSelectedListener(new ModelOnItemSelectedListener());
        CarTypes.setOnItemSelectedListener(new ModelOnItemSelectedListener());
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        list = new ArrayList<>();


        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);


        Resources res = getResources();
        String[] KnownColors = res.getStringArray(R.array.villes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, KnownColors);
        Ville.setAdapter(adapter);
        Ville.setThreshold(1);
        Ville.setDropDownWidth(300);

        //RecyclerView
        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);


        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adImagesChooser();
            }
        });


        SubmitAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
                Intent intent = new Intent(CreatCarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                int totalItemsSelected = data.getClipData().getItemCount();
                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);

                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");

                    //star uplaod
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Images").child(uuid).child(fileName);

                    StorageMetadata metadata = new StorageMetadata.Builder()
                            .setContentType("image/jpg")
                            .setCustomMetadata("myCustomProperty", "myValue")
                            .build();

                    final int finalI = i;

                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                       /*     Uri downloadUri = taskSnapshot.getDownloadUrl();
                            image = new ImagesUrl(downloadUri.toString());
                            url = downloadUri.toString();
                            list.add(image);
                            */

                          downloadUri = taskSnapshot.getDownloadUrl().toString();
                            list.add(downloadUri);

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI, "done");
                            uploadListAdapter.notifyDataSetChanged();
                        }
                    });
                    //end upload

                }

            } else if (data.getData() != null) {

                Toast.makeText(CreatCarActivity.this, "vous avez besoin d'au moins 2 images", Toast.LENGTH_SHORT).show();

            }

        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void saveInfo() {

        String Title = adTitle.getText().toString().trim();
        String Description = description.getText().toString().trim();
        String VilleCar = Ville.getText().toString().trim();
        String Price = PricePerDay.getText().toString().trim();
        String Vehicule_Model = String.valueOf(CarModels.getSelectedItem());
        String Vehicule_Type = String.valueOf(CarTypes.getSelectedItem());
        FirebaseUser user = mAuth.getCurrentUser();

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

        // radiogroup transsmission type
        int selectedID = TransmissionType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = (RadioButton) findViewById(selectedID);
        String TransType = selectedRadioButton.getText().toString();


        if (user != null) {
            Map<String, Object> ad = new HashMap<>();
            String uid = user.getUid();
            ad.put(AD_USER_ID, uid);
            ad.put(AD_TITLE_KEY, Title);
            ad.put(AD_VILLE, VilleCar);
            ad.put(AD_PRICE_KEY, Price);
            ad.put(AD_DESCRIPTION_KEY, Description);
            ad.put(AD_TRANSMISSION_KEY, TransType);
            ad.put(AD_CAR_MODEL_KEY, Vehicule_Model);
            ad.put(AD_CAR_TYPE_KEY, Vehicule_Type);
            ad.put(AD_FOLDER, uuid);
            ad.put("link",downloadUri);
            ad.put("links",list);

            db.collection("ads")
                    .add(ad)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(CreatCarActivity.this, "annonce deposé", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreatCarActivity.this, "error", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreatCarActivity.this, MainActivity.class));
        super.onBackPressed();
    }


    private void adImagesChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choix d'images"), CHOOSE_IMAGE);
    }
}
