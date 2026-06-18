package org.example.starter.startup

import com.netgrif.application.engine.startup.ApplicationEngineFinishRunner
import com.netgrif.application.engine.startup.annotation.RunnerOrder
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Slf4j
@Component
@RunnerOrder(150)
@RequiredArgsConstructor
class CustomRunner implements ApplicationEngineFinishRunner {

    private static Logger log = LoggerFactory.getLogger(CustomRunner.class)

    @Override
    void run(ApplicationArguments args) throws Exception {
        log.info("Calling custom runner");
        //TODO: Implement
    }
}
