package com.example.hangman.View.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.hangman.R;
import com.example.hangman.ViewModel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity
{
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariables();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setFragment(viewModel.getCurrentFragment());
    }

    public void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, fragment).commit();
        viewModel.setCurrentFragment(fragment);
    }

    public void setVariables()
    {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }
}