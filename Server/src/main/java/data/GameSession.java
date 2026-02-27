package data;

import protocol.*;
import server.ClientHandler;

import java.io.IOException;

import enums.GameEndInfo;
import enums.GameEndResult;
import enums.MoveResultStatus;
import enums.Status;

public class GameSession {
	
	private Client client1; //Colui che invia la richiesta
	private Client client2; //Colui che riceve la richiesta
	private Client currentClient; //se è true è il turno del client1 altrimenti è il turno del client 2
	private Grid grid;
	private boolean closed = false;
	//La prima mossa è del giallo
	
	public GameSession(Client client1,Client client2,Client currentClient) {
		this.client1 = client1; //giocatore giallo
		this.client2 = client2; //giocatore rosso
		this.currentClient = currentClient;
		this.grid = new Grid();
	}
	
	public boolean isGameFinished(Color color, int column) throws Exception {
		
		if(grid.isFinished(color, column)) {
			return true;
		}
		return false;
	}
	
	public void handleGameFinished(int column, Client mover, Client enemy) {
		try {
			mover.write(new MoveResult(MoveResultStatus.ok));
		} catch (IOException e) {
			disconnection(mover);
			return;
		}
		
		try {
			enemy.write(new Move(column));
			enemy.write(new GameEnd(GameEndResult.defeat, GameEndInfo.game_ended));
		} catch (IOException e) {
			disconnection(enemy);
		}
	}
	
	public Message insert(Client mover,int column) {
		
		//Controllo se è la sessione è stata chiusa
		if (closed) return null;
		
		//Controllo se non è il tuo turno
		if(currentClient != mover) {
			return new MoveResult(MoveResultStatus.not_your_turn); 
		}
		
		Color currentColor = currentClient == client2? Color.red : Color.yellow;
		Client enemyClient = mover == client1? client1 : client2;
		
		try {
			//Eseguo la mossa
			grid.insert(currentColor, column);
			
			//Controllo la fine del gioco
			if(isGameFinished(currentColor, column)) {
				handleGameFinished(column, mover, enemyClient);
				cleanup();
				return new GameEnd(GameEndResult.won,GameEndInfo.game_ended);
			}
			
			//Invio la mossa al client avversario
			enemyClient.write(new Move(column));
			
			
		} catch (IOException e) {
			//La disconnessione dell'avversario viene gestita dal clientHandler apposito
			return null;
			
		} catch (Exception e) {
			//Mossa non valida
			return new MoveResult(MoveResultStatus.invalid_move);
		}
		
		//Cambio il turno
		currentClient = enemyClient;

		return new MoveResult(MoveResultStatus.ok);
	}

	
	public void cleanup() {
		//Controllo che la sessione non sia già stata chiusa da l'altro client=
		if(closed) 
				return;
		
		closed = true;
		
		client1.setGameSession(null);
		client2.setGameSession(null);
		
		client1.setStatus(Status.free);
		client2.setStatus(Status.free);
	}
	
	public synchronized void disconnection(Client quitter) {
		Client enemy = quitter == client1 ? client2 : client1;

	    try {
	        enemy.write(new GameEnd(GameEndResult.won, GameEndInfo.enemy_disconnected));
	    } catch (IOException e) {
	    	//In caso che la scrittura non vada allora anche l'altro client si è disconnesso.
	    	//La gestione della disconnessione dell'altro client viene gestita dal clientHandler apposito
	    }

	    cleanup();
	}
	
	
	
	
	
}
