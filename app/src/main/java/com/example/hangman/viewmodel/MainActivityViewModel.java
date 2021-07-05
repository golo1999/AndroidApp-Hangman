package com.example.hangman.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hangman.R;
import com.example.hangman.repository.WordsRepository;
import com.example.hangman.room.Word;
import com.example.hangman.view.fragment.SplashScreenFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private static final int maxNumberOfMisses = 6;

    private final WordsRepository repository;
    private final LiveData<List<Word>> allEasyWords;
    private final LiveData<List<Word>> allMediumWords;
    private final LiveData<List<Word>> allHardWords;

    private String playerName;
    private int playerAge;
    private int playerGender;
    private int playerDifficulty;
    private int playerNumberOfMisses = 0;
    private String currentWord;
    private String currentWordGuessedLetters;
    private int currentImage = R.drawable.ic_hangman_0;
    private final ArrayList<Character> listOfLetters = new ArrayList<>();
    private final ArrayList<Integer> placesWhereTheLetterCanBeInserted = new ArrayList<>();
    private int[] currentWordLetterFrequency;
    private Fragment currentFragment = new SplashScreenFragment();
    private final char[] englishAlphabetLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private boolean gameWon = false;

    public MainActivityViewModel(final @NonNull Application application) {
        super(application);
        repository = new WordsRepository(application);
        allEasyWords = repository.getAllEasyWords();
        allMediumWords = repository.getAllMediumWords();
        allHardWords = repository.getAllHardWords();
    }

    public void insert(final Word word) {
        repository.insert(word);
    }

    public void delete(final Word word) {
        repository.delete(word);
    }

    public void deleteAllWords() {
        repository.deleteAllWords();
    }

    public void setPlayerName(final String name) {
        playerName = name;
    }

    public void setPlayerAge(final int age) {
        playerAge = age;
    }

    public void setPlayerGender(final int gender) {
        playerGender = gender;
    }

    public void setPlayerDifficulty(final int difficulty) {
        this.playerDifficulty = difficulty;
    }

    public void incrementPlayerMisses() {
        ++playerNumberOfMisses;
    }

    public void resetPlayerNumberOfMisses() {
        playerNumberOfMisses = 0;
    }

    public void setCurrentWord(final String word) {
        currentWord = word;
    }

    public void setCurrentWordGuessedLetters(final String currentWordGuessedLetters) {
        this.currentWordGuessedLetters = currentWordGuessedLetters;
    }

    public void setCurrentImage(final int image) {
        this.currentImage = image;
    }

    // setting the list of unique letters of the chosen word
    public void setListOfLetters() {
        int currentCharacterIndex = -1;
        boolean isUnique;

        for (char character : currentWord.toCharArray()) {
            ++currentCharacterIndex;
            isUnique = true;

            if (currentCharacterIndex > 0)
                for (final char arrayLetter : listOfLetters) {
                    if (arrayLetter == character) {
                        isUnique = false;
                        break;
                    }
                }

            if (isUnique) {
                listOfLetters.add(character);
            }
        }
    }

    public void setPlacesWhereTheLetterCanBeInserted(final char inputLetter) {
        int currentIndex = -1;

        if (!placesWhereTheLetterCanBeInserted.isEmpty()) {
            placesWhereTheLetterCanBeInserted.clear();
        }

        for (final char character : currentWord.toCharArray()) {
            ++currentIndex;

            // if the input character isn't the first of the last character of the chosen word
            if (character == inputLetter && currentIndex > 0 && currentIndex < currentWord.length() - 1) {
                placesWhereTheLetterCanBeInserted.add(currentIndex);
            }
        }
    }

    public void setCurrentFragment(final Fragment fragment) {
        this.currentFragment = fragment;
    }

    public void setGameWon(final boolean value) {
        gameWon = value;
    }

    public int getMaxNumberOfMisses() {
        return maxNumberOfMisses;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerAge() {
        return playerAge;
    }

    public int getPlayerGender() {
        return playerGender;
    }

    public int getPlayerDifficulty() {
        return playerDifficulty;
    }

    public int getPlayerNumberOfMisses() {
        return playerNumberOfMisses;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public String getCurrentWordGuessedLetters() {
        return currentWordGuessedLetters;
    }

    public int getCurrentImage() {
        return currentImage;
    }

    public ArrayList<Character> getListOfLetters() {
        return listOfLetters;
    }

    public ArrayList<Integer> getPlacesWhereTheLetterCanBeInserted() {
        return placesWhereTheLetterCanBeInserted;
    }

    public LiveData<List<Word>> getAllEasyWords() {
        return allEasyWords;
    }

    public LiveData<List<Word>> getAllMediumWords() {
        return allMediumWords;
    }

    public LiveData<List<Word>> getAllHardWords() {
        return allHardWords;
    }

    public int[] getCurrentWordLetterFrequency() {
        return currentWordLetterFrequency;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public char[] getEnglishAlphabetLetters() {
        return englishAlphabetLetters;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean checkIfTheInputLetterIsContainedIntoTheCurrentWord(final char inputLetter) {
        for (final char character : listOfLetters) {
            if (character == inputLetter) {
                return true;
            }
        }

        return false;
    }

    @NotNull
    public String initializeGuessedLetters() {
        String output = "";
        final int wordLength = currentWord.length();
        int currentIndex = -1;

        for (final char character : currentWord.toCharArray()) {
            ++currentIndex;

            output = output.concat(currentIndex == 0 || currentIndex == wordLength - 1 ?
                    String.valueOf(character) : currentIndex == wordLength - 2 ?
                    " _ " : " _");
        }

        return output;
    }

    public void createLetterFrequency() {
        int uniqueLetterIndex;
        int currentIndex = -1;

        currentWordLetterFrequency = new int[listOfLetters.size()];
        Arrays.fill(currentWordLetterFrequency, 0);

        for (final char characterOfWord : currentWord.toCharArray()) {
            ++currentIndex;
            uniqueLetterIndex = -1;

            if (currentIndex > 0 && currentIndex < currentWord.length() - 1) {
                for (final char uniqueLetter : listOfLetters) {
                    ++uniqueLetterIndex;

                    if (characterOfWord == uniqueLetter) {
                        currentWordLetterFrequency[uniqueLetterIndex]++;
                    }
                }
            }
        }
    }

    public boolean checkIfTheInputLetterCanBeInserted(final char inputLetter) {
        final int indexInsideLetterList = listOfLetters.indexOf(inputLetter);

        return indexInsideLetterList != -1 && currentWordLetterFrequency[indexInsideLetterList] > 0;
    }

    public String removeSpacesFromGuessedWord() {
        int currentIndex = -1;
        String output = "";

        for (final char character : currentWordGuessedLetters.toCharArray()) {
            ++currentIndex;

            if (character != ' ') {
                output = output.concat(currentWordGuessedLetters.substring(currentIndex, currentIndex + 1));
            }
        }

        return output;
    }

    public void insertLetter(final char inputLetter) {
        int currentIndex = -1;
        int uniqueLetterIndex = -1;
        String aux = removeSpacesFromGuessedWord();
        String output = "";

        // setting the letter frequency to 0
        for (final char uniqueLetter : listOfLetters) {
            ++uniqueLetterIndex;

            if (uniqueLetter == inputLetter) {
                currentWordLetterFrequency[uniqueLetterIndex] = 0;
                break;
            }
        }

        for (char ignored : aux.toCharArray()) {
            ++currentIndex;

            for (int place : placesWhereTheLetterCanBeInserted) {
                if (currentIndex == place) {
                    aux = aux.substring(0, currentIndex) + inputLetter + aux.substring(currentIndex + 1);
                }
            }
        }

        currentIndex = -1;

        for (final char letter : aux.toCharArray()) {
            ++currentIndex;

            // adding the character if it's the first or the last character
            // adding a space before and after it if it's in the middle and the second last character
            // adding a space before it if it's in the middle and not the second last character
            output = output.concat(currentIndex == 0 || currentIndex == currentWord.length() - 1 ?
                    String.valueOf(letter) : currentIndex == currentWord.length() - 2 ?
                    " " + letter + " " : " " + letter);
        }

        setCurrentWordGuessedLetters(output);
    }

    public boolean checkIfTheGameIsOver() {
        return getPlayerNumberOfMisses() == getMaxNumberOfMisses() ||
                removeSpacesFromGuessedWord().equals(currentWord) ||
                getCurrentImage() == R.drawable.ic_hangman_6_man ||
                getCurrentImage() == R.drawable.ic_hangman_6_woman;
    }
}
