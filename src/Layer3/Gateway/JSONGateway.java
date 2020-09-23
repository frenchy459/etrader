package Layer3.Gateway;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JSONGateway {

    public JSONObject getJson() throws IOException {
        InputStream is = new FileInputStream("phase2/config.json");
        String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
        return new JSONObject(jsonTxt);
    }
}
