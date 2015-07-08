package br.edu.ufcg.dsc.ia.marvin.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LocalProperties {
    private static final File LOCAL_PROPERTIES_FILE = new File("local.properties");
    private static final String ROBOCODE_DIRECTORY_PROPERTY = "robocode.dir";
    private static final String NUMBER_OF_ITERATIONS_PROPERTY = "iterations";

    private static LocalProperties instance = new LocalProperties();

    public static LocalProperties instance() {
        return instance;
    }

    public File robocodeDirectory() {
        return this.robocodeDirectoryFrom(this.readProperty(ROBOCODE_DIRECTORY_PROPERTY));
    }

    public int numberOfIterations() {
        return this.numberOfIterationsFrom(this.readProperty(NUMBER_OF_ITERATIONS_PROPERTY));
    }

    private String readProperty(String propertyName) {
        Scanner scanner = this.scannerForLocalPropertiesFile();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (line.startsWith(propertyName)) {
                scanner.close();
                return line.substring(propertyName.length() + 1);
            }
        }

        scanner.close();

        throw new RuntimeException("Local property " + propertyName + "should be defined");
    }

    private Scanner scannerForLocalPropertiesFile() {
        try {
            return new Scanner(LOCAL_PROPERTIES_FILE);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + LOCAL_PROPERTIES_FILE.getPath() + " not found");
        }
    }

    private File robocodeDirectoryFrom(String property) {
        File robocodeDirectory = new File(property);

        if (!robocodeDirectory.exists()) {
            throw new RuntimeException("Invalid path assigned to local property 'robocode.dir': " + robocodeDirectory.getPath());
        }

        return robocodeDirectory;
    }

    private int numberOfIterationsFrom(String property) {
        try {
            return Integer.valueOf(property);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Local property 'iterations' should be an integer");
        }
    }
}
