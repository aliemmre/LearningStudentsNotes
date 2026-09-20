package FileProcess;

public class ListValidation {

    // Boş String dönerse liste geçerlidir; aksi halde hata mesajını döndürür.
    public static String check(String text, boolean targetList) {
        if (text == null || text.isBlank()) {
            return targetList ? "Hedef liste boş olamaz." : "Kaynak liste boş olamaz.";
        }
        String[] lines = text.strip().split("\\R");
        if (!targetList && lines.length > 1001) {
            return "Kaynak liste en fazla 1001 öğrenci içerebilir.";
        }
        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].trim().split("\\s+", 2);
            if (parts.length != 2 || !parts[0].matches("[0-9]+")) {
                return (i + 1) + ". satır: öğrenci numarası ve "
                        + (targetList ? "not girin." : "ad soyad girin.");
            }
            if (targetList) {
                try {
                    double note = Double.parseDouble(parts[1]);
                    if (!Double.isFinite(note) || note < 0 || note > 100) {
                        return (i + 1) + ". satır: not 0 ile 100 arasında olmalı.";
                    }
                } catch (NumberFormatException e) {
                    return (i + 1) + ". satır: not sayısal olmalı.";
                }
            } else if (parts[1].contains(" : ") || parts[1].contains("  ->  ")) {
                return (i + 1) + ". satır: isim dosya ayırıcılarını içeremez.";
            }

        }
        return "";
    }

    public static boolean validName(String name) {
        return name != null && name.matches("[\\p{L}\\p{N}_ -]+") && !name.isBlank();
    }
}
