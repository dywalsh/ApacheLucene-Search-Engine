package com.mycompany.app;

import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.PorterStemFilter;


//http://web.cecs.pdx.edu/~maier/cs510iri/HW2010/Project_I_answer_key.pdf

public class CustomAnalyzer extends Analyzer{
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer standardTokenizer = new StandardTokenizer();
		TokenStream tokenizer = new StandardFilter(standardTokenizer);
		tokenizer = new LowerCaseFilter(tokenizer);
		//from https://github.com/Yoast/YoastSEO.js/blob/develop/src/config/stopwords.js
		List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
		CharArraySet stopWordSet = new CharArraySet( stopWordList, true);
//from https://stackoverflow.com/questions/31957986/how-to-combine-analyzer-instances-for-stop-word-removal-and-stemming-in-lucene
		tokenizer = new StopFilter(tokenizer, stopWordSet);
		tokenizer = new PorterStemFilter(tokenizer);
        return new TokenStreamComponents(standardTokenizer, tokenizer);
    }
}
