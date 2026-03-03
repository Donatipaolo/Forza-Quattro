# Forza-Quattro
Gioco Forza Quattro multiplayer in Java.

## GUI
Interfaccia grafica utente realizzata utilizzando la libreria **JavaFX** per un'esperienza di gioco moderna e reattiva.

---

## Come Iniziare

### Prerequisiti
Per eseguire il gioco o il server, assicurati di avere:
* **Java JDK/JRE 21** o superiore installato sul sistema.
* **WiX Toolset v3.11** (necessario solo se desideri ricompilare l'installer MSI del Client).

---

## Client (Il Gioco)
Il client è l'applicazione grafica che i giocatori utilizzano per sfidarsi.

### Installazione Client su Windows
1. Scarica il file `forza-quattro-client_1.0.amd64.msi`
2. Avvia l'installer e segui la procedura guidata.
3. Al termine, Avvia l'eseguibile situato in `C:\Program Files\ForzaQuattro`

### Installazione Client su Debian
1. Scarica il file `forza-quattro-client_1.0.amd64.deb`
2. Apri il file con App Store e segui la procedure guidata.

### Installazione Server
1. Scarica il file `java -jar forza_quattro_server-0.0.1-SNAPSHOT.jar`
2. Apri il terminale (CMD o PowerShell su Windows, Terminale su Linux/Mac).
3. Esegui il comando
```bash
java -jar forza_quattro_server-0.0.1-SNAPSHOT.jar
```

### Esecuzione per Sviluppatori (Maven) - Client
Se desideri avviare il client direttamente dal codice sorgente:

Su windows

```bash
mvn javafx:run -Pwindows
```

Oppure su linux

```bash
mvn javafx:run -Plinux
```

## Configurazione Client (Indirizzo Server)
Per permettere al Client di connettersi a un server specifico (locale o remoto), è possibile modificare il file di configurazione generato automaticamente al primo avvio:

- Percorso su Windows: `%USERPROFILE%\.forzaquattro\config.xml`
- Contenuto: In questo file puoi specificare l'IP del server e la Porta di ascolto.
- Utilità: Utile se il server è ospitato su un computer diverso nella rete locale o su un server cloud.

### Configurazione Server (Porta e Parametri)
Il Server permette di personalizzare i parametri di rete tramite un file dedicato, situato nella medesima cartella di sistema:

- Percorso su Windows: `%USERPROFILE%\.forzaquattro\configServer.xml`
- Contenuto: Permette di cambiare la Porta su cui il server accetta le connessioni in entrata.
- Nota: Assicurati che la porta scelta sia aperta nel Firewall di Windows per permettere ai Client esterni di collegarsi.

### Esecuzione per Sviluppatori (Maven) - Server

Se desideri avviare il server direttamente da terminale o da un IDE

```bash
mvn exec:java -Pwindows
```

Oppure su linux

```bash
mvn exec:java -Plinux
```

### Generazione Installer per Windows

Per creare un installatore Windows standard che includa il collegamento sul desktop e nel menu Start:

- Prerequisito: Installa WiX Toolset v3.11 e assicurati che sia presente nel PATH di sistema.

- Apri il terminale nella cartella del Client e digita:

```bash
mvn clean package jpackage:jpackage -Pwindows
```

Risultato: Troverai il file ForzaQuattro-1.0.msi nella cartella target/dist/.


Installazione: Esegui il file .msi per installare il gioco in `C:\Program Files\ForzaQuattro`.

### Generazione Installer per Linux
Su Linux, Maven genererà un pacchetto nativo (solitamente .deb per Debian/Ubuntu o .rpm per Fedora/RedHat) in base agli strumenti installati sul sistema.

- Prerequisito: Assicurati di avere installato i tool di packaging (es. fakeroot e dpkg-dev su Ubuntu).
- Apri il terminale nella cartella del Client e digita:

```bash
mvn clean package jpackage:jpackage -Plinux
```
Risultato: Troverai il pacchetto (es. forza-quattro_1.0_amd64.deb) nella cartella target/dist/.

Installazione: Puoi installarlo con il comando:

```bash
sudo dpkg -i target/dist/forza-quattro_1.0_amd64.deb
```

## Download

**[forza-quattro-client_1.0.amd64.msi](forza-quattro-client_1.0.amd64.msi)**

**[forza-quattro-client_1.0.amd64.deb](forza-quattro-client_1.0.amd64.deb)**

**[forza_quattro_server-0.0.1-SNAPSHOT.jar](forza_quattro_server-0.0.1-SNAPSHOT.jar)**