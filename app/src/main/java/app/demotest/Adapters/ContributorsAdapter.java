package app.demotest.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.demotest.ModelClass.Contributors;
import app.demotest.R;


public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.MyViewHolder> {


    Activity activity;
    List<Contributors> list_contributors;



    public ContributorsAdapter(Activity activity, List<Contributors> list_contributors) {
        this.list_contributors = list_contributors;
        this.activity=activity;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_loginUser;
        public TextView txt_loginNmae,txt_reposUrl,txt_contributorId;

        public MyViewHolder(View view) {
            super(view);
            img_loginUser = (ImageView) view.findViewById(R.id.img_loginUser);
            txt_loginNmae=(TextView)view.findViewById(R.id.txt_loginNmae);
            txt_reposUrl=(TextView)view.findViewById(R.id.txt_reposUrl);
            txt_contributorId=(TextView)view.findViewById(R.id.txt_contributorId);


        }

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_contributor, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contributors contributors = list_contributors.get(position);

        holder.txt_loginNmae.setText(contributors.login);
        holder.txt_reposUrl.setText(contributors.repos_url);
        holder.txt_contributorId.setText("Contributions :"+contributors.contributions);

        Glide.with(activity)
                .load(contributors.avatar_url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.img_loginUser);

    }

    @Override
    public int getItemCount() {
        return list_contributors.size();
    }
}