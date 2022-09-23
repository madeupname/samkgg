package helloworld

import helloworld.data.SetupDB
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

/**
 * Created by Philip Yurchuk on 9/16/2022.
 */
class CustomerDBTest {

    @Test
    fun createDB() {
        assertDoesNotThrow { SetupDB.setupCustomerTable() }
    }
}