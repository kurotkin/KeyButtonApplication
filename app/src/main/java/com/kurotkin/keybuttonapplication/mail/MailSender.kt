package com.kurotkin.keybuttonapplication.mail

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MailSender(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    val message = "Пользователь мобильного приложения попал в черезвычайные обстоятельства!"
    val subject = "SOS!"

    companion object{
        fun sendEmail(){
            val myWorkRequest = OneTimeWorkRequest.Builder(MailSender::class.java).build()
            WorkManager.getInstance().enqueue(myWorkRequest)
        }
    }

    override fun doWork(): Result {
        displayNotification(subject, message)
        try {
            val session = Session.getDefaultInstance(props(),
                object : Authenticator() {
                    override fun getPasswordAuthentication() = PasswordAuthentication(
                        Config.EMAIL,
                        Config.PASSWORD
                    )
                })
            val mm = MimeMessage(session)

            mm.setFrom(InternetAddress(Config.EMAIL))
            mm.addRecipient(Message.RecipientType.TO, InternetAddress(Config.TARGET_EMAIL))
            mm.subject = subject
            mm.setText(message)

            //Transport.send(mm)
            Log.e("TAG", "send...${mm.toString()}")
        } catch (e: MessagingException) {
            e.printStackTrace()
            Result.failure()
        }
        return Result.success()
    }

    private fun props() : Properties {
        val props = Properties()
        props["mail.smtp.host"] = Config.SMTP_HOST
        props["mail.smtp.socketFactory.port"] = Config.SMTP_PORT
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = Config.SMTP_PORT
        return props
    }

    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "simplifiedcoding",
                "simplifiedcoding",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(applicationContext, "simplifiedcoding")
            .setContentTitle(title)
            .setContentText(task)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
        notificationManager.notify(1, notification.build())
    }
}