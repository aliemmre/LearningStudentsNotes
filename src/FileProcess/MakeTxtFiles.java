package FileProcess;

import DataStructers.MyLinkedList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MakeTxtFiles {
    private final Path outputDirectory;

    public MakeTxtFiles() {
        this(Paths.get(System.getProperty("user.home"), "Desktop"));
    }

    public MakeTxtFiles(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public boolean makeTxtLesson(List<MyLinkedList> list, String lessonName) {
        if (list == null || !ListValidation.validName(lessonName)) return false;
        StringBuilder text = new StringBuilder();
        for (MyLinkedList student : list) {
            for (int i = 1; i < student.size2(); i++) {
                if (student.getStr1(i).equals(lessonName)) {
                    text.append(student.getStr1(0)).append(" -> ")
                            .append(student.getStr2(0)).append(" -> ")
                            .append(student.getStr2(i)).append('\n');
                    break;
                }
            }
        }
        return writeFile(lessonName, lessonName + " RESULTS", text.toString());
    }

    public boolean makeTxtStudentNumbers(List<MyLinkedList> list, String number) {
        if (list == null || number == null || !number.matches("[0-9]+")) return false;
        for (MyLinkedList student : list) {
            if (!student.isEmpty() && student.getStr2(0).equals(number)) {
                return writeFile(number, number + ": " + student.getStr1(0), studentText(student));
            }
        }
        return false;
    }

    public boolean makeTxtAllOfThem(List<MyLinkedList> list, String fileName) {
        if (list == null || !ListValidation.validName(fileName)) return false;
        StringBuilder text = new StringBuilder();
        for (MyLinkedList student : list) {
            if (!student.isEmpty()) {
                text.append(studentText(student)).append("\n--------------------\n");
            }
        }
        return writeFile(fileName + "-All-Results", "All RESULTS", text.toString());
    }

    private String studentText(MyLinkedList student) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < student.size2(); i++) {
            text.append(student.getStr1(i)).append(" -> ")
                    .append(student.getStr2(i)).append('\n');
        }
        return text.toString();
    }

    // Üç çıktı türünün ortak tarih, dosya adı ve yazma işlemleri burada.
    private boolean writeFile(String fileName, String title, String content) {
        if (content.isBlank()) return false;
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        try {
            Files.createDirectories(outputDirectory);
            Path path = outputDirectory.resolve(fileName + "-" + date + ".txt");
            // Aynı gün tekrar çıktı alınırsa mevcut dosyayı ezmeden yeni isim kullan.
            int number = 2;
            while (Files.exists(path)) {
                path = outputDirectory.resolve(fileName + "-" + date + "-" + number + ".txt");
                number++;
            }
            Files.writeString(path, "******** " + title + " " + date + " ********\n" + content,
                    StandardOpenOption.CREATE_NEW);
            return true;
        } catch (IOException e) {
            System.out.println("TXT oluşturulamadı: " + e.getMessage());
            return false;
        }
    }
}
