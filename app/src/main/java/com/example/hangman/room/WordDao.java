package com.example.hangman.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insert(final Word word);

    @Delete
    void delete(final Word word);

    @Query("DELETE FROM hangman_words_table")
    void deleteAllWords();

    @Query("SELECT * FROM hangman_words_table WHERE difficulty = 0 ORDER BY name")
    LiveData<List<Word>> getEasyWords();

    @Query("SELECT * FROM hangman_words_table WHERE difficulty = 1 ORDER BY name")
    LiveData<List<Word>> getMediumWords();

    @Query("SELECT * FROM hangman_words_table WHERE difficulty = 2 ORDER BY name")
    LiveData<List<Word>> getHardWords();
}