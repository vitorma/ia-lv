package br.edu.ufcg.dsc.ia.coppelia.infrastructure;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Action;
import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;
import br.edu.ufcg.dsc.ia.coppelia.util.LocalProperties;

public class CoppeliaWriter {
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


    public void write(Coppelia coppelia) throws FileNotFoundException {
        String filename = filenameOf(coppelia);

        OutputStream outputFile =
                new BufferedOutputStream(new FileOutputStream(filename));

        try {
            outputFile.write(generateJavaSourceFrom(coppelia).getBytes(Charset.forName("US-ASCII")));
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

    public String generateJavaSourceFrom(Coppelia coppelia) {
        String template =
                readTemplate();

        return template
                .replace(ROBOT_NAME, Coppelia.class.getSimpleName() + coppelia.instanceNumber())
                .replace(RUN, toString(coppelia.runActions()))
                .replace(SCANNED_ROBOT, toString(coppelia.scanEnemyActions()))
                .replace(HIT_WALL, toString(coppelia.hitWallActions()))
                .replace(HIT_ROBOT, toString(coppelia.hitRobotActions()))
                .replace(HIT_BY_BULLET, toString(coppelia.hitByBulletActions()));

    }

    public String toString(List<Action> actions) {
        StringBuffer buffer = new StringBuffer();

        for (Action action : actions) {
            buffer.append(INDENT).append(action.toString()).append(";\n");
        }

        return buffer.toString();
    }

    public String filenameOf(Coppelia coppelia) {
        return SRC_FOLDER + SEPARATOR + Coppelia.class.getCanonicalName().replaceAll("\\.", "/")
                + coppelia.instanceNumber()
                + JAVA_EXTENSION;
    }

    public String classnameOf(Coppelia coppelia) {
        return CLASS_FOLDER + SEPARATOR + Coppelia.class.getCanonicalName().replaceAll("\\.", "/")
                + coppelia.instanceNumber()
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
