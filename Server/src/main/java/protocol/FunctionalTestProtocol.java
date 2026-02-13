package protocol;

import data.ClientList;

public class FunctionalTestProtocol {
	public static void main(String[] args) {
		//Creazione di ogni possibile richiesta:
		
		
		
		toJsonTest(new ServerConnectionResult("Donati"));
		toJsonTest(new ChangeUsernameRequest("user1234","Donati"));
		toJsonTest(new ChangeUsernameResponse("ok"));
		toJsonTest(new PlayListRequest());
		toJsonTest(new PlayListResponse(clients()));
		toJsonTest(new ChallengeRequest("Donati"));
		toJsonTest(new ChallengeResponse(ChallengeResponseStatus.ok,"you"));
		toJsonTest(new Move(3));
		toJsonTest(new MoveResult(MoveResultStatus.ok));
		toJsonTest(new GameEnd("won","enemy_disconnected"));
		toJsonTest(new Disconnect());
		
	}
	
	private static void toJsonTest(Message msg) {
		
		System.out.println("Json of : " + msg.getClass() + "\n");
		System.out.println(MessageFormatterParser.toJson(msg));
		System.out.println("\n--------------------------------------------\n");
		
	}
	
	private static ClientList clients(){
		ClientList clientlist = new ClientList();
		clientlist.addClient(null, "donati");
		clientlist.addClient(null, "canocchi");
		clientlist.addClient(null, "iaquinta");
		clientlist.addClient(null, "polini");
		clientlist.addClient(null, "macchia");
		clientlist.addClient(null, "bacci");
		clientlist.addClient(null, "salcioli");
		clientlist.addClient(null, "guerrini");
		
		return clientlist;
	}
}
