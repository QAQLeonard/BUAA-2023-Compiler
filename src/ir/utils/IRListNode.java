package ir.utils;

public class IRListNode<N, L>
{
    IRListNode<N, L> prev = null;
    IRListNode<N, L> next = null;
    N value;
    IRLinkedList<N, L> parentList = null;

    public IRListNode(N value)
    {
        this.value = value;
    }

    public N getValue()
    {
        return value;
    }

    public IRLinkedList<N, L> getParentList()
    {
        return parentList;
    }

    public void init()
    {
        this.prev = null;
        this.next = null;
        this.parentList = null;
    }

    public void insertBefore(IRListNode<N, L> node)
    {
        this.next = node;
        this.prev = node.prev;
        node.prev = this;
        if (this.prev != null)
        {
            this.prev.next = this;
        }
        this.parentList = node.parentList;
        this.parentList.size++;
        if (this.parentList.head == node)
        {
            this.parentList.head = this;
        }
    }

    public void insertAfter(IRListNode<N, L> node)
    {
        this.prev = node;
        this.next = node.next;
        node.next = this;
        if (this.next != null)
        {
            this.next.prev = this;
        }
        this.parentList = node.getParentList();
        this.parentList.size++;
        if (this.parentList.tail == node)
        {
            this.parentList.tail = this;
        }
    }

//    public IRListNode<N, L> removeFromList()
//    {
//        parent.removeNode();
//        if (parent.getHead() == this)
//        {
//            this.parent.setHead(this.next);
//        }
//        if (parent.getTail() == this)
//        {
//            this.parent.setTail(this.prev);
//        }
//        if (this.prev != null)
//        {
//            this.prev.setNext(this.next);
//        }
//        if (this.next != null)
//        {
//            this.next.setPrev(this.prev);
//        }
//        init();
//        return this;
//    }


}
