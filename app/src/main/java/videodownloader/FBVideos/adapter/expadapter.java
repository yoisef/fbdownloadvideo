package videodownloader.FBVideos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import videodownloader.FBVideos.R;

public class expadapter extends RecyclerView.Adapter<expadapter.viewholder> {
    Context context;
    List<Integer> swr;
    public expadapter(Context context)
    {
        this.context=context;
        swr=new ArrayList<>();

        swr.add(R.drawable.gotofb);
        swr.add(R.drawable.copyurlfromfb);
        swr.add(R.drawable.downloadfromappmsg);
        swr.add(R.drawable.pastetoyourbrowser);
        swr.add(R.drawable.choosequailtyanddownload);
        swr.add(R.drawable.downloadfromappmsg);
        swr.add(R.drawable.copyurlfromapp);
        swr.add(R.drawable.choosequailtyanddownload);
        swr.add(R.drawable.downloadfromappcopyurl);
        swr.add(R.drawable.copyurlfromapp);
        swr.add(null);
        swr.add(R.drawable.downloadfromappcopyurl);











    }

    @NonNull
    @Override
    public expadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view =LayoutInflater.from(context).inflate(R.layout.rowexp,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull expadapter.viewholder holder, int position) {

        Glide.with(context)
                .load(swr.get(position))
                .into(holder.img);


    }

    @Override
    public int getItemCount() {
        return swr.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView img;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.rowimg);


        }
    }
}
