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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */

package simplenlg.syntax.portuguese;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import simplenlg.format.english.HTMLFormatter;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AbstractSPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class HTMLFormatterTest {

	/**
	 * Check the correct [part] web page contents are being generated
	 */
	@Test
	public final void testWebPageContent( ) {
		Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);

		Realiser realiser = new Realiser(lexicon);

		// now build a document ... 
		DocumentElement document = nlgFactory.createDocument("Este é um titulo");

		DocumentElement section = nlgFactory.createSection("Está é uma secção");

		DocumentElement paragraph1 = nlgFactory.createParagraph();
		DocumentElement sentence11  = nlgFactory.createSentence("Este é a primeira sentença do 1º paragrafo");
		paragraph1.addComponent(sentence11);
		DocumentElement sentence12 = nlgFactory.createSentence("Este é a segunda sentença do 1º paragrafo");
		paragraph1.addComponent(sentence12);
		section.addComponent(paragraph1);		
		document.addComponent(section);

		DocumentElement paragraph2 = nlgFactory.createParagraph();
		DocumentElement sentence2 = nlgFactory.createSentence("Este é a primeira sentença do 2º paragrafo");
		paragraph2.addComponent(sentence2);
		document.addComponent(paragraph2);		

		DocumentElement paragraph3 = nlgFactory.createParagraph();
		DocumentElement sentence3 = nlgFactory.createSentence("Este é a primeira sentença do 3º paragrafo");
		paragraph3.addComponent(sentence3);
		document.addComponent(paragraph3);		

		// now for a second section with three sentences in one paragraph using arrays.asList function
		AbstractSPhraseSpec p1 = nlgFactory.createClause( "Mary", "perseguir", "o macaco" ) ;
		AbstractSPhraseSpec p2 = nlgFactory.createClause( "o macaco", "lutar" ) ;
		AbstractSPhraseSpec p3 = nlgFactory.createClause( "Mary", "estar", "nervosa" ) ;

		DocumentElement s1 = nlgFactory.createSentence( p1 ) ;
		DocumentElement s2 = nlgFactory.createSentence( p2 ) ;
		DocumentElement s3 = nlgFactory.createSentence( p3 ) ;

		DocumentElement para1x3 = nlgFactory.createParagraph( Arrays.asList( s1, s2, s3 ) ) ;

		DocumentElement sectionList = nlgFactory.createSection( "Esta secção contem as listas" ) ;
		sectionList.addComponent( para1x3 ) ;
		document.addComponent( sectionList ) ;

		// from David Westwater 4-10-11
		DocumentElement element = nlgFactory.createList( ) ;
		List < NLGElement > list = new ArrayList < NLGElement > ( ) ;
		list.add( nlgFactory.createListItem( nlgFactory.createStringElement( "Item 1" ) ) ) ;
		list.add( nlgFactory.createListItem( nlgFactory.createStringElement( "Item 2" ) ) ) ;
		list.add( nlgFactory.createListItem( nlgFactory.createStringElement( "Item 3" ) ) ) ;

		element.addComponents( list ) ;		
		document.addComponent( element ) ;

		// ... finally produce some output with HMTL tags ...
		System.out.println( "HTML realisation ~ \n=============================\n" ) ;

		String output =  "" ; 

		// this.realiser.setFormatter( new TextFormatter( ) ) ;
		realiser.setFormatter( new HTMLFormatter( ) ) ;
		// realiser.setDebugMode( true ) ; // hide after testing
		output += realiser.realise( document ).getRealisation( )  ;

		System.out.println( output ) ; // just to visually check what is being produced

		String expectedResults = 
				"<h1>Este é um titulo</h1>" +
						"<h2>Está é uma secção</h2>" +
						"<p>Este é a primeira sentença do 1º paragrafo. Este é a segunda sentença do 1º paragrafo.</p>" +
						"<p>Este é a primeira sentença do 2º paragrafo.</p>" +
						"<p>Este é a primeira sentença do 3º paragrafo.</p>" +
						"<h2>Esta secção contem as listas</h2>" +
						"<p>Mary persegue o macaco. O macaco luta. Mary está nervosa.</p>" +
						"<ul>" +
						"<li>Item 1</li>" +
						"<li>Item 2</li>" +
						"<li>Item 3</li>" +
						"</ul>" ;

		assertEquals( expectedResults, output ) ; // when realisation is working then complete this test
	} // testWebPageContents
} // class


