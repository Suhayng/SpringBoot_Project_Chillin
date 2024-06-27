package com.chillin;

import com.slack.api.bolt.App;
import com.slack.api.model.event.MessageEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChillinApplicationTests {


        public App initSlackApp() {
            App app = new App();

            // 메시지 이벤트 처리
            app.event(MessageEvent.class, (payload, ctx) -> {
                String user = payload.getEvent().getUser();
                String text = payload.getEvent().getText();
                ctx.say("Hello, <@" + user + ">! You said: " + text);
                return ctx.ack();
            });

            return app;
        }


}
