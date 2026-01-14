package com.example.quotevault.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.quotevault.data.repository.QuoteRepository
import java.util.*
import java.util.concurrent.TimeUnit

class DailyQuoteWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val repository = QuoteRepository()
            // Background workers don't have access to the same auth session automatically sometimes
            // But public quotes should be accessible without login
            val quotes = repository.getQuotes()
            
            if (quotes.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                val seed = calendar.get(Calendar.YEAR) * 10000 + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)
                val random = Random(seed.toLong())
                val dailyQuote = quotes[random.nextInt(quotes.size)]
                
                showNotification(dailyQuote.text, dailyQuote.author)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            // If it's a network error, retry later
            Result.retry()
        }
    }

    private fun showNotification(quote: String, author: String) {
        val channelId = "daily_quote_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Daily Quotes", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Daily inspirational quotes"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Quote of the Day")
            .setContentText("\"$quote\" — $author")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            
            val now = Calendar.getInstance()
            if (calendar.before(now)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            
            val delay = calendar.timeInMillis - now.timeInMillis
            
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyQuoteWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .addTag("daily_quote")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "daily_quote_work",
                ExistingPeriodicWorkPolicy.REPLACE,
                dailyWorkRequest
            )
        }

        fun cancelNotification(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork("daily_quote_work")
        }
    }
}
