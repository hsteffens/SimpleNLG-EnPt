package simplenlg.orthography;

import java.util.ArrayList;
import java.util.List;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.InternalFeature;
import simplenlg.framework.AbstractCoordinatedPhraseElement;
import simplenlg.framework.DocumentCategory;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGModule;
import simplenlg.framework.StringElement;

public abstract class AbstractOrthographyProcessor extends NLGModule {

	private boolean commaSepPremodifiers; // set whether to separate
	// premodifiers using commas

	private boolean commaSepCuephrase;   // set whether to include a comma after a
	// cue phrase (if marked by the
	// CUE_PHRASE=true) feature.
	
	public abstract void formatPreModifier(NLGElement element, StringBuffer buffer);
	public abstract void formatPostModifier(NLGElement element, StringBuffer buffer);

	@Override
	public void initialise() {
		this.commaSepPremodifiers = true;
		this.commaSepCuephrase = false;
	}

	/**
	 * Check whether this processor separates premodifiers using a comma.
	 * 
	 * @return <code>true</code> if premodifiers in the noun phrase are
	 *         comma-separated.
	 */
	public boolean isCommaSepPremodifiers() {
		return commaSepPremodifiers;
	}

	/**
	 * Set whether to separate premodifiers using a comma. If <code>true</code>,
	 * premodifiers will be comma-separated, as in <i>the long, dark road</i>.
	 * If <code>false</code>, they won't.
	 * 
	 * @param commaSepPremodifiers
	 *            the commaSepPremodifiers to set
	 */
	public void setCommaSepPremodifiers(boolean commaSepPremodifiers) {
		this.commaSepPremodifiers = commaSepPremodifiers;
	}

	/**
	 * Check whether this processor separates cue phrases from a matrix phrase
	 * using a comma.
	 * 
	 * @return <code>true</code> if this parameter is set.
	 */
	public boolean isCommaSepCuephrase() {
		return commaSepCuephrase;
	}

	/**
	 * If set to <code>true</code>, separates a cue phrase from the matrix
	 * phrase using a comma. Cue phrases are typically at the start of a
	 * sentence (e.g. <i><u>However</u>, John left early</i>). This will only
	 * apply to phrases with the feature
	 * {@link simplenlg.features.DiscourseFunction#CUE_PHRASE} or {@link simplenlg.features.DiscourseFunction#FRONT_MODIFIER}.
	 * 
	 * @param commaSepCuephrase
	 *            whether to separate cue phrases using a comma
	 */
	public void setCommaSepCuephrase(boolean commaSepCuephrase) {
		this.commaSepCuephrase = commaSepCuephrase;
	}

	@Override
	public NLGElement realise(NLGElement element) {
		NLGElement realisedElement = null;
		Object function = null; //the element's discourse function

		//get the element's function first
		if(element instanceof ListElement) {
			List<NLGElement> children = element.getChildren();
			if(!children.isEmpty()) {
				NLGElement firstChild = children.get(0);
				function = firstChild.getFeature(InternalFeature.DISCOURSE_FUNCTION);
			}
		} else {
			if(element != null) {
				function = element.getFeature(InternalFeature.DISCOURSE_FUNCTION);
			}
		}

		if(element != null) {
			ElementCategory category = element.getCategory();

			if(category instanceof DocumentCategory && element instanceof DocumentElement) {
				List<NLGElement> components = ((DocumentElement) element).getComponents();

				switch((DocumentCategory) category){

				case SENTENCE :
					realisedElement = realiseSentence(components, element);
					break;

				case LIST_ITEM :
					if(components != null && components.size() > 0) {
						// recursively realise whatever is in the list item
						// NB: this will realise embedded lists within list
						// items
						realisedElement = new ListElement(realise(components));
						realisedElement.setParent(element.getParent());
					}
					break;

				default :
					((DocumentElement) element).setComponents(realise(components));
					realisedElement = element;
				}

			} else if(element instanceof ListElement) {
				// AG: changes here: if we have a premodifier, then we ask the
				// realiseList method to separate with a comma.
				// if it's a postmod, we need commas at the start and end only
				// if it's appositive
				StringBuffer buffer = new StringBuffer();

				if(DiscourseFunction.PRE_MODIFIER.equals(function)) {
					formatPreModifier(element, buffer);
				} else if(DiscourseFunction.POST_MODIFIER.equals(function)) {// &&
					formatPostModifier(element, buffer);
				} else if((DiscourseFunction.CUE_PHRASE.equals(function) || DiscourseFunction.FRONT_MODIFIER.equals(function))
						&& this.commaSepCuephrase){
					realiseList(buffer, element.getChildren(), this.commaSepCuephrase ? "," : "", "");

				} else {
					realiseList(buffer, element.getChildren(), "", "");
				}

				// realiseList(buffer, element.getChildren(), "");
				realisedElement = new StringElement(buffer.toString());

			} else if(element instanceof AbstractCoordinatedPhraseElement) {
				realisedElement = realiseCoordinatedPhrase(element.getChildren());
			} else {
				realisedElement = element;
			}

			// make the realised element inherit the original category
			// essential if list items are to be properly formatted later
			if(realisedElement != null) {
				realisedElement.setCategory(category);
			}

			//check if this is a cue phrase; if param is set, postfix a comma
			if((DiscourseFunction.CUE_PHRASE.equals(function) || DiscourseFunction.FRONT_MODIFIER.equals(function))
					&& this.commaSepCuephrase) {
				String realisation = realisedElement.getRealisation();

				if(!realisation.endsWith(",")) {
					realisation = realisation + ",";
				}

				realisedElement.setRealisation(realisation);
			}
		}

		//remove preceding and trailing whitespace from internal punctuation
		removePunctSpace(realisedElement);
		return realisedElement;
	}

	/**
	 * removes extra spaces preceding punctuation from a realised element
	 * 
	 * @param realisedElement
	 */
	private void removePunctSpace(NLGElement realisedElement) {

		if(realisedElement != null) {

			String realisation = realisedElement.getRealisation();

			if(realisation != null) {
				realisation = realisation.replaceAll(" ,", ",");
				realisation = realisation.replaceAll(",,+", ",");
				realisedElement.setRealisation(realisation);
			}

		}
	}

	/**
	 * Performs the realisation on a sentence. This includes adding the
	 * terminator and capitalising the first letter.
	 * 
	 * @param components
	 *            the <code>List</code> of <code>NLGElement</code>s representing
	 *            the components that make up the sentence.
	 * @param element
	 *            the <code>NLGElement</code> representing the sentence.
	 * @return the realised element as an <code>NLGElement</code>.
	 */
	private NLGElement realiseSentence(List<NLGElement> components, NLGElement element) {

		NLGElement realisedElement = null;
		if(components != null && components.size() > 0) {
			StringBuffer realisation = new StringBuffer();
			realiseList(realisation, components, "", "");

			stripLeadingCommas(realisation);
			capitaliseFirstLetter(realisation);
			terminateSentence(realisation, element.getFeatureAsBoolean(InternalFeature.INTERROGATIVE).booleanValue());

			((DocumentElement) element).clearComponents();
			// realisation.append(' ');
			element.setRealisation(realisation.toString());
			realisedElement = element;
		}

		return realisedElement;
	}

	/**
	 * Adds the sentence terminator to the sentence. This is a period ('.') for
	 * normal sentences or a question mark ('?') for interrogatives.
	 * 
	 * @param realisation
	 *            the <code>StringBuffer<code> containing the current 
	 * realisation of the sentence.
	 * @param interrogative
	 *            a <code>boolean</code> flag showing <code>true</code> if the
	 *            sentence is an interrogative, <code>false</code> otherwise.
	 */
	private void terminateSentence(StringBuffer realisation, boolean interrogative) {
		char character = realisation.charAt(realisation.length() - 1);
		if(character != '.' && character != '?') {
			if(interrogative) {
				realisation.append('?');
			} else {
				realisation.append('.');
			}
		}
	}

	/**
	 * Remove recursively any leading spaces or commas at the start 
	 * of a sentence.
	 * 
	 * @param realisation
	 *            the <code>StringBuffer<code> containing the current 
	 * realisation of the sentence.
	 */
	private void stripLeadingCommas(StringBuffer realisation) {
		char character = realisation.charAt(0);
		if(character == ' ' || character == ',') {
			realisation.deleteCharAt(0);
			stripLeadingCommas(realisation);
		}
	}


	/**
	 * Capitalises the first character of a sentence if it is a lower case
	 * letter.
	 * 
	 * @param realisation
	 *            the <code>StringBuffer<code> containing the current 
	 * realisation of the sentence.
	 */
	private void capitaliseFirstLetter(StringBuffer realisation) {
		char character = realisation.charAt(0);
		if(character >= 'a' && character <= 'z') {
			character = (char) ('A' + (character - 'a'));
			realisation.setCharAt(0, character);
		}
	}

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		List<NLGElement> realisedList = new ArrayList<NLGElement>();

		if(elements != null && elements.size() > 0) {
			for(NLGElement eachElement : elements) {
				if(eachElement instanceof DocumentElement) {
					realisedList.add(realise(eachElement));
				} else {
					realisedList.add(eachElement);
				}
			}
		}
		return realisedList;
	}

	/**
	 * Realises a list of elements appending the result to the on-going
	 * realisation.
	 * 
	 * @param realisation
	 *            the <code>StringBuffer<code> containing the current 
	 * 			  realisation of the sentence.
	 * @param components
	 *            the <code>List</code> of <code>NLGElement</code>s representing
	 *            the components that make up the sentence.
	 * @param listSeparator
	 *            the string to use to separate elements of the list, empty if
	 *            no separator needed
	 */
	public void realiseList(StringBuffer realisation, List<NLGElement> components, String listSeparator, String endSepartor) {

		NLGElement realisedChild = null;

		for(int i = 0; i < components.size(); i++ ) {
			NLGElement thisElement = components.get(i);
			realisedChild = realise(thisElement);
			String childRealisation = realisedChild.getRealisation();

			// check that the child realisation is non-empty
			if(childRealisation != null && childRealisation.length() > 0 && !childRealisation.matches("^[\\s\\n]+$")) {
				realisation.append(realisedChild.getRealisation());

				if(components.size() > 1 && i < components.size() - 2) {
					realisation.append(listSeparator);
				}else if (components.size() > 1 && i < components.size() - 1) {
					if (endSepartor != null && !endSepartor.equals("")) {
						realisation.append(endSepartor);
					}else{
						realisation.append(listSeparator);
					}
					
				}

				realisation.append(' ');
			}
		}

		if(realisation.length() > 0) {
			realisation.setLength(realisation.length() - 1);
		}
	}

	/**
	 * Realises coordinated phrases. Where there are more than two coordinates,
	 * then a comma replaces the conjunction word between all the coordinates
	 * save the last two. For example, <em>John and Peter and Simon</em> becomes
	 * <em>John, Peter and Simon</em>.
	 * 
	 * @param components
	 *            the <code>List</code> of <code>NLGElement</code>s representing
	 *            the components that make up the sentence.
	 * @return the realised element as an <code>NLGElement</code>.
	 */
	private NLGElement realiseCoordinatedPhrase(List<NLGElement> components) {
		StringBuffer realisation = new StringBuffer();
		NLGElement realisedChild = null;

		int length = components.size();

		for(int index = 0; index < length; index++ ) {
			realisedChild = components.get(index);
			if(index < length - 2
					&& DiscourseFunction.CONJUNCTION.equals(realisedChild.getFeature(InternalFeature.DISCOURSE_FUNCTION))) {

				realisation.append(", "); //$NON-NLS-1$
			} else {
				realisedChild = realise(realisedChild);
				realisation.append(realisedChild.getRealisation()).append(' ');
			}
		}
		realisation.setLength(realisation.length() - 1);
		return new StringElement(realisation.toString().replace(" ,", ",")); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
