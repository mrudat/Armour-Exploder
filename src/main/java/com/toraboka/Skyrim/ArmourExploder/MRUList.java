package com.toraboka.Skyrim.ArmourExploder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.list.AbstractLinkedList;

public class MRUList<E> extends AbstractLinkedList<E> implements List<E> {

    private final Map<E, Node<E>> nodeMap = new ConcurrentHashMap<E, Node<E>>();

    public MRUList() {
        super();
        init();
    }
    
    @Override
    protected synchronized org.apache.commons.collections4.list.AbstractLinkedList.Node<E> createNode(
            E value) {
        org.apache.commons.collections4.list.AbstractLinkedList.Node<E> node = super
                .createNode(value);
        nodeMap.put(value, node);
        return node;
    }

    @Override
    public synchronized boolean remove(final Object value) {
        org.apache.commons.collections4.list.AbstractLinkedList.Node<E> node = nodeMap
                .get(value);
        if (node == null) {
            return false;
        }
        super.removeNode(node);
        nodeMap.remove(value);
        return true;
    }

    public synchronized boolean use(final E value) {
        org.apache.commons.collections4.list.AbstractLinkedList.Node<E> node = nodeMap
                .get(value);
        if (node == null) {
            return false;
        }
        removeNode(node);
        addNode(node, getNode(0, true));
        return true;
    }

    @Override
    protected synchronized void addNode(
            org.apache.commons.collections4.list.AbstractLinkedList.Node<E> nodeToInsert,
            org.apache.commons.collections4.list.AbstractLinkedList.Node<E> insertBeforeNode) {
        super.addNode(nodeToInsert, insertBeforeNode);
    }

    @Override
    protected synchronized void removeNode(
            org.apache.commons.collections4.list.AbstractLinkedList.Node<E> node) {
        super.removeNode(node);
    }

    public void addIfMissing(E value) {
        if (!nodeMap.containsKey(value)) {
            add(value);
        }
    }

}
