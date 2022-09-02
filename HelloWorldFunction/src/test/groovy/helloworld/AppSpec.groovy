package helloworld

import static org.junit.jupiter.api.Assertions.*
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import spock.lang.Specification

/**
 * Created by Philip Yurchuk on 8/31/2022.
 */
class AppSpec extends Specification {

    def "Spock works"() {
        given:
        App app = new App()
        when:
        APIGatewayV2HTTPResponse result = app.handleRequest(null, null)
        then:
        assertEquals(result.getStatusCode(), 200)
        assertEquals(result.headers['Content-Type'], 'application/json')
        String content = result.getBody()
        assertNotNull(content)
        assertTrue(content.contains('"message"'))
        assertTrue(content.contains('"hello world"'))
        assertTrue(content.contains('"location"'))
    }
}
