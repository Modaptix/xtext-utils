package org.modaptix.xtext.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.BidiTreeIterator;
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
		/*
		The code below should be sufficient however, as node.getSemanticElement() sometimes
		returns the parent of the current node for a reason I don't yet understand, it is not!
		
		INode root = resource.getParseResult().getRootNode();
		for (INode node : root.getAsTreeIterable())
		{
			EObject grammarElement  = node.getGrammarElement();
			EObject grammarElementContainer = grammarElement != null ? grammarElement.eContainer() : null; 
			EObject semanticElement = node.getSemanticElement();
			highlightDispatcher.invoke(semanticElement, grammarElement, grammarElementContainer, node, acceptor);
		}
		*/
		
		EList<EObject> contents = resource.getContents();
		if (contents == null || contents.size() == 0)
		{
			return;
		}
		
		EObject model = contents.get(0);
		TreeIterator<EObject> all = model.eAllContents();
		while(all.hasNext())
		{
			EObject semanticElement = all.next();
			INode node = NodeModelUtils.getNode(semanticElement);
			BidiTreeIterator<INode> tree = node.getAsTreeIterable().iterator();
			tree.next();
			while (tree.hasNext())
			{
				INode tnode = tree.next();
				tree.prune();
				EObject grammarElement = tnode.getGrammarElement();
				highlightDispatcher.invoke(semanticElement, grammarElement, grammarElement.eContainer(), tnode, acceptor);
			}
		}
	}
	
	protected void highlight(EObject semanticElement, EObject grammarElement, EObject grammarElementContainer, INode node, IHighlightedPositionAcceptor acceptor)
	{
	}
}
