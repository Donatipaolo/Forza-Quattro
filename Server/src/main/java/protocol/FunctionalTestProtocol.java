package protocol;



import java.util.ArrayList;

import data.ClientList;

public class FunctionalTestProtocol {
	
	private static ArrayList<String> list;
	
	public static void main(String[] args) {
		
		list = new ArrayList<String>();
		
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
		
		
		for(String json : list) {
			toObjTest(json);
		}
		
	}
	
	private static void toJsonTest(Message msg) {
		
		System.out.println("Json of : " + msg.getClass() + "\n");
		list.add(MessageFormatterParser.toJson(msg));
		System.out.println(list.getLast());
		System.out.println("\n--------------------------------------------\n");
		
	}
	
	private static void toObjTest(String json) {
		
		System.out.print("the current file json in of type : ");
		System.out.println(MessageFormatterParser.fromJson(json).getClass());
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
