package com.example.hangman.View.Fragment;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hangman.R;
import com.example.hangman.Room.Word;
import com.example.hangman.View.Activity.MainActivity;
import com.example.hangman.ViewModel.MainActivityViewModel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameFragment extends Fragment
{
    private MainActivityViewModel viewModel;

    private ImageView gameImage;
    private GridLayout lettersLayout;
    private TextView gameWord;

    public GameFragment()
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
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        setVariables(v);
        generateRandomWord();

        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setImage(viewModel.getCurrentImage());
    }

    private void setVariables(View v)
    {
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        gameImage = v.findViewById(R.id.gameImage);
        lettersLayout = v.findViewById(R.id.gameLetters);
        gameWord = v.findViewById(R.id.gameWord);
    }

    private void setImage(int image)
    {
        gameImage.setImageResource(image);

        if(image != viewModel.getCurrentImage())
            viewModel.setCurrentImage(image);
    }

    private void createLetters()
    {
        char[] englishAlphabetLetters = viewModel.getEnglishAlphabetLetters();

        for(char letter : englishAlphabetLetters)
        {
            Button letterButton = new Button(requireActivity());
            GridLayout.LayoutParams buttonParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f), GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
            buttonParams.height = 0;
            buttonParams.width = 0;
            letterButton.setText(String.valueOf(letter));
            letterButton.setLayoutParams(buttonParams);
            lettersLayout.addView(letterButton);
        }
    }

    private void setOnLetterClickListener()
    {
        int numberOfLetters = lettersLayout.getChildCount();

        for(int counter = 0; counter < numberOfLetters; ++counter)
        {
            final Button letter = (Button) lettersLayout.getChildAt(counter);

            letter.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    char character = letter.getText().charAt(0); // saving the character displayed on the pressed button
                    int imageToBeDisplayed;

                    if(!viewModel.checkIfTheGameIsOver()) // if the game isn't over yet
                    {
                        if(viewModel.checkIfTheInputLetterIsContainedIntoTheCurrentWord(character) && viewModel.checkIfTheInputLetterCanBeInserted(character)) // if the character is contained into the word and can be inserted
                        {
                            viewModel.setPlacesWhereTheLetterCanBeInserted(character);
                            viewModel.insertLetter(character);
                            displayRandomWord();
                        }
                        else // if the character isn't contained into the word or/and can't be inserted into it
                        {
                            viewModel.incrementPlayerMisses();

                            switch(viewModel.getPlayerGender()) // checking player's gender
                            {
                                case 0: // if she's female
                                {
                                    switch(viewModel.getPlayerNumberOfMisses())
                                    {
                                        case 1:
                                            imageToBeDisplayed = R.drawable.ic_hangman_1;
                                            break;
                                        case 2:
                                            imageToBeDisplayed = R.drawable.ic_hangman_2_woman;
                                            break;
                                        case 3:
                                            imageToBeDisplayed = R.drawable.ic_hangman_3_woman;
                                            break;
                                        case 4:
                                            imageToBeDisplayed = R.drawable.ic_hangman_4_woman;
                                            break;
                                        case 5:
                                            imageToBeDisplayed = R.drawable.ic_hangman_5_woman;
                                            break;
                                        default:
                                            imageToBeDisplayed = R.drawable.ic_hangman_6_woman;
                                            break;
                                    }
                                    break;
                                }
                                case 1: // if he's male
                                {
                                    switch(viewModel.getPlayerNumberOfMisses())
                                    {
                                        case 1:
                                            imageToBeDisplayed = R.drawable.ic_hangman_1;
                                            break;
                                        case 2:
                                            imageToBeDisplayed = R.drawable.ic_hangman_2_man;
                                            break;
                                        case 3:
                                            imageToBeDisplayed = R.drawable.ic_hangman_3_man;
                                            break;
                                        case 4:
                                            imageToBeDisplayed = R.drawable.ic_hangman_4_man;
                                            break;
                                        case 5:
                                            imageToBeDisplayed = R.drawable.ic_hangman_5_man;
                                            break;
                                        default:
                                            imageToBeDisplayed = R.drawable.ic_hangman_6_man;
                                            break;
                                    }
                                    break;
                                }
                                default:
                                    imageToBeDisplayed = R.drawable.ic_hangman_0;
                                    break;
                            }

                            setImage(imageToBeDisplayed);
                        }

                        if(viewModel.checkIfTheGameIsOver())
                        {
                            if(viewModel.getPlayerNumberOfMisses() != viewModel.getMaxNumberOfMisses())
                                viewModel.setGameWon(true);
                            else viewModel.setGameWon(false);

                            new CountDownTimer(1500,1000)
                            {
                                @Override
                                public void onTick(long millisUntilFinished)
                                {

                                }

                                @Override
                                public void onFinish()
                                {
                                    ((MainActivity)requireActivity()).setFragment(new FinishFragment());
                                }
                            }.start();
                        }
                    }
                }
            });
        }
    }

    private void displayRandomWord()
    {
        String guessedLetters = viewModel.getCurrentWordGuessedLetters();

        gameWord.setText(guessedLetters);
    }

    public void generateRandomWord()
    {
        if(viewModel.getPlayerDifficulty() == 0) // if the selected difficulty is easy
        {
            viewModel.getAllEasyWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
                    Collections.shuffle(words);

                    int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(words.size());
                    String randomWord = words.get(randomGeneratedNumber).getName();

                    viewModel.setCurrentWord(randomWord);
                    viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                    displayRandomWord();
                    viewModel.setListOfLetters();
                    viewModel.createLetterFrequency();
                    createLetters();
                    setOnLetterClickListener();
                }
            });
        }
        else if(viewModel.getPlayerDifficulty() == 1) // if the selected difficulty is medium
        {
            viewModel.getAllMediumWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
                    Collections.shuffle(words);

                    int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(0, words.size());
                    String randomWord = words.get(randomGeneratedNumber).getName();

                    viewModel.setCurrentWord(randomWord);
                    viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                    displayRandomWord();
                    viewModel.setListOfLetters();
                    viewModel.createLetterFrequency();
                    createLetters();
                    setOnLetterClickListener();
                }
            });
        }
        else if(viewModel.getPlayerDifficulty() == 2) // if the selected difficulty is hard
        {
            viewModel.getAllHardWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
                    Collections.shuffle(words);

                    int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(0, words.size());
                    String randomWord = words.get(randomGeneratedNumber).getName();

                    viewModel.setCurrentWord(randomWord);
                    viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
                    displayRandomWord();
                    viewModel.setListOfLetters();
                    viewModel.createLetterFrequency();
                    createLetters();
                    setOnLetterClickListener();
                }
            });
        }
    }
}