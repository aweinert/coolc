package net.alexweinert.coolc.processors.cool.frontend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
class Configuration {
    @Bean
    Logger logger() { return LogManager.getLogger("CoolParser"); }
}
