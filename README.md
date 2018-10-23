# Apache Lucene Search Engine

Here is an implementation of the Apache Lucene Library, written in Java. This search engine creates an index of the cranfield collection (IndexFiles.java), which allows you to query this index (SearchFiles.java).
It works off the basic Apache Lucene tutorials, with specific alerterations and additions to the code to improve the search engine for my needs (accuracy).

Trec-eval can then be used to compare the scores of this search engine with the recommended scores and see the accuracy degree of this search engine.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

```
Unix OS
Maven 3.5.2
JDK 10.0.2
GCC 7.3.0
```

### Building, Compiling, Creating the Index, Querying the Index and Using Trec-eval to compare scores.
```
1. Clone this repository to your designated directory.
2. Using a terminal - cd the cloned directory.
3. Run "mvn clean"
4. Build and compile the project: "mvn package"
5. Run "java -cp target/app-1.0-SNAPSHOT.jar com.mycompany.app.IndexFiles -docs {path-to-this-directory}/cran/cran.all.1400
6. Run "java -cp target/app-1.0-SNAPSHOT.jar com.mycompany.app.SearchFiles -queries {path-to-this-directory}/cran/cran.qry -model 1
   (The “-model” flag indicates the type of scoring you wish to use: 0 is vector space (classic) and 1 is BM25: usage "-model 1")
   SearchFiles stores the scoring -output.txt in {path-to-this-directory}/cran/outputs.txt
7. cd {path-to-this-directory}/trec-eval.8.1/
8. Run ./trec_eval {path-to-this-directory}/cran/QRelsCorrectedforTRECeval {path-to-this-directory}/cran/outputs.txt
```
