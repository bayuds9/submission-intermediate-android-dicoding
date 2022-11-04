package com.flowerencee9.storyapp

import com.flowerencee9.storyapp.models.response.Story

object DataDummy {

    fun generateDummyQuoteResponse() : List<Story> {
        val items: MutableList<Story> = arrayListOf()

        for (i in 0..100){
            val story = Story(
                description = "desc $i",
                id = "id $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "name $i",
                photoUrl = "url/$i.jpg"
            )
            items.add(story)
        }

        return items
    }
}