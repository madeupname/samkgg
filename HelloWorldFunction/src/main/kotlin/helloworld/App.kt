package helloworld

import com.amazonaws.lambda.thirdparty.com.google.gson.GsonBuilder
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.util.logging.LogManager


/**
 * Handler for requests to Lambda function.
 */
class App : RequestHandler<APIGatewayV2HTTPEvent?, APIGatewayV2HTTPResponse> {

    /**
     * Must set the logging properties file here so CloudWatchFormatter is used.
     */
    companion object {

        val gson = GsonBuilder().setPrettyPrinting().create()

        init {
            var configFile: String
            try {
                configFile = System.getenv("LOGGING_PROPERTIES")
                if (!configFile.endsWith("logging.properties")) {
                    throw IllegalArgumentException("LOGGING_PROPERTIES environment variable must contain a resource name that ends in logging.properties")
                }
                println("LOGGING_PROPERTIES = $configFile")
            } catch (ex: Exception) {
                configFile = "logging.properties"
            }
            LogManager.getLogManager().readConfiguration(App::class.java.getResourceAsStream("/$configFile"))
        }
    }

    private val logger = LoggerFactory.getLogger(App::class.java.name)

    override fun handleRequest(input: APIGatewayV2HTTPEvent?, context: Context?): APIGatewayV2HTTPResponse {
        // Store the AWS Request ID in MDC (a thread-bound map) so the logger can access it
        MDC.getMDCAdapter().put("AWSRequestId", context?.awsRequestId)
        logger.debug("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()))
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