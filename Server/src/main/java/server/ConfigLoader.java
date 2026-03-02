package server;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class ConfigLoader {
    private int port = 20000;            // Valore di default

    public ConfigLoader(String filePath) {
        try {
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                System.err.println("File di configurazione non trovato, utilizzo delle impostazioni di default:\nPort : 20'000");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            // Estrazione porta
            String portStr = doc.getElementsByTagName("port").item(0).getTextContent();
            port = Integer.parseInt(portStr);

        } catch (Exception e) {
            System.err.println("Errore nel caricamento config: " + e.getMessage());
        }
    }

    public int getPort() { return port; }
}