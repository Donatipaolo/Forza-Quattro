package protocol;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageFormatterParser {
	
	
	public static String toJson(Message msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
	
	public static Message fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
        try {
        	return mapper.readValue(json, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
}
