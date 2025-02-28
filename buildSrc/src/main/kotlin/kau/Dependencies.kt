package kau

/**
 * Some common dependencies, backed by the supplied versions
 */
object Dependencies {
    @JvmStatic
    fun kau(version: String) = "com.github.ivan-avalos.kau:$version"

    @JvmStatic
    fun kau(type: String, version: String) = "com.github.ivan-avalos.kau:$type:$version"

    @JvmStatic
    fun kauAbout(version: String) = kau("about", version)

    @JvmStatic
    fun kauAdapter(version: String) = kau("adapter", version)

    @JvmStatic
    fun kauColorPicker(version: String) = kau("colorpicker", version)

    @JvmStatic
    fun kauCore(version: String) = kau("core", version)

    @JvmStatic
    fun kauCoreUi(version: String) = kau("core-ui", version)

    @JvmStatic
    fun kauFastAdapter(version: String) = kau("fastadapter", version)

    @JvmStatic
    fun kauFastAdapterViewBinding(version: String) = kau("fastadapter-viewbinding", version)

    @JvmStatic
    fun kauKprefActivity(version: String) = kau("kpref-activity", version)

    @JvmStatic
    fun kauMediaPicker(version: String) = kau("mediapicker", version)

    @JvmStatic
    fun kauSearchView(version: String) = kau("searchview", version)

    @JvmStatic
    fun kotlin(type: String) = "org.jetbrains.kotlin:kotlin-$type:${Versions.kotlin}"

    @JvmField
    val kotlin = kotlin("stdlib")
    @JvmField
    val kotlinTest = kotlin("test-junit")
    @JvmField
    val kotlinReflect = kotlin("reflect")

    @JvmField
    val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"

    @JvmField
    val hiltCompiler = hilt("compiler")

    @JvmField
    val hiltTest = hilt("testing")

    @JvmStatic
    fun hilt(type: String) = "com.google.dagger:hilt-android-$type:${Versions.hilt}"

    @JvmStatic
    fun compose(type: String) = "androidx.ui:ui-$type:${Versions.compose}"

    const val junit = "junit:junit:${Versions.junit}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val googleMaterial = "com.google.android.material:material:${Versions.googleMaterial}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    const val iconics = "com.mikepenz:iconics-core:${Versions.iconics}"
    const val iconicsGoogle =
        "com.mikepenz:google-material-typeface:${Versions.iconicsGoogle}-kotlin@aar"
    const val iconicsMaterial =
        "com.mikepenz:material-design-iconic-typeface:${Versions.iconicsMaterial}-kotlin@aar"
    const val iconicsCommunity =
        "com.mikepenz:community-material-typeface:${Versions.iconicsCommunity}-kotlin@aar"

    const val aboutLibraries = "com.mikepenz:aboutlibraries:${Versions.aboutLibraries}"
    const val aboutLibrariesCore = "com.mikepenz:aboutlibraries-core:${Versions.aboutLibraries}"

    const val blurry = "jp.wasabeef:blurry:${Versions.blurry}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"

    @JvmStatic
    fun materialDialog(type: String) =
        "com.afollestad.material-dialogs:$type:${Versions.materialDialog}"

    @JvmField
    val materialDialog = materialDialog("core")

    const val fastAdapter = "com.mikepenz:fastadapter:${Versions.fastAdapter}"
    @JvmStatic
    fun fastAdapter(type: String) =
        "com.mikepenz:fastadapter-extensions-$type:${Versions.fastAdapter}"

    const val bugsnag = "com.bugsnag:bugsnag-android:${Versions.bugsnag}"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

    @JvmStatic
    fun espresso(type: String) = "androidx.test.espresso:espresso-$type:${Versions.espresso}"

    @JvmField
    val espresso = espresso("core")

    const val testRunner = "androidx.test.ext:junit:${Versions.testRunner}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
}