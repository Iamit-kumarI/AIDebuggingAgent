package ai.debugger.agent.controller;

import ai.debugger.agent.dto.DebugResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Files;

@RestController
@RequestMapping("/api")
public class DebugController {

    private final ChatClient chatClient;

    public DebugController(ChatClient chatClient){
        this.chatClient=chatClient;
    }
    @PostMapping("/debug")
    public DebugResponse  debugControll(@RequestBody String code){
        String prompt= """
                Your are senior java debugger exper.
                
                Return response in json only in this format
                 Return ONLY JSON:
                    {
                      "bug": "...",
                      "explanation": "...",
                      "fixedCode": "..."
                    }
                code:
                """+code;
        String response=chatClient.prompt().user(prompt).call().content();
        String json=extractJson(response);
        json = fixJson(json);
        try {
            ObjectMapper mapper=new ObjectMapper();
            DebugResponse result=mapper.readValue(json,DebugResponse.class);
            writeToFile("D:/agent/test/Test.java",result.getFixedCode());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Filed to parse Ollama response"+json);
        }
    }
    public void writeToFile(String filePath,String code){
        try{
            Files.writeString(Path.of(filePath),code);
        }catch (Exception e){
            throw new RuntimeException("Failed to write file "+e);
        }
    }
    private String extractJson(String response) {
        int start=response.indexOf("{");
        int end=response.lastIndexOf("}");
        if (start!=-1&&end!=-1&&end>start) {
            return response.substring(start,end+1);
        }
        throw new RuntimeException("Invalid JSON response from AI: " + response);
    }
}
