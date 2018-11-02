package com.techno.thinkers.ttt.control;

import java.util.ArrayList;

import com.techno.thinkers.ttt.model.Node;

public class TUtil {

	public static Node findHigestSucess(ArrayList<Node> list) {
		Node higestSucessNode=list.get(0);
		for(Node node : list) {
			if(higestSucessNode.getSucess()<node.getSucess()) {
				higestSucessNode=node;
			}	
		}
		
		return higestSucessNode;
	}
	public static char[] toCharArray(String code) {
		char[] chararray=new char[code.length()] ;
		for(int i=0;i<code.length();i++) {
			chararray[i]=code.charAt(i);
		}
		
		return chararray;
	}

	public static void changeParentSuccess(Node currentNode) {
		while(currentNode!=null) {
			Node parent=currentNode.getParent();
			Integer parentSucess;
			if(parent!=null) {
			if(parent.getSucess()==null) {
				parentSucess=0;
			}else {
				parentSucess=parent.getSucess();
			}
			parent.setSucess(parentSucess+currentNode.getSucess());
			}
			currentNode=parent;
		}
		
	}
	
	
}
