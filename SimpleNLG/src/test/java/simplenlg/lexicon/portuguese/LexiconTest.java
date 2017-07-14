/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */

package simplenlg.lexicon.portuguese;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

/**
 * 
 * @author Hélinton P. Steffens
 * @Jun 6, 2017
 */
public class LexiconTest {


	/**
	 * Verb 'be' conjugation tests.
	 */
	@Test
	public void beInflectionTest() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		Realiser r = new Realiser(lexicon);
		WordElement word = lexicon.getWord("ser", LexicalCategory.VERB);
		InflectedWordElement inflWord = new InflectedWordElement(word);
		
		//1st person sg past
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("fui", r.realise(inflWord).toString());
		
		//2nd person sg past
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("foste", r.realise(inflWord).toString());
		
		//3rd person sg past
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("foi", r.realise(inflWord).toString());
		
		//1st person sg present
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);		
		Assert.assertEquals("sou", r.realise(inflWord).toString());
		
		//2nd person sg present
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("és", r.realise(inflWord).toString());
		
		//3rd person sg present
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("é", r.realise(inflWord).toString());
		
		inflWord.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
		
		//1st person pl past
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("fomos", r.realise(inflWord).toString());
		
		//2nd person pl past
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("fostes", r.realise(inflWord).toString());
		
		//3rd person pl past
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PAST);
		Assert.assertEquals("foram", r.realise(inflWord).toString());
		
		//1st person pl present
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);		
		Assert.assertEquals("somos", r.realise(inflWord).toString());
		
		//2nd person pl present
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("sois", r.realise(inflWord).toString());
		
		//3rd person pl present
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.PRESENT);
		Assert.assertEquals("são", r.realise(inflWord).toString());
		
		//1st person pl present
		inflWord.setFeature(Feature.PERSON, Person.FIRST);
		inflWord.setFeature(Feature.TENSE, Tense.FUTURE);		
		Assert.assertEquals("seremos", r.realise(inflWord).toString());
		
		//2nd person pl present
		inflWord.setFeature(Feature.PERSON, Person.SECOND);
		inflWord.setFeature(Feature.TENSE, Tense.FUTURE);
		Assert.assertEquals("sereis", r.realise(inflWord).toString());
		
		//3rd person pl present
		inflWord.setFeature(Feature.PERSON, Person.THIRD);
		inflWord.setFeature(Feature.TENSE, Tense.FUTURE);
		Assert.assertEquals("serão", r.realise(inflWord).toString());
		
	}
	
}
