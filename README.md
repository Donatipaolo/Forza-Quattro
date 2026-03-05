# Forza-Quattro
Gioco **Forza Quattro** multiplayer in Java con interfaccia grafica moderna.

## GUI
L'interfaccia utente è realizzata con **JavaFX**, offrendo un'esperienza di gioco reattiva e intuitiva.

---

## Come Iniziare

### Prerequisiti
Assicurati di avere installato sul sistema:
* **Java JDK/JRE 21** o superiore
* **WiX Toolset v3.11** (solo se vuoi ricompilare l'installer MSI per Windows)

---

## Client (Il Gioco)

### Installazione su Windows
1. Scarica il file MSI dalla sezione **Download**.
2. Esegui l'installer e segui la procedura guidata.
3. Al termine, avvia il gioco dal collegamento creato in `C:\Program Files\ForzaQuattro`.

### Installazione su Debian/Ubuntu
1. Scarica il file `.deb` dalla sezione **Download**.
2. Apri il file con il gestore pacchetti o tramite terminale.
3. Oppure installa da terminale:

```bash
sudo dpkg -i forza-quattro-client_1.0_amd64.deb
```

### Installazione Server
1. Scarica il file `.jar` dalla sezione **Download**.
2. Apri il terminale (CMD, PowerShell o Terminale su Linux/Mac).
3. Esegui il comando:

```bash
java -jar forza_quattro_server-0.0.1-SNAPSHOT.jar
```

---

## Esecuzione per Sviluppatori

### Client (Maven)
**Windows:**
```bash
mvn javafx:run -Pwindows
```
**Linux:**
```bash
mvn javafx:run -Plinux
```

### Server (Maven)
**Windows:**
```bash
mvn exec:java -Pwindows
```
**Linux:**
```bash
mvn exec:java -Plinux
```

---

## Configurazione

### Client
- Percorso Windows: `%USERPROFILE%\.forzaquattro\config.xml`
- Modifica l'IP del server e la porta per connetterti a server diversi.

### Server
- Percorso Windows: `%USERPROFILE%\.forzaquattro\configServer.xml`
- Modifica la porta per consentire connessioni esterne (apri la porta nel Firewall).

---

## Generazione Installer

### Windows
```bash
mvn clean package jpackage:jpackage -Pwindows
```
Risultato: `target/dist/ForzaQuattro-1.0.msi`

### Linux
```bash
mvn clean package jpackage:jpackage -Plinux
```
Risultato: pacchetto `.deb` in `target/dist/forza-quattro_1.0_amd64.deb`

---

## 📝 Documentazione e Manuale

- [Manuale Utenti](Manuale%20Utenti.pdf)  
  Guida completa all'utilizzo del gioco e delle funzionalità.

- [Documentazione Forza Quattro](Documentazione-Forza-Quattro.pdf)                   
  Approfondimenti
