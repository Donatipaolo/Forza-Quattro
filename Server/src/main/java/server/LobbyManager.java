package server;

import protocol.*;
import data.*;
import enums.*;

public class LobbyManager {
	
	public static ClientList clientList = null;
	
	public static Message handleMessage(Message msg, Client client, GameSession gameSession) {
		
		MessageType msgType = msg.getType();

        switch (msgType) {

            case change_username_request:
                return handleChangeUsernameRequest(msg, client);

            case play_list_request:
                return new PlayListResponse(clientList);

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

	
	private static Message handleChallengeRequest(Message generalMsg) {
		ChallengeRequest msg = (ChallengeRequest) generalMsg;
		String username = msg.getUsername();
		
		Client c = clientList.getClient(username);
		
		c.getOut().print(msg);

		return null;		
	}
	
	private static Message handleChallengeResponse(Message generalMsg, Client c) {
		ChallengeResponse msg = (ChallengeResponse) generalMsg;
		Client enemy = clientList.getClient(msg.getUsername());
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
