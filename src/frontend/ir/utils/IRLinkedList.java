package frontend.ir.utils;

import java.util.Iterator;

public class IRLinkedList<N, L> implements Iterable<IRListNode<N, L>>
{
    IRListNode<N, L> head;
    IRListNode<N, L> tail;
    final L container;
    int size;

    public IRLinkedList(L container)
    {
        this.head = null;
        this.container = container;
        this.tail = null;
        this.size = 0;
    }

    public IRListNode<N, L> getHead()
    {
        return head;
    }

    public IRListNode<N, L> getTail()
    {
        return tail;
    }

    public L getContainer()
    {
        return container;
    }

    public boolean isEmpty()
    {
        return (this.head == null) && (this.tail == null) && (size == 0);
    }

    public void insertAtHead(IRListNode<N, L> node)
    {
        node.parentList = this;
        if (this.head == null)
        {
            this.head = node;
            this.tail = node;
            this.size++;
        }
        else
        {
            node.insertBefore(this.head);
        }
    }

    public void insertAtTail(IRListNode<N, L> node)
    {
        node.parentList = this;
        if (this.tail == null)
        {
            this.head = node;
            this.tail = node;
            this.size++;
        }
        else
        {
            node.insertAfter(this.tail);
        }
    }

    @Override
    public Iterator<IRListNode<N, L>> iterator()
    {
        return new IRNodeIterator(this.head);
    }

    class IRNodeIterator implements Iterator<IRListNode<N, L>>
    {
        IRListNode<N, L> currentNode = new IRListNode<>(null);
        IRListNode<N, L> nextNode = null;

        public IRNodeIterator(IRListNode<N, L> head)
        {
            currentNode.next = head;
        }

        @Override
        public boolean hasNext()
        {
            return nextNode != null || currentNode.next != null;
        }

        @Override
        public IRListNode<N, L> next()
        {
            if (nextNode == null)
            {
                currentNode = currentNode.next;
            }
            else
            {
                currentNode = nextNode;
            }
            nextNode = null;
            return currentNode;
        }

        @Override
        public void remove()
        {
            IRListNode<N, L> prev = currentNode.prev;
            IRListNode<N, L> next = currentNode.next;
            IRLinkedList<N, L> parentList = currentNode.parentList;
            if (prev != null)
            {
                prev.next = next;
            }
            else
            {
                parentList.head = next;
            }
            if (next != null)
            {
                next.prev = prev;
            }
            else
            {
                parentList.tail = prev;
            }
            parentList.size--;
            this.nextNode = next;
            currentNode.init();
        }
    }
}
