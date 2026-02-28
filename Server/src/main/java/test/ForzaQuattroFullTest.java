package test;

import com.fasterxml.jackson.databind.JsonNode;

public class ForzaQuattroFullTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[36m";

    public static void main(String[] args) {

        try {

            // 🔥 AVVIO SERVER
            Thread serverThread = new Thread(() -> {
                try {
                    server.Main.startServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            serverThread.setDaemon(true);
            serverThread.start();
            Thread.sleep(2000);

            // 🔥 CREO DUE CLIENT
            TestClient client1 = new TestClient();
            TestClient client2 = new TestClient();

            client1.connect();
            client2.connect();

            System.out.print("\n====================================");
            System.out.print(YELLOW + " AVVIO TEST " + RESET);
            System.out.println("====================================\n");
            System.out.println("Connessione " + GREEN + " OK " + RESET);

            // 🔹 CHANGE USERNAME
            JsonNode changeResp = client1.sendAndReceive(
                    "{\"type\":\"change_username_request\",\"newUsername\":\"Mario\"}"
            );

            client1.setUsername("Mario");
            
            if (!changeResp.get("status").asText().equals("ok"))
                fail("Cambio username fallito");
            
            System.out.println("Cambio Username " + GREEN + " OK" + RESET);

            // 🔹 PLAY LIST
            JsonNode listResp = client1.sendAndReceive(
                    "{\"type\":\"play_list_request\"}"
            );
            
            if (!listResp.has("listOfPlayer"))
                fail("Lista giocatori non ricevuta");
            
            System.out.println("List Of Player " + GREEN + " OK" + RESET);

            // 🔹 CHALLENGE
            client1.send(
                    "{\"type\":\"challenge_request\",\"username\":\""
                            + client2.getUsername() + "\"}"
            );
            
            
            JsonNode challReq = client2.read();
            
            if(!challReq.get("type").asText().equals("challenge_request")) {
            	fail("Challenge Request non ricevuta");
            }
            
            System.out.println("Challenge Request " + GREEN + " OK" + RESET);
            
            JsonNode challengeResponse = client2.sendAndReceive(
                    "{\"type\":\"challenge_response\",\"username\":\""
                            + client1.getUsername() + "\",\"status\":\"ok\"}"
            );
            
            
            if (!challengeResponse.get("status").asText().equals("ok"))
                fail("Challenge fallita");
            
            JsonNode challResult1 = client1.read();
            
            if(!challResult1.get("type").asText().equals("challenge_result")) {
            	fail("Challenge Result non ricevuta");
            }
            
            TestClient first;
            TestClient second;
            
            if(challResult1.get("firstMove").asText().equals("you")) {
            	first = client1;
            	second = client2;
            }
            else {
            	first = client2;
            	second = client1;
            }
            

            System.out.println("Challenge Response " + GREEN + " OK" + RESET);
           
            
            // 🔹 MOSSE SIMULATE
            first.sendAndReceive("{\"type\":\"move\",\"column\":\"0\"}");
            second.read();
            second.sendAndReceive("{\"type\":\"move\",\"column\":\"1\"}");
            first.read();
            first.sendAndReceive("{\"type\":\"move\",\"column\":\"0\"}");
            second.read();
            second.sendAndReceive("{\"type\":\"move\",\"column\":\"1\"}");
            first.read();
            first.sendAndReceive("{\"type\":\"move\",\"column\":\"0\"}");
            second.read();
            second.sendAndReceive("{\"type\":\"move\",\"column\":\"1\"}");
            first.read();
            JsonNode lastMove = first.sendAndReceive("{\"type\":\"move\",\"column\":\"0\"}");
         

            if (!lastMove.get("status").asText().equals("ok"))
                fail("Errore nelle mosse");

            JsonNode ReadLastMove = second.read();
            

            if (!ReadLastMove.get("type").asText().equals("move"))
                fail("Errore nelle mosse");
            
            System.out.println("Partita simulata " + GREEN + " OK" + RESET);

            JsonNode gameEnd1 = first.read();
            
            if(!gameEnd1.get("type").asText().equals("game_end") ||
            		!gameEnd1.get("result").asText().equals("won")) {
            	fail("Game end errato o non ricevuto");
            }
            
            JsonNode gameEnd2 = second.read();
            
            if(!gameEnd2.get("type").asText().equals("game_end") ||
            		!gameEnd2.get("result").asText().equals("defeat")) {
            	fail("Game end errato o non ricevuto");
            }
            
            System.out.println("Game End " + GREEN + " OK" + RESET);
            
            client1.close();
            client2.close();

            System.out.print("\n====================================");
            
            System.out.print(YELLOW + " TEST PASSED " + RESET);

        } catch (Exception e) {
            System.out.print(RED + "TEST FAILED" + RESET);
            e.printStackTrace();
        }
        
        System.out.println("===================================");
    }

    private static void fail(String msg) {
        System.out.println(RED + msg + RESET);
        System.exit(1);
    }
}