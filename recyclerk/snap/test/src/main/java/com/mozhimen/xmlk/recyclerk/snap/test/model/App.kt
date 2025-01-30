package com.mozhimen.xmlk.recyclerk.snap.test.model

import java.util.ArrayList
import com.mozhimen.xmlk.recyclerk.snap.test.R

data class App(val name: String, val drawable: Int, val rating: Float) {
    companion object {
        @JvmStatic
        fun getApps(): List<App> {
            val apps = ArrayList<App>()
            repeat(2) {
                apps.add(App("Google+", R.drawable.ic_google_48dp, 4.6f))
                apps.add(App("Gmail", R.drawable.ic_gmail_48dp, 4.8f))
                apps.add(App("Inbox", R.drawable.ic_inbox_48dp, 4.5f))
                apps.add(App("Google Keep", R.drawable.ic_keep_48dp, 4.2f))
                apps.add(App("Google Drive", R.drawable.ic_drive_48dp, 4.6f))
                apps.add(App("Hangouts", R.drawable.ic_hangouts_48dp, 3.9f))
                apps.add(App("Google Photos", R.drawable.ic_photos_48dp, 4.6f))
                apps.add(App("Messenger", R.drawable.ic_messenger_48dp, 4.2f))
                apps.add(App("Sheets", R.drawable.ic_sheets_48dp, 4.2f))
                apps.add(App("Slides", R.drawable.ic_slides_48dp, 4.2f))
                apps.add(App("Docs", R.drawable.ic_docs_48dp, 4.2f))
            }
            return apps
        }
    }
}

