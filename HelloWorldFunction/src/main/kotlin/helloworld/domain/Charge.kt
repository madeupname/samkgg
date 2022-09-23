package helloworld.domain

import java.math.BigDecimal
import java.time.Instant

/**
 * Created by Philip Yurchuk on 3/2/2022.
 */
class Charge {
    var amount: BigDecimal? = null
    var createdOn: Instant? = null

    companion object {
        val AMOUNT = "AMOUNT"
        val CREATED_ON = "CREATED_ON"
    }
}