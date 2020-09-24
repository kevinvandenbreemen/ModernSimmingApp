package com.vandenbreemen.modernsimmingapp.data.googlegroups

import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.inject.Inject

/**
 * Helper:  Loads the content of a post based on its URL in the RSS feed
 */
class GooglePostContentLoader @Inject constructor() {

    fun getPostBody(postUrl: String): String {
        val urlToLoad = "(/d/)".toRegex().replace(postUrl, "/forum/print/")
        val url = URL(urlToLoad)
        val connection = url.openConnection() as HttpURLConnection
        var postBody = ""

        val bld = StringBuilder()
        connection.getInputStream().use({ inputStream ->
            val scanner = Scanner(inputStream)
            while (scanner.hasNextLine()) {
                bld.append(scanner.nextLine()).append("\n")
            }

            postBody = bld.toString()
        })

        //  Now we parse
        val document = Jsoup.parse(postBody)
        document.outputSettings().prettyPrint(false)
        document.select("div").append("\\n")
        document.select("br").append("\\n")

        val element = document.getElementsByClass("messageContent").get(0)

        var html = "\\\\n".toRegex().replace(element.html(), "\n")
        postBody = Jsoup.clean(html, "", Whitelist.none(), org.jsoup.nodes.Document.OutputSettings().prettyPrint(false))
        postBody = postBody.trim({ it <= ' ' })

        postBody = StringEscapeUtils.unescapeHtml4(postBody)

        return postBody
    }

}