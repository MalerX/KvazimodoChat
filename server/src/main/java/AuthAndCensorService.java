import java.util.Map;

public interface AuthAndCensorService {
    String getNicknameByLoginAndPassword(String login, String password);
    boolean registration(String login, String password, String nickname);
    boolean changeNickname(String login, String newNickname);
    Map<String,String> getFilter();
}