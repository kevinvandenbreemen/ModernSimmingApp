package com.vandenbreemen.modernsimmingapp.tts

import com.vandenbreemen.modernsimmingapp.data.localstorage.PostBean

class PostParser(val sim: PostBean) {
    fun toUtterances(): List<String> {
        val utterances = mutableListOf<String>()
        utterances.add(sim.title)
        //utterances.add("By ${sim.}")

        val rawLines = "[\n]+".toRegex().split(sim.content)
            .filter { line -> !line.trim().isBlank() }

        var sentence: String? = null
        rawLines.forEach { str ->
            var line = str.trim()
            if (line.startsWith("::")) {
                line = line.substring("::".length)
            }
            if (line.endsWith("::")) {
                line = line.substring(0, (line.length - "::".length))
            }
            line = line.trim()

            //  Setting
            val settingRegex = "[(]+([^)]+)[)]+".toRegex()
            if (settingRegex.matches(line)) {
                utterances.add(settingRegex.matchEntire(line)!!.groupValues[1])
                return@forEach
            }

            val sentenceRegex = "[.!]+".toRegex()
            if (sentenceRegex.containsMatchIn(line)) {
                var betweenBreaks = sentenceRegex.split(line).filter { str -> !str.isBlank() }
                if (sentence != null && betweenBreaks.isNotEmpty()) {
                    sentence = "$sentence ${betweenBreaks.get(0)}"
                    utterances.add(sentence!!)
                    sentence = null
                    if (betweenBreaks.size > 1) {
                        betweenBreaks = betweenBreaks.subList(1, betweenBreaks.size)
                    } else {
                        return@forEach
                    }
                }

                //  Handle start of sentence on this line
                if(!line.endsWith(".") && !line.endsWith("!")){
                    val lastPart = betweenBreaks.last()
                    sentence = lastPart
                    if (betweenBreaks.size > 1) {
                        betweenBreaks = betweenBreaks.subList(0, betweenBreaks.size-1)
                    } else {
                        return@forEach
                    }
                }

                utterances.addAll(betweenBreaks)
                return@forEach
            } else {
                if (sentence.isNullOrBlank()) {
                    sentence = line
                } else {
                    sentence += " $line"
                }
                return@forEach
            }

            utterances.add(line)
        }

        sentence?.let { it ->
            utterances.add(it)
        }

        return utterances.map { item -> item.trim() }
    }
}