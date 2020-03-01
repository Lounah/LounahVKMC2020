object ApplicationId {
    const val id = "com.lounah.vkmc"
}

object Modules {
    const val app = ":app"
    const val coreExtensions = ":core:core-extensions"
    const val coreUi = ":core:core-ui"
    const val coreDi = ":core:core-di"
    const val coreRecycler = ":core:core-recycler"
    const val coreVk = ":core:core-vk"
    const val featureSharing = ":feature:feature-sharing"
    const val featureMarket = ":feature:feature-market"
    const val featureAlbums = ":feature:feature-albums"
    const val featureEventsMap = ":feature:feature-map"
    const val featureUnsubscribe = ":feature:feature-unsubscribe"
    const val featureImageViewer = ":feature:feature-image-viewer"
    const val featureImagePicker = ":feature:feature-image-picker"
}

object Releases {
    const val versionName = "0.0.1"
}

object Versions {
    const val gradle = "3.5.3"
    const val compileSdk = 29
    const val minSdk = 21
    const val targetSdk = 29

    const val vkSdk = "2.2.1"

    const val appcompat = "1.0.2"
    const val design = "1.0.0"
    const val recyclerview = "1.0.0"
    const val lifecycle = "2.2.0"
    const val ktx = "1.0.0-alpha1"
    const val kotlin = "1.3.50"

    const val rxJava = "2.1.1"
    const val rxKotlin = "2.4.0"
    const val rxRelay = "2.1.1"
    const val rxRedux = "1.0.1"
    const val okhttp = "4.2.1"
    const val gson = "2.8.6"
    const val constraintLayout = "2.0.0-alpha4"
    const val permissions = "3.0.0"

    const val glide = "4.10.0"
    const val gMaps = "17.0.0"
}

object Libraries {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val rxJava = "io.reactivex.rxjava2:rxandroid:${Versions.rxJava}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"
    const val rxRedux = "com.freeletics.rxredux:rxredux:${Versions.rxRedux}"
    const val rxAndroid = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideAp = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val vkSdk = "com.vk:androidsdk:${Versions.vkSdk}"
    const val permissions = "pub.devrel:easypermissions:${Versions.permissions}"
    const val gmaps = "com.google.android.gms:play-services-maps:${Versions.gMaps}"
}

object SupportLibraries {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val design = "com.google.android.material:material:${Versions.design}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
}
