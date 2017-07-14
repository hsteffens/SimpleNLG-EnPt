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

package simplenlg.syntax.portuguese;

import java.util.Arrays;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class DocumentElementTest {

	/**
	 * Basic tests.
	 */
	@Test
	public void testBasics() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec p1 = nlgFactory.createClause("você", "estar", nlgFactory.createAdjectivePhrase("feliz"));
		AbstractSPhraseSpec p2 = nlgFactory.createClause("eu", "estar", nlgFactory.createAdjectivePhrase("triste"));
		AbstractSPhraseSpec p3 = nlgFactory.createClause("eles", "estar", nlgFactory.createAdjectivePhrase("nervoso"));
		
		DocumentElement s1 = nlgFactory.createSentence(p1);
		DocumentElement s2 = nlgFactory.createSentence(p2);
		DocumentElement s3 = nlgFactory.createSentence(p3);

		DocumentElement par1 = nlgFactory.createParagraph(Arrays
				.asList(s1, s2, s3));

		Assert.assertEquals("Você está feliz. Eu estou triste. Eles estão nervosos.\n\n",
				realizer.realise(par1).getRealisation());

	}

	/**
	 * test whether sents can be embedded in a section without intervening paras
	 */
	@Test
	public void testEmbedding() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		DocumentElement sent = nlgFactory.createSentence("Este é um teste");
		
		VPPhraseSpec estar = nlgFactory.createVerbPhrase("estar");
		VPPhraseSpec faltar = nlgFactory.createVerbPhrase("faltar");
		DocumentElement sent2 = nlgFactory.createSentence(nlgFactory
				.createClause("John", estar, faltar));
		DocumentElement section = nlgFactory.createSection("SECTION TITLE");
		section.addComponent(sent);
		section.addComponent(sent2);

		Assert.assertEquals(
				"SECTION TITLE\nEste é um teste.\n\nJohn está faltando.\n\n",
				realizer.realise(section).getRealisation());
	}

	@Test
	public void testSections() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		// doc which contains a section, and two paras
		DocumentElement doc = nlgFactory.createDocument("Documento teste");

		DocumentElement section = nlgFactory.createSection("secção teste");
		doc.addComponent(section);

		DocumentElement para1 = nlgFactory.createParagraph();
		DocumentElement sent1 = nlgFactory.createSentence("Este é o primeiro parágrafo de teste");
		para1.addComponent(sent1);
		section.addComponent(para1);

		DocumentElement para2 = nlgFactory.createParagraph();
		DocumentElement sent2 = nlgFactory.createSentence("Este é o segundo parágrafo de teste");
		para2.addComponent(sent2);
		section.addComponent(para2);

		Assert.assertEquals(
						"Documento teste\n\nsecção teste\nEste é o primeiro parágrafo de teste.\n\nEste é o segundo parágrafo de teste.\n\n",
						realizer.realise(doc).getRealisation());
	}

	/**
	 * Tests for lists and embedded lists
	 */
	@Test
	public void testListItems() {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
		
		Realiser realizer = new Realiser(lexicon);
		
		AbstractSPhraseSpec p1 = nlgFactory.createClause("você", "estar", "feliz");
		AbstractSPhraseSpec p2 = nlgFactory.createClause("eu", "estar", "triste");
		
		DocumentElement list = nlgFactory.createList();
		list.addComponent(nlgFactory.createListItem(p1));
		list.addComponent(nlgFactory.createListItem(p2));
		list.addComponent(nlgFactory.createListItem(nlgFactory.createCoordinatedPhrase(p1, p2)));
		String realisation = realizer.realise(list).getRealisation();
		Assert.assertEquals(
				"* você está feliz\n* eu estou triste\n* você está feliz e eu estou triste\n",
				realisation);
	}
}
