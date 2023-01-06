package org.thecyberspace.mynote;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.thecyberspace.mynote.model.Adapter;
import org.thecyberspace.mynote.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL;

public class HomeFragment extends Fragment {


    FloatingActionButton mAddNote;
    RecyclerView mNoteList;
    FirebaseFirestore fstore;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;
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
        fstore = FirebaseFirestore.getInstance();

        Query query = fstore.collection("notes").orderBy("title",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> allNotes = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Note note) {
                noteViewHolder.noteTitle.setText(note.getTitle());
                noteViewHolder.noteContent.setText(note.getContent());
                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.view.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(v.getContext(), NoteDetails.class);
                        intent.putExtra("title",note.getTitle());
                        intent.putExtra("content",note.getContent());
                        intent.putExtra("noteId",docId);
                        v.getContext().startActivity(intent);
                    }
                });
                ImageView menuIcon=noteViewHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String docId=noteAdapter.getSnapshots().getSnapshot(i).getId();
                        PopupMenu menu=new PopupMenu(view.getContext(),view);
                        menu.setGravity(Gravity.END);
                        menu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent editIntent = new Intent(view.getContext(), EditNote.class);
                                editIntent.putExtra("title",note.getTitle());
                                editIntent.putExtra("content",note.getContent());
                                editIntent.putExtra("noteId",docId);
                                startActivity(editIntent);
                                return false;
                            }
                        });

                        menu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference docRef=fstore.collection("notes").document(docId);
                                docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //note deleted
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),"Error in deleting note",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        menu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(org.thecyberspace.mynote.R.layout.fragment_note,parent,false);
                return new NoteViewHolder(view);
            }
        };

        mNoteList.setLayoutManager(new StaggeredGridLayoutManager(2,VERTICAL));
        mNoteList.setAdapter(noteAdapter);
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),AddNote.class);
                startActivity(intent);

            }
        });
        return v;
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteContent;
        View view;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(org.thecyberspace.mynote.R.id.title);
            noteContent = itemView.findViewById(org.thecyberspace.mynote.R.id.content);
            view = itemView;
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        noteAdapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        if(noteAdapter!=null) {
            noteAdapter.stopListening();
        }
    }
}
