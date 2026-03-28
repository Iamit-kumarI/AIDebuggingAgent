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

    @Autowired
    private final ChatClient chatClient;

    public DebugController(ChatClient chatClient){
        this.chatClient=chatClient;
    }
    @PostMapping("/debug")
    public String debugControll(@RequestBody String code){
        String prompt= """
                Your are senior java debugger exper.
                
                Analyze the following code
                1. find bug
                2. Explain the issue clearly
                3. Provide corrected code
                
                code:
                """+code;
        return chatClient.prompt().user(prompt).call().content();
    }

}
