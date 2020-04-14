package com.example.cacaphony;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.api.Context;

public class MenuAdapter extends  RecyclerView.Adapter<MenuAdapter.OrderViewHolder> {
    private static final String TAG ="MENU ADAPTER" ;
    private Context context;
    DriverOrders driverOrders;
    private List<MenuObject> MenuList ;
    FirebaseFirestore fStore;
    FirebaseAuth mFAuth;
    String UserID,name,phone;

    public MenuAdapter(Context context, List<MenuObject> ordersList) {
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
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_menu, null, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        MenuObject items = MenuList.get(position);
        int[] arr = items.getPrice();
        holder.textViewName.setText(items.getName());
        holder.priceOne.setText(arr[0]+" ");
        holder.priceTwo.setText(arr[1]+" ");
        holder.priceThree.setText(arr[2]+" ");
        holder.priceFour.setText(arr[3]+" ");
        holder.priceOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("MenuItem",MenuList.get(position).getName());
                int[] arr = MenuList.get(position).getPrice();
                user.put("Price",arr[0]);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(v.getContext(), MenuList.get(position).getName() + " " +arr[0]+ " Selected!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.priceTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("MenuItem",MenuList.get(position).getName());
                int[] arr = MenuList.get(position).getPrice();
                user.put("Price",arr[1]);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(v.getContext(), MenuList.get(position).getName() + " " +arr[0]+ " Selected!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.priceThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("MenuItem",MenuList.get(position).getName());
                int[] arr = MenuList.get(position).getPrice();
                user.put("Price",arr[2]);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(v.getContext(), MenuList.get(position).getName() + " " +arr[0]+ " Selected!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.priceFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("MenuItem",MenuList.get(position).getName());
                int[] arr = MenuList.get(position).getPrice();
                user.put("Price",arr[3]);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(v.getContext(), MenuList.get(position).getName() + " " +arr[0]+ " Selected!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return MenuList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName, priceOne,priceTwo,priceThree,priceFour/*, textViewPrice*/;
        /*        Button decline, accept;*/


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.Item);
            priceOne = itemView.findViewById(R.id.price1);
            priceTwo = itemView.findViewById(R.id.price2);
            priceThree = itemView.findViewById(R.id.price3);
            priceFour = itemView.findViewById(R.id.price4);

        }
    }
}
