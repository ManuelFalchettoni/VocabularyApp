package com.falchettoni.linguist.exceptions;

public class VocabularyException extends RuntimeException{

    // Constructors
    public VocabularyException (String message, Throwable cause){ // Constructor with message and cause
        super(message, cause);
    }

    public VocabularyException (String message){ // Constructor with only message
        super(message);
    }
}