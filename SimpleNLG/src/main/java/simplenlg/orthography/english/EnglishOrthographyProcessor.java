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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.orthography.english;

import java.util.List;

import simplenlg.features.Feature;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.orthography.AbstractOrthographyProcessor;

/**
 * <p>
 * This processing module deals with punctuation when applied to
 * <code>DocumentElement</code>s. The punctuation currently handled by this
 * processor includes the following (as of version 4.0):
 * <ul>
 * <li>Capitalisation of the first letter in sentences.</li>
 * <li>Termination of sentences with a period if not interrogative.</li>
 * <li>Termination of sentences with a question mark if they are interrogative.</li>
 * <li>Replacement of multiple conjunctions with a comma. For example,
 * <em>John and Peter and Simon</em> becomes <em>John, Peter and Simon</em>.</li>
 * </ul>
 * </p>
 * 
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class EnglishOrthographyProcessor extends AbstractOrthographyProcessor {

	@Override
	public void formatPreModifier(NLGElement element, StringBuffer buffer) {
		boolean all_appositives = true;
		for(NLGElement child : element.getChildren()){
			all_appositives = all_appositives && child.getFeatureAsBoolean(Feature.APPOSITIVE);
		}

		// TODO: unless this is the end of the sentence
		if(all_appositives){
			buffer.append(", ");
		}
		realiseList(buffer, element.getChildren(), isCommaSepPremodifiers() ? "," : "", "");
		if(all_appositives){
			buffer.append(", ");
		}
	}
	
	@Override
	public void formatPostModifier(NLGElement element, StringBuffer buffer) {
		// appositive)
		// {
		List<NLGElement> postmods = element.getChildren();
		// bug fix due to Owen Bennett
		int len = postmods.size();

		for(int i = 0; i < len; i++ ) {
			// for(NLGElement postmod: element.getChildren()) {
			NLGElement postmod = postmods.get(i);

			// if the postmod is appositive, it's sandwiched in
			// commas
			if(postmod.getFeatureAsBoolean(Feature.APPOSITIVE)) {
				buffer.append(", ");
				buffer.append(realise(postmod));
				buffer.append(", ");
			} else {
				buffer.append(realise(postmod));
				if(postmod instanceof ListElement
						|| (postmod.getRealisation() != null && !postmod.getRealisation().equals(""))) {
					buffer.append(" ");
				}
			}
		}
	}
}
