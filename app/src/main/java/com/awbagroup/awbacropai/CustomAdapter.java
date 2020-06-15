package com.awbagroup.awbacropai;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

public class CustomAdapter extends ArrayAdapter<DBModel> implements View.OnClickListener{

    private List<DBModel> dbModels;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView row_item_time, row_item_ai;
        ImageView row_item_img;
        LinearLayout layout;
    }

    public CustomAdapter(List<DBModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dbModels = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DBModel dbModel=(DBModel) object;

        switch (v.getId())
        {
            case R.id.row_item_img:
                // action on click image
                break;
            case R.id.row_item_time:
                // action on click time
                break;
            case R.id.layout:
                // action on click linear layout
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DBModel dbModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.row_item_img = convertView.findViewById(R.id.row_item_img);
            viewHolder.layout = convertView.findViewById(R.id.layout);
            viewHolder.row_item_time = convertView.findViewById(R.id.row_item_time);
            viewHolder.row_item_ai = convertView.findViewById(R.id.row_item_ai);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.row_item_time.setText(dbModel.getTime());
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("posts", 0);
        String str_photo = sharedPreferences.getString(dbModel.getTime()+"photo", "");
        String str_ai = sharedPreferences.getString(dbModel.getTime()+"ai", "");
        Bitmap bitmap = decodeBase64(str_photo);
        viewHolder.row_item_img.setImageBitmap(bitmap);
        viewHolder.row_item_ai.setText(str_ai);
        // Return the completed view to render on screen
        return convertView;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
