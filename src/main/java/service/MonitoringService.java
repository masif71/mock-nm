package service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
public class MonitoringService {

    private static final String STATUS =
            "<status>\n"+
            "<result>OK</result>\n"+
            "</status>";

    @RequestMapping(value = "/monitoring", method = RequestMethod.GET)
    public @ResponseBody String getStatus() {
        return STATUS;
    }
}
