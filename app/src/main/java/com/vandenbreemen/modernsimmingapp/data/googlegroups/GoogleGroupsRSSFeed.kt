package com.vandenbreemen.modernsimmingapp.data.googlegroups

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class GoogleGroupsRSSFeed {

    @set:Element(name = "title")
    @get:Element(name = "title")
    @get:Path("channel")
    var channelTitle: String? = null

    @set:ElementList(name = "item", inline = true)
    @get:ElementList(name = "item", inline = true)
    @get:Path("channel")
    var articleList: List<GoogleGroupsPost>? = null


}