package com.akshay.newsgateway1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {



    private static final String TAG = "ArticleFragment";
    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters

    static ArticleFragment newInstance(Article country, int index, int max)
    {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle(1);
        args.putSerializable("ARTICLE_DATA", country);
        args.putSerializable("INDEX", index);
        args.putSerializable("TOTAL_COUNT", max);
        fragment.setArguments(args);
        return fragment;

    }
    private boolean checkIfNull(String data)
    {
        if(data == null || data.equals("null"))
            return true;
        else
            return false;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.fragment_article, container, false);
        Bundle args = getArguments();
        if (args != null)
        {
            final Article temp = (Article) args.getSerializable("ARTICLE_DATA");
            if (temp == null)
            {
                return null;
            }
            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");
            TextView title = fragmentLayout.findViewById(R.id.fragmentTitle);
            if(!checkIfNull(temp.getTitle()))
            {
                title.setText(temp.getTitle());
            }
            else
            {
                title.setText("No Title");
            }
            title.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(temp.getUrl() != null){
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(temp.getUrl()));
                        startActivity(i);
                    }
                }
            });
            if(!checkIfNull(temp.getAuthor()))
            {
                TextView author = fragmentLayout.findViewById(R.id.fragmentAuthor);
                author.setText(temp.getAuthor());
            }
            if(!checkIfNull(temp.getPublishedAt()))
            {
                TextView publishedAt = fragmentLayout.findViewById(R.id.fragmentPublished);
                publishedAt.setText(temp.getPublishedAt());
            }
            if(!checkIfNull(temp.getDescription()))
            {
                TextView desc = fragmentLayout.findViewById(R.id.fragmentDescription);
                desc.setText(temp.getDescription());
                desc.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(temp.getUrl() != null){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(temp.getUrl()));
                            startActivity(i);
                        }
                    }
                });
            }
            if(temp.getUrltoImage().equals(""))
            {
                ImageView picture = fragmentLayout.findViewById(R.id.image);
                picture.setBackgroundResource(R.drawable.placeholder);
                picture.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(temp.getUrl() != null){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(temp.getUrl()));
                            startActivity(i);
                        }
                    }
                });
            }
            else
            {
                ImageView picture = fragmentLayout.findViewById(R.id.image);
                Picasso.get().load(temp.getUrltoImage())
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(picture);
                picture.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(temp.getUrl() != null){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(temp.getUrl()));
                            startActivity(i);
                        }
                    }
                });
            }
            TextView pageNum = fragmentLayout.findViewById(R.id.fragmentPageNumber);
            pageNum.setText(String.format(Locale.US, "%d of %d", index, total));
            return fragmentLayout;
        }
        else
            return null;
    }
}