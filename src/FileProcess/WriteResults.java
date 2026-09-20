/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileProcess;

/**
 *
 * @author aliemre
 */
import DataStructers.MyLinkedList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class WriteResults {

    public WriteResults() {
    }

    public boolean writeNewMemberInformation(String username, String password, String email) {

        try {
            if (username == null || !username.matches("[\\p{L}\\p{N}_]+")
                    || password == null || password.isBlank()) return false;
            if (email == null || !email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
                return false;
            }
            String line = username + "  " + password + "  " + email + "\n";
            Path path = Paths.get("src", "TxtFiles", "MembersInformation.txt");
            Files.createDirectories(path.getParent());
            Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "üye txtye yazılamadı");
        }

        return false;
    }

    public boolean writeLessonNotes(List<MyLinkedList> list, Path path) {

        try {
            Files.createDirectories(path.getParent());
            // İçeriği önce bellekte hazırla; hatalı veri mevcut dosyayı boşaltmasın.
            StringBuilder text = new StringBuilder();
            for (MyLinkedList student : list) {
                if (student.isEmpty()) continue;
                for (int i = 0; i < student.size2(); i++) {
                    text.append(student.getStr1(i)).append(" : ")
                            .append(student.getStr2(i)).append("  ->  ");
                }
                text.append('\n');
            }
            Path temporary = Files.createTempFile(path.getParent(), "notes-", ".tmp");
            try {
                Files.writeString(temporary, text);
                Files.move(temporary, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }


        }catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        
        }
        return true;
        }
    
        //
    
    
}
