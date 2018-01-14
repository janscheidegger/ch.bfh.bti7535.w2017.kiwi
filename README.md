# Sentiment Analysis of IMDB Reviews

## How to Start

add lib/snowball-20051019.jar to classpath and run App.java

## How to try new Evaluations

* Create a new Configuration with the Preprocessor
* Configure:
    * Stopword Handler
    * Number of words to keep for Naive Bayes
    * Tokenizer
    * Stemmer
    * Idf Tranform True/False
    * TF Transform True/False
    * Attribute Selection/Attributes to keep after selection
    * Own Features implementing the AttributeCreator Interface
* Name your Configuration
* Compare against others
