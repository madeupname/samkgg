package helloworld.data

import helloworld.domain.Charge
import helloworld.domain.Customer
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.*
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID


/**
 * Created by Philip Yurchuk on 9/18/2022.
 */
class CustomerRepository: DynamoDbRepository() {
    companion object {
        val tableName = getTableName(Customer::class.java)

        private val chargeTableSchema = StaticTableSchema.builder(Charge::class.java)
            .newItemSupplier(::Charge)
            .addAttribute(BigDecimal::class.java) {
                it.name(Charge.AMOUNT)
                    .getter(Charge::amount::get)
                    .setter(Charge::amount::set)
            }.addAttribute(Instant::class.java) {
                it.name(Charge.CREATED_ON)
                    .getter(Charge::createdOn::get)
                    .setter(Charge::createdOn::set)
            }.build()

        private val customerTableSchema = StaticTableSchema.builder(Customer::class.java)
            .newItemSupplier(::Customer)
            .addAttribute(UUID::class.java) {
                it.name(Customer.ID)
                    .getter(Customer::id::get)
                    .setter(Customer::id::set)
                    .tags(primaryPartitionKey())
            }.addAttribute(Instant::class.java) {
                it.name(Customer.REGISTERED_ON)
                    .getter(Customer::registeredOn::get)
                    .setter(Customer::registeredOn::set)
                    .tags(secondarySortKey(Customer.EMAIL_INDEX))
            }.addAttribute(String::class.java) {
                it.name(Customer.EMAIL)
                    .getter(Customer::email::get)
                    .setter(Customer::email::set)
                    .tags(secondaryPartitionKey(Customer.EMAIL_INDEX))
            }.addAttribute(String::class.java) {
                it.name(Customer.NAME)
                    .getter(Customer::name::get)
                    .setter(Customer::name::set)
            }.addAttribute(EnhancedType.listOf(EnhancedType.documentOf(Charge::class.java, chargeTableSchema))) {
                it.name(Customer.CHARGES)
                    .getter(Customer::charges::get)
                    .setter(Customer::charges::set)
            }.build()

        val mappedTable = enhancedClient.table(tableName, customerTableSchema)
    }
}