package protocol;

import java.util.ArrayList;

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
		toJsonTest(new PlayListResponse());
		toJsonTest(new ChallengeRequest("Donati"));
		toJsonTest(new ChallengeResponse(ChallengeResponseStatus.ok,"Canocchi"));
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
	
}
