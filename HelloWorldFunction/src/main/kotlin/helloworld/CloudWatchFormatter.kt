package helloworld

import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

/**
 * CloudWatch has an issue where it sees every new line in a log message as a separate message.
 * This formatter replaces newlines with carriage returns so CloudWatch creates a single log record from a single
 * message.
 * Created by Philip Yurchuk on 9/11/2022.
 */
class CloudWatchFormatter : SimpleFormatter() {

    /**
     * Replaces the log message's newlines with carriage returns before formatting.
     */
    override fun format(record: LogRecord): String {

        val carriageReturnMsg = record.message.replace("\n", "\r") + "\n"
        record.message = carriageReturnMsg
        return super.format(record)
    }
}