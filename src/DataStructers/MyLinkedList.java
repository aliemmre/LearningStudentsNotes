/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructers;



/**
 *
 * @author aliemre
 */
public class MyLinkedList {
 private Node head;

    private int size;

    private class Node {

        String str1;

        String str2;

        Node next;

        Node(String str1, String str2) {

            this.str1 = str1;

            this.str2 = str2;

            this.next = null;

        }

    }

    // Başa ekle

    public void addFirst(String str1, String str2) {

        Node newNode = new Node(str1, str2);

        newNode.next = head;

        head = newNode;

        size++;

    }

    // Sona ekle

    public void addLast(String str1, String str2) {

        Node newNode = new Node(str1, str2);

        if (head == null) {

            head = newNode;

            size++;

            return;

        }

        Node current = head;

        while (current.next != null) {

            current = current.next;

        }

        current.next = newNode;

        size++;

    }

    // Belirli index'e ekle

    public void add(int index, String str1, String str2) {

        if (index < 0 || index > size) {

            throw new IndexOutOfBoundsException();

        }

        if (index == 0) {

            addFirst(str1, str2);

            return;

        }

        if (index == size) {

            addLast(str1, str2);

            return;

        }

        Node newNode = new Node(str1, str2);

        Node current = head;

        for (int i = 0; i < index - 1; i++) {

            current = current.next;

        }

        newNode.next = current.next;

        current.next = newNode;

        size++;

    }

    // Baştaki elemanı sil

    public void removeFirst() {

        if (head == null) {

            return;

        }

        head = head.next;

        size--;

    }

    // Sondaki elemanı sil

    public void removeLast() {

        if (head == null) {

            return;

        }

        if (head.next == null) {

            head = null;

            size--;

            return;

        }

        Node current = head;

        while (current.next.next != null) {

            current = current.next;

        }

        current.next = null;

        size--;

    }

    // Index'teki elemanı sil

    public void remove(int index) {

        if (index < 0 || index >= size) {

            throw new IndexOutOfBoundsException();

        }

        if (index == 0) {

            removeFirst();

            return;

        }

        Node current = head;

        for (int i = 0; i < index - 1; i++) {

            current = current.next;

        }

        current.next = current.next.next;

        size--;

    }

    // Index'teki node'u getir

    public String[] get(int index) {

        if (index < 0 || index >= size) {

            throw new IndexOutOfBoundsException();

        }

        Node current = head;

        for (int i = 0; i < index; i++) {

            current = current.next;

        }

        return new String[]{current.str1, current.str2};

    }

    // İlk data'yı getir

    public String getStr1(int index) {

        if (index < 0 || index >= size) {

            throw new IndexOutOfBoundsException();

        }

        Node current = head;

        for (int i = 0; i < index; i++) {

            current = current.next;

        }

        return current.str1;

    }

    // İkinci data'yı getir

    public String getStr2(int index) {

        if (index < 0 || index >= size) {

            throw new IndexOutOfBoundsException();

        }

        Node current = head;

        for (int i = 0; i < index; i++) {

            current = current.next;

        }

        return current.str2;

    }

    // Listenin ilk node'unu göster

    public String[] peekFirst() {

        if (head == null) {

            return null;

        }

        return new String[]{head.str1, head.str2};

    }

    // Listenin boş olup olmadığını kontrol et

    public boolean isEmpty() {

        return head == null;

    }

    // Eleman sayısı


    public int size2() {

        return size;

    }

    // Listeyi temizle

    public void clear() {

        head = null;

        size = 0;

    }

    // Belirli bir String'in varlığını kontrol et

    public boolean contains(String str1, String str2) {

        Node current = head;

        while (current != null) {

            if (current.str1.equals(str1)

                    && current.str2.equals(str2)) {

                return true;

            }

            current = current.next;

        }

        return false;

    }

    // str1'e göre ara

    public String[] findByStr1(String str1) {

        Node current = head;

        while (current != null) {

            if (current.str1.equals(str1)) {

                return new String[]{current.str1, current.str2};

            }

            current = current.next;

        }

        return null;

    }

    // Listeyi yazdır

    public void printList() {

        Node current = head;

        while (current != null) {

            System.out.println(

                    "str1: " + current.str1

                    + " | str2: " + current.str2

            );

            current = current.next;

        }

    }

}