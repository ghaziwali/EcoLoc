package ecoloc.startapp.com;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    private TextView mobile, name;
    private ImageView editBtn;
    private CircleImageView profileImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mobile = view.findViewById(R.id.user_profile_mobile);
        name = view.findViewById(R.id.user_profile_name);

        mAuth = FirebaseAuth.getInstance();

        editBtn = view.findViewById(R.id.edit_icon);

        profileImg = view.findViewById(R.id.user_profile_image);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        loadUserInfo();

        super.onStart();
    }

    private void loadUserInfo() {

        final FirebaseUser user = mAuth.getCurrentUser();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePics/");

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this )
                        .load(user.getPhotoUrl().toString())
                        .into(profileImg);
            }
            if (user.getDisplayName() != null) {
                name.setText(user.getDisplayName());
            }
            if (user.getPhoneNumber() != null) {
                mobile.setText(user.getPhoneNumber());
            }
        }
    }

    private void editProfile() {
        Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
        startActivity(intent);

    }

    /*private void loadPhoneNumber() {
       SharedPreferences shrd = getActivity().getSharedPreferences("saveNumber", Context.MODE_PRIVATE);
        String phoneNumber = shrd.getString("mobile", "ON_DATA");
        mobile.setText(phoneNumber);}*/
}

