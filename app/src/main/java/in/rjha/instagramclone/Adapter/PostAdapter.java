package in.rjha.instagramclone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.rjha.instagramclone.Model.Post;
import in.rjha.instagramclone.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myHolder>
{

    List<Post> postList;
    public PostAdapter(List<Post> postList) {
        this.postList = postList;

    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.postitem,viewGroup,false);
        myHolder myHolder=new myHolder(view);
        return myHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull myHolder myHolder, int i) {
        Post data=postList.get(i);
        myHolder.title.setText(data.geturl());
        myHolder.desc.setText(data.getDesc());
        myHolder.name.setText(data.getEmail());
        Picasso.get()
                .load(data.geturl())
                .placeholder(R.drawable.postimg)
                .error(R.drawable.postimg)
                .into(myHolder.postimg);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class myHolder extends RecyclerView.ViewHolder

    {
        TextView title,desc,name;
        ImageView postimg;
        CircleImageView circleImageView;

        public myHolder(@NonNull View itemView)
        {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            desc=itemView.findViewById(R.id.desc);
            name=itemView.findViewById(R.id.name);
            postimg=itemView.findViewById(R.id.postimg);
            circleImageView=itemView.findViewById(R.id.profile);

        }
    }
}
