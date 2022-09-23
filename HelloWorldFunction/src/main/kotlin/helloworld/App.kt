package helloworld

import com.amazonaws.lambda.thirdparty.com.google.gson.GsonBuilder
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import helloworld.data.SetupDB
import org.slf4j.MDC


/**
 * Handler for requests to Lambda function.
 */
class App : RequestHandler<APIGatewayV2HTTPEvent?, APIGatewayV2HTTPResponse> {

    /**
     * Must set the logging properties file here so CloudWatchFormatter is used.
     */
    companion object {
        val gson = GsonBuilder().setPrettyPrinting().create()
    }

    private val logger = Config.getLogger(App::class.java.name)

    override fun handleRequest(input: APIGatewayV2HTTPEvent?, context: Context?): APIGatewayV2HTTPResponse {

        // Store the AWS Request ID in MDC (a thread-bound map) so the logger can access it
        MDC.getMDCAdapter().put("AWSRequestId", context?.awsRequestId)
        logger.debug("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()))
        SetupDB.setupCustomerTable()
        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["X-Custom-Header"] = "application/json"
        val response = APIGatewayV2HTTPResponse.builder().withHeaders(headers)
        return try {
            val output = "{ \"message\": \"hello world\", \"location\": \"The Sol System\" }"
            response.withStatusCode(200).withBody(output).build()
        } catch (e: Exception) {
            e.printStackTrace()
            response.withBody("{}").withStatusCode(500).build()
        }
    }
}