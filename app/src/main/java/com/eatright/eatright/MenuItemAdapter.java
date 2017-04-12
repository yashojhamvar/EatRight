package com.eatright.eatright;

/**
 * Created by Yasho on 4/8/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.DataObjectHolder> {
    public ArrayList<RestMenuItem> m_data;
    private static RestMenuItemClickListener clickListener;
    private static Context m_context;
    public static int color;
    private ImageLoader imgLoad;

    public MenuItemAdapter(Context con) {
        m_context = con;
        m_data = new ArrayList<RestMenuItem>();
        imgLoad = VolleySingleton.getInstance(con).getImageLoader();
    }

    public MenuItemAdapter(Context con, ArrayList<RestMenuItem> data) {
        m_context = con;
        m_data = data;
        imgLoad = VolleySingleton.getInstance(con).getImageLoader();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView cal;
        NetworkImageView img;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            cal = (TextView) itemView.findViewById(R.id.date);
            img   = (NetworkImageView) itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    // Sets the callback function to handle clicking on individual datum
    public void setOnItemClickListener(RestMenuItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        view.setBackgroundColor(Color.GREEN);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;


    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.title.setText(m_data.get(position).getDishName());
        holder.cal.setText("Total Calories: "+String.valueOf(m_data.get(position).getTotCal()));
        holder.img.setImageUrl(m_data.get(position).getImageUrl(), imgLoad);

    }

    public void addItem(RestMenuItem data, int index) {
        m_data.add(index, data);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        m_data.remove(index);
        notifyItemRemoved(index);
    }

    public void clear() {
        m_data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return m_data.size();
    }

    // Get a specific datum
    public RestMenuItem getItem(int position) {
        if (position > -1 && position < m_data.size())
            return m_data.get(position);
        else
            return null;
    }

    public interface RestMenuItemClickListener {
        public void onItemClick(int position, View v);
    }


}
