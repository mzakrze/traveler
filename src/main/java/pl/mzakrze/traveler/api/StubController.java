package pl.mzakrze.traveler.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mzakrze.traveler.config.GoogleApiKeyProvider;
import pl.mzakrze.traveler.google_maps_integration.GoogleMapsIntegration;

@RestController
@RequestMapping("/api/stub")
public class StubController {

    @Autowired
    private GoogleApiKeyProvider googleApiKeyProvider;

    @Autowired
    private GoogleMapsIntegration googleMapsIntegration;

    @GetMapping("/simple_string")
    public String simpleStr(){
        return "plz implement me";
    }

    @GetMapping("/simple_json")
    public SimpleJsonResponse createSpace(){
        return new SimpleJsonResponse();
    }

    @GetMapping("/simple_map_req")
    public String simpleMapRequest() {
        return googleMapsIntegration.mockRequest();
    }

    class SimpleJsonResponse {
        Integer someInt = 1;
        String someString = "abcde";

        public Integer getSomeInt() {
            return someInt;
        }

        public void setSomeInt(Integer someInt) {
            this.someInt = someInt;
        }

        public String getSomeString() {
            return someString;
        }

        public void setSomeString(String someString) {
            this.someString = someString;
        }
    }

}
