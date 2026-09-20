import DataStructers.*;
import FileProcess.*;
import java.nio.file.*;
import java.util.List;

public class ProjectChecks {
    private static int checks;

    private static void check(boolean passed, String message) {
        if (!passed) throw new AssertionError(message);
        checks++;
    }

    private static MyLinkedList student(List<MyLinkedList> list, String number) {
        for (MyLinkedList item : list) {
            if (!item.isEmpty() && item.getStr2(0).equals(number)) return item;
        }
        throw new AssertionError("Öğrenci bulunamadı: " + number);
    }

    public static void main(String[] args) throws Exception {
        // Bu test boş, geçici bir çalışma klasöründe çalıştırılır.
        check(!Files.exists(Path.of("src")), "Testi proje klasöründe çalıştırmayın.");
        String source = Files.readString(Path.of(args[0], "source.txt"));
        String target = Files.readString(Path.of(args[0], "target.txt"));
        check(!ListValidation.check("  \n", false).isEmpty(), "Boş kaynak");
        check(!ListValidation.check(null, true).isEmpty(), "Null hedef");
        check(!ListValidation.check("123", false).isEmpty(), "Eksik ad");
        check(!ListValidation.check("123 NaN", true).isEmpty(), "NaN not");
        check(!ListValidation.check("123 101", true).isEmpty(), "Not aralığı");
        check(ListValidation.check("123 20\n123 30", true).isEmpty(), "Tekrar eden hedef numarası kabul edilmeli");
        check(ListValidation.check("123 Ali\n123 Ali", false).isEmpty(), "Tekrar eden kaynak numarası kabul edilmeli");
        check(ListValidation.check(source.replace("\n", "\r\n"), false).isEmpty(), "Windows satırları");
        SourceListAndTargetListİmplementaion engine = new SourceListAndTargetListİmplementaion();
        check(engine.returnTable() != null, "Başlangıç tablosu null değil");
        try {
            engine.implement(source, "", "Matematik");
            throw new AssertionError("Boş hedef kabul edildi");
        } catch (IllegalArgumentException expected) { checks++; }
        List<MyLinkedList> list = engine.implement(source, target, "Matematik");
        check(student(list, "230501001").getStr2(1).equals("75"), "Sırasız eşleştirme");
        engine.implement(source, "230501001 95", "Matematik");
        check(student(list, "230501001").size2() == 2, "Tekrarlanan ders eklenmemeli");
        check(student(list, "230501001").getStr2(1).equals("95"), "Not güncellenmeli");
        engine.implement(source, "230501001 60", "Fizik");
        check(student(list, "230501001").size2() == 3, "İkinci ders");
        engine.implement(source, "999 10\n230501002 88\n998 20", "Matematik");
        check(student(list, "230501002").getStr2(1).equals("88"), "Bilinmeyen numaralar eşleşmeyi durdurmamalı");
        engine.implement(source, "230501001 80\n230501001 95", "Matematik");
        check(student(list, "230501001").getStr2(1).equals("95"), "Tekrar eden hedefte son not kullanılmalı");
        SeperateChaniningTable full = new SeperateChaniningTable();
        for (int i = 0; i < full.getterSize(); i++) full.insertNamesAndNumbers("" + i, "Aynı İsim");
        check(student(full.getterList(), "1000") != null, "Aynı isimli öğrenciler");
        try {
            full.insertNamesAndNumbers("2000", "Yeni Öğrenci");
            throw new AssertionError("Dolu tablo kabul edildi");
        } catch (IllegalStateException expected) { checks++; }
        check(full.matchingNumbers("Ders", "2000", "10") == full.getterList(),
                "Dolu tabloda bilinmeyen numara sessizce atlanmalı");

        ExtractSourceFiles sources = new ExtractSourceFiles();
        check(!sources.addNewSourceList("", "ali", "Sinif"), "Boş liste yazılmamalı");
        check(sources.addNewSourceList(source, "ali", "Sinif-A"), "Liste ekleme");
        check(!sources.addNewSourceList(source, "ali", "Sinif-A"), "Aynı liste adı");
        check(sources.addNewSourceList(source, "ayse", "Sinif-A"), "Başka kullanıcının aynı adı");
        check(sources.chooseSourceList("ali", "Sinif-A").strip().equals(source.strip()), "Tireli liste okuma");
        check(sources.chooseSourceList("mehmet", "Sinif-A").isEmpty(), "Kullanıcı listesi ayrımı");
        WriteResults writer = new WriteResults();
        Path aliPath = sources.logPath("Sinif-A");
        check(aliPath.equals(Path.of("src/TxtFiles/SourceListLogs/SourceListLogsSinif-A.txt")),
                "Seçilen listenin mevcut log dosyası kullanılmalı");
        check(Files.exists(aliPath), "Liste eklenince log dosyası oluşturulmalı");
        check(writer.writeLessonNotes(list, aliPath), "Not kaydetme");
        SourceListAndTargetListİmplementaion reload = new SourceListAndTargetListİmplementaion();
        reload.readOldInformation(aliPath);
        check(student(reload.returnTable(), "230501001").getStr2(2).equals("60"), "Notları tekrar okuma");
        sources.addNewSourceList(source, "mehmet", "Sinif-A");
        check(!Files.readString(aliPath).isEmpty(), "Liste eklemek mevcut not dosyasını boşaltmamalı");

        Path output = Path.of("exports");
        MakeTxtFiles exports = new MakeTxtFiles(output);
        check(exports.makeTxtLesson(list, "Matematik"), "Ders çıktısı");
        check(exports.makeTxtStudentNumbers(list, "230501001"), "Öğrenci çıktısı");
        check(exports.makeTxtAllOfThem(list, "Sinif"), "Tüm liste çıktısı");
        check(exports.makeTxtLesson(list, "Matematik"), "Tekrar çıktı");
        check(!exports.makeTxtLesson(list, "Olmayan"), "Olmayan ders");
        check(!exports.makeTxtStudentNumbers(list, "999"), "Olmayan öğrenci");
        check(!exports.makeTxtAllOfThem(list, "../test"), "Dosya adı");
        try (var files = Files.list(output)) {
            List<Path> paths = files.toList();
            check(paths.size() == 4, "Boş/hatalı çıktı dosyası oluşmamalı");
            for (Path path : paths) {
                String text = Files.readString(path);
                check(text.contains("Ali Yılmaz") && text.contains("95"), "Çıktı içeriği");
            }
        }
        Files.writeString(Path.of("blocked"), "file");
        check(!new MakeTxtFiles(Path.of("blocked")).makeTxtAllOfThem(list, "Sinif"), "Yazma hatası");
        String hash = "" + "deneme123".hashCode();
        check(writer.writeNewMemberInformation("ali", hash, "ali@example.com"), "Üye kayıt");
        check(Files.readString(Path.of("src/TxtFiles/MembersInformation.txt"))
                .equals("ali  " + hash + "  ali@example.com\n"), "Şifre eski hashCode biçiminde saklanmalı");
        MemberInformaion member = new MemberInformaion();
        check(member.checkUserInformation("ali", hash), "Giriş");
        check("ali@example.com".equals(member.getterEmail()), "E-posta");
        check(!member.checkUserInformation("ali", "" + "yanlis".hashCode()), "Yanlış giriş");
        FileEngine facade = new FileEngine(sources, exports, member,
                new SourceListAndTargetListİmplementaion(), writer);
        check(!facade.RegisterProcess("", "abc", "abc"), "Boş kullanıcı adı");
        check(!facade.RegisterProcess("yeni", "", ""), "Boş şifre");
        check(!facade.RegisterProcess("yeni", "abc", "def"), "Şifre tekrarı");
        check(!facade.RegisterProcess("ali", "abc", "abc"), "Mevcut kullanıcı");
        check(facade.SignInProcess("ali", hash), "FileEngine girişi");
        check(facade.writeSouceListLogs(source, "230501001 99", "Matematik", "Sinif-A"), "FileEngine kaydetme");
        List<MyLinkedList> reopened = facade.writeResultsOnTextArea(
                source, "230501002 70", "Fizik", "Sinif-A");
        check(student(reopened, "230501001").getStr2(1).equals("99"), "Eski ders korunmalı");
        check(student(reopened, "230501001").getStr2(2).equals("60"), "İkinci eski ders korunmalı");
        check(facade.writeSouceListLogs(source, "230501002 70", "Fizik", "Sinif-A"), "Tekrar kaydetme");
        check(student(facade.returnTable(), "230501002").size2() == 3, "Kaydetme notları çoğaltmamalı");
        String before = Files.readString(aliPath);
        check(facade.writeSouceListLogs(source, "999 5", "Matematik", "Sinif-A"),
                "Hiçbir hedef eşleşmese de kaydetme engellenmemeli");
        check(Files.readString(aliPath).equals(before), "Eşleşmeyen hedef eski notları değiştirmemeli");
        try (var files = Files.list(aliPath.getParent())) {
            check(files.noneMatch(Files::isDirectory), "Kullanıcıya özel log klasörü oluşturulmamalı");
        }
        System.out.println(checks + " kontrol başarılı.");
    }
}
