package ecoloc.startapp.com;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ecoloc.startapp.com.adapters.AdsListAdapter;
import ecoloc.startapp.com.constractors.Ads;

public class MyCarsFragment extends Fragment {


    private static final String TAG = "FireLog";
    private RecyclerView mMainListe;
    private FirebaseFirestore mFirestore;
    private List<Ads> adsList;
    private AdsListAdapter adsListAdapter;

    @Override
    public void onResume() {
        adsListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cars, container, false);

        mMainListe = view.findViewById(R.id.main_list);
        mFirestore = FirebaseFirestore.getInstance();
        adsList = new ArrayList<>();
        adsListAdapter = new AdsListAdapter(getContext(), adsList);

        mMainListe.setHasFixedSize(true);
        mMainListe.setLayoutManager(new LinearLayoutManager(getContext()));
        mMainListe.setAdapter(adsListAdapter);


        loadAd();


        return view;
    }


    private void loadAd() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            mFirestore.collection("ads").whereEqualTo("UID", uid)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {

                                Log.d(TAG, "Error :" + e.getMessage());
                            }
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String ad_id = doc.getDocument().getId();
                                    Ads ads = doc.getDocument().toObject(Ads.class).withId(ad_id);
                                    adsList.add(ads);
                                    adsListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });


    }
}

}