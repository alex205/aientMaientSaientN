package controller;

public class Controller {
    public enum State_t {
        DISCONNECTED,
        CONNECTED
    }

    private State_t app_state;

    public Controller() {
        this.app_state = State_t.DISCONNECTED;
    }

    public State_t getAppState() {
        return app_state;
    }
}
