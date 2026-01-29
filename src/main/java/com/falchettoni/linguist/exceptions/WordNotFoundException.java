package com.falchettoni.linguist.exceptions;

public class WordNotFoundException extends VocabularyException{

    public WordNotFoundException(String word){
        super("Word not found: " + word);
    }
}