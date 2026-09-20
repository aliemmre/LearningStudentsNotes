package FileProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ExtractSourceFiles {
    private final Path sourceFile = Paths.get("src", "TxtFiles", "SourceLists.txt");

    public boolean addNewSourceList(String sourceList, String username, String sourceListName) {
        if (!ListValidation.check(sourceList, false).isEmpty()
                || !ListValidation.validName(sourceListName)
                || username == null || !username.matches("[\\p{L}\\p{N}_]+")) {
            return false;
        }
        if (!chooseSourceList(username, sourceListName).isEmpty()) {
            return false;
        }
        sourceList = sourceList.strip().replace("\r\n", "\n").replace('\r', '\n');
        String header = sourceList.split("\n").length + "-" + username + "-" + sourceListName;
        try {
            Files.createDirectories(sourceFile.getParent());
            Path logFile = logPath(sourceListName);
            Files.createDirectories(logFile.getParent());
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }
            Files.writeString(sourceFile, header + "\n" + sourceList + "\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.out.println("Liste yazılamadı: " + e.getMessage());
            return false;
        }
    }

    public String collectSourceList(String username) {
        return readSourceLists(username, null);
    }

    public String chooseSourceList(String username, String sourceListName) {
        return readSourceLists(username, sourceListName);
    }

    private String readSourceLists(String username, String selectedName) {
        StringBuilder result = new StringBuilder();
        if (!Files.exists(sourceFile)) {
            return "";
        }
        try (BufferedReader reader = Files.newBufferedReader(sourceFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] header = line.split("-", 3);
                if (header.length != 3) {
                    throw new IOException("Liste başlığı bozuk.");
                }
                int count = Integer.parseInt(header[0]);
                if (count < 1) {
                    throw new IOException("Öğrenci sayısı geçersiz.");
                }
                boolean selected = header[1].equals(username)
                        && (selectedName == null || header[2].equals(selectedName));
                if (selected && selectedName == null) {
                    result.append(header[2]).append('\n');
                }
                for (int i = 0; i < count; i++) {
                    String student = reader.readLine();
                    if (student == null) {
                        throw new IOException("Liste tamamlanmamış.");
                    }
                    if (selected && selectedName != null) {
                        result.append(student).append('\n');
                    }
                }
                if (selected && selectedName != null) {
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Kaynak listeler okunamadı: " + e.getMessage(), e);
        }
        return result.toString();
    }

    public Path logPath(String listName) {
        if (!ListValidation.validName(listName)) {
            throw new IllegalArgumentException("Liste adı geçersiz.");
        }
        return Paths.get("src", "TxtFiles", "SourceListLogs", "SourceListLogs" + listName + ".txt");
    }
}
