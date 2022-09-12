package helloworld

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AppTest {
    @Test
    fun successfulResponse() {
        val app = App()
        val result = app.handleRequest(null, null)
        Assertions.assertEquals(result.statusCode, 200)
        Assertions.assertEquals(result.headers["Content-Type"], "application/json")
        val content = result.body
        Assertions.assertNotNull(content)
        Assertions.assertTrue(content.contains("\"message\""))
        Assertions.assertTrue(content.contains("\"hello world\""))
        Assertions.assertTrue(content.contains("\"location\""))
    }
}