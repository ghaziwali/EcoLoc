package ecoloc.startapp.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ecoloc.startapp.com.adapters.AdsInfoAdapter;
import ecoloc.startapp.com.constractors.Ads;
import ecoloc.startapp.com.adapters.CarouselAdapter;
import ecoloc.startapp.com.constractors.ImagesUrl;

public class adPageActivity extends AppCompatActivity {

    private static final String TAG = "fireLog";
/*
    private CarouselView adImages;
*/
    private RecyclerView adImages;
    private CircleImageView profileImg;
    private TextView profileName, profileNumber;
    FirebaseAuth mAuth;
    private RecyclerView mMainListe;
    private FirebaseFirestore mFirestore;
    private String id,uid;

    private List<Ads> infoList;
    private AdsInfoAdapter adsInfoAdapter;
    private CarouselAdapter carouselAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        adImages = findViewById(R.id.adImages);
        profileImg = findViewById(R.id.user_profile_image);
        profileName = findViewById(R.id.user_profile_name);
        profileNumber = findViewById(R.id.user_profile_mobile);
        mMainListe = (RecyclerView) findViewById(R.id.info_list);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        infoList = new ArrayList<>();



/*
        carouselAdapter = new CarouselAdapter(this,infoList ,  mUploads);
        adImages.setHasFixedSize(true);
        adImages.setLayoutManager(new LinearLayoutManager(this));
        adImages.setAdapter(carouselAdapter);
*/

/*
        carouselAdapter = new CarouselAdapter(this,infoList);
        adImages.setHasFixedSize(true);
        adImages.setLayoutManager(new LinearLayoutManager(this));
        adImages.setAdapter(carouselAdapter);*/


        adsInfoAdapter = new AdsInfoAdapter(this, infoList);
        mMainListe.setHasFixedSize(true);
        mMainListe.setLayoutManager(new LinearLayoutManager(this));
        mMainListe.setAdapter(adsInfoAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            uid = bundle.getString("user_id");
        }

        loadAdInfo();
        loadUserInfo();

    }


    private void loadAdInfo() {

        mFirestore.collection("ads").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Ads ads = documentSnapshot.toObject(Ads.class);
                        infoList.add(ads);
                        adsInfoAdapter.notifyDataSetChanged();
                    }

                });

                  /*  @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {

                            Log.d(TAG, "Error :" + e.getMessage());
                        }
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String ad_id = doc.getDocument().getId();
                                Ads ads = doc.getDocument().toObject(Ads.class).withId(ad_id);
                                infoList.add(ads);
                                adsInfoAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });*/
    }

    private void loadUserInfo() {

   final FirebaseUser user = mAuth.getCurrentUser();



        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePics/");



        if ( user != null) {

            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(profileImg);
            }
            if (user.getDisplayName() != null) {
                profileName.setText(user.getDisplayName());
            }

            if (user.getPhoneNumber() != null) {
                profileNumber.setText(user.getPhoneNumber());
            }
        }
    }


}
