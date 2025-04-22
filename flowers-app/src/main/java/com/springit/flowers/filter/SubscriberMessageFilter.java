package com.springit.flowers.filter;

import com.springit.flowers.service.FlowService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.core.GenericSelector;
import org.springframework.messaging.Message;

import java.util.Set;

@Slf4j
@AllArgsConstructor
public class SubscriberMessageFilter implements GenericSelector<Message<?>> {


    private final int id;


    @Override
    public boolean accept(Message<?> message) {

        Set<Integer> subscribersId = (Set<Integer>) message.getHeaders().get(FlowService.FILTERED_SUBSCRIBERS);

        if (subscribersId == null) {
            return false;
        }

        boolean result = subscribersId.contains(id);

        log.debug("Message {}accepted for subscriber with Id = {}", (result ? "" : "not "), id);

        return result;
    }


}
