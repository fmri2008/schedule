/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Dawei Li
 */
public class Log {
    
    private static final String LOG_FILENAME = "login_timestamps.txt";
    
    public static void logTimestamp(String message) throws IOException {
        File logFile = new File(LOG_FILENAME);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILENAME, true));
        writer.append(message);
        writer.close();
    }
}
