package com.example.cacaphony;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//import com.google.api.Context;

public class OrderAdapter extends  RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
   private Context context;
    DriverOrders driverOrders;
    private List<Orders> ordersList ;

    public OrderAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
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
       /* holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dec = driverOrders.fStore.collection("Orders")
                        .document(ordersList.get(position).getAssigned());
                dec.set(false);
                Toast.makeText(driverOrders.getBaseContext(), "Order Declined", Toast.LENGTH_SHORT).show();
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dec = driverOrders.fStore.collection("Orders")
                        .document(ordersList.get(position).getAssigned());
                dec.set(true);
                Toast.makeText(driverOrders.getBaseContext(), "Order Accepted", Toast.LENGTH_SHORT).show();
            }
        });*/
   /*     holder.textViewPrice.setText((int) orders.getAmount());*/
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView textViewRest, textViewuname, textViewPhone/*, textViewPrice*/;
/*        Button decline, accept;*/


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRest = itemView.findViewById(R.id.textViewRest);
            textViewuname = itemView.findViewById(R.id.textViewuname);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
/*            decline = itemView.findViewById(R.id.decline);
            accept = itemView.findViewById(R.id.Accept);*/
/*            textViewPrice = itemView.findViewById(R.id.textViewPrice);*/
        }
    }
}
