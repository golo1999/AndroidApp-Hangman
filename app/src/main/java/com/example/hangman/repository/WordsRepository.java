package com.example.hangman.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.hangman.room.Word;
import com.example.hangman.room.WordDao;
import com.example.hangman.room.WordsDatabase;

import java.util.List;

public class WordsRepository {
    private final WordDao wordDao;
    private final LiveData<List<Word>> allEasyWords;
    private final LiveData<List<Word>> allMediumWords;
    private final LiveData<List<Word>> allHardWords;

    public WordsRepository(Application application) {
        final WordsDatabase database = WordsDatabase.getInstance(application);
        wordDao = database.wordDao();
        allEasyWords = wordDao.getEasyWords();
        allMediumWords = wordDao.getMediumWords();
        allHardWords = wordDao.getHardWords();
    }

    public void insert(Word word) {
        new InsertWordAsyncTask(wordDao).execute(word);
    }

    public void delete(Word word) {
        new DeleteWordAsyncTask(wordDao).execute(word);
    }

    public void deleteAllWords() {
        new DeleteAllWordsAsyncTask(wordDao).execute();
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

    private static class InsertWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        private InsertWordAsyncTask(final WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            wordDao.insert(words[0]);
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private final WordDao wordDao;

        public DeleteWordAsyncTask(final WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            wordDao.delete(words[0]);
            return null;
        }
    }

    private static class DeleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WordDao wordDao;

        public DeleteAllWordsAsyncTask(final WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}