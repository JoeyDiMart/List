package com.example.csc301.List;
/* ***************************************************
 * <Joseph DiMartino>
 * <9.3.2024>
 * List Class - handles any form of data
 *************************************************** */

public class List<Type> { // Will be used for Linked Lists

    // We don't actually have to set a max size with linked lists
    // But it is a good idea.
    // Just picture an infinite loop adding to the list! :O
    public static final int MAX_SIZE = 40000; // public class variable that's a constant integer (unchangeable)

    private Node<Type> head; // first node
    private Node<Type> tail; // last node
    private Node<Type> curr; // current node
    private int num_items;

    // constructor
    // remember that an empty list has a "size" of -1 and its "position" is at -1
    public List() {
        // by default, three properties of the list are all null, no items in list so set to 0
        this.head = null;
        this.tail = null;
        this.curr = null;
        this.num_items = 0;
    }

    // copy constructor
    // clones the list l and sets the last element as the current
    public List(List<Type> l) {
        Node<Type> n = l.head;

        this.head = this.tail = this.curr = null; // all null
        this.num_items = 0; // originally 0

        while (n != null) { // **** This loop sets the pointer to the next element?
            this.InsertAfter(n.getData());
            n = n.getLink(); }
    }

    // navigates to the beginning of the list
    public void First() {
        this.curr = this.head;
    }

    // navigates to the end of the list
    // the end of the list is at the last valid item in the list
    public void Last() {
        this.curr = this.tail;
    }

    // navigates to the specified element (0-index)
    // this should not be possible for an empty list
    // this should not be possible for invalid positions
    public void SetPos(int pos) {
        try {
            if (pos < 0 || pos >= num_items) {
                throw new IndexOutOfBoundsException("Position out of bounds");
            } // exception handling
            else {
                this.curr = this.head;
                for (int i = 0; i < pos; i++) { // for loop until i is before pos
                    this.curr = this.curr.getLink(); } } // keep going to next node until curr's position = pos
        }
        catch (IndexOutOfBoundsException e) {
            //System.out.println("Error: " + e.getMessage());
        }
    }

    // navigates to the previous element
    // this should not be possible for an empty list
    // there should be no wrap-around
    public void Prev() {
        try {
            if (this.curr == this.head)
                throw (new IndexOutOfBoundsException("Cannot navigate to element previous to head")); // going previous to the first element is out of bounds
            else if (this.curr == null) throw new NullPointerException("Current element is null");
            else {
                Node<Type> temp = this.head;
                while (temp.getLink() != this.curr) {
                    temp = temp.getLink();
                }
                this.curr = temp;
            }
        }
        catch (IndexOutOfBoundsException | NullPointerException e ) {
            //System.out.println("Error: " + e.getMessage());
        }
    }

    
    // navigates to the next element
    // this should not be possible for an empty list
    // there should be no wrap-around
    public void Next() {
        try {  // for exception handling, try to do these three that probably will throw an error
            if (this.curr == this.tail) {
                throw new IndexOutOfBoundsException("Cannot navigate to element next to tail"); }
            else if (this.curr == null) {
                throw new NullPointerException("Current element is null"); }
            else if (num_items == 0) {
                throw new IllegalStateException("List is empty"); }
            else {
                this.curr = this.curr.getLink(); }  // this shouldn't give an error if the other 3 don't happen
        }

        catch (IndexOutOfBoundsException | NullPointerException | IllegalStateException e) {
            //System.out.println("Error: " + e.getMessage()); } // if any of the three errors happen, print the error message
        }
    }


    // returns the location of the current element (or null)
    public int GetPos() {
        if (this.curr == null) return -1;
        else {
            Node<Type> temp = this.head;
            for (int i = 0; i < this.num_items; i++) {
                if (temp == this.curr) return i;
                temp = temp.getLink(); }
            return -1; }  // not found
    }

    // returns the value of the current element (or null)
    public Type GetValue()
    {
        if (this.curr == null) return null;
        else return this.curr.getData();
    }

    // returns the size of the list
    // size does not imply capacity
    public int GetSize() {
        if (this.head == null) return 0;
        if (this.head == this.tail) return 1;
        else {
            int size = 0;
            Node<Type> temp = this.head;
            while (temp != this.tail) {
                size++;
                temp = temp.getLink(); }
            size++;
            return size; }
    }

    // inserts an item before the current element
    // the new element becomes the current
    // this should not be possible for a full list
    public void InsertBefore(Type data) { // If curr insertBefore head then move head
        try {
            if (this.GetSize() >= MAX_SIZE) throw new IllegalStateException("List is full");

            else {
                Node<Type> n = new Node<Type>();
                n.setData(data);
                if (this.head == null) {  // if there's nothing in the list
                    this.head = this.tail = this.curr = n;
                    this.num_items++;
                }  // list grows by 1 element

                else if (this.head == this.curr) {
                    n.setLink(this.curr);
                    this.head = this.curr = n;
                    num_items++;
                } else {
                    n.setLink(this.curr);
                    this.Prev();
                    this.curr.setLink(n);
                    this.curr = n;
                    num_items++; } }
        }
        catch (IllegalStateException _) {}
         //   System.out.println("Error: " + e.getMessage()); }
    }

    // inserts an item after the current element
    // the new element becomes the current
    // this should not be possible for a full list
    public void InsertAfter(Type data) {
        try {
            if (this.GetSize() >= MAX_SIZE ) throw new IllegalStateException("List is full"); // handle max sized list

            else {
                Node<Type> n = new Node<Type>();  // create new node n
                n.setData(data);  // n is set to equal the data
                if (num_items == 0) {  // if there's nothing in the list
                    this.head = this.tail = this.curr = n;  // n is everything
                    this.num_items++; }

                else if (this.tail == this.curr) {  // inserting element to the end
                    this.curr.setLink(n);  // current points to n
                    this.tail = n;  // n is the tail
                    this.Last();  // Last function (curr = tail)
                    num_items++; }  // increase item count

                else {
                    n.setLink(this.curr.getLink());
                    this.curr.setLink(n);  // let n point to where curr points
                    this.Next();
                    num_items++; } }
        }
        catch (IllegalStateException e ) {
            //System.out.println("Error: " + e.getMessage());
        }
    }

    // removes the current element 
    // this should not be possible for an empty list
    public void Remove() {
        try {
            if (num_items == 0) throw new IllegalStateException("List is empty");

            else if (num_items == 1) {
                this.head = this.tail = this.curr = null;
                num_items = 0; }

            else if (this.curr == this.tail) {
                this.curr.setData(null);
                this.Prev();
                this.curr.setLink(null);
                this.tail = this.curr;
                num_items--; }

            else if (this.curr == this.head) {
                this.curr.setData(null);
                this.head = this.head.getLink();
                this.curr = this.head;
                num_items--; }

            else {
                Node<Type> temp = this.curr;  // create a temp node
                this.Prev();  // move back 1 position
                this.curr.setLink(temp.getLink());  // make the curr point to the next next node
                temp.setData(null);
                num_items--; }
        }
        catch (IllegalStateException e ) {
            //System.out.println("Error: " + e.getMessage());
        }

    }

    // replaces the value of the current element with the specified value
    // this should not be possible for an empty list
    public void Replace(Type data) {
        if (num_items == 0) throw new IllegalStateException("List is empty");
        if (this.curr == null) throw new IllegalStateException("Current node is null");
        else {
            this.curr.setData(data); }
    }

    // returns if the list is empty
    public boolean IsEmpty() {
        return this.num_items == 0;
    }

    // returns if the list is full
    public boolean IsFull() {
        return this.num_items == MAX_SIZE;
    }

    // returns if two lists are equal (by value)
    public boolean Equals(List<Type> l) {
        return false;
    }

    // returns the concatenation of two lists
    // l should not be modified
    // l should be concatenated to the end of *this
    // the returned list should not exceed MAX_SIZE elements
    // the last element of the new list is the current
    public List<Type> Add(List<Type> l) {
        try {
            if ((this.GetSize() >= MAX_SIZE) || (l.GetSize() >= MAX_SIZE)) {
                throw new IndexOutOfBoundsException("List is already full"); }
            else if (this.GetSize() == 0) return l;

            else {
                this.Last();  // set this curr to last in *this
                l.First();  // set l curr to head in l
                this.curr.setLink(l.curr);  // set the last of *this to the first of l
                num_items++;
                for (int i = 1; i < l.GetSize(); i++) {
                    l.Next();  // move l to next position
                    if (num_items < MAX_SIZE) {
                        this.curr.setLink(l.curr);
                        this.Next();
                        num_items++; }
                }
            }
        }
        catch (IndexOutOfBoundsException e ) {
            //System.out.println("Error: " + e.getMessage());
        }

        return this;  // returns modified *this
    }
    public void Clear() {
        this.head = this.curr = this.tail = null;
        num_items = 0;
    }


    // returns a string representation of the entire list (e.g., 1 2 3 4 5)
    // the string "NULL" should be returned for an empty list
    public String toString() {
        if (this.head == null) { return "NULL"; }
        else {
            Node<Type> n = this.head;
            String s = "";
            while (n != null) {
                s += n.getData() + " ";
                n = n.getLink(); } // move to next node
            return s; }
    }
}