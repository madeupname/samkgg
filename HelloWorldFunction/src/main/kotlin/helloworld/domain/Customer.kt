package helloworld.domain

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey
import java.time.Instant
import java.util.*

/**
 * Created by Philip Yurchuk on 2/23/2022.
 */
//@DynamoDBTable(tableName = Customer.PROD_TABLE_NAME) // OVERRIDE VIA DynamoDBMapperConfig !!!
class Customer {
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute(ID)
    var id: UUID? = null

    @get:DynamoDbSecondarySortKey(indexNames = [EMAIL_INDEX])
    @get:DynamoDbAttribute(REGISTERED_ON)
    var registeredOn: Instant? = null

    //    @DynamoDBAttribute(attributeName = NAME)
    @get:DynamoDbAttribute(NAME)
    var name: String? = null

    //    @DynamoDBIndexHashKey(attributeName = EMAIL, globalSecondaryIndexName = EMAIL_INDEX)
    @get:DynamoDbSecondaryPartitionKey(indexNames = [EMAIL_INDEX])
    @get:DynamoDbAttribute(EMAIL)
    var email: String? = null

    //    @DynamoDBAttribute(attributeName = CHARGES)
    //    @DynamoDBTypeConvertedJson
    @get:DynamoDbAttribute(CHARGES)
    var charges: List<Charge>? = null

    companion object {
        // Attributes and indexes of the Customer table
        const val ID = "ID"
        const val EMAIL = "EMAIL"
        const val NAME = "NAME"
        const val REGISTERED_ON = "REGISTERED_ON"
        const val EMAIL_INDEX = "EMAIL_GSI"
        const val CHARGES = "CHARGES"
    }
}