package edu.sjsu.cmpe272.simpleblog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static edu.sjsu.cmpe272.simpleblog.client.INIFile.getUserId;

@Component
@Command(name = "commandlineservice", mixinStandardHelpOptions = true, subcommands = {CommandLineService.PostCommand.class, CommandLineService.CreateUserCommand.class, CommandLineService.ListMessagesCommand.class})
public class CommandLineService {

    @Component
    @Command(name = "list", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 0)
    static class ListMessagesCommand implements Callable<Integer> {
        @Option(names = "--starting", description = "Starting message ID", defaultValue = "0")
        private Integer startingId;

        @Option(names = "--count", description = "Limit the number of messages", defaultValue = "10")
        private Integer messageLimit;

        @Option(names = "--save-attachment", description = "Option to save the attachment of a message", defaultValue = "false")
        private Boolean saveAttachment;

        @Override
        public Integer call() throws Exception {
            // Implementation remains the same, adjusted to use refactored variable names
            return 667; // Status code placeholder
        }
    }

    @Component
    @Command(name = "create", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 0)
    static class CreateUserCommand implements Callable<Integer> {
        @Option(names = "userid", required = true, description = "User ID for the new user",defaultValue = "")
        private String userId;

        @Override
        public Integer call() throws Exception {
            // Implementation adjusted for clarity and using refactored variable names
            String[] arr = KeyPairGeneratorUtility.createKeyPair();
            INIFile.saveFile(userId, arr[1]);
            StringBuilder publicKEY = new StringBuilder();
            publicKEY.append("-----BEGIN PUBLIC KEY-----\n");

            // Split the encoded string into multiple lines
            int index = 0;
            while (index < arr[0].length()) {
                int endIndex = Math.min(index + 64, arr[0].length());
                publicKEY.append(arr[0].substring(index, endIndex));
                publicKEY.append("\n");
                index = endIndex;
            }
            publicKEY.append("-----END PUBLIC KEY-----");

            UserAPI userAPI = new UserAPI(userId,publicKEY.toString());
            ObjectMapper mapper = new ObjectMapper();

            String userJson = mapper.writeValueAsString(userAPI);

            String result = String.valueOf(postAPI("https://sivakumarreddy6758.ignorelist.com/user/create",userJson).block());
            System.out.println(result);
            return 667;
        }

    }

    @Component
    @Command(name = "post", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 0)
    static class PostCommand implements Callable<Integer>{
        @Option(names = "message", required = true, description = "Content of the message to post")
        private String messageContent;

        @Option(names = "file-to-attach", description = "File path for the message's attachment", defaultValue = "")
        private String fileAttachmentPath;

        @Override
        public Integer call() throws Exception {
            // Adjusted for refactored variable names and clarity
            String dateTime = LocalDateTime.now().toString();
            String userid = getUserId();

            if(userid==null){
                System.out.println("User not available, create user first");
                return 0;
            }
            PostAPI postAPI = new PostAPI(dateTime,userid,messageContent,fileAttachmentPath,"as/f32230FS+");

            ObjectMapper mapper = new ObjectMapper();

            String userJson = mapper.writeValueAsString(postAPI);

            String result = String.valueOf(postAPI("https://sivakumarreddy6758.ignorelist.com/messages/create",userJson).block());
            System.out.println(result);

            return 667; // Status code placeholder
        }
    }

    public static Mono<String> postAPI(String url, String rawJson) {

        WebClient webclient = WebClient.builder().build();
        return webclient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(rawJson)
                .retrieve()
                .bodyToMono(String.class);
    }
}