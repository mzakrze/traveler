package pl.mzakrze.traveler.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class GoogleApiKeyProvider {

    @Value("classpath:secrets/google_api_key")
    private Resource resource;

    public String getGoogleApiKey(){
        try {
            return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
