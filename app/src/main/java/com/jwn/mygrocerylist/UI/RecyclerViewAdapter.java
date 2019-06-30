package com.jwn.mygrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jwn.mygrocerylist.Activities.DetailsActivity;
import com.jwn.mygrocerylist.Data.DatabaseHandler;
import com.jwn.mygrocerylist.Model.Grocery;
import com.jwn.mygrocerylist.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<Grocery>groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;


    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button edidtButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);

            context = ctx;

            groceryItemName = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            dateAdded = view.findViewById(R.id.dateAdded);

            edidtButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);

            edidtButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to next screen / DetailsActivity
                    int position = getAdapterPosition();

                    Grocery grocery = groceryItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateItemAdded());
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.editButton:

                    break;
                case R.id.deleteButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());
                    break;
            }

        }

        public void deleteItem(final int id) {
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete item
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });

        }
    }
}// end RecyclerViewAdapter