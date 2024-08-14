package hoanglong180903.myproject.socialhub.utils

import android.annotation.SuppressLint
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.collect.Lists
import java.io.ByteArrayInputStream
import java.io.IOException

class Accesstoken {
    private val url = " https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send"
    @SuppressLint("SuspiciousIndentation")
    fun getAccessToken(): String {
        try {
            val jsonString: String = "{\n" +
                    " \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"socialhubproject-f816b\",\n" +
                    "  \"private_key_id\": \"2955c7a52a201bd1b3c8cc4a8d1ec5a30427ecba\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCwYFpw1yuoj2UI\\nEXu6vZTX0+01pbz+pUELtPCTVnlvzGXQXTMlKc09433z7NEXJ9fEK/uIj/lBeznA\\nolLQw9E/DTKoZRQTuPoF9rTA2T1tuiQe1N+nqcReDOWITDDSMW+m9oRlvkQFEn41\\nrk8mPmHIYSPTRpyhtvWckM7o1LPmArw5V6VQ6rfcgISuTvi25B0+bixiasEpJlqD\\nYzExTHoRG7vY95QGM3gMyZlvhk5mdqAIXdqUhtJ3FRGleYod2l+mndKkIykFG29N\\nywkAXZ3sAj0xPzN7ZsZrbseRtYklBofapMm12znG0G/GLlcWLIy17S4aN3F7rtYC\\nyRFWLVOJAgMBAAECggEADF5YE/Va4pavsz76n2//r0T1gQoZif1PuuuaOqgumuhJ\\nXtvHpDxdYBDYenxpy0cS1UxvoWDV+MFp8uLPOO71I7KgEfld95DEuRoBt0u6CDxX\\nSnGLf51Nn896tzH/SHY0ChAxMlYDPMcnt57805s9+IiAs9EOAd8r3n9fKypjX+aa\\nORcS6ZGHiCJ6V3fUysXdPahVi6S5T8SUvI3QXZbBG+AOqDGsPQ/aQFwyyyeImqVn\\nIXG3ucd98kVZ+LnYW/+6IsgabHyDy/4cK7pBW8MtEH049H0UipUFTD79z0GCizbr\\nBBWkmIOdhAKHzSJ+oTQA2C/6d2fnuYqo33lPNfhi6QKBgQDtBj76qRM/xUZh6ZqA\\nUbjgCwzK+5JBXFHqiuG042s5goY9jG447bXKrxhDZRSqiZMptcJn9n70WGZOYSVT\\n69UT+eAYidvNfwFieuyM55BWa7FukOUIoHryQJ3VANehcEv7ZKFKfyidYlS8eCcE\\nT4VnuQgzZ9M3tSOnvVQ2tGtf/QKBgQC+fyRKLhaX2cSaxf68OdU0oW0RFG2NfB9p\\nslh0ranFR6MtA8xjcB/piBNwPQ8y+xHWeVQ48lo2H+j8gZ8P3xMck+TKeWdXyDUY\\nCcvF7gLUFeE6hRfVS9FUjmV5uUxkpMzE5/tO0WGlPgSZop/Zqt5TFvkgM1An+0XM\\ncNax14rZfQKBgG5eizMDSfBW9zgepA/+fdztqE5YqLkAuTBC7WjiwQotij7cxnKB\\n1jYEJIM1bVMf7sPsXBM3B9TV2QXEXLJgTX8Yys4CW7Sr4EQwQzg1l9N2IFAA7nV+\\n7OEjT5tg4gfNl8P/Ds/2uD3ZDOYZ+Okb8l3jHjt5rDPziB/SnIVArAMtAoGANQqh\\nyCXdFJsGKzJGI5LW0ri/7amKd4LAHGYUSyivxMaVoMsV9foaaKQDBZV6ze8qoFVY\\nJiMVC00HSoetCnZKlAJ6CgaTx9VvMypHGu2SzArrk6fLYxChULH1f1cg9kUM3ybY\\nyrFyRthl34FwkOMK1IfpBhZDk15bqPkiTpJ9juECgYArAK4iA5Da8KHXyejlRqVc\\n4Dj5Jkbevc1yMxQ1erJcz7dF+Xmj5G9gwG3zdoIytVvtoOXi+3nDj/9yGim0Xnbc\\nuf2r6x+X8rwRGJKXA8pbvcOvy8kg8k6Dj2HgtRDso2RDphAZb2DgugqxBvI924iQ\\nRFDtZQkQXtoTS6tnUuOOqQ==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-m9u6l@socialhubproject-f816b.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"107825760882455668277\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-m9u6l%40socialhubproject-f816b.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\""
                    "}\n"
            val inputStream = ByteArrayInputStream(jsonString.toByteArray())
            var googleCredentials : GoogleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(Lists.newArrayList(url))

            googleCredentials.refresh()
            return googleCredentials.accessToken.tokenValue
        }catch (e: IOException){
            Log.e("error", e.message!!)
            return null!!
        }
    }
}