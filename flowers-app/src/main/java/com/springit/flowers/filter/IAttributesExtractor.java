package com.springit.flowers.filter;


import org.springframework.messaging.Message;

import java.util.Collection;
import java.util.Map;

public interface IAttributesExtractor {

	Map<String, Object> extractAttributes(Collection<String> attributes, Message<?> message);
	
}
