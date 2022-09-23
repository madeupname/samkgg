package helloworld

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import software.amazon.awssdk.regions.Region
import java.util.logging.LogManager

/**
 * Created by Philip Yurchuk on 9/14/2022.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
class Config {
    companion object {

        val environment = System.getenv("APP_ENV") ?: "TEST" // defined in template.yaml
        val isDeployedToAWS: Boolean = isInsideLambda() && !isLocalLambda()
        val region: Region
        val dynamoDbEndpoint: String
        init {
            // Load the correct logging.properties file for this environment
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

            val envRegion = System.getenv("AWS_DEFAULT_REGION")
            region = if (envRegion == null) Region.US_WEST_2 else Region.of(envRegion)

            dynamoDbEndpoint = System.getenv("DYNAMODB_ENDPOINT") ?: ""
        }

        /**
         * true if running in ANY Lambda, including via "sam local invoke"
         */
        fun isInsideLambda(): Boolean {
            val name = System.getenv("AWS_LAMBDA_FUNCTION_NAME")
            return name?.isNotBlank() ?: false
        }

        /**
         * true only if code is running in a local Lambda (sam local invoke)
         */
        fun isLocalLambda(): Boolean {
            val isSamLocal = System.getenv("AWS_SAM_LOCAL")
            return isSamLocal?.toBoolean() ?: false
        }

        /**
         * By using this instead of SLF4J directly, we guarantee the correct logging.properties files has been used in
         * the initialization block of this class.
         */
        fun getLogger(name: String): Logger {
            return LoggerFactory.getLogger(name)
        }
    }
}