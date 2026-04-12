package com.agent.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

//import lombok.AllArgsConstructor;
//
//@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
