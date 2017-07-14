# SimpleNLG-EnPt
SimpleNLG-EnPt 0.1 is a bilingual English/Portuguese adaption of [SimpleNLG v4.4](https://github.com/simplenlg/simplenlg).

SimpleNLG is a Java API for Natural Language Generation. Originally developed by Ehud Reiter, Professor at the University of Aberdeen's Department of Computing Science.

The current release of SimpleNLG-EnPt produces texts in English and Portuguese and is intended to function as a "realisation engine" for Natural Language Generation architectures. It handles the following:

* Lexicon/morphology system: This project has 2 default lexicons one in English other in Portuguese, default lexicons computes inflected forms (morphological realisation). 
* Realiser: Generates texts from a syntactic form. 
* Microplanning: Currently just simple aggregation.

The code had to be reorganized so as to separate what was specific to English from what was more generic, before adding the Portuguese grammar. The API is almost identical, though.
