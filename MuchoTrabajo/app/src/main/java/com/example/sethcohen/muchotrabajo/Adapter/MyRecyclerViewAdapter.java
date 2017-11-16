package com.example.sethcohen.muchotrabajo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sethcohen.muchotrabajo.Interface.JobADClickListener;
import com.example.sethcohen.muchotrabajo.Model.Item;
import com.example.sethcohen.muchotrabajo.PopupActivity;
import com.example.sethcohen.muchotrabajo.R;

import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyVHRecView> {

    private Context context;
    private List<Item> listItem;

    public MyRecyclerViewAdapter(Context context, List<Item> listItem){
        this.context = context;
        this.listItem = listItem;
    }


    @Override
    public MyVHRecView onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row;

        row = inflater.inflate(R.layout.row, parent, false);
        MyVHRecView vh = new MyVHRecView(row);

        return vh;
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @Override
    public void onBindViewHolder(MyVHRecView holder, int pos) {

        final Item item = listItem.get(pos);

        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDescription());
        holder.location.setText(item.getLocation());
        holder.company.setText(item.getCompanyName());

        if (item.getDescription().length() < 40) {
            holder.desc.setText(item.getDescription());
        } else {
            holder.desc.setText(item.getDescription().substring(0, 40));
        }


        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                context.startActivity(new Intent(context, PopupActivity.class));
            }
        });



        holder.setJobADClickListener(new JobADClickListener() {
            @Override
            public void onJobADClick(View view, int position) {
                
            }
        });
    }

    class MyVHRecView extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View row;
        private TextView title, desc, location, company;
        private JobADClickListener jobADClickListener;
        private Button popup;

        public MyVHRecView(View row) {
            super(row);
            this.row = row;
            title = (TextView) row.findViewById(R.id.tv_title_row);
            desc = (TextView) row.findViewById(R.id.tv_desc_row);
            location = (TextView) row.findViewById(R.id.tv_location_row);
            company = (TextView) row.findViewById(R.id.tv_company_row);
            popup = (Button) row.findViewById(R.id.btn_viewPosition_row);
        }

        @Override
        public void onClick(View v) {
            this.jobADClickListener.onJobADClick(v, getLayoutPosition());
        }

        public void setJobADClickListener(JobADClickListener listener){
            this.jobADClickListener = listener;
        }

    }
}
