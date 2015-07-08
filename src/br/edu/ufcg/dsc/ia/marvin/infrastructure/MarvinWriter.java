package br.edu.ufcg.dsc.ia.marvin.infrastructure;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Action;
import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Marvin;
import br.edu.ufcg.dsc.ia.marvin.util.LocalProperties;

public class MarvinWriter {
    private static final String JAVA_EXTENSION = ".java";
    private static final String CLASS_EXTENSION = ".class";
    private static final String SRC_FOLDER = "src";
    private static final String CLASS_FOLDER = "bin";
    private static final String SEPARATOR = "/";
    private static final String TEMPLATE = "templates/Robot.template";
    private static final String RUN = "{RUN}";
    private static final String ROBOT_NAME = "{ROBOT_NAME}";
    private static final String SCANNED_ROBOT = "{SCANNED_ROBOT}";
    private static final String HIT_WALL = "{HIT_WALL}";
    private static final String HIT_ROBOT = "{HIT_ROBOT}";
    private static final String HIT_BY_BULLET = "{HIT_BY_BULLET}";
    private static final String INDENT = "        ";
    private static final String ROBOTS = "robots";


    public void write(Marvin marvin) throws FileNotFoundException {
        String filename = filenameOf(marvin);

        OutputStream outputFile =
                new BufferedOutputStream(new FileOutputStream(filename));

        try {
            outputFile.write(generateJavaSourceFrom(marvin).getBytes(Charset.forName("US-ASCII")));
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot write file");
        }

        String robocodeHome = LocalProperties.instance().robocodeDirectory().getAbsolutePath();

        String args[] = new String[]{
                filename,
                "-classpath",
                "lib/robocode.jar",
                "-sourcepath",
                SRC_FOLDER,
                "-d",
                robocodeHome + SEPARATOR + ROBOTS


        };

        com.sun.tools.javac.Main.compile(args);

    }

    public String generateJavaSourceFrom(Marvin marvin) {
        String template =
                readTemplate();

        return template
                .replace(ROBOT_NAME, Marvin.class.getSimpleName() + marvin.instanceNumber())
                .replace(RUN, toString(marvin.runActions()))
                .replace(SCANNED_ROBOT, toString(marvin.scanEnemyActions()))
                .replace(HIT_WALL, toString(marvin.hitWallActions()))
                .replace(HIT_ROBOT, toString(marvin.hitRobotActions()))
                .replace(HIT_BY_BULLET, toString(marvin.hitByBulletActions()));

    }

    public String toString(List<Action> actions) {
        StringBuffer buffer = new StringBuffer();

        for (Action action : actions) {
            buffer.append(INDENT).append(action.toString()).append(";\n");
        }

        return buffer.toString();
    }

    public String filenameOf(Marvin marvin) {
        return SRC_FOLDER + SEPARATOR + Marvin.class.getCanonicalName().replaceAll("\\.", "/")
                + marvin.instanceNumber()
                + JAVA_EXTENSION;
    }

    public String classnameOf(Marvin marvin) {
        return CLASS_FOLDER + SEPARATOR + Marvin.class.getCanonicalName().replaceAll("\\.", "/")
                + marvin.instanceNumber()
                + CLASS_EXTENSION;
    }

    public static String readTemplate() {
        String content;
        try {
            content = new Scanner(new File(TEMPLATE), "UTF-8").useDelimiter("\\A").next();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot read template");
        }

        return content;
    }
}
