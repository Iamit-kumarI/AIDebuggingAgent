package ai.debugger.agent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DebugController {

    private final ChatClient chatClient;

    public DebugController(ChatClient chatClient){
        this.chatClient=chatClient;
    }
    @PostMapping("/debug")
    public String debugControll(@RequestBody String code){
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
        return extractJson(response);
    }
    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start!=-1&&end!= -1) {
            return response.substring(start, end + 1);
        }
        return response; // fallback
    }

}
