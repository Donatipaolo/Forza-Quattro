package test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;

public class TestClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectMapper mapper = new ObjectMapper();
    private String username;

    public void connect() throws Exception {
        socket = new Socket("localhost", 20000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        String response = in.readLine();
        JsonNode json = mapper.readTree(response);

        username = json.get("username").asText();
    }

    public String getUsername() {
        return username;
    }

    public JsonNode sendAndReceive(String jsonRequest) throws Exception {
        out.println(jsonRequest);
        String response = in.readLine();
        return mapper.readTree(response);
    }
    
    public void send(String jsonRequest) throws Exception {
        out.println(jsonRequest);
    }
    
    public JsonNode read() throws Exception {
    	String response = in.readLine();
        return mapper.readTree(response);
    }

    public void close() throws Exception {
        socket.close();
    }

	public void setUsername(String string) {
		this.username = string;
	}
}