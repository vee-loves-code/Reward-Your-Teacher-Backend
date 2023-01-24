package com.decagon.rewardyourteacherapi.logging;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoggingController {

    @RequestMapping("/")
    public String index() {
        log.trace("A TRACE MESSAGE");
        log.debug("A DEBUG MESSAGE");
        log.info("AN INFO MESSAGE");
        log.warn("A WARN MESSAGE");
        log.error("AN ERROR MESSAGE");

        return "checkout the log to see messages";
    }
}
