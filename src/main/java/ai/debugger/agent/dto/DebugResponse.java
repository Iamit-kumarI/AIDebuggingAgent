package ai.debugger.agent.dto;
//import lombok.AllArgsConstructor;
//
//@AllArgsConstructor
public class DebugResponse {
    private String bug;
    private String explanation;
    private String fixedCode;
    public String getBug() {
        return bug;
    }
    public String getExplanation() {
        return explanation;
    }
    public String getFixedCode() {
        return fixedCode;
    }
    public void setBug(String bug) {
        this.bug=bug;
    }
    public void setExplanation(String explanation) {
        this.explanation=explanation;
    }
    public void setFixedCode(String fixedCode) {
        this.fixedCode=fixedCode;
    }
}
