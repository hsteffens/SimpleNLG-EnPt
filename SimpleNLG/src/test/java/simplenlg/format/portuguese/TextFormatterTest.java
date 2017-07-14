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

package simplenlg.format.portuguese;

import org.junit.Test;

import junit.framework.Assert;
import simplenlg.format.english.TextFormatter;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.portuguese.PortugueseNLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

/**
 * @author Hélinton P. Steffens
 */
public class TextFormatterTest {

    @Test
    public void testEnumeratedList() {
    	Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);
        realiser.setFormatter(new TextFormatter());
        DocumentElement document = nlgFactory.createDocument("Document");
        DocumentElement paragraph = nlgFactory.createParagraph();


        DocumentElement subListItem1 = nlgFactory.createListItem();
        DocumentElement subListSentence1 = nlgFactory.createSentence("Esta", "ser", "a sub-lista da sentença 1");
        subListItem1.addComponent(subListSentence1);

        DocumentElement subListItem2 = nlgFactory.createListItem();
        DocumentElement subListSentence2 = nlgFactory.createSentence("Esta", "ser", "a sub-lista da sentença 2");
        subListItem2.addComponent(subListSentence2);

        DocumentElement subList = nlgFactory.createEnumeratedList();
        subList.addComponent(subListItem1);
        subList.addComponent(subListItem2);

        DocumentElement item1 = nlgFactory.createListItem();
        DocumentElement sentence1 = nlgFactory.createSentence("Esta", "ser", "a primeira sentença");
        item1.addComponent(sentence1);

        DocumentElement item2 = nlgFactory.createListItem();
        DocumentElement sentence2 = nlgFactory.createSentence("Esta", "ser", "a segunda sentença");
        item2.addComponent(sentence2);

        DocumentElement list = nlgFactory.createEnumeratedList();
        list.addComponent(subList);
        list.addComponent(item1);
        list.addComponent(item2);
        paragraph.addComponent(list);
        document.addComponent(paragraph);
        String expectedOutput = "Document\n" +
                                "\n" +
                                "1.1 - Esta é a sub-lista da sentença 1.\n" +
                                "1.2 - Esta é a sub-lista da sentença 2.\n"+
                                "2 - Esta é a primeira sentença.\n" +
                                "3 - Esta é a segunda sentença.\n" +
                                "\n\n"; // for the end of a paragraph

        String realisedOutput = realiser.realise(document).getRealisation();
        Assert.assertEquals(expectedOutput, realisedOutput);
    }

    @Test
    public void testEnumeratedListWithSeveralLevelsOfNesting() {
    	Lexicon lexicon = Lexicon.getPortugueseDefaultLexicon();
		NLGFactory nlgFactory = new PortugueseNLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);
        realiser.setFormatter(new TextFormatter());
        DocumentElement document = nlgFactory.createDocument("Document");
        DocumentElement paragraph = nlgFactory.createParagraph();

        // sub item 1
        DocumentElement subList1Item1 = nlgFactory.createListItem();
        DocumentElement subList1Sentence1 = nlgFactory.createSentence("sub-lista item 1");
        subList1Item1.addComponent(subList1Sentence1);

        // sub sub item 1
        DocumentElement subSubList1Item1 = nlgFactory.createListItem();
        DocumentElement subSubList1Sentence1 = nlgFactory.createSentence("sub-sub-lista item 1");
        subSubList1Item1.addComponent(subSubList1Sentence1);

        // sub sub item 2
        DocumentElement subSubList1Item2 = nlgFactory.createListItem();
        DocumentElement subSubList1Sentence2 = nlgFactory.createSentence("sub-sub-lista item 2");
        subSubList1Item2.addComponent(subSubList1Sentence2);

        // sub sub list
        DocumentElement subSubList1 = nlgFactory.createEnumeratedList();
        subSubList1.addComponent(subSubList1Item1);
        subSubList1.addComponent(subSubList1Item2);

        // sub item 2
        DocumentElement subList1Item2 = nlgFactory.createListItem();
        DocumentElement subList1Sentence2 = nlgFactory.createSentence("sub-lista item 3");
        subList1Item2.addComponent(subList1Sentence2);

        // sub list 1
        DocumentElement subList1 = nlgFactory.createEnumeratedList();
        subList1.addComponent(subList1Item1);
        subList1.addComponent(subSubList1);
        subList1.addComponent(subList1Item2);

        // item 2
        DocumentElement item2 = nlgFactory.createListItem();
        DocumentElement sentence2 = nlgFactory.createSentence("item 2");
        item2.addComponent(sentence2);

        // item 3
        DocumentElement item3 = nlgFactory.createListItem();
        DocumentElement sentence3 = nlgFactory.createSentence("item 3");
        item3.addComponent(sentence3);

        // list
        DocumentElement list = nlgFactory.createEnumeratedList();
        list.addComponent(subList1);
        list.addComponent(item2);
        list.addComponent(item3);

        paragraph.addComponent(list);

        document.addComponent(paragraph);

        String expectedOutput = "Document\n" +
                                "\n" +
                                "1.1 - Sub-lista item 1.\n" +
                                "1.2.1 - Sub-sub-lista item 1.\n" +
                                "1.2.2 - Sub-sub-lista item 2.\n" +
                                "1.3 - Sub-lista item 3.\n"+
                                "2 - Item 2.\n" +
                                "3 - Item 3.\n" +
                                "\n\n";

        String realisedOutput = realiser.realise(document).getRealisation();
        Assert.assertEquals(expectedOutput, realisedOutput);
    }

}
