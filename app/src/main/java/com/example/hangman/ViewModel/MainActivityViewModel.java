package com.example.hangman.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hangman.R;
import com.example.hangman.View.Fragment.RegisterFragment;
import com.example.hangman.Repository.WordsRepository;
import com.example.hangman.Room.Word;
import com.example.hangman.View.Fragment.SplashScreenFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel
{
    private static final int maxNumberOfMisses = 6;

    private WordsRepository repository;
    private LiveData<List<Word>> allEasyWords;
    private LiveData<List<Word>> allMediumWords;
    private LiveData<List<Word>> allHardWords;

    private String playerName;
    private int playerAge;
    private int playerGender;
    private int playerDifficulty;
    private int playerNumberOfMisses = 0;
    private String currentWord;
    private String currentWordGuessedLetters;
    private int currentImage = R.drawable.ic_hangman_0;
    private ArrayList<Character> listOfLetters = new ArrayList<>();
    private ArrayList<Integer> placesWhereTheLetterCanBeInserted = new ArrayList<>();
    private int[] currentWordLetterFrequency;
    private Fragment currentFragment = new SplashScreenFragment();
    private char[] englishAlphabetLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private boolean gameWon = false;

    public MainActivityViewModel(@NonNull Application application)
    {
        super(application);
        repository = new WordsRepository(application);
        allEasyWords = repository.getAllEasyWords();
        allMediumWords = repository.getAllMediumWords();
        allHardWords = repository.getAllHardWords();
    }

    public void insert(Word word)
    {
        repository.insert(word);
    }

    public void delete(Word word)
    {
        repository.delete(word);
    }

    public void deleteAllWords()
    {
        repository.deleteAllWords();
    }

    public void setPlayerName(String name)
    {
        playerName = name;
    }

    public void setPlayerAge(int age)
    {
        playerAge = age;
    }

    public void setPlayerGender(int gender)
    {
        playerGender = gender;
    }

    public void setPlayerDifficulty(int difficulty)
    {
        this.playerDifficulty = difficulty;
    }

    public void incrementPlayerMisses()
    {
        ++playerNumberOfMisses;
    }

    public void resetPlayerNumberOfMisses()
    {
        playerNumberOfMisses = 0;
    }

    public void setCurrentWord(String word)
    {
        currentWord = word;
    }

    public void setCurrentWordGuessedLetters(String currentWordGuessedLetters)
    {
        this.currentWordGuessedLetters = currentWordGuessedLetters;
    }

    public void setCurrentImage(int image)
    {
        this.currentImage = image;
    }

    public void setListOfLetters() // setting the list of unique letters of the chosen word
    {
        int currentCharacterIndex = -1;
        boolean isUnique;

        for(char character : currentWord.toCharArray())
        {
            ++currentCharacterIndex;
            isUnique = true;

            if(currentCharacterIndex > 0)
                for(char arrayLetter : listOfLetters)
                    if(arrayLetter == character)
                    {
                        isUnique = false;
                        break;
                    }

            if(isUnique)
                listOfLetters.add(character);
        }
    }

    public void setPlacesWhereTheLetterCanBeInserted(char inputLetter)
    {
        int currentIndex = -1;

        if(!placesWhereTheLetterCanBeInserted.isEmpty())
            placesWhereTheLetterCanBeInserted.clear();

        for(char character : currentWord.toCharArray())
        {
            ++currentIndex;

            if(character == inputLetter && currentIndex > 0 && currentIndex < currentWord.length() - 1) // if the input character isn't the first of the last character of the chosen word
                placesWhereTheLetterCanBeInserted.add(currentIndex);
        }
    }

    public void setCurrentFragment(Fragment fragment)
    {
        this.currentFragment = fragment;
    }

    public void setGameWon(boolean value)
    {
        gameWon = value;
    }

    public int getMaxNumberOfMisses()
    {
        return maxNumberOfMisses;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public int getPlayerAge()
    {
        return playerAge;
    }

    public int getPlayerGender()
    {
        return playerGender;
    }

    public int getPlayerDifficulty()
    {
        return playerDifficulty;
    }

    public int getPlayerNumberOfMisses()
    {
        return playerNumberOfMisses;
    }

    public String getCurrentWord()
    {
        return currentWord;
    }

    public String getCurrentWordGuessedLetters()
    {
        return currentWordGuessedLetters;
    }

    public int getCurrentImage()
    {
        return currentImage;
    }

    public ArrayList<Character> getListOfLetters()
    {
        return listOfLetters;
    }

    public ArrayList<Integer> getPlacesWhereTheLetterCanBeInserted()
    {
        return placesWhereTheLetterCanBeInserted;
    }

    public LiveData<List<Word>> getAllEasyWords()
    {
        return allEasyWords;
    }

    public LiveData<List<Word>> getAllMediumWords()
    {
        return allMediumWords;
    }

    public LiveData<List<Word>> getAllHardWords()
    {
        return allHardWords;
    }

    public int[] getCurrentWordLetterFrequency()
    {
        return currentWordLetterFrequency;
    }

    public Fragment getCurrentFragment()
    {
        return currentFragment;
    }

    public char[] getEnglishAlphabetLetters()
    {
        return englishAlphabetLetters;
    }

    public boolean isGameWon()
    {
        return gameWon;
    }

    public boolean checkIfTheInputLetterIsContainedIntoTheCurrentWord(char inputLetter)
    {
        boolean isContained = false;

        for(char character : listOfLetters)
            if(character == inputLetter)
            {
                isContained = true;
                break;
            }

        return isContained;
    }

    @NotNull
    public String initializeGuessedLetters()
    {
        String output = "";
        int wordLength = currentWord.length();
        int currentIndex = -1;

        for(char character : currentWord.toCharArray())
        {
            ++currentIndex;

            if(currentIndex == 0 || currentIndex == wordLength - 1)
                output = output.concat(String.valueOf(character));
            else if(currentIndex == wordLength - 2)
                output = output.concat(" _ ");
            else output = output.concat(" _");
        }

        return output;
    }

    public void createLetterFrequency()
    {
        int uniqueLetterIndex, currentIndex = -1;

        currentWordLetterFrequency = new int[listOfLetters.size()];
        Arrays.fill(currentWordLetterFrequency, 0);

        for(char characterOfWord : currentWord.toCharArray())
        {
            ++currentIndex;
            uniqueLetterIndex = -1;

            if(currentIndex > 0 && currentIndex < currentWord.length() - 1)
                for(char uniqueLetter : listOfLetters)
                {
                    ++uniqueLetterIndex;

                    if(characterOfWord == uniqueLetter)
                        currentWordLetterFrequency[uniqueLetterIndex]++;
                }
        }
    }

    public boolean checkIfTheInputLetterCanBeInserted(char inputLetter)
    {
        int indexInsideLetterList = listOfLetters.indexOf(inputLetter);
        boolean canBeInserted = false;

        if(indexInsideLetterList != -1)
            if(currentWordLetterFrequency[indexInsideLetterList] > 0)
                canBeInserted = true;

        return canBeInserted;
    }

    public String removeSpacesFromGuessedWord()
    {
        int currentIndex = -1;
        String output = "";

        for(char character : currentWordGuessedLetters.toCharArray())
        {
            ++currentIndex;

            if(character != ' ')
                output = output.concat(currentWordGuessedLetters.substring(currentIndex, currentIndex + 1));
        }

        return output;
    }

    public void insertLetter(char inputLetter)
    {
        int currentIndex = -1;
        int uniqueLetterIndex = -1;
        String aux = removeSpacesFromGuessedWord();
        String output = "";

        for(char uniqueLetter : listOfLetters) // setting the letter frequency to 0
        {
            ++uniqueLetterIndex;

            if(uniqueLetter == inputLetter)
            {
                currentWordLetterFrequency[uniqueLetterIndex] = 0;
                break;
            }
        }

        for(char letter : aux.toCharArray())
        {
            ++currentIndex;

            for(int place : placesWhereTheLetterCanBeInserted)
                if(currentIndex == place)
                    aux = aux.substring(0, currentIndex) + inputLetter + aux.substring(currentIndex + 1);
        }

        currentIndex = -1;

        for(char letter : aux.toCharArray())
        {
            ++currentIndex;

            if(currentIndex == 0 || currentIndex == currentWord.length() - 1) // if it's the first or the last character, adding the character
                output = output.concat(String.valueOf(letter));
            else if(currentIndex == currentWord.length() - 2) // if it's in the middle and the second last character, adding a space before and after it
                output = output.concat(" " + letter + " ");
            else output = output.concat(" " + letter); // if it's in the middle and not the second last character, adding a space before it
        }

        setCurrentWordGuessedLetters(output);
    }

    public boolean checkIfTheGameIsOver()
    {
        boolean isOver = false;

        if(getPlayerNumberOfMisses() == getMaxNumberOfMisses() || removeSpacesFromGuessedWord().equals(currentWord) || getCurrentImage() == R.drawable.ic_hangman_6_man || getCurrentImage() == R.drawable.ic_hangman_6_woman)
            isOver = true;

        return isOver;
    }
}
