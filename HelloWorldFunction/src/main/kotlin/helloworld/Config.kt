package helloworld

/**
 * Created by Philip Yurchuk on 9/14/2022.
 */
@Suppress("BooleanMethodIsAlwaysInverted")
class Config {
    companion object {

        var isDeployedToAWS: Boolean = isInsideLambda() && !isLocalLambda()

        init {
            println("Deployed to AWS? $isDeployedToAWS")
        }

        /**
         * true if running in ANY Lambda, including via "sam local invoke"
         */
        fun isInsideLambda(): Boolean {
            val name = System.getenv("AWS_LAMBDA_FUNCTION_NAME")
            return name?.isNotBlank() ?: false
        }

        /**
         * true only if code is running in a local Lambda (sam local invoke)
         */
        fun isLocalLambda(): Boolean {
            val isSamLocal = System.getenv("AWS_SAM_LOCAL")
            return isSamLocal?.toBoolean() ?: false
        }
    }
}