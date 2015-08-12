package org.modaptix.xtext.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.util.PolymorphicDispatcher;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

public class PolymorphicSemanticHighlightingCalculator implements ISemanticHighlightingCalculator
{
	protected PolymorphicDispatcher<Void> highlightDispatcher = PolymorphicDispatcher.createForSingleTarget("highlight", 5, 5, this);
	
	@Override
	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor)
	{
		INode root = resource.getParseResult().getRootNode();
		for (INode node : root.getAsTreeIterable())
		{
			EObject grammarElement  = node.getGrammarElement();
			EObject grammarElementContainer = grammarElement != null ? grammarElement.eContainer() : null; 
			EObject semanticElement = NodeModelUtils.findActualSemanticObjectFor(node);
			try
			{
				highlightDispatcher.invoke(semanticElement, grammarElement, grammarElementContainer, node, acceptor);
			}
			catch (IllegalStateException e)	{ }
		}
	}
	
	protected void highlight(EObject semanticElement, EObject grammarElement, EObject grammarElementContainer, INode node, IHighlightedPositionAcceptor acceptor)
	{
	}
}
