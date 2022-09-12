package helloworld

import com.amazonaws.lambda.thirdparty.com.google.gson.GsonBuilder
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.logging.LogManager


/**
 * Handler for requests to Lambda function.
 */
class App : RequestHandler<APIGatewayV2HTTPEvent?, APIGatewayV2HTTPResponse> {

    /**
     * Static initializer block to ensure JUL uses the logging.properties file in resources.
     * GraalVM must be instructed to include the file. It should also be initialized at build time.
     */
    companion object {
        @JvmStatic
        var logger: Logger //= LoggerFactory.getLogger(App::class.java.name)
        init {
            var clInputStream = ClassLoader.getSystemResourceAsStream("/logging.properties")
            var appInputStream = App::class.java.getResourceAsStream("/logging.properties")
            System.out.println("ClassLoader stream: $clInputStream\nApp stream: $appInputStream")
//            LogManager.getLogManager().readConfiguration()
//            try {
//
//            } catch (ex: Exception) {
//                when (ex) {
//                    is IOException, is SecurityException -> {
////                        Logger.getLogger(App::class.simpleName)
////                            .log(Level.SEVERE, "Failed to read logging.properties file", ex)
//                        System.out.println("System.out: Failed to read logging.properties file")
//                        throw ex
//                    }
//                }
//            }
            logger = LoggerFactory.getLogger(App::class.java.name)

            /*val propertiesFile = System.getenv("LOGGING_PROPERTIES")
            System.out.println("properties file: $propertiesFile")
            val inputStream: InputStream?
            if (propertiesFile != null && propertiesFile.isNotBlank()) {
                inputStream = File(propertiesFile).inputStream()
            } else {
                inputStream = App::class.java.getResourceAsStream("/logging.properties")
            }
            if (inputStream == null) {
                throw IllegalStateException("Logging properties input stream is null")
            }*/
//            LogManager.getLogManager().readConfiguration(App::class.java.getResourceAsStream("/logging.properties"))

        }
    }
    /*init {
        LogManager.getLogManager().readConfiguration(App::class.java.getResourceAsStream("/logging.properties"))
    }*/
//    val logger: Logger = LoggerFactory.getLogger(javaClass)
    var gson = GsonBuilder().setPrettyPrinting().create()

    override fun handleRequest(input: APIGatewayV2HTTPEvent?, context: Context?): APIGatewayV2HTTPResponse {
        val inputStream = App::class.java.getResourceAsStream("/logging.properties")
        logger.info("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()))
        logger.error("This is an error.")
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