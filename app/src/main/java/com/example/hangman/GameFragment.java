package com.example.hangman;

import android.app.Application;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameFragment extends Fragment
{
    private MainActivityViewModel viewModel;
    //private WordsRepository repository = new WordsRepository((Application) getContext());

    private ImageView gameImage;
    private GridLayout lettersLayout;
    private TextView gameWord;
    private ArrayList<Word> wordsList = new ArrayList<>();

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
        setWordsList();

        generateRandomWord();

//        viewModel.getAllEasyWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
//        {
//            @Override
//            public void onChanged(List<Word> words)
//            {
//                int randomGeneratedNumber = ThreadLocalRandom.current().nextInt(0, words.size());
//                String randomWord = words.get(randomGeneratedNumber).getName();
//
//                viewModel.setCurrentWord(randomWord);
//
//                viewModel.setCurrentWordGuessedLetters(viewModel.initializeGuessedLetters());
//                displayRandomWord();
//                viewModel.setListOfLetters();
//                viewModel.createLetterFrequency();
//                createLetters();
//                setOnLetterClickListener();
//
//                //gameWord.setText(randomWord);
//
//                //Toast.makeText(requireActivity(), "EASY WORDS SIZE: " + words.size(), Toast.LENGTH_SHORT).show();
//            }
//        });

        //viewModel.setCurrentWord(String.valueOf(gameWord.getText()));
        //Toast.makeText(requireActivity(), "CURRENT WORD: " + viewModel.getCurrentWord(), Toast.LENGTH_SHORT).show();
        //gameWord.setText("ZEU");

        //generateRandomWord();
        //viewModel.setCurrentWord(viewModel.generateRandomWord());
        //Toast.makeText(requireActivity(), "WORD VALUE FROM VIEWMODEL OUTSIDE LISTENER: " + viewModel.getCurrentWord(), Toast.LENGTH_SHORT).show();


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
        //viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(MainActivityViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        //viewModel.insert(new Word("ABCD", 1)); // merge
        //viewModel.insert(new Word("ABCD2", 1)); // merge
        //viewModel.delete(new Word("ABCD", 1));
//        viewModel.insert(new Word("W1", 1));
//        viewModel.insert(new Word("W2", 1));
//        viewModel.insert(new Word("W3", 0));
//        viewModel.insert(new Word("W4", 2));
//        viewModel.insert(new Word("W5", 0));
        //viewModel.deleteAllWords();

        //Toast.makeText(requireActivity(), "EASY WORDS", Toast.LENGTH_SHORT).show();

        viewModel.getAllEasyWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
        {
            @Override
            public void onChanged(List<Word> words)
            {
//                for(Word word : words)
//                    Toast.makeText(requireActivity(), word.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(requireActivity(), "MEDIUM WORDS", Toast.LENGTH_SHORT).show();

        viewModel.getAllMediumWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
        {
            @Override
            public void onChanged(List<Word> words)
            {
//                for(Word word : words)
//                    Toast.makeText(requireActivity(), word.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Toast.makeText(requireActivity(), "HARD WORDS", Toast.LENGTH_SHORT).show();

        viewModel.getAllHardWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
        {
            @Override
            public void onChanged(List<Word> words)
            {
//                for(Word word : words)
//                    Toast.makeText(requireActivity(), word.getName(), Toast.LENGTH_SHORT).show();
            }
        });


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

    private void setWordsList() // merge
    {
        if(viewModel.getPlayerDifficulty() == 0)
        {
            viewModel.getAllEasyWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {

                    wordsList.addAll(words);
                    viewModel.setListOfWords(wordsList);
//                    ArrayList<Word> list123 = viewModel.getListOfWords();
//
//                    Toast.makeText(requireActivity(), "LIST123", Toast.LENGTH_SHORT).show();
//                    for(Word w1 : list123)
//                        Toast.makeText(requireActivity(), w1.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(viewModel.getPlayerDifficulty() == 1)
        {
            viewModel.getAllMediumWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
                    wordsList.addAll(words);
                    viewModel.setListOfWords(wordsList);
                }
            });
        }
        else if(viewModel.getPlayerDifficulty() == 2)
        {
            viewModel.getAllHardWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
                    wordsList.addAll(words);
                    viewModel.setListOfWords(wordsList);
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
        else if(viewModel.getPlayerDifficulty() == 1) // if the selected difficulty is medium
        {
            viewModel.getAllMediumWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
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
        else if(viewModel.getPlayerDifficulty() == 2) // // if the selected difficulty is hard
        {
            viewModel.getAllHardWords().observe(getViewLifecycleOwner(), new Observer<List<Word>>()
            {
                @Override
                public void onChanged(List<Word> words)
                {
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