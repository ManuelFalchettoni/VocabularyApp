package com.falchettoni.linguist.exceptions;

public class DuplicateWordException extends VocabularyException{

    public DuplicateWordException(String word){
        super("Duplicate word found: " + word);
    }
}