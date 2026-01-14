package com.example.quotevault.data

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.auth.auth

object SupabaseConfig {
    const val SUPABASE_URL = "https://xvqddoullcljyrxqncdj.supabase.co"
    const val SUPABASE_ANON_KEY = "sb_publishable_u1X68rjhjhuJrYG004yqjw_jYuwPrwg"
}

val supabase = createSupabaseClient(
    supabaseUrl = SupabaseConfig.SUPABASE_URL,
    supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY
) {
    install(Auth) {
        // The library handles persistence automatically on Android 
        // if the session manager is initialized correctly.
        // We ensure the Auth module is installed.
    }
    install(Postgrest)
    install(Storage)
    install(Realtime)
}
