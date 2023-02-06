package com.myunidays.segmenkt

import platform.Foundation.NSDictionary
import platform.Foundation.NSURL
import platform.Foundation.NSUserActivity

actual class Analytics internal constructor(val ios: cocoapods.Analytics.SEGAnalytics) {

    actual companion object {
        actual fun setupWithConfiguration(configuration: Configuration): Analytics {
            val analyticsConfig = cocoapods.Analytics.SEGAnalyticsConfiguration.configurationWithWriteKey(configuration.writeKey)
            analyticsConfig.trackApplicationLifecycleEvents = configuration.trackApplicationLifecycleEvents
            analyticsConfig.flushInterval = configuration.flushInterval.toDouble()
            analyticsConfig.flushAt = configuration.flushAt.toULong()
            analyticsConfig.trackDeepLinks = configuration.trackDeepLinks
            configuration.factories.forEach { analyticsConfig.use(it) }
            cocoapods.Analytics.SEGAnalytics.setupWithConfiguration(analyticsConfig)
            return shared(null)
        }

        actual fun shared(context: Any?): Analytics =
            Analytics(cocoapods.Analytics.SEGAnalytics.sharedAnalytics())

        fun shared(): Analytics = shared(null)
    }

    actual fun alias(userId: String, options: Map<Any?, *>?) = ios.alias(userId, options?.let { mapOf("context" to it) })

    actual fun track(name: String, properties: Map<Any?, *>?, options: Map<Any?, *>?) = ios.track(name, properties, options?.let { mapOf("context" to it) })

    actual fun identify(userId: String?, traits: Map<Any?, *>?, options: Map<Any?, *>?) = ios.identify(userId, traits, options?.let { mapOf("context" to it) })

    actual fun screen(
        screenTitle: String,
        properties: Map<Any?, *>?,
        options: Map<Any?, *>?
    ) = ios.screen(screenTitle, properties, options?.let { mapOf("context" to it) })

    actual fun group(groupId: String, traits: Map<Any?, *>?, options: Map<Any?, *>?) = ios.group(groupId, traits, options?.let { mapOf("context" to it) })

    actual fun optOut(status: Boolean) = if (status) {
        ios.disable()
    } else {
        ios.enable()
    }

    actual fun reset() {
        ios.reset()
    }
    fun continueUserActivity(activity: NSUserActivity) = ios.continueUserActivity(activity)
    fun openURL(url: NSURL, options: Map<Any?, *>) = ios.openURL(url, options)
}
