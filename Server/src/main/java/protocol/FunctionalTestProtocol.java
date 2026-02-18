package protocol;

import java.util.ArrayList;

import data.ClientList;

import enums.*;

public class FunctionalTestProtocol {
	
	private static ArrayList<String> list;
	
	public static void main(String[] args) {
		
		list = new ArrayList<String>();
		
		//Creazione di ogni possibile richiesta:
		
		toJsonTest(new ServerConnectionResult("Donati"));
		toJsonTest(new ChangeUsernameRequest("Donati"));
		toJsonTest(new ChangeUsernameResponse(ChangeUsernameResult.ok));
		toJsonTest(new PlayListRequest());
		toJsonTest(new PlayListResponse(clients()));
		toJsonTest(new ChallengeRequest("Donati"));
		toJsonTest(new ChallengeResponse(ChallengeResponseStatus.ok,MoveValue.you,"Canocchi"));
		toJsonTest(new Move(3));
		toJsonTest(new MoveResult(MoveResultStatus.ok));
		toJsonTest(new GameEnd(GameEndResult.won,GameEndInfo.game_ended));
		toJsonTest(new Disconnect());
		
		
		for(String json : list) {
			toObjTest(json);
		}
		
	}
	
	private static void toJsonTest(Message msg) {
		
		System.out.println("Json of : " + msg.getClass() + "\n");
		list.add(MessageFormatterParser.toJson(msg));
		System.out.println(list.get(list.size()-1));
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
