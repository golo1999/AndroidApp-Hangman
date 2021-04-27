package com.example.hangman;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Word.class, version = 1)
public abstract class WordsDatabase extends RoomDatabase
{
    private static WordsDatabase instance;

    public abstract WordDao wordDao();

    public static synchronized WordsDatabase getInstance(Context context)
    {
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), WordsDatabase.class, "words_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            super.onCreate(db);
            new PopulateWordsDatabaseAsyncTask(instance).execute();
        }
    };

    private static class PopulateWordsDatabaseAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private final String[] easyWords = { "CAT", "DOG", "HUMAN", "EYE", "HOUSE", "CAR", "DEER", "DOOR", "FLY", "JAW", "RAT", "RED", "GRAY", "FLU" };
        private final String[] mediumWords = { "JUICE", "IVY", "CIRCLE", "UNZIP", "GALAXY", "AZURE", "FAKING", "QUIZ", "QUARTZ", "JELLY", "COIN", "SPIDER", "WHITE", "YELLOW", "JOKER", "IVORY", "INJURY", "BLITZ" };
        private final String[] hardWords = { "RAZZMATAZZ", "RHUBARB", "ZIGZAGGING", "PNEUMONIA", "BEEKEEPER", "BUZZWORDS", "XYLOPHONE", "FLUFFINESS", "WELLSPRING", "MNEMONIC", "EMBEZZLE", "KNAPSACK", "WITCHCRAFT", "PEEKABOO", "JAWBREAKER", "VOYEURISM", "BUFFALO", "BOOKWORM", "BANDWAGON" };

        private WordDao wordDao;

        private PopulateWordsDatabaseAsyncTask(WordsDatabase database)
        {
            wordDao = database.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) // MERGE
        {
            for(String wordName : easyWords)
                wordDao.insert(new Word(wordName, 0));

            for(String wordName : mediumWords)
                wordDao.insert(new Word(wordName, 1));

            for(String wordName : hardWords)
                wordDao.insert(new Word(wordName, 2));

            return null;
        }
    }
}