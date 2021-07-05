package com.example.hangman.view.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman.R;
import com.example.hangman.view.activity.MainActivity;
import com.example.hangman.viewmodel.MainActivityViewModel;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class GameFragment extends Fragment {
    private MainActivityViewModel viewModel;
    private ImageView gameImage;
    private GridLayout lettersLayout;
    private TextView gameWord;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_game, container, false);

        setVariables(v);
        generateRandomWord();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        setImage(viewModel.getCurrentImage());
    }

    private void setVariables(final View v) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        gameImage = v.findViewById(R.id.gameImage);
        lettersLayout = v.findViewById(R.id.gameLetters);
        gameWord = v.findViewById(R.id.gameWord);
    }

    private void setImage(final int image) {
        gameImage.setImageResource(image);

        if (image != viewModel.getCurrentImage()) {
            viewModel.setCurrentImage(image);
        }
    }

    private void createLetters() {
        final char[] englishAlphabetLetters = viewModel.getEnglishAlphabetLetters();

        for (final char letter : englishAlphabetLetters) {
            final Button letterButton = new Button(requireActivity());
            final GridLayout.LayoutParams buttonParams = new GridLayout
                    .LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));

            buttonParams.height = 0;
            buttonParams.width = 0;
            letterButton.setText(String.valueOf(letter));
            letterButton.setLayoutParams(buttonParams);
            lettersLayout.addView(letterButton);
        }
    }

    private void setOnLetterClickListener() {
        final int numberOfLetters = lettersLayout.getChildCount();

        for (int counter = 0; counter < numberOfLetters; ++counter) {
            final Button letter = (Button) lettersLayout.getChildAt(counter);

            letter.setOnClickListener(v -> {
                // saving the character displayed on the pressed button
                final char character = letter.getText().charAt(0);
                final int imageToBeDisplayed;

                // if the game isn't over yet
                if (!viewModel.checkIfTheGameIsOver()) {
                    // if the character is contained into the word and can be inserted
                    if (viewModel.checkIfTheInputLetterIsContainedIntoTheCurrentWord(character) &&
                            viewModel.checkIfTheInputLetterCanBeInserted(character)) {
                        viewModel.setPlacesWhereTheLetterCanBeInserted(character);
                        viewModel.insertLetter(character);
                        displayRandomWord();
                    }
                    // if the character isn't contained into the word or/and can't be inserted into it
                    else {
                        viewModel.incrementPlayerMisses();
                        // checking player's gender (0 -> female, 1 -> male, default -> other) and
                        // assigning the image to be displayed depending on it
                        if (viewModel.getPlayerGender() == 0) {
                            imageToBeDisplayed = viewModel.getPlayerNumberOfMisses() == 1 ?
                                    R.drawable.ic_hangman_1 : viewModel.getPlayerNumberOfMisses() == 2 ?
                                    R.drawable.ic_hangman_2_woman : viewModel.getPlayerNumberOfMisses() == 3 ?
                                    R.drawable.ic_hangman_3_woman : viewModel.getPlayerNumberOfMisses() == 4 ?
                                    R.drawable.ic_hangman_4_woman : viewModel.getPlayerNumberOfMisses() == 5 ?
                                    R.drawable.ic_hangman_5_woman : R.drawable.ic_hangman_6_woman;
                        } else if (viewModel.getPlayerGender() == 1) {
                            imageToBeDisplayed = viewModel.getPlayerNumberOfMisses() == 1 ?
                                    R.drawable.ic_hangman_1 : viewModel.getPlayerNumberOfMisses() == 2 ?
                                    R.drawable.ic_hangman_2_man : viewModel.getPlayerNumberOfMisses() == 3 ?
                                    R.drawable.ic_hangman_3_man : viewModel.getPlayerNumberOfMisses() == 4 ?
                                    R.drawable.ic_hangman_4_man : viewModel.getPlayerNumberOfMisses() == 5 ?
                                    R.drawable.ic_hangman_5_man : R.drawable.ic_hangman_6_man;
                        } else {
                            imageToBeDisplayed = R.drawable.ic_hangman_0;
                        }

                        setImage(imageToBeDisplayed);
                    }

                    if (viewModel.checkIfTheGameIsOver()) {
                        viewModel.setGameWon(viewModel.getPlayerNumberOfMisses() != viewModel.getMaxNumberOfMisses());

                        new CountDownTimer(1500, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                ((MainActivity) requireActivity()).setFragment(new FinishFragment());
                            }
                        }.start();
                    }
                }
            });
        }
    }

    private void displayRandomWord() {
        final String guessedLetters = viewModel.getCurrentWordGuessedLetters();

        gameWord.setText(guessedLetters);
    }

    public void generateRandomWord() {
        // if the selected difficulty is easy
        if (viewModel.getPlayerDifficulty() == 0) {
            viewModel.getAllEasyWords().observe(getViewLifecycleOwner(), words -> {
                Collections.shuffle(words);

                final int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(words.size());
                final String randomWord = words.get(randomGeneratedNumber).getName();

                viewModel.setCurrentWord(randomWord);
                viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                displayRandomWord();
                viewModel.setListOfLetters();
                viewModel.createLetterFrequency();
                createLetters();
                setOnLetterClickListener();
            });
        }
        // if the selected difficulty is medium
        else if (viewModel.getPlayerDifficulty() == 1) {
            viewModel.getAllMediumWords().observe(getViewLifecycleOwner(), words -> {
                Collections.shuffle(words);

                final int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(0, words.size());
                final String randomWord = words.get(randomGeneratedNumber).getName();

                viewModel.setCurrentWord(randomWord);
                viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                displayRandomWord();
                viewModel.setListOfLetters();
                viewModel.createLetterFrequency();
                createLetters();
                setOnLetterClickListener();
            });
        }
        // if the selected difficulty is hard
        else if (viewModel.getPlayerDifficulty() == 2) {
            viewModel.getAllHardWords().observe(getViewLifecycleOwner(), words -> {
                Collections.shuffle(words);

                final int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(0, words.size());
                final String randomWord = words.get(randomGeneratedNumber).getName();

                viewModel.setCurrentWord(randomWord);
                viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                displayRandomWord();
                viewModel.setListOfLetters();
                viewModel.createLetterFrequency();
                createLetters();
                setOnLetterClickListener();
            });
        }
    }
}