package com.example.hangman.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.hangman.Room.Word;
import com.example.hangman.Room.WordDao;
import com.example.hangman.Room.WordsDatabase;

import java.util.List;

public class WordsRepository
{
    private WordDao wordDao;
    private LiveData<List<Word>> allEasyWords;
    private LiveData<List<Word>> allMediumWords;
    private LiveData<List<Word>> allHardWords;

    public WordsRepository(Application application)
    {
        WordsDatabase database = WordsDatabase.getInstance(application);
        wordDao = database.wordDao();
        allEasyWords = wordDao.getEasyWords();
        allMediumWords = wordDao.getMediumWords();
        allHardWords = wordDao.getHardWords();
    }

    public void insert(Word word)
    {
        new InsertWordAsyncTask(wordDao).execute(word);
    }

    public void delete(Word word)
    {
        new DeleteWordAsyncTask(wordDao).execute(word);
    }

    public void deleteAllWords()
    {
        new DeleteAllWordsAsyncTask(wordDao).execute();
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

    private static class InsertWordAsyncTask extends AsyncTask<Word, Void, Void>
    {
        private WordDao wordDao;

        private InsertWordAsyncTask(WordDao wordDao)
        {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words)
        {
            wordDao.insert(words[0]);
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void>
    {
        private WordDao wordDao;

        public DeleteWordAsyncTask(WordDao wordDao)
        {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words)
        {
            wordDao.delete(words[0]);
            return null;
        }
    }

    private static class DeleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private WordDao wordDao;

        public DeleteAllWordsAsyncTask(WordDao wordDao)
        {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            wordDao.deleteAllWords();
            return null;
        }
    }
}