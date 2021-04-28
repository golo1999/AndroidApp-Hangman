package com.example.hangman.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hangman_words_table")
public class Word
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private int difficulty;

    public Word(String name, int difficulty)
    {
        this.name = name;
        this.difficulty = difficulty;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getDifficulty()
    {
        return difficulty;
    }
}
