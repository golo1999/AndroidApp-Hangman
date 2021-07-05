package com.example.hangman.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.hangman.R;
import com.example.hangman.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariables();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFragment(viewModel.getCurrentFragment());
    }

    public void setFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityFragment, fragment).commit();
        viewModel.setCurrentFragment(fragment);
    }

    public void setVariables() {
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }
}