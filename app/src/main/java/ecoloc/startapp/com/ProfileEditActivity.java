package ecoloc.startapp.com;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private CircleImageView profileImage;
    private EditText name;
    private Button edit;
    FirebaseAuth mAuth;
    Uri uriProfileImg;
    String profileImgUrl;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        profileImage = findViewById(R.id.image_input);
        name = findViewById(R.id.name_input);
        edit = findViewById(R.id.edit_btn);
        progressBar = findViewById(R.id.pb);

        mAuth = FirebaseAuth.getInstance();

        //change profile pic
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImageChooser();
            }
        });

        //apply edit
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();

            }
        });
    }

    private void saveUserInfo() {

        String displayName = name.getText().toString().trim();

        if (displayName.isEmpty()) {
            name.setError("Champs obligatoire");
            name.requestFocus();
        }

        FirebaseUser user = mAuth.getCurrentUser();


        if (user != null && profileImgUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImgUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileEditActivity.this, "Profile modifié", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImg = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImg);
                profileImage.setImageBitmap(bitmap);
                uploadImgFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImgFirebase() {
        StorageReference profileImgRef =
                FirebaseStorage.getInstance().getReference("profilePics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImg != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImgRef.putFile(uriProfileImg)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileImgUrl = taskSnapshot.getDownloadUrl().toString();
                            progressBar.setVisibility(View.GONE);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }

    }

    private void profileImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "selectionnez l'image du profil"), CHOOSE_IMAGE);
    }

}

