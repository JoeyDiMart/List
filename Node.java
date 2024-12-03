package List;/* ***************************************************
 * <Joseph DiMartino>
 * <9.2.2024>
 *
 * Node Class - handles any form of data
 *************************************************** */

class Node<Type> // <Type> meaning it can handle a node being of any data type (String, int, etc.)
{
	private Type data;
	private Node<Type> link;

	// constructor
	public Node()
	{
		this.data = null;
		this.link = null;
	}

	// accessor and mutator for the data component
	public Type getData()
	{
		return this.data;
	}

	public void setData(Type data)
	{
		this.data = data;
	}

	// accessor and mutator for the link component
	public Node<Type> getLink()
	{
		return this.link;
	}

	public void setLink(Node<Type> link)
	{
		this.link = link;
	}

	public String toString() {
		if (this.data == null) { return "NULL"; }
		else { return this.data.toString(); }
	}
}
