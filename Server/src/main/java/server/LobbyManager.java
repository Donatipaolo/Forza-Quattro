package server;

import protocol.*;
import data.*;
import enums.*;

/*
 * Allora tutti questi metodi devono essere nella classe clientHandler, ci deve essere una varibile per lo stato, in game oin lobby e in 
 * base alla variabile chiamo handleMessageGame o handleMessageLobby
 */
public class LobbyManager {
	
	public static ClientList clientList = null;
	
	public static Message handleMessageGame(Message msg, Client client, GameSession gameSession) {
		
		MessageType msgType = msg.getType();

        switch (msgType) {

            case change_username_request:
                return handleChangeUsernameRequest(msg, client);

            case play_list_request:
                return new PlayListResponse(clientList.getPlayerList());

            case challenge_request:
                return handleChallengeRequest(msg);

            case challenge_response:
                return handleChallengeResponse(msg, client);

            case move:
                return handleMove(msg);

            case disconnect:
                return handleDisconnect(msg);

            default:
                System.out.println("Unhandled message type: " + msgType);
                return null;
        }
		
	}
	

	private static ChangeUsernameResponse handleChangeUsernameRequest(Message generalMsg, Client c) {
		ChangeUsernameRequest msg = (ChangeUsernameRequest) generalMsg;
		String new_username = msg.getNewUsername();
		
		if(clientList.isClientInList(new_username)) {
			return (new ChangeUsernameResponse(ChangeUsernameResult.taken));
		}
		
		c.setUsername(new_username);
		return (new ChangeUsernameResponse(ChangeUsernameResult.ok));
	}

	
	private static ChallengeResult handleChallengeRequest(Message generalMsg) {
		ChallengeRequest msg = (ChallengeRequest) generalMsg;
		String username = msg.getUsername();
		
		Client enemy = clientList.getClient(username);
		if(enemy == null) {
			return (new ChallengeResult(ChallengeResultStatus.client_not_found, MoveValue.none, username));
		}
		
		enemy.getOut().print(msg);

		return null;		
	}
	
	private static Message handleChallengeResponse(Message generalMsg, Client c) {
		ChallengeResponse msg = (ChallengeResponse) generalMsg;
		
		Client enemy = clientList.getClient(msg.getUsername());
		
		if(enemy == null) {
			return new ChallengeResult(ChallengeResultStatus.client_not_found,MoveValue.none,enemy.getUsername());
		}
		
		Client starter = (msg.getFirstMove() == MoveValue.you) ? c : enemy;

		if(c.getStatus().equals(Status.free) && enemy.getStatus().equals(Status.free) ) {
			GameSession gameSession = new GameSession(c, enemy, starter);
		}
		
		return null;
	}

	private static Message handleMove(Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Message handleDisconnect(Message msg) {
		return null;
	}
	

}
