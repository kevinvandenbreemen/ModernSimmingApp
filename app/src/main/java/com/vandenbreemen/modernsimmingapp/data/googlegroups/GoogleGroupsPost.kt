package com.vandenbreemen.modernsimmingapp.data.googlegroups

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class GoogleGroupsPost(

    @set:Element(name = "title")
    @get:Element(name = "title")
    var title: String? = null,

    @set:Element(name = "link")
    @get:Element(name = "link")
    var link: String? = null,

    @set:Element(name = "author")
    @get:Element(name = "author")
    var author:String? = null,

    @get:Element
    @set:Element
    var pubDate: String? = null

) {}