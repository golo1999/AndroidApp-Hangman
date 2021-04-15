package com.example.hangman;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameFragment extends Fragment
{
    private MainActivityViewModel viewModel;
    private ImageView gameImage;
    private GridLayout lettersLayout;
    private Button letterButton;
    private GridLayout.LayoutParams buttonParams;
    private TextView gameWord;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public GameFragment()
    {
        // Required empty public constructor
    }

    public static GameFragment newInstance(String param1, String param2)
    {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        setVariables(v);
        displayRandomWord();
        viewModel.setListOfLetters();
        viewModel.createLetterFrequency();

        System.out.println("list of letters length: " + viewModel.getListOfLetters().size());

        int[] frequency = viewModel.getCurrentWordLetterFrequency();

//        for(int c : frequency)
//            Toast.makeText(requireActivity(), String.valueOf(c), Toast.LENGTH_SHORT).show();

        //Toast.makeText(requireActivity(), viewModel.removeSpacesFromGuessedWord(), Toast.LENGTH_LONG).show();
        viewModel.setPlacesWhereTheLetterCanBeInserted('Z');
        viewModel.insertLetter('Z');

        viewModel.setPlacesWhereTheLetterCanBeInserted('I');
        viewModel.insertLetter('I');

        viewModel.setPlacesWhereTheLetterCanBeInserted('E');
        viewModel.insertLetter('E');

        viewModel.setPlacesWhereTheLetterCanBeInserted('N');
        viewModel.insertLetter('N');

//        for(char c : viewModel.getListOfLetters())
//            Toast.makeText(requireActivity(), String.valueOf(c), Toast.LENGTH_SHORT).show();
        createLetters();
        setOnLetterClickListener();


//        gameImage.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                viewModel.incrementPlayerMisses();
//                switch(viewModel.getPlayerNumberOfMisses())
//                {
//                    case 1:
//                        setGameImage(R.drawable.ic_hangman_1);
//                        break;
//                    case 2:
//                        setGameImage(R.drawable.ic_hangman_2_man);
//                        break;
//                    case 3:
//                        setGameImage(R.drawable.ic_hangman_3_man);
//                        break;
//                    case 4:
//                        setGameImage(R.drawable.ic_hangman_4_man);
//                        break;
//                    case 5:
//                        setGameImage(R.drawable.ic_hangman_5_man);
//                        break;
//                    case 6:
//                        setGameImage(R.drawable.ic_hangman_6_man);
//                    default:
//                        Toast.makeText(requireActivity(), "GAME OVER", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });

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
            letterButton = new Button(requireActivity());
            buttonParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f), GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f));
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
                    char character = letter.getText().charAt(0);

                    if(!viewModel.checkIfTheInputLetterIsContainedIntoTheCurrentWord(character))
                    {
                        setImage(R.drawable.ic_hangman_1);
                    }
                    else setImage(R.drawable.ic_hangman_0);

                    //Toast.makeText(requireActivity(), String.valueOf(viewModel.checkIfTheInputLetterIsContainedIntoTheCurrentWord(character)), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void displayRandomWord()
    {
        String generatedWord = viewModel.getCurrentWord();
        String guessedLetters = viewModel.getCurrentWordGuessedLetters();

        gameWord.setText(guessedLetters);
    }

    private void checkIfTheWordContainsTheLetter(char letter)
    {

    }
}