package somepackage;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

	private final ThirdPartyGateway thirdPartyGateway;

	private static final Logger logger = LoggerFactory.getLogger(MyService.class);

	@Autowired
	public MyService(final ThirdPartyGateway thirdPartyGateway) {
		this.thirdPartyGateway = thirdPartyGateway;
	}

	public Map<String, Object> someFunction(String param1) {
		logger.info("entering my service class function 1. getting data using gateway. param {}", param1);
		final Map<String, Object> data = thirdPartyGateway.method1(param1);
		logger.info("existing my service class function 1. getting data using gateway. data {}", data);
		return data;
	}

}
