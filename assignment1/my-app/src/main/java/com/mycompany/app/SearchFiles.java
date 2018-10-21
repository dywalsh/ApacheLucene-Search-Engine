/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *As per the apache license:
 *The original file was modified for the purposes of the CS7IS3 Assignment 1 by Dylan Walsh
 */
package com.mycompany.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import java.util.*;

/** Simple command-line based search demo. */
public class SearchFiles {

  
  public static String outputs = "";
  private SearchFiles() {}

  public static void main(String[] args) throws Exception {
    String index = "index";
    String queries = null;
    String queryString = null;
    int hitsPerPage = 10;
	int scoringType = 0;
    
    for(int i = 0;i < args.length;i++) {
      if ("-queries".equals(args[i])) {
        queries = args[i+1];
        i++;
      }
	  else if("-model".equals(args[i])){
	  	scoringType = Integer.parseInt(args[i+1]);
		i++;
	  }
    }
	//int scoringType = 1; // 0 is VSM , 1 is BM25

    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    IndexSearcher searcher = new IndexSearcher(reader);
	if(scoringType == 0) searcher.setSimilarity(new ClassicSimilarity());
	if(scoringType == 1) searcher.setSimilarity(new BM25Similarity());
	Analyzer analyzer = new DylansAnalyzer();
    BufferedReader in = null;
    if (queries != null) {
      in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
    } else {
      in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }
	HashMap<String, Float> boostedScores = new HashMap<String, Float>();
	boostedScores.put("Title", 0.65f);
	boostedScores.put("Author", 0.04f);
	boostedScores.put("Bibliography", 0.02f);
	boostedScores.put("Words", 0.35f);
	MultiFieldQueryParser parser = new MultiFieldQueryParser (
        new String[]{"Title", "Author", "Bibliography", "Words"},
        analyzer, boostedScores);
	
	String line=in.readLine();
	String nextLine ="";
	int queryNumber = 1;
	PrintWriter writer = new PrintWriter("/home/dywalsh/Desktop/MCS/CS7IS3-IR/assignment1/cran/outputs.txt", "UTF-8");
    while (true) {
      if (queries == null && queryString == null) {                        // prompt the user
        System.out.println("Enter query: ");
      }
      if (line == null || line.length() == -1) {
      	break;
      }

      line = line.trim();
      if (line.length() == 0) {
        break;
      }
	
	 if( line.substring(0,2).equals(".I") ){
		line = in.readLine();	  	
		if( line.equals(".W") ){
			line = in.readLine();
		}
		nextLine = "";
		while( !line.substring(0,2).equals(".I") ){
			nextLine = nextLine + " " + line;
			line = in.readLine();
			if( line == null ) break;
		}
	  } 

      Query query = parser.parse( QueryParser.escape( nextLine.trim() ) );
  
      doPagingSearch(queryNumber, in, searcher, query, hitsPerPage, queries == null && queryString == null, writer);
	  queryNumber++;
      if (queryString != null) {
        break;
      }
    }	
	writer.close();
    reader.close();
  }

  public static void doPagingSearch(int queryNumber, BufferedReader in, IndexSearcher searcher, Query query, 
                                     int hitsPerPage, boolean interactive, PrintWriter writer) throws IOException {
    TopDocs results = searcher.search(query, 5 * hitsPerPage);
	int numTotalHits = Math.toIntExact(results.totalHits);
	results = searcher.search(query, numTotalHits);
	//System.out.println(numTotalHits +" "+ results.totalHits);
    ScoreDoc[] hits = results.scoreDocs;
    System.out.println(numTotalHits + " total matching documents");
    int start = 0;
    int end = Math.min(numTotalHits, hitsPerPage);
        
    while (true) {      
      end = Math.min(hits.length, start + hitsPerPage);
      for (int i = start; i < numTotalHits; i++) {
        Document doc = searcher.doc(hits[i].doc);
        String path = doc.get("path");
        if (path != null) {
			System.out.println(queryNumber + " 0 " + path.replace(".I ","") + " " +(i+1)+ " " + hits[i].score);		  
			writer.println(queryNumber+" 0 " + path.replace(".I ","") + " " + (i+1) + " " + hits[i].score +" EXP");
		}    
      }
      if (!interactive || end == 0) {
        break;
      }
    }
  }
}




























































