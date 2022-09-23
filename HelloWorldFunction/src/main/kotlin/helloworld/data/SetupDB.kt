package helloworld.data

import helloworld.domain.Customer
import software.amazon.awssdk.core.waiters.WaiterResponse
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProjectionType
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput


/**
 * Created by Philip Yurchuk on 9/16/2022.
 */
class SetupDB {
    companion object {
        fun setupCustomerTable() {
            val request = CreateTableEnhancedRequest.builder()
                .globalSecondaryIndices(
                    EnhancedGlobalSecondaryIndex.builder()
                        .indexName(Customer.EMAIL_INDEX)
                        .provisionedThroughput(
                            ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build()
                        )
                        .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
                        .build()
                )
                .build()
            CustomerRepository.mappedTable.createTable(request)
            // Wait for the table to be created before exiting the function
            val waiterResponse: WaiterResponse<DescribeTableResponse> =
                DynamoDbRepository.ddb.waiter().waitUntilTableExists { r ->
                    r.tableName(CustomerRepository.tableName)
                }
            waiterResponse.matched()
        }
    }
}