package com.example.hangman.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hangman.R;
import com.example.hangman.view.activity.MainActivity;
import com.example.hangman.viewmodel.MainActivityViewModel;

public class FinishFragment extends Fragment {
    private MainActivityViewModel viewModel;
    private TextView finishText;
    private TextView remainingGuesses;
    private TextView difficulty;
    private TextView word;
    private Button playAgainButton;
    private Button restartButton;

    public FinishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_finish, container, false);

        setVariables(v);
        setOnClickListeners();
        setTexts();

        return v;
    }

    private void setVariables(final View v) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        finishText = v.findViewById(R.id.finishText);
        remainingGuesses = v.findViewById(R.id.finishDetailsGuesses);
        difficulty = v.findViewById(R.id.finishDetailsDifficulty);
        word = v.findViewById(R.id.finishDetailsWord);
        playAgainButton = v.findViewById(R.id.finishPlayAgainButton);
        restartButton = v.findViewById(R.id.finishRestartButton);
    }

    private void setOnClickListeners() {
        playAgainButton.setOnClickListener(v -> {
            viewModel.setCurrentImage(R.drawable.ic_hangman_0);
            viewModel.resetPlayerNumberOfMisses();
            viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
            viewModel.setGameWon(false);
            ((MainActivity) requireActivity()).setFragment(new GameFragment());
        });

        restartButton.setOnClickListener(v -> {
            viewModel.setCurrentImage(R.drawable.ic_hangman_0);
            viewModel.resetPlayerNumberOfMisses();
            viewModel.setGameWon(false);
            ((MainActivity) requireActivity()).setFragment(new RegisterFragment());
        });
    }

    private void setTexts() {
        String message;
        String remainingGuessesText;
        String difficultyText;
        final String wordText = getResources().getString(R.string.word) + ": " + viewModel.getCurrentWord();

        message = getResources().getString(viewModel.isGameWon() ?
                R.string.congratulations : R.string.bad_news) + " " + viewModel.getPlayerName() + "!" +
                getResources().getString(viewModel.isGameWon() ?
                        R.string.game_won : R.string.game_won);

        remainingGuessesText = getResources().getString(R.string.remaining_guesses) + ": " +
                (viewModel.getMaxNumberOfMisses() - viewModel.getPlayerNumberOfMisses());
        difficultyText = getResources().getString(R.string.difficulty) + ": ";

        difficultyText = difficultyText.concat(viewModel.getPlayerDifficulty() == 0 ?
                getResources().getString(R.string.easy) : viewModel.getPlayerDifficulty() == 1 ?
                getResources().getString(R.string.medium) : getResources().getString(R.string.hard));

        word.setText(wordText);

        finishText.setText(message);
        remainingGuesses.setText(remainingGuessesText);
        difficulty.setText(difficultyText);
    }
}