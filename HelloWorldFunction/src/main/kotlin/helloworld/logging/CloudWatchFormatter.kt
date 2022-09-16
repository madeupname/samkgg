package helloworld.logging

import helloworld.Config
import org.slf4j.MDC
import org.slf4j.spi.MDCAdapter
import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

/**
 * CloudWatch has an issue where it sees every new line in a log message as a separate message.
 *
 * Replaces the log message's newlines with carriage returns if running in a deployed Lambda so CloudWatch does not
 * turn every single line into a separate log entry.
 *
 * Created by Philip Yurchuk on 9/11/2022.
 */
@Suppress("unused")
class CloudWatchFormatter : SimpleFormatter() {

    companion object {
        var mdcAdapter: MDCAdapter = MDC.getMDCAdapter()
    }

    /**
     * WARNING: any log statements in this method results in an infinite loop.
     *
     * Replaces the log message's newlines with carriage returns if running in a deployed Lambda so CloudWatch does not
     * turn every single line into a separate log entry.
     */
    override fun format(record: LogRecord): String {

        if (Config.isDeployedToAWS) {
            val awsRequestId = mdcAdapter.get("AWSRequestId")
            if (awsRequestId != null) {
                record.message = "[AWSRequestId: $awsRequestId] ${record.message}"
            }
            val carriageReturnMsg = record.message.replace("\n", "\r")
            record.message = carriageReturnMsg
        }
        return super.format(record)
    }
}