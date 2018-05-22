package service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_XML_VALUE)
public class NamematcherService implements InitializingBean {

    private String sourcesResponse;

    private String typeCategoriesResponse;

    private List<String> entitites;

    private String responseWithResults;

    @Value("${response.results.percent:5}")
    private int response_with_results_percent;

    private static final Random randomGenerator = new Random();

    private static final String MOCK_EMPTY_RESPONSE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<resultset xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n" +
                    "      <indexDate>%s</indexDate>\n" +
                    "      <results/>\n" +
                    "</resultset>";

    @Override
    public void afterPropertiesSet() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        sourcesResponse = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("sources.xml")))
                .lines().collect(Collectors.joining("\n"));

        typeCategoriesResponse = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("typeCategories.xml")))
            .lines().collect(Collectors.joining("\n"));

        entitites = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("entities.csv")))
                .lines().collect(Collectors.toList());

        responseWithResults = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream("results.xml")))
                .lines().collect(Collectors.joining("\n"));

    }

    @RequestMapping(value = "/entity/search/advanced/secondary", method = RequestMethod.POST)
    public @ResponseBody String search(@RequestBody String query) {
        int rnd = randomGenerator.nextInt(100) + 1;
        Instant indexDate = Instant.now();
        if (rnd <= response_with_results_percent) {
            Instant entityModifiedDate = indexDate;
            String resultEntityId = entitites.get(randomGenerator.nextInt(entitites.size()));
            return String.format(responseWithResults, indexDate, resultEntityId, entityModifiedDate);
        } else {
            return String.format(MOCK_EMPTY_RESPONSE, indexDate);
        }
    }

    @RequestMapping(value = "/provider/source", method = RequestMethod.GET, produces = "application/xml; charset=utf-8")
    public @ResponseBody String providerSources() {
        return sourcesResponse;
    }

    @RequestMapping(value = "/provider/source/type/category", method = RequestMethod.GET, produces = "application/xml; charset=utf-8")
    public @ResponseBody String typeCategories() {
        return typeCategoriesResponse;
    }
}
