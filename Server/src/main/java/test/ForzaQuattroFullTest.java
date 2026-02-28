package test;

import com.fasterxml.jackson.databind.JsonNode;

public class ForzaQuattroFullTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";
    private static final String YELLOW = "\u001B[33m";

    public static void testChangeUsername(TestClient client1,String username) throws Exception {
    	// 🔹 CHANGE USERNAME
        JsonNode changeResp = client1.sendAndReceive(
                "{\"type\":\"change_username_request\",\"newUsername\":\""+ username + "\"}"
        );
        
        if (!changeResp.get("type").asText().equals("change_username_response")) {
        	fail("Errore nel cambio username");
        }
        
        System.out.printf("Cambio Username " + GREEN + "OK" + RESET + " Status ");

        String status = changeResp.get("status").asText();

        if (status.equals("ok")) {
            System.out.println(YELLOW + "Username changed" + RESET);
            client1.setUsername(username);
        } 
        else if (status.equals("taken")) {
            System.out.println(YELLOW + "Username taken" + RESET);
        }
        
        
        

    }
    
    public static void testPlayList(TestClient client1) throws Exception{
    	// 🔹 PLAY LIST
        JsonNode listResp = client1.sendAndReceive(
                "{\"type\":\"play_list_request\"}"
        );
        
        if (!listResp.has("listOfPlayer"))
            fail("Lista giocatori non ricevuta");
        
        System.out.println("List Of Player " + GREEN + " OK" + RESET);

    }
    
    public static void testChallengeRequest(TestClient sender, TestClient destination) throws Exception {
    	// 🔹 CHALLENGE
        sender.send(
                "{\"type\":\"challenge_request\",\"username\":\""
                        + destination.getUsername() + "\"}"
        );
        
        JsonNode challReq = destination.read();
        
        if(!challReq.get("type").asText().equals("challenge_request")) {
        	fail("Challenge Request non ricevuta");
        }
        
        System.out.printf("Challenge Request " + GREEN + " OK" + RESET + " Status " + YELLOW + "Ok\n" + RESET);
    }
    
    public static void testChallengeRequest(TestClient sender) throws Exception {
    	// 🔹 CHALLENGE
        sender.send(
                "{\"type\":\"challenge_request\",\"username\":\""
                        + "........." + "\"}"
        );
        
        JsonNode challReq = sender.read();
        
        if(!challReq.get("type").asText().equals("challenge_result")) {
        	fail("Challenge Result non ricevuta");
        }
        
        if(!challReq.get("status").asText().equals("client_not_found")) {
        	fail("Challenge Result errata");
        }
        
        System.out.printf("Challenge Request " + GREEN + " OK " + RESET + " Status " + YELLOW + "Not Found\n" + RESET);
    }
    
    public static TestClient testChallengeResponse(TestClient destination, TestClient sender) throws Exception {
    	JsonNode challengeResponse = destination.sendAndReceive(
                "{\"type\":\"challenge_response\",\"username\":\""
                        + sender.getUsername() + "\",\"status\":\"ok\"}"
        );
        
        
    	if(!challengeResponse.get("type").asText().equals("challenge_result")) {
    		fail("Risposta Errata");
    	}
    	
        if (!challengeResponse.get("status").asText().equals("ok"))
            fail("Challenge fallita");
        
        JsonNode challResult1 = sender.read();
        
        if(!challResult1.get("type").asText().equals("challenge_result")) {
        	fail("Challenge Result non ricevuta");
        }
        
        TestClient first;
 
        if(challResult1.get("firstMove").asText().equals("you")) {
        	first = sender;

        }
        else {
        	first = destination;
        }
        
        System.out.println("Challenge Response " + GREEN + " OK" + RESET);
        
        return first;
    }
    
    public static void testChallengeResponseRefused(TestClient destination, TestClient sender) throws Exception {
    	destination.send(
                "{\"type\":\"challenge_response\",\"username\":\""
                        + sender.getUsername() + "\",\"status\":\"refused\"}"
        );
        
        JsonNode challResult1 = sender.read();
        
        if(!challResult1.get("type").asText().equals("challenge_result")) {
        	fail("Challenge Result non ricevuta");
        }
        
        if (!challResult1.get("status").asText().equals("refused"))
            fail("Challenge fallita");
        
        
        System.out.println("Challenge Response Refused " + GREEN + " OK" + RESET);
    }
    
    public static String getMove(int column) {
    	return "{\"type\":\"move\",\"column\":\"" + Integer.toString(column)+ "\"}";
    }
    
    public static void testMove(TestClient first, TestClient second) throws Exception {
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
        
        System.out.println("Game End " + GREEN + " OK" + RESET + " Status " + YELLOW + "Game Ended\n" + RESET);    }
    



    public static void testTie(TestClient first, TestClient second)throws Exception {
    	
    	TestClient current = first;
    	TestClient enemy = second;
    	
    	int[][] tiePattern = {
    		    {1,2,3,4,5,6,0},
    		    {0,1,2,3,4,5,6},
    		    {0,1,2,3,4,5,6},
    		    {1,2,3,4,5,6,0},
    		    {1,2,3,4,5,6,0},
    		    {0,1,2,3,4,5,6}
    	};
    	
    	for (int row = 0; row < 6; row++) {

            for (int col = 0; col < 7; col++) {

            	current.sendAndReceive(getMove(tiePattern[row][col]));
            	enemy.read();

                // swap player
                TestClient temp = current;
                current = enemy;
                enemy = temp;
            }
        }
    	
    	JsonNode gameEnd1 = first.read();
        
        if(!gameEnd1.get("type").asText().equals("game_end") ||
        		!gameEnd1.get("result").asText().equals("tie")) {
        	fail("Game end errato o non ricevuto");
        }
        
        JsonNode gameEnd2 = second.read();
        
        if(!gameEnd2.get("type").asText().equals("game_end") ||
        		!gameEnd2.get("result").asText().equals("tie")) {
        	fail("Game end errato o non ricevuto");
        }
        
        System.out.println("Game End " + GREEN + " OK" + RESET + " Status " + YELLOW + "Tie\n" + RESET);
    }
    
    
    public static void testDisconnection(TestClient first, TestClient second) throws Exception{
    	//Chiudo il primo dei due
    	first.close();
    	
    	JsonNode gameEnded = second.read();
    	
    	if(!gameEnded.get("type").asText().equals("game_end"))
    		fail("Game End non valido");
    	
    	if(!gameEnded.get("result").asText().equals("won")) {
    		fail("Result non valido");
    	}

    	if(!gameEnded.get("info").asText().equals("enemy_disconnected")) {
    		fail("Game info non valide");
    	}
    	
    	System.out.println("Disconnect" + GREEN + " OK" + RESET);
    }
    
    public static void main(String[] args) {

        try {

            //AVVIO SERVER
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
            System.out.print(CYAN + " AVVIO TEST " + RESET);
            System.out.println("====================================");
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST CONNESSIONE " + RESET);
            System.out.println("Connessione " + GREEN + " OK " + RESET);

            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST CAMBIO USERNAME " + RESET);
            
            testChangeUsername(client1,"Donutsbad00");
            testChangeUsername(client2,"Donutsbad00");
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST PLAY LIST" + RESET);
            testPlayList(client1);
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST CHALLENGE CLIENT NOT FOUND " + RESET);
            testChallengeRequest(client1);
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST CHALLENGE REFUSED " + RESET);
            testChallengeRequest(client1, client2);
            
            //Challenge request refused
            testChallengeResponseRefused(client2,client1);
            
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST CHALLENGE ACCEPTED" + RESET);
            //Challenge request accepted
            testChallengeRequest(client1,client2);
            
            TestClient first = testChallengeResponse(client2, client1);
            TestClient second = first == client1? client2: client1;
            
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST MOVE " + RESET);
            testMove(first,second);

            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST TIE " + RESET);
            
            //Challenge request accepted
            testChallengeRequest(client1,client2);
            
            first = testChallengeResponse(client2, client1);
            second = first == client1? client2: client1;
            testTie(first,second);
            
            System.out.print("\n===========================");
            System.out.println(PURPLE + " TEST DISCONNECTION " + RESET);
          //Challenge request accepted
            testChallengeRequest(client1,client2);
            
            first = testChallengeResponse(client2, client1);
            second = first == client1? client2: client1;
            
            testDisconnection(first,second);
            
            //TEST FINISHED
            client1.close();
            client2.close();

            System.out.print("\n====================================");
            
            System.out.print(CYAN + " TEST PASSED " + RESET);

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