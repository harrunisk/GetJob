package com.example.harun.getjob.JobSearch.JobUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harun.getjob.R;

public class SimpleAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isGrid;

    public SimpleAdapter(Context context, boolean isGrid) {
        layoutInflater = LayoutInflater.from(context);
        this.isGrid = isGrid;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;


        if (view == null) {
            if (isGrid) {

            } else {
                view = layoutInflater.inflate(R.layout.content2, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (position) {
            case 0:
                viewHolder.textView.setText("HAHHA");
                viewHolder.imageView.setImageResource(R.drawable.scientist);
                break;
            case 1:
                viewHolder.textView.setText("HAHHA");
                viewHolder.imageView.setImageResource(R.drawable.scientist);
                break;
            default:
                viewHolder.textView.setText("HAHHA");
                viewHolder.imageView.setImageResource(R.drawable.scientist);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}