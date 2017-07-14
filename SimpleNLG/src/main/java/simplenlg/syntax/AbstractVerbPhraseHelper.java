package simplenlg.syntax;

import java.util.Stack;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseElement;

public abstract class AbstractVerbPhraseHelper {
	
	public abstract Stack<NLGElement> createVerbGroup(SyntaxProcessor parent, PhraseElement phrase);
	public abstract void splitVerbGroup(Stack<NLGElement> vgComponents, Stack<NLGElement> mainVerbRealisation, Stack<NLGElement> auxiliaryRealisation);
	public abstract boolean isCopular(NLGElement element);
	public abstract void realiseComplements(SyntaxProcessor parent, PhraseElement phrase, ListElement realisedElement);
	
	/**
	 * The main method for realising verb phrases.
	 * 
	 * @param parent
	 *            the <code>SyntaxProcessor</code> that called this method.
	 * @param phrase
	 *            the <code>PhraseElement</code> to be realised.
	 * @return the realised <code>NLGElement</code>.
	 */
	public NLGElement realise(SyntaxProcessor parent, PhraseElement phrase) {
		ListElement realisedElement = null;
		Stack<NLGElement> vgComponents = null;
		Stack<NLGElement> mainVerbRealisation = new Stack<NLGElement>();
		Stack<NLGElement> auxiliaryRealisation = new Stack<NLGElement>();

		if (phrase != null) {
			vgComponents = createVerbGroup(parent, phrase);
			splitVerbGroup(vgComponents, mainVerbRealisation,
					auxiliaryRealisation);

			realisedElement = new ListElement();

			if (!phrase.hasFeature(InternalFeature.REALISE_AUXILIARY)
					|| phrase.getFeatureAsBoolean(
							InternalFeature.REALISE_AUXILIARY).booleanValue()) {

				realiseAuxiliaries(parent, realisedElement,
						auxiliaryRealisation);

				PhraseHelper.realiseList(parent, realisedElement, phrase
						.getPreModifiers(), DiscourseFunction.PRE_MODIFIER);

				realiseMainVerb(parent, phrase, mainVerbRealisation,
						realisedElement);

			} else if (isCopular(phrase.getHead())) {
				realiseMainVerb(parent, phrase, mainVerbRealisation,
						realisedElement);
				PhraseHelper.realiseList(parent, realisedElement, phrase
						.getPreModifiers(), DiscourseFunction.PRE_MODIFIER);

			} else {
				PhraseHelper.realiseList(parent, realisedElement, phrase
						.getPreModifiers(), DiscourseFunction.PRE_MODIFIER);
				realiseMainVerb(parent, phrase, mainVerbRealisation,
						realisedElement);
			}
			realiseComplements(parent, phrase, realisedElement);
			PhraseHelper.realiseList(parent, realisedElement, phrase
					.getPostModifiers(), DiscourseFunction.POST_MODIFIER);
		}

		return realisedElement;
	}

	/**
	 * Realises the auxiliary verbs in the verb group.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param realisedElement
	 *            the current realisation of the noun phrase.
	 * @param auxiliaryRealisation
	 *            the stack of auxiliary verbs.
	 */
	private static void realiseAuxiliaries(SyntaxProcessor parent,
			ListElement realisedElement, Stack<NLGElement> auxiliaryRealisation) {

		NLGElement aux = null;
		NLGElement currentElement = null;
		while (!auxiliaryRealisation.isEmpty()) {
			aux = auxiliaryRealisation.pop();
			currentElement = parent.realise(aux);
			if (currentElement != null) {
				realisedElement.addComponent(currentElement);
				currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
						DiscourseFunction.AUXILIARY);
			}
		}
	}

	/**
	 * Realises the main group of verbs in the phrase.
	 * 
	 * @param parent
	 *            the parent <code>SyntaxProcessor</code> that will do the
	 *            realisation of the complementiser.
	 * @param phrase
	 *            the <code>PhraseElement</code> representing this noun phrase.
	 * @param mainVerbRealisation
	 *            the stack of the main verbs in the phrase.
	 * @param realisedElement
	 *            the current realisation of the noun phrase.
	 */
	private static void realiseMainVerb(SyntaxProcessor parent,
			PhraseElement phrase, Stack<NLGElement> mainVerbRealisation,
			ListElement realisedElement) {

		NLGElement currentElement = null;
		NLGElement main = null;

		while (!mainVerbRealisation.isEmpty()) {
			main = mainVerbRealisation.pop();
			main.setFeature(Feature.INTERROGATIVE_TYPE, phrase
					.getFeature(Feature.INTERROGATIVE_TYPE));
			currentElement = parent.realise(main);

			if (currentElement != null) {
				realisedElement.addComponent(currentElement);
			}
		}
	}

}
