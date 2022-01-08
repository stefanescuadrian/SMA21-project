package com.upt.cti.photogmap.photographerfragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.upt.cti.photogmap.Photographer;
import com.upt.cti.photogmap.PhotographerAdapter;
import com.upt.cti.photogmap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankPhotographerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankPhotographerFragment extends Fragment {
    private RecyclerView recyclerPhotographersList;
    private ArrayList<Photographer> photographersList;
    private PhotographerAdapter photographerAdapter;
    private FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;


    private ProgressBar progressRankPhotographers;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RankPhotographerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankPhotographerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankPhotographerFragment newInstance(String param1, String param2) {
        RankPhotographerFragment fragment = new RankPhotographerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank_photographer, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();


        recyclerPhotographersList = view.findViewById(R.id.recyclerPhotographersList);

        recyclerPhotographersList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerPhotographersList.setLayoutManager(linearLayoutManager);

        firebaseFirestore = FirebaseFirestore.getInstance();
        photographersList = new ArrayList<Photographer>();
        photographerAdapter = new PhotographerAdapter(getActivity(), photographersList);

        recyclerPhotographersList.setAdapter(photographerAdapter);

        EventChangeListener();




//        firebaseAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance("https://photog-map-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Gallery Uploads/"+ Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
//        storageReference = FirebaseStorage.getInstance().getReference();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//
//        recyclerPhotographersList.setHasFixedSize(true);
//        recyclerPhotographersList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        photographerList = new ArrayList<>();


        return view;
    }

    private void EventChangeListener() {
        firebaseFirestore.collection("Users").whereEqualTo("userType","Fotograf").orderBy("score", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    Log.e("Firestore error...", error.getMessage());
                    return;
                }

                for (DocumentChange documentChange : value.getDocumentChanges()){
                    photographersList.add(documentChange.getDocument().toObject(Photographer.class));
                }

                photographerAdapter.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }


}