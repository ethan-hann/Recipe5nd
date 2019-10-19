package com.uhcl.recipe5nd.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapater class for the RecyclerView of the SearchResultsFragment
 * This class handles displaying and updating CardViews in the RecyclerView
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>
{
    private ArrayList<Recipe> returnedRecipes;
    private ImageView cardImage;
    private TextView cardText;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        if (recipes == null) {
            returnedRecipes = new ArrayList<>();
        }
        this.returnedRecipes = recipes;
    }

    private void setImage(Drawable drawable) {
        cardImage.setImageDrawable(drawable);
    }

    public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

        private static final String TAG = "DownloadImage";
        @Override
        protected Drawable doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            setImage(drawable);
        }

        private Drawable downloadImage(String url) {
            URL imageURL;
            InputStream in;
            BufferedInputStream bufferedInputStream;

            try {
                imageURL = new URL(url);
                in = imageURL.openStream();
                bufferedInputStream = new BufferedInputStream(in);

                Bitmap bMap = BitmapFactory.decodeStream(bufferedInputStream);
                in.close();
                bufferedInputStream.close();

                return new BitmapDrawable(bMap);
            } catch (IOException e) {
                Log.e(TAG, "Error downloading image file:", e);
            }
            return null;
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.returned_recipe_card, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        new DownloadImage().execute(returnedRecipes.get(position).getStrMealThumb());
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (returnedRecipes == null)
        {
            return 0;
        }
        return returnedRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        MaterialCardView cardView;

        ViewHolder(View view)
        {
            super(view);
            cardView = view.findViewById(R.id.search_results_cardView);
            view.setOnClickListener(this);
        }

        void bind(int pos) {
            cardImage = cardView.findViewById(R.id.search_results_card_image);
            cardText = cardView.findViewById(R.id.search_results_card_text);

            cardText.setText(returnedRecipes.get(pos).getStrMeal());
        }

        @Override
        public void onClick(View view) {
            //TODO: Implement recipe details
        }
    }
}
