package com.itq.VentasColeccionables;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogToFile {
    // Logger privado para el manejo de logs
    private static final Logger logger = Logger.getLogger(LogToFile.class.getName());

    // Ruta donde se creará el archivo de log7
    private static final String LOG_FOLDER = "src/main/resources/LOGS";
    private static final String LOG_FILE_PATH = LOG_FOLDER + File.separator + "log.txt";

    // Método estático para obtener el logger
    public static Logger getLogger() {
        return logger;
    }

    // Bloque de inicialización estático
    static {
        try {
            // Crear la carpeta de logs si no existe
            File logFolder = new File(LOG_FOLDER);
            if (!logFolder.exists()) {
                logFolder.mkdirs();
            }

            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, true); // "true" indica que se agregará al archivo existente
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error al configurar el manejador de archivo", e);
        }
    }
}
