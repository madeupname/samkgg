package helloworld.data

import helloworld.Config
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

/**
 * Contains DynamoDB-related configuration and convenience methods.
 *
 * Created by Philip Yurchuk on 9/17/2022.
 */
open class DynamoDbRepository {

    companion object {

        val ddb = getClient()
        val enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build()

        private fun getClient(): DynamoDbClient {
            val ddb = DynamoDbClient.builder()
                .region(Config.region)
            // If running locally, configure the endpoint
            if (!Config.isDeployedToAWS) {
                if (Config.dynamoDbEndpoint.isNotBlank()) {
                    ddb.endpointOverride(URI.create(Config.dynamoDbEndpoint))
                } else {
                    throw IllegalStateException("Environment variable DYNAMODB_ENDPOINT is required for running locally. Value: " + Config.dynamoDbEndpoint)
                }
            }
            return ddb.build()
        }

        private fun get64LeastSignificantBitsForVersion1(): Long {
            val random63BitLong = Random.nextLong() and 0x3FFFFFFFFFFFFFFFL
            // Kotlin rejects the hex literal as out of range
            // https://youtrack.jetbrains.com/issue/KT-4749
            val variant3BitFlag: Long = java.lang.Long.parseUnsignedLong("0x8000000000000000")
            return random63BitLong + variant3BitFlag
        }

        private fun get64MostSignificantBitsForVersion1(): Long {
            val start = LocalDateTime.of(1582, 10, 15, 0, 0, 0)
            val duration = Duration.between(start, LocalDateTime.now())
            val seconds = duration.getSeconds()
            val nanos = duration.getNano()
            val timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100
            val least12SignificantBitOfTime = (timeForUuidIn100Nanos and 0x000000000000FFFFL) shr 4
            val version = 1 shl 12
            return (timeForUuidIn100Nanos and java.lang.Long.parseUnsignedLong("0xFFFFFFFFFFFF0000")) + version + least12SignificantBitOfTime
        }

        /**
         * Generates a version 1 UUID, which is the ideal version for DB IDs.
         * Solution from https://www.baeldung.com/java-uuid
         * @return UUID
         */
        fun createUUID(): UUID {
            return UUID(get64MostSignificantBitsForVersion1(), get64LeastSignificantBitsForVersion1())
        }

        /**
         * Turns a class name like Customer into a table name with app abbreviation, environment, and class.
         * E.g., Customer.class becomes GT_DEV_CUSTOMER or GT_PROD_CUSTOMER
         * @param aClass
         * @return
         */
        fun getTableName(aClass: Class<*>): String {
            // Convention for table name environment variable is upper case app initials, environment, and class name
            // E.g., SAMKGG_DEV_CUSTOMER
            val className = aClass.name.split(".").last() // remove package
            return "SAMKGG_${Config.environment}_${className}".uppercase()
        }
    }
}