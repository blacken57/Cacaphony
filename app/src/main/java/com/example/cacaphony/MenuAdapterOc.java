package com.example.cacaphony;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.api.Context;

public class MenuAdapterOc extends  RecyclerView.Adapter<MenuAdapterOc.MenuViewHolder> {
    private static final String TAG ="MENU ADAPTER" ;
    private Context context;
    private List<MenuObjectOc> MenuList ;
    FirebaseFirestore fStore;
    FirebaseAuth mFAuth;
    String UserID,name,phone;

    //FirebaseUser User;

    public MenuAdapterOc(Context context, List<MenuObjectOc> ordersList) {
        this.context = context;
        this.MenuList = ordersList;
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = mFAuth.getUid();
        DocumentReference documentReference = fStore.collection("Customers").document(UserID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getString("fName");
                        phone = document.getString("Phone number");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_menu_oc, null, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, final int position) {
        MenuObjectOc item = MenuList.get(position);
        int[] arr = item.getPrice();
        holder.textViewName.setText(item.getName());
        holder.priceOne.setText(arr[0]+" ");
        holder.priceOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("MenuItem",MenuList.get(position).getName());
                int[] arr = MenuList.get(position).getPrice();

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                String[] days = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
                String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                String time;
                if(min<=9){
                time = hour + ":0" + min + ", " + day + ", " + date + ".";
                    user.put("Time", time);}
                else{
                    time = hour + ":" + min + ", " + day + ", " + date + ".";
                    user.put("Time", time);
                }
                DocumentReference doc = fStore.collection("Customers").document(UserID);
                Map<String,Object> us = new HashMap<>();
                us.put("Time",time);
                doc.set(us,SetOptions.merge());

                user.put("Price",arr[0]);
                documentReference.set(user, SetOptions.merge());


                Toast.makeText(v.getContext(), MenuList.get(position).getName() + " " +arr[0]+ " Selected!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return MenuList.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, priceOne;


        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item12);
            priceOne = itemView.findViewById(R.id.price1);
        }
    }
}