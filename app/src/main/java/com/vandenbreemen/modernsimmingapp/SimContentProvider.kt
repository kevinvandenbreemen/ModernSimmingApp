package com.vandenbreemen.modernsimmingapp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.UriMatcher.NO_MATCH
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.vandenbreemen.modernsimmingapp.data.localstorage.PostsDatabase
import com.vandenbreemen.modernsimmingapp.di.hilt.BackendEntryPoint
import dagger.hilt.android.EntryPointAccessors

class SimContentProvider : ContentProvider() {

    val backendEntryPoint: BackendEntryPoint get() = EntryPointAccessors.fromApplication(context!!, BackendEntryPoint::class.java)

    private val postDatabase: PostsDatabase by lazy {
        backendEntryPoint.getPostsDatabase()
    }

    companion object {

        val AUTHORITY = SimContentProvider::class.java.canonicalName

        val ID_LIST_GROUPS = 1
        val PATH_LIST_GROUPS = "/groups"

        val ID_LIST_SIMS_IN_GRP = 2
        val PATH_LIST_SIMS_IN_GRP = "/groups/*/list"
    }

    private val uriMatcher = UriMatcher(NO_MATCH)
    init {
        uriMatcher.addURI(AUTHORITY, PATH_LIST_GROUPS, ID_LIST_GROUPS)
        uriMatcher.addURI(AUTHORITY, PATH_LIST_SIMS_IN_GRP, ID_LIST_SIMS_IN_GRP)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return -1
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        if(uriMatcher.match(uri) == ID_LIST_SIMS_IN_GRP) {

            Log.i(AUTHORITY, "LIST SIMS IN GROUP")
            val groupName = uri.pathSegments[1]

            var postCount = 10
            uri.getQueryParameter("count")?.let { count->postCount = count.toInt() }

            postDatabase.groupDao().findGroupByName(groupName)?.let { group ->

                Log.d(javaClass.simpleName, "found group $group for request")

                val rawPosts = postDatabase.postDao().fetchRawPostsForGroup(group.id, postCount)

                Log.d(javaClass.simpleName, "Will provide posts [$rawPosts]")

                val cursor = MatrixCursor(arrayOf(
                    "postedDate",
                    "title",
                    "url",
                    "content"
                ))

                rawPosts.forEach { post->
                    postDatabase.postDao().loadContent(post.id)?.let { content->
                        cursor.newRow().apply {
                            add("postedDate", post.postedDate)
                            add("title", post.title)
                            add("url", post.url)
                            add("content", content)
                        }
                    }
                }

                return cursor

            }

        } else if(uriMatcher.match(uri) == ID_LIST_GROUPS) {
            val groups = postDatabase.groupDao().list()
            val cursor = MatrixCursor(arrayOf("name"))
            groups.forEach { group->
                cursor.newRow().add(group.name)
            }
            return cursor
        }

        return null

    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return -1
    }
}
