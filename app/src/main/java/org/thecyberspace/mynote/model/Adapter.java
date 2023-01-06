package org.thecyberspace.mynote.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.thecyberspace.mynote.NoteDetails;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> title;
    List<String> content;

    public Adapter(List<String> title, List<String> content){
        this.title = title;
        this.content = content;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(org.thecyberspace.mynote.R.layout.fragment_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.noteTitle.setText(title.get(position));
        holder.noteContent.setText(content.get(position));

        holder.view.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               Intent intent = new Intent(v.getContext(), NoteDetails.class);
               intent.putExtra("title",title.get(position));
               intent.putExtra("content",content.get(position));
               v.getContext().startActivity(intent);
           }
        });
    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle, noteContent;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(org.thecyberspace.mynote.R.id.title);
            noteContent = itemView.findViewById(org.thecyberspace.mynote.R.id.content);
            view = itemView;
        }
    }
}
