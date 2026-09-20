package FileProcess;

import DataStructers.MyLinkedList;
import DataStructers.SeperateChaniningTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SourceListAndTargetListİmplementaion {
    private SeperateChaniningTable table = new SeperateChaniningTable();

    public List<MyLinkedList> implement(String sourceList, String targetList, String lessonName) {
        String error = ListValidation.check(sourceList, false);
        if (error.isEmpty()) {
            error = ListValidation.check(targetList, true);
        }
        if (!error.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
        if (!ListValidation.validName(lessonName)) {
            throw new IllegalArgumentException("Geçerli bir ders adı girin.");
        }
        for (String line : sourceList.strip().split("\\R")) {
            String[] parts = line.trim().split("\\s+", 2);
            table.insertNamesAndNumbers(parts[0], parts[1]);
        }
        for (String line : targetList.strip().split("\\R")) {
            String[] parts = line.trim().split("\\s+", 2);
            table.matchingNumbers(lessonName, parts[0], parts[1]);
        }
        return table.getterList();
    }

    public void readOldInformation(Path path) {
        if (!Files.exists(path)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(path)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] items = line.split("  ->  ");
                String[] student = items[0].split(" : ", 2);
                if (student.length != 2) {
                    throw new IllegalStateException("Kayıtlı not dosyasının biçimi bozuk.");
                }
                table.insertNamesAndNumbers(student[1], student[0]);
                for (int i = 1; i < items.length; i++) {
                    String[] lesson = items[i].split(" : ", 2);
                    if (lesson.length != 2) {
                        throw new IllegalStateException("Kayıtlı ders satırının biçimi bozuk.");
                    }
                    table.matchingNumbers(lesson[0], student[1], lesson[1]);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Eski notlar okunamadı; işlem durduruldu.", e);
        }
    }

    public void resetTable() {
        table = new SeperateChaniningTable();
    }

    public List<MyLinkedList> returnTable() {
        return table.getterList();
    }
}
