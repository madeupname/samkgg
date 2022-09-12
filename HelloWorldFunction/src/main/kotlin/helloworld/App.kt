package helloworld

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse

/**
 * Handler for requests to Lambda function.
 */
class App : RequestHandler<APIGatewayV2HTTPEvent?, APIGatewayV2HTTPResponse> {
    companion object {
        @JvmStatic
        val inputStream = App::class.java.getResourceAsStream("/logging.properties")
        init {

            System.out.println("logging.properties: $inputStream")
        }
    }

    override fun handleRequest(input: APIGatewayV2HTTPEvent?, context: Context?): APIGatewayV2HTTPResponse {

        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["X-Custom-Header"] = "application/json"
        val response = APIGatewayV2HTTPResponse.builder()
            .withHeaders(headers)
        return try {
            val output = "{ \"message\": \"hello world\", \"location\": \"The Sol System\" }"
            response
                .withStatusCode(200)
                .withBody(output).build()
        } catch (e: Exception) {
            e.printStackTrace()
            response
                .withBody("{}")
                .withStatusCode(500).build()
        }
    }
}