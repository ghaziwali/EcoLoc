package ecoloc.startapp.com;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ecoloc.startapp.com.adapters.AdsListAdapter;
import ecoloc.startapp.com.adapters.HomeAdsListAdapter;
import ecoloc.startapp.com.constractors.Ads;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment {

    private static final String TAG = "FireLog";
    private RecyclerView mMainListe;
    private FirebaseFirestore mFirestore;
    private List<Ads> adsList;
    private HomeAdsListAdapter homeAdsListAdapter;

    @Override
    public void onResume() {
        homeAdsListAdapter.notifyDataSetChanged();
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        mMainListe = view.findViewById(R.id.main_list);
        mFirestore = FirebaseFirestore.getInstance();
        adsList = new ArrayList<>();

        homeAdsListAdapter = new HomeAdsListAdapter(getContext(), adsList);

        mMainListe.setHasFixedSize(true);
        mMainListe.setLayoutManager(new LinearLayoutManager(getContext()));
        mMainListe.setAdapter(homeAdsListAdapter);

        loadAd();
        return view;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {



                return false;
            }
        });
    }


    private void loadAd() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            mFirestore.collection("ads")
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
                                    homeAdsListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

    }
}
