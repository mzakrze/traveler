package pl.mzakrze.traveler.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stub")
public class StubController {

    @GetMapping("/simple_string")
    public String simpleStr(){
        return "plz implement me";
    }

    @GetMapping("/simple_json")
    public SimpleJsonResponse createSpace(){
        return new SimpleJsonResponse();
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
