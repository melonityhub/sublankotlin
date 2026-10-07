
package com.androyal.subxplayer.utils

import com.androyal.subxplayer.data.models.SubtitleCue
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object SubtitleParsingService {
    fun parseSrt(input: InputStream): List<SubtitleCue> {
        val reader = BufferedReader(InputStreamReader(input, Charsets.UTF_8))
        val cues = mutableListOf<SubtitleCue>()
        var id = 0L
        val block = mutableListOf<String>()
        fun flushBlock(){
            if(block.size<2) { block.clear(); return }
            try{
                // block[0] is index, block[1] is time, rest are text
                val timeLine = block[1]
                val m = Regex("""(\d+):(\d+):(\d+),(\d+)\s*-->\s*(\d+):(\d+):(\d+),(\d+)""").find(timeLine) ?: run{ block.clear(); return}
                fun toMs(h:String,m:String,s:String,ms:String) = h.toLong()*3600000 + m.toLong()*60000 + s.toLong()*1000 + ms.toLong()
                val start = toMs(m.groupValues[1], m.groupValues[2], m.groupValues[3], m.groupValues[4])
                val end = toMs(m.groupValues[5], m.groupValues[6], m.groupValues[7], m.groupValues[8])
                val text = block.drop(2).joinToString("\n").trim()
                if(text.isNotEmpty()) cues.add(SubtitleCue(id++, start, end, text))
            }catch(_:Exception){}
            block.clear()
        }
        reader.forEachLine { line ->
            if(line.isBlank()) flushBlock() else block.add(line)
        }
        flushBlock()
        return cues
    }

    fun parseVtt(input: InputStream): List<SubtitleCue> {
        // Simplified: strip WEBVTT header and reuse SRT logic with . instead of ,
        val text = input.readBytes().toString(Charsets.UTF_8).replace("WEBVTT","").replace('.',',')
        return parseSrt(text.byteInputStream())
    }
}
