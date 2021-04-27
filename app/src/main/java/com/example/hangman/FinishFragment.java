package com.example.hangman;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FinishFragment extends Fragment
{
    private MainActivityViewModel viewModel;
    private TextView finishText;
    private TextView remainingGuesses;
    private TextView difficulty;
    private Button playAgainButton;

    public FinishFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_finish, container, false);

        setVariables(v);
        setOnClickListeners();
        setTexts();

        return v;
    }

    private void setVariables(View v)
    {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        finishText = v.findViewById(R.id.finishText);
        remainingGuesses = v.findViewById(R.id.finishDetailsGuesses);
        difficulty = v.findViewById(R.id.finishDetailsDifficulty);
        playAgainButton = v.findViewById(R.id.finishPlayAgainButton);
    }

    private void setOnClickListeners()
    {
        playAgainButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewModel.setCurrentImage(R.drawable.ic_hangman_0);
                viewModel.resetPlayerNumberOfMisses();
                viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                viewModel.setGameWon(false);
                //viewModel.setCurrentWord(viewModel.generateRandomWord());
                ((MainActivity)requireActivity()).setFragment(new GameFragment());
            }
        });
    }

    private void setTexts()
    {
        String message, remainingGuessesText, difficultyText;

        if(viewModel.isGameWon())
            message = getResources().getString(R.string.congratulations) + " " + viewModel.getPlayerName() + "! " + getResources().getString(R.string.game_won);
        else message = getResources().getString(R.string.bad_news) + " " + viewModel.getPlayerName() + "! " + getResources().getString(R.string.game_lost);

        remainingGuessesText = "Remaining guesses: " + (viewModel.getMaxNumberOfMisses() - viewModel.getPlayerNumberOfMisses());
        difficultyText = "Difficulty: ";

        if(viewModel.getPlayerDifficulty() == 0)
            difficultyText = difficultyText.concat("Easy");
        else if(viewModel.getPlayerDifficulty() == 1)
            difficultyText = difficultyText.concat("Medium");
        else if(viewModel.getPlayerDifficulty() == 2)
            difficultyText = difficultyText.concat("Hard");

        finishText.setText(message);
        remainingGuesses.setText(remainingGuessesText);
        difficulty.setText(difficultyText);
    }
}