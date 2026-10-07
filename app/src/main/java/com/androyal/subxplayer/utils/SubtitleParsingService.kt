package com.androyal.subxplayer.utils

import com.androyal.subxplayer.data.models.SubtitleCue
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object SubtitleParsingService {

    fun parse(text: String, mime: String): List<SubtitleCue> {
        return when {
            mime.contains("vtt", true) || text.trimStart().startsWith("WEBVTT") -> parseVtt(text.byteInputStream())
            mime.contains("lrc", true) || text.contains("[") && text.contains("]") && Regex("""\[\d{1,2}:\d{2}\.\d{2}\]""").containsMatchIn(text) -> parseLrc(text)
            else -> parseSrt(text.byteInputStream())
        }
    }

    fun parse(input: InputStream, mime: String): List<SubtitleCue> {
        val txt = input.readBytes().toString(Charsets.UTF_8)
        return parse(txt, mime)
    }

    fun parseSrt(input: InputStream): List<SubtitleCue> {
        val text = input.readBytes().toString(Charsets.UTF_8)
        return parseSrtString(text)
    }

    private fun parseSrtString(text: String): List<SubtitleCue> {
        val cues = mutableListOf<SubtitleCue>()
        var id = 0L
        // Normalize line endings
        val normalized = text.replace("\r\n", "\n").replace("\r", "\n")
        val blocks = normalized.split(Regex("\n\\s*\n"))
        val timeRegex = Regex("""(\d+):(\d+):(\d+)[,\.](\d+)\s*-->\s*(\d+):(\d+):(\d+)[,\.](\d+)""")
        for (block in blocks) {
            val lines = block.trim().lines()
            if (lines.size < 2) continue
            // Find time line
            var timeLineIdx = -1
            var match: MatchResult? = null
            for (i in lines.indices) {
                match = timeRegex.find(lines[i])
                if (match != null) { timeLineIdx = i; break }
            }
            if (timeLineIdx == -1 || match == null) continue
            fun toMs(h: String, m: String, s: String, ms: String): Long {
                var msVal = ms.toLong()
                // If ms is 2 digits, it's centiseconds? normalize to ms
                if (ms.length == 2) msVal *= 10
                if (ms.length == 1) msVal *= 100
                return h.toLong() * 3600000 + m.toLong() * 60000 + s.toLong() * 1000 + msVal
            }
            try {
                val start = toMs(match.groupValues[1], match.groupValues[2], match.groupValues[3], match.groupValues[4])
                val end = toMs(match.groupValues[5], match.groupValues[6], match.groupValues[7], match.groupValues[8])
                val txt = lines.drop(timeLineIdx + 1).joinToString("\n").trim()
                    .replace(Regex("<[^>]+>"), "") // strip html tags
                    .trim()
                if (txt.isNotEmpty()) cues.add(SubtitleCue(id++, start, end, txt))
            } catch (_: Exception) {}
        }
        return cues.sortedBy { it.startMs }
    }

    fun parseVtt(input: InputStream): List<SubtitleCue> {
        val text = input.readBytes().toString(Charsets.UTF_8)
            .replace("WEBVTT", "")
        // VTT uses . instead of , for ms; normalize to ,
        val normalized = text.replace('.', ',')
        return parseSrtString(normalized)
    }

    fun parseLrc(text: String): List<SubtitleCue> {
        val cues = mutableListOf<SubtitleCue>()
        val regex = Regex("""\[(\d{1,2}):(\d{2})\.(\d{2,3})\](.*)""")
        var id = 0L
        val entries = mutableListOf<Pair<Long, String>>()
        for (line in text.lines()) {
            val m = regex.find(line) ?: continue
            val min = m.groupValues[1].toLong()
            val sec = m.groupValues[2].toLong()
            val msRaw = m.groupValues[3]
            var ms = msRaw.toLong()
            if (msRaw.length == 2) ms *= 10
            val start = min * 60000 + sec * 1000 + ms
            val txt = m.groupValues[4].trim()
            if (txt.isNotEmpty()) entries.add(start to txt)
        }
        entries.sortBy { it.first }
        for (i in entries.indices) {
            val start = entries[i].first
            val end = if (i + 1 < entries.size) entries[i + 1].first else start + 3000
            cues.add(SubtitleCue(id++, start, end.coerceAtLeast(start + 800), entries[i].second))
        }
        return cues
    }

    fun parseVtt(cues: List<SubtitleCue>): String {
        // not needed
        return ""
    }

    // Export helpers
    fun toSrt(cues: List<SubtitleCue>): String = buildString {
        cues.forEachIndexed { idx, c ->
            append("${idx + 1}\n")
            append("${formatSrtTime(c.startMs)} --> ${formatSrtTime(c.endMs)}\n")
            append(c.text)
            append("\n\n")
        }
    }

    fun toVtt(cues: List<SubtitleCue>): String = buildString {
        append("WEBVTT\n\n")
        cues.forEach { c ->
            append("${formatVttTime(c.startMs)} --> ${formatVttTime(c.endMs)}\n")
            append(c.text)
            append("\n\n")
        }
    }

    fun toTxt(cues: List<SubtitleCue>): String = cues.joinToString("\n") { it.text }

    private fun formatSrtTime(ms: Long): String {
        val h = ms / 3600000
        val m = (ms % 3600000) / 60000
        val s = (ms % 60000) / 1000
        val ms2 = ms % 1000
        return String.format("%02d:%02d:%02d,%03d", h, m, s, ms2)
    }
    private fun formatVttTime(ms: Long): String {
        val h = ms / 3600000
        val m = (ms % 3600000) / 60000
        val s = (ms % 60000) / 1000
        val ms2 = ms % 1000
        return String.format("%02d:%02d:%02d.%03d", h, m, s, ms2)
    }
}
