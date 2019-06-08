package si.uni_lj.fri.pbd2019.runsup.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.Iterator;

import si.uni_lj.fri.pbd2019.runsup.HistoryListAdapter;
import si.uni_lj.fri.pbd2019.runsup.Item;
import si.uni_lj.fri.pbd2019.runsup.MainActivity;
import si.uni_lj.fri.pbd2019.runsup.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    public DatabaseReference mDatabase;
    public String mUserId;
    public HistoryListAdapter adapter;
    public ArrayList<Item> list;
    public ListView listView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity

        } else {
            mUserId = mFirebaseUser.getUid();
        }
        listView = (ListView) v.findViewById(R.id.listview_history_workouts);
        list=new ArrayList<>();
        list.add(
                new Item("Workout",
                        "0","0",
                        "0","0")); // define you
        adapter = new HistoryListAdapter(container.getContext(),0,list);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        /*Query query=FirebaseDatabase.getInstance().getReference().child("users").child(mUserId).child("items");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshott) {
                list=new ArrayList<>();

                for (DataSnapshot dataSnapshot : dataSnapshott.getChildren()) {

                    list.add(
                            new Item(Integer.parseInt(dataSnapshot.child("imageDrawable").getValue().toString()),dataSnapshot.child("title").getValue().toString(),
                                    dataSnapshot.child("date").getValue().toString(),dataSnapshot.child("distance").getValue().toString(),
                                    dataSnapshot.child("duration").getValue().toString(),dataSnapshot.child("calories").getValue().toString())); // define your pojo class for getting firebase data.
                }

                adapter = new HistoryListAdapter(container.getContext(),0,list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Log.d("database3",String.valueOf(adapter.getCount()));
                Log.d("database3",String.valueOf(list.toString()));
                Log.d("database3",String.valueOf(adapter.getItem(0)));
                Log.d("database3",String.valueOf(adapter.getItem(1)));
                Log.d("database3",String.valueOf(adapter.getItem(2)));

                Log.d("database3",container.getContext().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

            // Delete items when clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mDatabase.child("users").child(mUserId).child("items")
                            .orderByChild("title")
                            .equalTo((String) listView.getItemAtPosition(position))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()) {
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        firstChild.getRef().removeValue();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            });

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
