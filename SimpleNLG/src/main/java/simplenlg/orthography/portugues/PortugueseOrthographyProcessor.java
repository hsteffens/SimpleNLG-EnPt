package simplenlg.orthography.portugues;

import java.util.List;

import simplenlg.features.Feature;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.orthography.AbstractOrthographyProcessor;

public class PortugueseOrthographyProcessor extends AbstractOrthographyProcessor{

	@Override
	public void formatPreModifier(NLGElement element, StringBuffer buffer) {
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

	@Override
	public void formatPostModifier(NLGElement element, StringBuffer buffer) {
		boolean all_appositives = true;
		
		for(NLGElement child : element.getChildren()){
			all_appositives = all_appositives && child.getFeatureAsBoolean(Feature.APPOSITIVE);
		}

		// TODO: unless this is the end of the sentence
		if(all_appositives){
			buffer.append(", ");
		}
		realiseList(buffer, element.getChildren(), isCommaSepPremodifiers() ? "," : "", " e");
		if(all_appositives){
			buffer.append(", ");
		}
		
	}

}
