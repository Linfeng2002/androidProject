package com.example.myapplication.Fragment.journey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.ArticleAdapter;
import com.example.myapplication.databinding.FragmentJourneyBinding;
import com.example.myapplication.entity.Article;

import java.util.List;

public class JourneyFragment extends Fragment {

    private FragmentJourneyBinding binding;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        JourneyViewModel journeyViewModel= new ViewModelProvider(this).get(JourneyViewModel.class);

        binding=FragmentJourneyBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        List<Article> articleList = Article.getDefault();
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), articleList);
        binding.journeyRecommend.setAdapter(articleAdapter);



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}