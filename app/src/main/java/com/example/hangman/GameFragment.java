package com.example.hangman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
        displayRandomWord();
        viewModel.setListOfLetters();
        viewModel.createLetterFrequency();
        createLetters();
        setOnLetterClickListener();

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
                                case 0: // if he's male
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
                                case 1: // if she's female
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
                                default:
                                    imageToBeDisplayed = R.drawable.ic_hangman_0;
                                    break;
                            }

                            setImage(imageToBeDisplayed);

                            Toast.makeText(requireActivity(), "Remaining attempts: " + (viewModel.getMaxNumberOfMisses() - viewModel.getPlayerNumberOfMisses()), Toast.LENGTH_SHORT).show();
                        }

                        if(viewModel.checkIfTheGameIsOver())
                        {
                            if(viewModel.getPlayerNumberOfMisses() != viewModel.getMaxNumberOfMisses())
                            {
                                viewModel.setGameWon(true);
                                Toast.makeText(requireActivity(), "YOU WON!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                viewModel.setGameWon(false);
                                Toast.makeText(requireActivity(), "GAME OVER!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else if(viewModel.isGameWon())
                        Toast.makeText(requireActivity(), "YOU WON!", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(requireActivity(), "GAME OVER!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void displayRandomWord()
    {
        String guessedLetters = viewModel.getCurrentWordGuessedLetters();

        gameWord.setText(guessedLetters);
    }
}