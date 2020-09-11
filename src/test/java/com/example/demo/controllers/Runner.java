package com.example.demo.controllers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Runner {
    public static void main(String[] args) {
        File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath() + "/metric-log");
        System.out.println(jarDir.getAbsolutePath());
        if (!jarDir.exists()) {
            try {
                jarDir.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        File file = null;

        try {
            InputStream input = new ByteArrayInputStream("[{\"name\":\"mkyong\",\"age\":38,\"position\":[\"Founder\",\"CTO\",\"Writer\"],\"salary\":{\"2010\":10000.69},\"skills\":[\"java\",\"python\",\"node\",\"kotlin\"]}]".getBytes());
            SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
            file = File.createTempFile(pattern.format(new Date()),".json" );
            OutputStream out = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.close();
            file.deleteOnExit();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }


    }
}
