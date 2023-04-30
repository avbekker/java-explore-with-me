package ru.practicum.main_service.collaboration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"ru.practicum.main_service", "ru.practicum.stats_client"})
public class CustomSpringConfiguration {

}
