package com.jadd.easyrestro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jadd.easyrestro.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmployeeRecyclerViewAdapter extends RecyclerView.Adapter<EmployeeRecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<String> names;
    OnEmployeeNameClickListner onEmployeeNameClickListner;

    public EmployeeRecyclerViewAdapter(Context context, ArrayList<String> names, OnEmployeeNameClickListner onEmployeeNameClickListner) {
        this.context = context;
        this.names = names;
        this.onEmployeeNameClickListner = onEmployeeNameClickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_employee,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view,onEmployeeNameClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnEmployeeNameClickListner onEmployeeNameClickListner;
        TextView name;

        public MyViewHolder(@NonNull View itemView,OnEmployeeNameClickListner onEmployeeNameClickListner) {
            super(itemView);
            name = itemView.findViewById(R.id.employee_each_name);
            this.onEmployeeNameClickListner = onEmployeeNameClickListner;
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEmployeeNameClickListner.onEmployeeClick(getAdapterPosition());
        }
    }

    public interface OnEmployeeNameClickListner{
        void onEmployeeClick(int pos);
    }
}
