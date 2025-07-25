package com.basicJava.linkedlistexample;

public class LinkedList {

	Node head;

	public void add(int data) {
		Node container = new Node();
		container.setData(data);
		if (head == null) {
			head = container;
		} else {
			Node tempNode = head;
			while (tempNode.getNext()!= null) {
				tempNode = tempNode.getNext();
			}
			tempNode.setNext(container);
		}
	}

	public void deleteLast() 
	{
		Node temp;
		Node pre=null;
		temp=head;
		pre=temp.getNext();

		if(temp.getNext()==null) {
			System.out.println("Deleted: "+temp.getData()+" from the list");
			head=null;
		}else {
			while(pre.getNext()!=null){
				temp=pre;
				pre=pre.getNext();
			}
			System.out.println("Deleted: "+pre.getData()+" from the list");
			temp.setNext(null);
		}
	}

	public void insertAt(int index,int data) {
		Node tempNode = new Node();
		tempNode.setData(data);
		Node preNode;
		Node currentNode;

		if(head==null) 
		{
			head=tempNode;
		}
		else 
		{
			if(index==1) 
			{
				tempNode.setNext(head);
				head=tempNode;
			}
			else 
			{
				preNode=head;
				int count=1;
				while(count<(index-1)) {
					preNode=preNode.getNext();
					count++;
				}
				currentNode=preNode.getNext();
				preNode.setNext(tempNode);
				tempNode.setNext(currentNode);

			}
		}
	}

	public void find(int data) {
		Node currentNode=new Node();	
		currentNode=head;
		boolean matchFound=false;

		if(head==null)
		{
			System.out.println("list is empty cannot search for given number");
			return;
		}
		while((currentNode != null) && (!matchFound)) {
			if((currentNode.getData())==data){
				System.out.println(data+" Match found in the List ");
				matchFound=true;
			}
			currentNode = currentNode.getNext();
		}
		if(!matchFound) {
			System.out.println("Match Not Found in the List");
		}
	}

	public void show() {
		Node container = head;
		if(container==null) {
			System.out.println("list is empty");
		}else {
			while (container.getNext() != null) {
				System.out.println(container.getData());
				container = container.getNext();
			}
			System.out.println(container.getData());
		}
	}

	
	public static void main(String[] args) {
		LinkedList list1 = new LinkedList();
		LinkedList list2 =new LinkedList();
		

		list1.add(10);
		list1.add(12);
		list1.add(17);
		list1.add(25);

		list2.add(5);
		list2.add(11);
		list2.add(22);
		list2.add(26);

	}
}
