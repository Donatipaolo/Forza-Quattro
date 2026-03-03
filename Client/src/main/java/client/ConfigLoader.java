package client;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class ConfigLoader {
    private String address = "localhost"; 
    private int port = 20000;            

    public ConfigLoader() {
        try {
            // 1. Definiamo il percorso nella Home dell'utente (~/.forzaquattro/config.xml)
            String userHome = System.getProperty("user.home");
            File configDir = new File(userHome, ".forzaquattro");
            File configFile = new File(configDir, "config.xml");

            // 2. Se la cartella o il file non esistono, li creiamo con i default
            if (!configFile.exists()) {
                configDir.mkdirs(); // Crea la cartella se manca
                createDefaultConfig(configFile);
                System.out.println("Creato file di configurazione di default in: " + configFile.getAbsolutePath());
            }

            // 3. Carichiamo il file XML dal percorso sicuro dell'utente
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);
            doc.getDocumentElement().normalize();

            address = doc.getElementsByTagName("address").item(0).getTextContent();
            String portStr = doc.getElementsByTagName("port").item(0).getTextContent();
            port = Integer.parseInt(portStr);

        } catch (Exception e) {
            System.err.println("Errore nel caricamento config: " + e.getMessage() + ". Uso i default.");
        }
    }

    // Metodo per creare il file XML se l'utente non lo ha ancora
    private void createDefaultConfig(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element rootElement = doc.createElement("config");
        doc.appendChild(rootElement);

        Element addr = doc.createElement("address");
        addr.appendChild(doc.createTextNode("localhost"));
        rootElement.appendChild(addr);

        Element p = doc.createElement("port");
        p.appendChild(doc.createTextNode("20000"));
        rootElement.appendChild(p);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public String getAddress() { return address; }
    public int getPort() { return port; }
}