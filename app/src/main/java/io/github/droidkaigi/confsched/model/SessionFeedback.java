package io.github.droidkaigi.confsched.model;

public class SessionFeedback {
    public final int sessionId;
    public final int relevancy;
    public final int asExpected;
    public final int difficulty;
    public final int knowledgeable;
    public final String comment;
    public final String sessionName;

    public SessionFeedback(int sessionId, String sessionName, int relevancy, int asExpected, int difficulty, int knowledgeable, String comment) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.relevancy = relevancy;
        this.asExpected = asExpected;
        this.difficulty = difficulty;
        this.knowledgeable = knowledgeable;
        this.comment = comment;
    }
}
