package protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes; 
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import enums.MessageType;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY,
	    visible = true,
	    property = "type"
	)

@JsonSubTypes({
    @JsonSubTypes.Type(value = ServerConnectionResult.class, name = "server_connection_result"),
    @JsonSubTypes.Type(value = ChangeUsernameRequest.class, name = "change_username_request"),
    @JsonSubTypes.Type(value = ChangeUsernameResponse.class, name = "change_username_response"),
    @JsonSubTypes.Type(value = PlayListRequest.class, name = "play_list_request"),
    @JsonSubTypes.Type(value = PlayListResponse.class, name = "play_list_response"),
    @JsonSubTypes.Type(value = ChallengeRequest.class, name = "challenge_request"),
    @JsonSubTypes.Type(value = ChallengeResponse.class, name = "challenge_response"),
    @JsonSubTypes.Type(value = ChallengeResult.class, name = "challenge_result"),
    @JsonSubTypes.Type(value = Move.class, name = "move"),
    @JsonSubTypes.Type(value = MoveResult.class, name = "move_response"),
    @JsonSubTypes.Type(value = GameEnd.class, name = "game_end"),
    @JsonSubTypes.Type(value = Disconnect.class, name = "disconnect")
})



public interface Message {
	
	public MessageType getType();
}

