package principal;

import java.io.*;
import java.util.Scanner;

public class YouTubeMP3DownloaderCLI {
    private static final String OUTPUT_PATH = "C:/Users/Angel J Ragel/Music/New Music/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Verificar si el directorio de salida existe, si no, crearlo
        File outputDir = new File(OUTPUT_PATH);
        if (!outputDir.exists()) {
            outputDir.mkdirs();  // Crear directorio si no existe
        }

        while (true) {
            System.out.print("Enter YouTube URL: ");
            String url = scanner.nextLine();

            if (url.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program.");
                break;  // Salir del programa si el usuario escribe "exit"
            }
            // Iniciar la descarga del audio
            downloadAudio(url);
        }
        scanner.close();
    }

    private static void downloadAudio(String url) {
        try {
            // Usamos yt-dlp para obtener el título del video y descargar el audio en formato mp3
            ProcessBuilder pb = new ProcessBuilder(
                    "yt-dlp", "-x", "--audio-format", "mp3", "--no-playlist", "--verbose",
                    "-o", OUTPUT_PATH + "%(title)s.%(ext)s", url
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Mostrar las salidas del proceso de descarga
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            process.waitFor();

            // Revisión del directorio para encontrar el archivo descargado más reciente
            File folder = new File(OUTPUT_PATH);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3"));
            if (files != null && files.length > 0) {
                File latestFile = files[0];
                System.out.println("Download complete: " + latestFile.getAbsolutePath());
            } else {
                System.out.println("No MP3 file found in the specified directory.");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
