package com.example.hangman.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hangman.R;

@Database(entities = Word.class, version = 1)
public abstract class WordsDatabase extends RoomDatabase {
    private static WordsDatabase instance;

    public abstract WordDao wordDao();

    public static synchronized WordsDatabase getInstance(final Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), WordsDatabase.class,
                    context.getResources().getString(R.string.database_name))
                    .fallbackToDestructiveMigration().addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            new PopulateWordsDatabaseAsyncTask(instance, context).execute();
                        }
                    }).build();

        return instance;
    }

    private static class PopulateWordsDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WordDao wordDao;
        private final String[] easyWords;
        private final String[] mediumWords;
        private final String[] hardWords;

        private PopulateWordsDatabaseAsyncTask(final WordsDatabase database, final Context context) {
            wordDao = database.wordDao();
            easyWords = context.getResources().getStringArray(R.array.easy_words);
            mediumWords = context.getResources().getStringArray(R.array.medium_words);
            hardWords = context.getResources().getStringArray(R.array.hard_words);
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            for (final String wordName : easyWords) {
                wordDao.insert(new Word(wordName, 0));
            }

            for (final String wordName : mediumWords) {
                wordDao.insert(new Word(wordName, 1));
            }

            for (final String wordName : hardWords) {
                wordDao.insert(new Word(wordName, 2));
            }

            return null;
        }
    }
}