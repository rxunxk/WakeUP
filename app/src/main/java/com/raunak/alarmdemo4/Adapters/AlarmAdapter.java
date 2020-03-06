package com.raunak.alarmdemo4.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raunak.alarmdemo4.Interfaces.AlarmRecyclerViewListener;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;
import static com.raunak.alarmdemo4.R.layout.alarm_profile;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmView> {
    //Variables for the main recycler view
    private ArrayList<String> hoursArrayList;
    private ArrayList<String> minArrayList;
    private ArrayList<String> modeArrayList;
    private ArrayList<String> repeatArrayList;
    private ArrayList<String> nameArrayList;
    private ArrayList<String> statusArrayList;
    private AlarmRecyclerViewListener mInterface;
    private int easy = Color.parseColor("#ab72c0");
    private int regular = Color.parseColor("#dc5353");
    private int difficult = Color.parseColor("#617be3");

    public AlarmAdapter(ArrayList<String> hours, ArrayList<String> mins, ArrayList<String> mode, ArrayList<String> repeat, ArrayList<String> name,ArrayList<String> status, AlarmRecyclerViewListener mInterface){
        this.hoursArrayList = hours;
        this.minArrayList = mins;
        this.modeArrayList = mode;
        this.nameArrayList = name;
        this.repeatArrayList = repeat;
        this.statusArrayList = status;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public AlarmView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(alarm_profile,parent,false);
        return new AlarmView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmView holder, int position) {
        //Adding 0 for numbers less than 10.
        if(Integer.parseInt(hoursArrayList.get(position)) < 10 ){
            holder.hours.setText("0"+hoursArrayList.get(position));
        }else {
            holder.hours.setText(hoursArrayList.get(position));
        }
        //Logic for displaying the timezone
        if (Integer.parseInt(hoursArrayList.get(position)) > 12){
            if (Integer.parseInt(hoursArrayList.get(position)) > 20){
                holder.hours.setText(""+(Integer.parseInt(hoursArrayList.get(position)) - 12));
            }else{
                holder.hours.setText("0"+(Integer.parseInt(hoursArrayList.get(position)) - 12));
            }

            holder.timeZone.setText("PM");
        }else{
            holder.timeZone.setText("AM");
        }

        //Adding 0 for numbers less than 0 this time for minutes.
        if (Integer.parseInt(minArrayList.get(position)) < 10){
            holder.mins.setText("0"+minArrayList.get(position));
        }else {
            holder.mins.setText(minArrayList.get(position));
        }

        holder.repeat.setText(repeatArrayList.get(position));
        holder.mode.setText(modeArrayList.get(position));
        holder.name.setText(nameArrayList.get(position));

        //Logic for coloring the mode for mart alarm mode.
        if(modeArrayList.get(position).equals("E")){
            holder.mode.setTextColor(easy);
        }else if(modeArrayList.get(position).equals("R")){
            holder.mode.setTextColor(regular);
        }else{
            holder.mode.setTextColor(difficult);
        }
        if(statusArrayList.get(position).equals("ON")){
            holder.mSwitch.setChecked(true);
        }else{
            holder.mSwitch.setChecked(false);
        }

    }
    @Override
    public int getItemCount() {
        return hoursArrayList.size();
    }

    public class AlarmView extends RecyclerView.ViewHolder{
        TextView hours,mins,repeat,name,mode,timeZone;
        Switch mSwitch;
        public AlarmView(@NonNull View itemView) {
            super(itemView);
            hours = itemView.findViewById(R.id.txtHOUR);
            mins = itemView.findViewById(R.id.txtMins);
            repeat = itemView.findViewById(R.id.txtRepeatDays);
            name = itemView.findViewById(R.id.txtName);
            mode = itemView.findViewById(R.id.txtMode);
            timeZone = itemView.findViewById(R.id.txtTimeZone);
            mSwitch =itemView.findViewById(R.id.onOff);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInterface.onItemClick(getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mInterface.onLongItemClick(getAdapterPosition());
                      return true;
                }
            });

            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mInterface.onSwitchClicked(b,getAdapterPosition());
                }
            });

            mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInterface.onModeClicked(getAdapterPosition(),modeArrayList.get(getAdapterPosition()));
                }
            });
        }
    }
}
