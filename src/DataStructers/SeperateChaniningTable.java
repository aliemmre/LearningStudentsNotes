package DataStructers;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.hash;

// Mevcut sınıf adı korunuyor. Çakışmalar linear probing ile çözülüyor.
public class SeperateChaniningTable {
    private final List<MyLinkedList> list = new ArrayList<>();
    private final int defaultSize = 1001;

    public SeperateChaniningTable() {
        for (int i = 0; i < defaultSize; i++) {
            list.add(new MyLinkedList());
        }
    }

    public int getterSize() {
        return defaultSize;
    }

    private int findSlot(String number) {
        int index = Math.floorMod(hash(number), defaultSize);
        // En fazla bir tam tur: tablo dolu olsa bile döngü biter.
        for (int count = 0; count < defaultSize; count++) {
            MyLinkedList student = list.get(index);
            if (student.isEmpty() || student.getStr2(0).equals(number)) {
                return index;
            }
            index = (index + 1) % defaultSize;
        }
        return -1;
    }

    public void insertNamesAndNumbers(String number, String name) {
        int index = findSlot(number);
        if (index == -1) {
            throw new IllegalStateException("Liste en fazla " + defaultSize + " öğrenci içerebilir.");
        }
        MyLinkedList student = list.get(index);
        if (student.isEmpty()) {
            student.addFirst(name, number);
        }
    }

    public List<MyLinkedList> matchingNumbers(String lessonName, String number, String note) {
        int index = findSlot(number);
        if (index == -1 || list.get(index).isEmpty()) {
            // Kaynak listede olmayan hedef numaralarını atla.
            return list;
        }
        MyLinkedList student = list.get(index);
        // Aynı ders yeniden girilirse eski notu değiştir.
        for (int i = 1; i < student.size2(); i++) {
            if (student.getStr1(i).equals(lessonName)) {
                student.remove(i);
                student.add(i, lessonName, note);
                return list;
            }
        }
        student.addLast(lessonName, note);
        return list;
    }

    public List<MyLinkedList> getterList() {
        return list;
    }
}
