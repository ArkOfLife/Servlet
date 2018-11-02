package com.techno.thinkers.ttt.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1710177194078885458L;

	private String code;
	
	private ArrayList<Node> list;
	
	private Integer sucess;
	
	private Node parent;
	
	private char position[];

	public String getCode() {
		return code;
	}

	public void setCode(char[] position) {
		this.code = new String(position);
	}

	public ArrayList<Node> getList() {
		return list;
	}

	public void setList(ArrayList<Node> list) {
		this.list = list;
	}

	public Integer getSucess() {
		return sucess;
	}

	public void setSucess(Integer sucess) {
		this.sucess = sucess;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public char[] getPosition() {
		return position;
	}

	public void setPosition(char[] position) {
		this.position = position;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
