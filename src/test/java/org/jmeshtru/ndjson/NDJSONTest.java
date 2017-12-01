package org.jmeshtru.ndjson;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class NDJSONTest {

	private final String SAMPLE_JSON = "{\"foo\":17,\"bar\":false,\"quux\":true}";

	@Test
	public void parseValidJson() {
		List<JSONObject> jsonObjects = new NDJSON().parse(getUTF8StreamFromString(SAMPLE_JSON));

		assertThat(jsonObjects.size()).isEqualTo(1);
		JSONObject actual = jsonObjects.get(0);
		assertThat(actual).isInstanceOf(JSONObject.class);
		assertThat(actual.getInt("foo")).isEqualTo(17);
	}

	@Test
	public void parseValidMultiLineJson() {
		String newLineSeparatedJson =
				IntStream.range(0, 100)
						.mapToObj(i -> SAMPLE_JSON)
						.collect(Collectors.joining("\n"));

		List<JSONObject> jsonObjects = new NDJSON().parse(getUTF8StreamFromString(newLineSeparatedJson));

		assertThat(jsonObjects.size()).isEqualTo(100);
		JSONObject actual1 = jsonObjects.get(0);
		JSONObject actual2 = jsonObjects.get(1);
		JSONAssert.assertEquals(actual1, actual2, true);
	}

	@Test
	public void parseWithEmptyLines() {
		String emptyLineJson = SAMPLE_JSON +
				"\n\n\n" +
				SAMPLE_JSON;
		List<JSONObject> jsonObjects = new NDJSON().parse(getUTF8StreamFromString(emptyLineJson));
		assertThat(jsonObjects.size()).isEqualTo(2);
	}

	@Test(expected = JSONException.class)
	public void parseInvalidJson() {
		String invalidJson = "{\"foo\":17,\"bar\":false,\"quux\":}";
		new NDJSON().parse(getUTF8StreamFromString(invalidJson));
	}

	@Test(expected = JSONException.class)
	public void parseInvalidJsonWithNewLine() {
		String invalidJson = "{\"foo\":17,\"bar\":false,\n\"quux\":true}";
		new NDJSON().parse(getUTF8StreamFromString(invalidJson));
	}

	@Test
	public void generateNDJson() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			new NDJSON().generate(Collections.singletonList(new JSONObject(SAMPLE_JSON)), outputStream);
			String actual = outputStream.toString();
			JSONAssert.assertEquals(SAMPLE_JSON, actual, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private InputStream getUTF8StreamFromString(String input) {
		return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
	}

}