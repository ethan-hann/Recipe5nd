package com.uhcl.recipe5nd.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.ShoppingItemsAdapter;
import com.uhcl.recipe5nd.helperClasses.ShoppingList;

public class ShoppingItemsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "ShoppingItemsFragment: ";
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private ShoppingItemsAdapter shoppingItemsAdapter;
    private ShoppingList shoppingList;
    private Context context;

    public ShoppingItemsFragment(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        context = getContext();

        addButton = rootView.findViewById(R.id.shoppingAddButton);
        addButton.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.shoppingList_recycler_view);

        shoppingItemsAdapter = new ShoppingItemsAdapter(shoppingList);
        recyclerView.setAdapter(shoppingItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    private ItemTouchHelper.SimpleCallback swipe(){
        return new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT )
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                shoppingList.getItems().remove(position);
                shoppingList.getIsCheckedArray().put(position, false);
                shoppingItemsAdapter.notifyDataSetChanged();
            }
        };
    }


    @Override
    public void onClick(View view)
    {
        View dialogView =  LayoutInflater.from(getContext()).inflate(R.layout.shopping_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        AlertDialog alert = dialogBuilder.create();

        EditText dialogEditText = dialogView.findViewById(R.id.shoppingDialogEditText);
        dialogEditText.setVisibility(View.VISIBLE);

        TextView dialogTextView = dialogView.findViewById(R.id.shoppingDialogTextView);
        dialogTextView.setText(R.string.shopping_item_add);

        MaterialButton okButton = dialogView.findViewById(R.id.shoppingDialogOk);
        MaterialButton cancelButton = dialogView.findViewById(R.id.shoppingDialogCancel);

        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (dialogTextView.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Item cannot be empty.", Toast.LENGTH_SHORT).show();
                }else {
                    shoppingList.addItem(dialogEditText.getText().toString());
                }
                shoppingItemsAdapter.notifyDataSetChanged();

                alert.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }
}
