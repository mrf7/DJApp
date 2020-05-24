package com.mfriend.djapp

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

// TODO make object documentation parse a gradle task
private const val objectModelUrl =
    "https://developer.spotify.com/documentation/web-api/reference/object-model/"

fun main() {
    // Fetch the html of the object model page
    val doc = Jsoup.connect(objectModelUrl).get()

    // Get all the table in the doc
    val tables = doc.select("table") as List<Element>

    // Get all the object names, assuming they are the only objects on the page at h2 (true atm)
    val modelNames = doc.select("h2").map { it.text()!! }

    // Drop the disallows object because it has a dumb table
    // https://developer.spotify.com/documentation/web-api/reference/object-model/#disallows-objectf
    val namesTablesMap = modelNames.zip(tables).toMap() - "disallows object"
    val objectsWithProperties = namesTablesMap.mapValues { entry ->
        val table = entry.value
        table
            .select("tbody")
            .select("tr")
            .map { row ->
                row.select("td").map { it.text() }
            }.map { (name, type, description) ->
                Property(name, type, description)
            }
    }
    objectsWithProperties.entries.forEach { (objectName, properties) ->
        println("$objectName: ")
        properties.forEach {
            println("\t$it")
        }
    }
}

data class Property(val name: String, val type: String, val description: String)