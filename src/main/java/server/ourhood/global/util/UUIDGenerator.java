package server.ourhood.global.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UUIDGenerator {

	public String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
