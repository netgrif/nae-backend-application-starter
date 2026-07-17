package org.example.starter.startup

import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.startup.ImportHelper
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Slf4j
@Component
class CustomRunner extends AbstractOrderedCommandLineRunner {

    private final ImportHelper importHelper

    CustomRunner(ImportHelper importHelper) {
        this.importHelper = importHelper
    }

    @Override
    void run(String... args) throws Exception {
        importHelper.upsertNet("settings.xml", "settings")
    }
}
