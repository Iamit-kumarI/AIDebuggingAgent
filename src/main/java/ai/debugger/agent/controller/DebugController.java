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
    public DebugResponse debugControll(@RequestBody String code) {

        String prompt = """
            You are a senior Java debugger expert.

            Return ONLY valid JSON:
            {
              "bug": "...",
              "explanation": "...",
              "fixedCode": "..."
            }

            code:
            """ + code;

        try {
            DebugResponse response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .entity(DebugResponse.class);
            writeToFile("D:/agent/Test.java", response.getFixedCode());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            String raw = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            System.out.println("RAW RESPONSE: " + raw);
            try {
                String json = extractJson(raw);
                ObjectMapper mapper = new ObjectMapper();
                DebugResponse response = mapper.readValue(json, DebugResponse.class);
                writeToFile("D:/agent/test/Test.java", response.getFixedCode());
                return response;
            } catch (Exception ex) {
                throw new RuntimeException("FINAL FAILURE -> RAW: " + raw, ex);
            }
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
    private String extractFixedCode(String response) {
        int start = response.indexOf("public class");
        int end = response.lastIndexOf("}");
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }
        throw new RuntimeException("Failed to extract fixed code");
    }
}
