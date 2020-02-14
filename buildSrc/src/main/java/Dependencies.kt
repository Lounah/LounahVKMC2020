object ApplicationId {
    const val id = "com.lounah.vkmc"
}

object Modules {
    const val app = ":app"
    const val coreExtensions = ":core:extensions"
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
    const val ktx = "1.0.0-alpha1"
    const val kotlin = "1.3.50"

    const val rxJava = "2.1.1"
    const val rxKotlin = "2.4.0"
    const val rxRelay = "2.1.1"
    const val rxRedux = "1.0.1"
    const val okhttp = "4.2.1"
    const val constraintLayout = "2.0.0-alpha4"

    const val glide = "4.10.0"
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
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideAp = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val vkSdk = "com.vk:androidsdk:${Versions.vkSdk}"
}

object SupportLibraries {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val design = "com.google.android.material:material:${Versions.design}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
}
