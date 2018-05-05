package com.home.hipstabot.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class HtmlResponseParser(responseContent: String,
                         containerFunction: (Document) -> Element,
                         private val tag: String,
                         private val pointingAttributeKey: String,
                         private val contentAttributeKey: String) {

    private val parsedResponse : Document = Jsoup.parse(responseContent)
    private val container : Element = containerFunction.invoke(parsedResponse)

    fun extractElements(pointingAttributeValue: String) : List<Element> {
        return container
                .getElementsByTag(tag)
                .filter { x -> pointingAttributeValue == x.attr(pointingAttributeKey) }
    }

    fun getExtractedValues(pointingAttributeValue: String) : List<String> {
        return extractElements(pointingAttributeValue).map { x -> x.attr(contentAttributeKey) }
    }

    fun customSearch(containerFunction: (Document) -> Element,
                     tag: String,
                     elementFilter: (Element) -> Boolean) : List<Element> {
        return containerFunction.invoke(parsedResponse).getElementsByTag(tag).filter(elementFilter)
    }
}