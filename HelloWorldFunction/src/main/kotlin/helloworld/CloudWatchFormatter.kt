package helloworld

import java.util.logging.LogRecord
import java.util.logging.SimpleFormatter

/**
 * CloudWatch has an issue where it sees every new line in a log message as a separate message.
 * This checks if environment variable LOGGER_REPLACE_NEWLINES is false. Otherwise, the formatter replaces newlines with
 * carriage returns so CloudWatch creates a single log record from a single message.
 * Created by Philip Yurchuk on 9/11/2022.
 */
class CloudWatchFormatter : SimpleFormatter() {

    companion object {

        var replaceNewlines: Boolean = true

        init {
            try {
                val envValue = System.getenv("LOGGER_REPLACE_NEWLINES")
                System.out.println("LOGGER_REPLACE_NEWLINES = $envValue")
                if (envValue.isNotBlank()) {
                    replaceNewlines = envValue.toBoolean()
                }
            } catch (_: Exception) {
            }
            System.out.println("Replace newlines? $replaceNewlines")
        }
    }

    /**
     * WARNING: any log statements in this method results in an infinite loop.
     * If LOGGER_REPLACE_NEWLINES == true (default) replaces the log message's newlines with carriage returns before formatting.
     */
    override fun format(record: LogRecord): String {

        if (replaceNewlines) {
            val carriageReturnMsg = record.message.replace("\n", "\r") + "\n"
            record.message = carriageReturnMsg
        }
        return super.format(record)
    }
}