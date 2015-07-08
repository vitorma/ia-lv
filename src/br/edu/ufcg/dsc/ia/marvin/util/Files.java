package br.edu.ufcg.dsc.ia.marvin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Files {

    public static void copy(File sourceFile, File destinationFile) throws IOException {
        FileChannel source = new FileInputStream(sourceFile).getChannel();
        FileChannel destination = new FileOutputStream(destinationFile).getChannel();

        long size = source.size();
        source.transferTo(0, size, destination);

        source.close();
        destination.close();
    }

    public static File ensuredDirectory(String path) {
        File directory = new File(path);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        return directory;
    }
}
