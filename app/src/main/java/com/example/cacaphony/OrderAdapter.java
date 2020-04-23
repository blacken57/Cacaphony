package com.example.cacaphony;

import android.content.Context;
import android.content.Intent;
import android.icu.text.BreakIterator;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.StructuredQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//import com.google.api.Context;

public class OrderAdapter extends  RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
   private Context context;
    DriverOrders driverOrders;
    private List<Orders> ordersList ;
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String Email, phoneNumber;
    String OTP;
    Orders od;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    //Button accept;

    public OrderAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }


    public static int generateRandomDigits() {
        int m = (int) Math.pow(10, 3);
        return m + new Random().nextInt(9 * m);
    }

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, null, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        Orders orders = ordersList.get(position);
        holder.textViewRest.setText(orders.getRest());
        holder.textViewuname.setText(orders.getuName());
        holder.textViewPhone.setText(orders.getuPhone());
        holder.textViewPrice.setText(orders.getAmount()+" ");


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                mFAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                final String userId = mFAuth.getUid();
                od = ordersList.get(position);
                DocumentReference documentReference = fStore.collection("Orders").document(od.getId());
                Map<String,Object> user = new HashMap<>();
                user.put("DeliveryId",userId);
                user.put("Assigned",true);
                documentReference.set(user, SetOptions.merge());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                String Uid = document.getString("UserID");
                                DocumentReference doc = fStore.collection("Customers").document(Uid);
                                doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot User = task.getResult();
                                            if(User.exists()){
                                                Email = User.getString("email");
                                                phoneNumber = User.getString("UserPhone");
                                                OTP = String.valueOf(generateRandomDigits());

                                                try{
                                                    SendingSMS.sendSms(OTP, phoneNumber);
                                                    enableStrictMode();
                                                    Mailer.send("obliviousalwaysforever@gmail.com","wha1sup??",Email,"Order OTP",OTP+" Is yout OTP. Do you understand?");
                                                    Toast.makeText(v.getContext(),  " Selected! Now, go back", Toast.LENGTH_SHORT).show();
                                                    v.getContext().startActivity(new Intent(context,DriverHomePage.class));
                                                }
                                                catch(Exception e){
                                                    System.out.println(e.toString()+ "  Hallelujah");
                                                }
                                                DocumentReference documentReference = fStore.collection("Orders").document(od.getId());
                                                Map<String,Object> users = new HashMap<>();
                                                users.put("OTP",OTP);
                                                users.put("Status",2);
                                                documentReference.set(users, SetOptions.merge());

                                            }
                                        }
                                    }
                                });


                            } else {

                            }
                        } else {

                        }
                    }
                });


            }
        });
   /*     holder.textViewPrice.setText((int) orders.getAmount());*/
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView textViewRest, textViewuname, textViewPhone, textViewPrice;
        Button decline, accept;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRest = itemView.findViewById(R.id.textViewRest);
            textViewuname = itemView.findViewById(R.id.textViewuname);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
//            decline = itemView.findViewById(R.id.decline);
            accept = itemView.findViewById(R.id.Accept);
/*            textViewPrice = itemView.findViewById(R.id.textViewPrice);*/
        }
    }
}
