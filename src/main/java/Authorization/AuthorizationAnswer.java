package Authorization;

public class AuthorizationAnswer {

    boolean result;
    String cause;
    String sessionKey;

    public AuthorizationAnswer() {
    }

    public AuthorizationAnswer(boolean result, String cause, String sessionKey) {
        this.result = result;
        this.cause = cause;
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public boolean isResult() {
        return result;
    }

    public AuthorizationAnswer setResult(boolean result) {
        this.result = result;
        return this;
    }

    public String getCause() {
        return cause;
    }

    public AuthorizationAnswer setCause(String cause) {
        this.cause = cause;
        return this;
    }
}
