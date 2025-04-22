package com.springit.flowers.filter.impl;



import com.springit.flowers.filter.IAttributesExtractor;
import org.springframework.messaging.Message;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HeadersAttributesExtractor implements IAttributesExtractor {

	@Override
	public Map<String, Object> extractAttributes(Collection<String> attributes, Message<?> message) {
		Map<String, Object> headers = message.getHeaders();
		Map<String, Object> result = new HashMap<>();
		for (String attribute : attributes) {
			result.put(attribute, headers.get(attribute));
		}
		return result;
	}

}
