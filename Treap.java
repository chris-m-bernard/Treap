package CS284;
import java.util.*;
/*
 * Name: Christopher Bernard
 * Section: D
 * Pledge: I pledge my honor that I have abided by the Stevens Honor System.
 */
public class Treap<E extends Comparable<E>> {
	private static class Node<E>{
		public E data; // key for the search
		public int priority; // random heap priority
		public Node <E> left;
		public Node <E> right;
		/*
		 * Creates a node with specific data and priority values.
		 */
		public Node (E data , int priority) {
			if(data == null) throw new IllegalArgumentException("Data cannot be null.");
			this.data = data;
			this.priority = priority;
			left = null;
			right = null;
		}
		/*
		 * Performs a right rotation on a treap.
		 * Returns the root of the treap.
		 */
		public Node<E> rotateRight(){
			if(this.left == null) return this;
			Node<E> temp = left;
			left = left.right;
			temp.right = this;
			return temp;
		}
		/*
		 * Performs a left rotation on a treap.
		 * Returns the root of the treap.
		 */
		public Node<E> rotateLeft(){
			if(this.right == null) return this;
			Node<E> temp = right;
			right = right.left;
			temp.left = this;
			return temp;
		}
		/*
		 * Returns the node as a String 
		 */
		public String toString() {
			return "(key="+data+", priority="+priority+")";
		}
	}
	
	private Random priorityGenerator;
	private Node<E> root;
	/*
	 * Creates an empty treap
	 */
	public Treap (){
		priorityGenerator = new Random();
		root = null;
	}
	/*
	 * Creates an empty treap with a seed for random priority
	 * generation.
	 */
	public Treap (long seed){
		priorityGenerator = new Random(seed);
		root = null;
	}
	/*
	 * Adds a node with a given key and random priority to the treap.
	 * returns false if item already exists, and true if the node
	 * is successfully added.
	 */
	public boolean add(E key) {
		return add(key, priorityGenerator.nextInt());
	}
	/*
	 * Adds a node with a given key and priority to the treap.
	 * returns false if item already exists, and true if the node
	 * is successfully added.
	 */
	public boolean add(E key, int priority) {
		Node<E> node = new Node(key, priority);
		if(root == null) {
			root = node;
			return true;
		}
		Node<E> current = root;
		Stack<Node<E>> stack = new Stack<Node<E>>();
		while(true) {
			if(current.data.compareTo(key)==0) return false;

			else if(key.compareTo(current.data) < 0) {
				stack.push(current);
				if(current.left!=null) current = current.left;
				
				else {
					current.left = node;
					current = current.left;
					break;
				}
			}
			else{
				stack.push(current);
				if(current.right!=null) current = current.right;
				else {
					current.right = node;
					current = current.right;
					break;
				}
			}
		}
		this.reheap(stack, node);
		return true;
	}
	/*
	 * Helper function for add to balance heap
	 */
	private void reheap(Stack<Node<E>> s, Node<E> n) {
		while(!s.isEmpty()) {
			Node<E> current = s.pop();
			if(n.priority > current.priority) {
				if(current.data.compareTo(n.data)>0) n = current.rotateRight();
				else {
					n = current.rotateLeft();
				}
				if(s.isEmpty()) root = n;
				else {
					if(s.peek().left == current) s.peek().left = n;
					else {
						s.peek().right=n;
					}
				}
			}
			else {
				break;
			}
		}
	}
	/*
	 * Deletes the node with the given key from the treap
	 * and returns true. If the key was not found, the method
	 * does not modify the treap and returns false.
	 */
	public boolean delete(E key){
		if(!find(key)) return false;
		if(root == null) return false;
		deleteHelper(key, root);
		return true;
	}
	/*
	 * Helper function for delete
	 */
	private Node<E> deleteHelper(E key, Node<E> n){
		if(key.compareTo(n.data) < 0) n.left = deleteHelper(key, n.left);
		else if(key.compareTo(n.data) > 0) n.right = deleteHelper(key, n.right);
		else {
			if(n.left==null) return n.right;
			else if(n.right==null) return n.left;
			else {
				if(n.right.priority<n.left.priority) {
					n = n.rotateRight();
					n.right = deleteHelper(key, n.right);
				}
				else {
					n = n.rotateLeft();
					n.left = deleteHelper(key, n.left);
				}
			}
		}
		return n;
	}
	/*
	 * Finds a node with the given key in the treap and returns
	 * true if it finds it and false otherwise.
	 */
	public boolean find(E key) {
		return find(root, key);
	}
	/*
	 * Finds a node with the given key in the treap rooted at
	 * root and returns true if it finds it and false otherwise.
	 */
	private boolean find(Node<E> root, E key) {
		if(root == null) return false;
		if(key.compareTo(root.data)==0) return true;
		else if(key.compareTo(root.data)<0) return find(root.left, key);
		else return find(root.right, key);
	}
	/*
	 * Returns a the treap as a String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		preOrderTraverse(root, 1, sb);
		return sb.toString();
	}
		
	/*
	 * Returns a preOrderTraversal of a tree
	 */
	private void preOrderTraverse(Node<E> node, int depth, StringBuilder sb) {
		for (int i = 1; i < depth; i++) {
			sb.append(" ");
		}
		if (node == null) sb.append("null\n");
		else {
			sb.append(node.toString());
			sb.append("\n");
			preOrderTraverse(node.left, depth + 1, sb);
			preOrderTraverse(node.right, depth + 1, sb);
		}
	}
	
	
	public static void main(String[] args) {
		Treap<Integer> testTree = new Treap<Integer>();
		testTree.add(4,19);
		testTree.add(2,31);
		testTree.add(6,70);
		testTree.add(1,84);
		testTree.add(3,12);
		testTree.add(5,83);
		testTree.add(7,26);
		System.out.println(testTree);
		System.out.println("Deleting 5: "+ testTree.delete(5));
		System.out.println("Deleting 10: "+ testTree.delete(10));
		System.out.println("\n"+testTree.toString());
		System.out.println("Finding 7: "+ testTree.find(7));
		System.out.println("Finding 9: "+ testTree.find(9));
		
	}
}
