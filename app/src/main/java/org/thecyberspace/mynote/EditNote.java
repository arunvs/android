package org.thecyberspace.mynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNote extends AppCompatActivity {
    @BindView(R.id.editNoteTitle)
    EditText editNoteTitle;
    @BindView(R.id.editNoteContent)
    EditText editNoteContent;
    @BindView(R.id.editToolbar)
    Toolbar editToolbar;
    @BindView(R.id.editNoteSave)
    FloatingActionButton mEditNoteSave;
    @BindView(R.id.editProgressBar)
    ProgressBar editProgressBar;
    Intent data;
    FirebaseFirestore editFstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        setSupportActionBar(editToolbar);
        ButterKnife.bind(this);
        editFstore = FirebaseFirestore.getInstance();

        data = getIntent();

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        editNoteTitle.setText(noteTitle);
        editNoteContent.setText(noteContent);

        mEditNoteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = editNoteTitle.getText().toString();
                String nContent = editNoteContent.getText().toString();

                if(nTitle.isEmpty() || nContent.isEmpty()){
                    Toast.makeText(EditNote.this,"Cannot save note without contents",Toast.LENGTH_SHORT).show();
                }

                editProgressBar.setVisibility(View.VISIBLE);

                DocumentReference docRef = editFstore.collection("notes")
                        .document(data.getStringExtra("noteId"));
                Map<String, Object> note = new HashMap<String, Object>();
                note.put("title",nTitle);
                note.put("content" ,nContent);

                docRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditNote.this,"Note Added", Toast.LENGTH_SHORT).show();
                        editProgressBar.setVisibility(View.INVISIBLE);
                        Intent intent=new Intent(view.getContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
                docRef.update(note).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this,"Error : Try Again", Toast.LENGTH_SHORT).show();
                        editProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}