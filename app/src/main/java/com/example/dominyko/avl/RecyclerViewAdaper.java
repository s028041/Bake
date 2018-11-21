package com.example.dominyko.avl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dominyko.avl.Models.GPS.AVL.AVLRecord;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdaper extends RecyclerView.Adapter<RecyclerViewAdaper.ViewHolder>{

    private static final String TAG = "RecyclerViewAdaper";

    private Context mContext;
    private List<AVLRecord> mRecords;
    private LayoutInflater mLayoutInflater;
    private LocationListRecyclerClickListener mClickListener;

    public RecyclerViewAdaper(Context context, List<AVLRecord> mRecords, LocationListRecyclerClickListener clickListener) {
        mClickListener = clickListener;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.mRecords = mRecords;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view, mClickListener); // pas mClickListener interface to viewholder adapter
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called.");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String dateInString = simpleDateFormat.format(mRecords.get(position).getRecordHeader().getTimestamp());
        holder.textTimestamp.setText(String.valueOf("Timestamp:"));
        holder.timestamp.setText(dateInString);

        holder.textPriority.setText(String.valueOf("Priority:"));
        holder.priority.setText(String.valueOf(mRecords.get(position).getRecordHeader().getRecordPriority()));

        holder.textLongitude.setText(String.valueOf("Longitude:"));
        holder.longitude.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getLongitude()));

        holder.textLatitude.setText(String.valueOf("Latidude:"));
        holder.latitude.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getLatitude()));

        holder.textAltitude.setText(String.valueOf("Altitude:"));
        holder.altitude.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getAltitude()));

        holder.textAngle.setText(String.valueOf("Angle:"));
        holder.angle.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getAngle()));

        holder.textSatellites.setText(String.valueOf("Satellites:"));
        holder.satellites.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getSatellites()));

        holder.textKmh.setText(String.valueOf("Kmh:"));
        holder.kmh.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getKmh()));

        holder.textEventID.setText(String.valueOf("EventID:"));
        holder.eventid.setText(String.valueOf(mRecords.get(position).getRecordIO_elements().getEventID()));

        holder.textElementCount.setText(String.valueOf("Element Count:"));
        holder.elemCount.setText(String.valueOf(mRecords.get(position).getRecordIO_elements().getElementCount()));

        //TODO Pirmas bandymas X

//        Intent intent = new Intent(mContext, MapsActivity.class);
//        String latitude = intent.getExtras().getString("latitude");
//        intent.putExtra("Latitude",latitude);
//        //holder.latitude.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getLatitude()));
//
//
//        String longitude = intent.getExtras().getString("longitude");
//        intent.putExtra("Longitude",longitude);
        //holder.longitude.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getLongitude()));

//        Intent intent;
//    intent.putExtra("lat","fff");
//    String lat = intent.getExtras().getString("lat");
//       holder.latitude.setText(String.valueOf(mRecords.get(position).getRecordGPS_elements().getLatitude()));


    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LocationListRecyclerClickListener mClickListener;

        TextView timestamp;
        TextView priority;
        TextView longitude;
        TextView latitude;
        TextView altitude;
        TextView angle;
        TextView satellites;
        TextView kmh;
        TextView eventid;
        TextView elemCount;
        TextView textPriority;
        TextView textTimestamp;
        TextView textLongitude;
        TextView textLatitude;
        TextView textAltitude;
        TextView textAngle;
        TextView textSatellites;
        TextView textKmh;
        TextView textEventID;
        TextView textElementCount;




        public ViewHolder(View itemView, LocationListRecyclerClickListener clickListener) {
            super(itemView);


            textTimestamp = itemView.findViewById(R.id.tvTextTimestamp);
            timestamp = itemView.findViewById(R.id.tvTimestamp);

            textPriority = itemView.findViewById(R.id.tvTextPriority);
            priority = itemView.findViewById(R.id.tvPriority);

            textLongitude = itemView.findViewById(R.id.tvTextLongitude);
            longitude = itemView.findViewById(R.id.tvLongitude);

            textLatitude = itemView.findViewById(R.id.tvTextLatitude);
            latitude = itemView.findViewById(R.id.tvLatitude);

            mClickListener = clickListener;
            itemView.setOnClickListener(this);

            textAltitude = itemView.findViewById(R.id.tvTextAltitude);
            altitude = itemView.findViewById(R.id.tvAltitude);

            textAngle = itemView.findViewById(R.id.tvTextAngle);
            angle = itemView.findViewById(R.id.tvAngle);

            textSatellites = itemView.findViewById(R.id.tvTextSatellites);
            satellites = itemView.findViewById(R.id.tvSatellites);

            textKmh = itemView.findViewById(R.id.tvTextKmh);
            kmh = itemView.findViewById(R.id.tvKmh);

            textEventID = itemView.findViewById(R.id.tvTextEventID);
            eventid = itemView.findViewById(R.id.tvEventID);

            textElementCount = itemView.findViewById(R.id.tvTextElementCount);
            elemCount = itemView.findViewById(R.id.tvElementCount);



        }

        @Override
        public void onClick(View v) {
            mClickListener.onLocationClicked(getAdapterPosition());

        }

    }
    public interface LocationListRecyclerClickListener {
        void onLocationClicked(int position);
    }
}
