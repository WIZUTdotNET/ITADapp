package pl.dotnet.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dotnet.main.mapper.EventMapper;
import pl.dotnet.main.mapper.EventMapperImpl;

@Configuration
public class MapperConfig {
    @Bean
    public EventMapper eventMapper() {
        return new EventMapperImpl();
    }
}
