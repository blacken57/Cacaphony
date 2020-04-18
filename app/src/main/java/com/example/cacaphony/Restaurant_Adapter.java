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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.api.Context;

public class Restaurant_Adapter extends  RecyclerView.Adapter<Restaurant_Adapter.OrderViewHolder> {
    private static final String TAG = "ADAPTER CONSTRUCTOR";
    private Context context;
    DriverOrders driverOrders;
    List<String> IDs;
    private List<Restros> RestroList ;
    FirebaseFirestore fStore;
    FirebaseAuth mFAuth;
    String UserID,name,phone;




    public Restaurant_Adapter(Context context, List<Restros> RestroList) {
        this.context = context;
        this.RestroList = RestroList;
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
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_aurant, null, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        Restros restros = RestroList.get(position);
        holder.textViewRest.setText(restros.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("RestroID", RestroList.get(position).getID());
                user.put("Restaurant",RestroList.get(position).getName());
                user.put("UserID",UserID);
                user.put("UserName",name);
                user.put("UserPhone",phone);
                user.put("Assigned",false);
                user.put("Status",1);
                documentReference.set(user);
                Toast.makeText(v.getContext(), RestroList.get(position).getName() + " " + "Selected!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return RestroList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView textViewRest;/*, textViewPrice*/;
        /*        Button decline, accept;*/


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRest = itemView.findViewById(R.id.Minion);
        }
    }
}
