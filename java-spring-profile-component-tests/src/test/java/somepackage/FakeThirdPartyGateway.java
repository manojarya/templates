package somepackage;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Fake implementation of third party gateway. Notice its profile is default.
 *
 */
@Component
@Profile("default")
public class FakeThirdPartyGateway implements ThirdPartyGateway {

	private static final Logger logger = LoggerFactory.getLogger(FakeThirdPartyGateway.class);

	@Override
	public Map<String, Object> method1(String param1) {
		logger.info("fake third party gateway return data for param {}", param1);
		final Map<String, Object> fakeData = new HashMap<>();
		fakeData.put("key1", "val1");
		fakeData.put("key2", "val2");
		return fakeData;
	}

}
