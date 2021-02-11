package org.thecyberspace.mynote;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.thecyberspace.mynote.model.Adapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL;

public class HomeFragment extends Fragment {


    FloatingActionButton mAddNote;
    RecyclerView mNoteList;
    Adapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mNoteList = v.findViewById(R.id.noteList);
        mAddNote = v.findViewById(R.id.main_fab);

        List<String> title = new ArrayList<>();
        List<String> content = new ArrayList<>();

        title.add("First note title");
        content.add("First note content");

        title.add("Second note title");
        content.add("Second note content");

        title.add("Third note title");
        content.add("Third note content");

        adapter = new Adapter(title,content);
        mNoteList.setLayoutManager(new StaggeredGridLayoutManager(2,VERTICAL));
        mNoteList.setAdapter(adapter);
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AddNote.class);
                startActivity(intent);

            }
        });
        return v;
    }
}
