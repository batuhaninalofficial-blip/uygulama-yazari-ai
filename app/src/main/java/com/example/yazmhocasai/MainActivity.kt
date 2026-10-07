package com.example.yazmhocasai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {

    private val workerUrl =
        "https://aged-smoke-5647.batuhaninalofficial.workers.dev"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PremiumTheme {
                UygulamaYazari()
            }
        }
    }

    /*
     * =========================================================
     * ANA ROBOT
     * =========================================================
     */

    private fun anaRobotIste(
        istek: String,
        callback: (String) -> Unit
    ) {
        Thread {
            try {
                val url = URL(workerUrl)

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"

                connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
                )

                connection.doOutput = true
                connection.connectTimeout = 30000
                connection.readTimeout = 60000

                val json = JSONObject()

                json.put(
                    "istek",
                    istek
                )

                connection.outputStream.use { output ->
                    output.write(
                        json.toString()
                            .toByteArray(Charsets.UTF_8)
                    )
                }

                val responseCode =
                    connection.responseCode

                val stream =
                    if (responseCode in 200..299) {
                        connection.inputStream
                    } else {
                        connection.errorStream
                    }

                val response =
                    stream
                        ?.bufferedReader()
                        ?.use { it.readText() }
                        ?: ""

                if (responseCode !in 200..299) {

                    runOnUiThread {
                        callback(
                            "❌ Gemini hatası:\n$response"
                        )
                    }

                    return@Thread
                }

                val result =
                    JSONObject(response)

                val sonuc =
                    if (result.has("ana_robot")) {

                        val anaRobot =
                            result.getJSONObject(
                                "ana_robot"
                            )

                        buildString {

                            append("📋 PROJE\n")

                            append(
                                anaRobot.optString(
                                    "proje_adi",
                                    "Belirlenmedi"
                                )
                            )

                            append("\n\n🎯 AMAÇ\n")

                            append(
                                anaRobot.optString(
                                    "amac",
                                    "Belirlenmedi"
                                )
                            )

                            append("\n\n📱 PLATFORM\n")

                            append(
                                anaRobot.optString(
                                    "platform",
                                    "Belirlenmedi"
                                )
                            )

                            append(
                                "\n\n❓ EKSİK BİLGİLER\n"
                            )

                            val eksikler =
                                anaRobot.optJSONArray(
                                    "eksik_bilgiler"
                                )

                            if (
                                eksikler != null &&
                                eksikler.length() > 0
                            ) {

                                for (
                                i in 0 until eksikler.length()
                                ) {

                                    append("• ")

                                    append(
                                        eksikler.getString(i)
                                    )

                                    append("\n")
                                }

                            } else {

                                append(
                                    "Eksik bilgi yok.\n"
                                )
                            }

                            append(
                                "\n🤖 AI EKİBİ GÖREVLERİ\n"
                            )

                            val ekipler =
                                anaRobot.optJSONArray(
                                    "ekipler"
                                )

                            if (ekipler != null) {

                                for (
                                i in 0 until ekipler.length()
                                ) {

                                    val ekip =
                                        ekipler.getJSONObject(i)

                                    append("\n")

                                    append(
                                        ekip.optString(
                                            "ekip",
                                            "Ekip"
                                        ).uppercase()
                                    )

                                    append("\n")

                                    append(
                                        ekip.optString(
                                            "gorev",
                                            "Görev belirtilmedi."
                                        )
                                    )

                                    append("\n")
                                }
                            }
                        }

                    } else {

                        "❌ Hata: ${
                            result.optString(
                                "hata",
                                "Bilinmeyen hata"
                            )
                        }"
                    }

                runOnUiThread {
                    callback(sonuc)
                }

                connection.disconnect()

            } catch (e: Exception) {

                runOnUiThread {
                    callback(
                        "❌ Bağlantı hatası:\n${e.message}"
                    )
                }
            }
        }.start()
    }

    /*
     * =========================================================
     * KODLAMA AI
     * =========================================================
     */

    private fun kodlamaAIIste(
        istek: String,
        callback: (String) -> Unit
    ) {
        Thread {

            try {

                val url = URL(workerUrl)

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"

                connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
                )

                connection.doOutput = true
                connection.connectTimeout = 30000
                connection.readTimeout = 120000

                val json = JSONObject()

                json.put(
                    "islem",
                    "kodla"
                )

                json.put(
                    "istek",
                    istek
                )

                connection.outputStream.use { output ->

                    output.write(
                        json.toString()
                            .toByteArray(Charsets.UTF_8)
                    )
                }

                val responseCode =
                    connection.responseCode

                val stream =
                    if (responseCode in 200..299) {
                        connection.inputStream
                    } else {
                        connection.errorStream
                    }

                val response =
                    stream
                        ?.bufferedReader()
                        ?.use { it.readText() }
                        ?: ""

                runOnUiThread {

                    if (responseCode in 200..299) {

                        callback(response)

                    } else {

                        callback(
                            "❌ Kodlama AI hatası:\n$response"
                        )
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {

                runOnUiThread {

                    callback(
                        "❌ Kodlama AI bağlantı hatası:\n${e.message}"
                    )
                }
            }

        }.start()
    }

    /*
     * =========================================================
     * ANA UYGULAMA
     * =========================================================
     */

    @Composable
    fun UygulamaYazari() {

        var istek by remember {
            mutableStateOf("")
        }

        var sonuc by remember {
            mutableStateOf("")
        }

        var kodlamaSonucu by remember {
            mutableStateOf("")
        }

        var analizTamamlandi by remember {
            mutableStateOf(false)
        }

        var analizYukleniyor by remember {
            mutableStateOf(false)
        }

        var uygulamaOlusturuluyor by remember {
            mutableStateOf(false)
        }

        var apkHazir by remember {
            mutableStateOf(false)
        }

        var kodlamaTamamlandi by remember {
            mutableStateOf(false)
        }

        var beklemeBitti by remember {
            mutableStateOf(false)
        }

        var gelenSonuc by remember {
            mutableStateOf<String?>(null)
        }

        /*
         * =====================================================
         * ANA ROBOT BEKLEME
         * =====================================================
         */

        LaunchedEffect(analizYukleniyor) {

            if (analizYukleniyor) {

                beklemeBitti = false

                delay(20_000)

                beklemeBitti = true

                if (gelenSonuc != null) {

                    sonuc =
                        gelenSonuc ?: ""

                    analizYukleniyor = false

                    analizTamamlandi = true
                }
            }
        }

        /*
         * =====================================================
         * ANA EKRAN
         * =====================================================
         */

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF050712),
                                Color(0xFF090D1D),
                                Color(0xFF03040A)
                            )
                        )
                    )
        ) {

            PremiumBackground()

            when {

                /*
                 * =================================================
                 * KODLAMA SONUCU
                 * =================================================
                 */

                kodlamaTamamlandi -> {

                    KodlamaResultScreen(
                        sonuc = kodlamaSonucu
                    )
                }

                /*
                 * =================================================
                 * UYGULAMA OLUŞTURULUYOR
                 * =================================================
                 */

                uygulamaOlusturuluyor -> {

                    AIWorkingScreen(
                        apkMode = true
                    )
                }

                /*
                 * =================================================
                 * ESKİ APK DURUMU
                 * =================================================
                 */

                apkHazir -> {

                    APKResultScreen()
                }

                /*
                 * =================================================
                 * PROJE ANALİZİ TAMAMLANDI
                 * =================================================
                 */

                analizTamamlandi -> {

                    ProjectAnalysisScreen(
                        sonuc = sonuc,

                        onCreateApp = {

                            apkHazir = false

                            kodlamaTamamlandi = false

                            uygulamaOlusturuluyor = true

                            kodlamaAIIste(
                                istek = istek
                            ) { cevap ->

                                kodlamaSonucu =
                                    cevap

                                uygulamaOlusturuluyor =
                                    false

                                kodlamaTamamlandi =
                                    true
                            }
                        }
                    )
                }

                /*
                 * =================================================
                 * ANALİZ YAPILIYOR
                 * =================================================
                 */

                analizYukleniyor -> {

                    AIWorkingScreen(
                        apkMode = false
                    )
                }

                /*
                 * =================================================
                 * ANA EKRAN
                 * =================================================
                 */

                else -> {

                    MainInputScreen(

                        istek = istek,

                        onIstekChange = {
                            istek = it
                        },

                        onAnalyze = {

                            gelenSonuc = null

                            beklemeBitti = false

                            sonuc = ""

                            analizYukleniyor = true

                            anaRobotIste(
                                istek = istek
                            ) { cevap ->

                                gelenSonuc =
                                    cevap

                                if (beklemeBitti) {

                                    sonuc =
                                        cevap

                                    analizYukleniyor =
                                        false

                                    analizTamamlandi =
                                        true
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    /*
     * =========================================================
     * ANA GİRİŞ EKRANI
     * =========================================================
     */

    @Composable
    fun MainInputScreen(
        istek: String,
        onIstekChange: (String) -> Unit,
        onAnalyze: () -> Unit
    ) {

        val scrollState =
            rememberScrollState()

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        scrollState
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "UYGULAMA YAZARI",

                        color =
                            Color(0xFF7E8CFF),

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            2.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "AI Studio",

                        color =
                            Color.White,

                        fontSize =
                            28.sp,

                        fontWeight =
                            FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Color(0xFF151A35)
                            )
                            .border(
                                1.dp,
                                Color(0xFF303A78),
                                CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "✦",

                        color =
                            Color(0xFF9CA8FF),

                        fontSize =
                            22.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Text(
                text =
                    "Aklındaki uygulamayı anlat.\nGerisini Ana Robot halletsin.",

                color =
                    Color(0xFFE9ECFF),

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    "Fikrini yaz, AI ekibimiz arka planda çalışmaya başlasın.",

                color =
                    Color(0xFF8E95AD),

                fontSize =
                    14.sp,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            PremiumRobot(
                aktif = false
            )

            Text(
                text =
                    "●  Ana Robot hazır",

                color =
                    Color(0xFF68718D),

                fontSize =
                    13.sp
            )

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .shadow(
                            18.dp,
                            RoundedCornerShape(24.dp)
                        ),

                shape =
                    RoundedCornerShape(24.dp),

                color =
                    Color(0xFF0D1122),

                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            1.dp,
                            Color(0xFF252D52)
                        )
            ) {

                Column(
                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Text(
                        text =
                            "Uygulamanı anlat",

                        color =
                            Color(0xFFE9ECFF),

                        fontSize =
                            14.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    OutlinedTextField(

                        value =
                            istek,

                        onValueChange =
                            onIstekChange,

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp),

                        placeholder = {

                            Text(
                                text =
                                    "Örneğin:\n\nButik restoranım için şık bir uygulama istiyorum. Müşteriler menüyü görebilsin ve rezervasyon yapabilsin.",

                                color =
                                    Color(0xFF626A83),

                                fontSize =
                                    14.sp
                            )
                        },

                        colors =
                            OutlinedTextFieldDefaults
                                .colors(

                                    focusedBorderColor =
                                        Color(0xFF6875FF),

                                    unfocusedBorderColor =
                                        Color(0xFF272E4A),

                                    focusedTextColor =
                                        Color.White,

                                    unfocusedTextColor =
                                        Color.White,

                                    cursorColor =
                                        Color(0xFF8B96FF),

                                    focusedContainerColor =
                                        Color(0xFF080B17),

                                    unfocusedContainerColor =
                                        Color(0xFF080B17)
                                ),

                        shape =
                            RoundedCornerShape(18.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    Button(

                        onClick =
                            onAnalyze,

                        enabled =
                            istek.isNotBlank(),

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .shadow(
                                    12.dp,
                                    RoundedCornerShape(18.dp)
                                ),

                        shape =
                            RoundedCornerShape(18.dp),

                        colors =
                            ButtonDefaults
                                .buttonColors(

                                    containerColor =
                                        Color(0xFF5865F2),

                                    disabledContainerColor =
                                        Color(0xFF242943)
                                )
                    ) {

                        Text(
                            text =
                                "🚀  PROJEYİ ANALİZ ET",

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            letterSpacing =
                                0.5.sp
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            AIStatusBar(
                aktif = false
            )

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            Text(
                text =
                    "AI EKİBİ",

                color =
                    Color(0xFF6875FF),

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.Bold,

                letterSpacing =
                    3.sp
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    "Sen fikrini anlat.\nEkip geri kalanını halletsin.",

                color =
                    Color(0xFFB4BAD0),

                fontSize =
                    15.sp,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            AIIcons()

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }
    }

    /*
     * =========================================================
     * PROJE ANALİZİ
     * =========================================================
     */

    @Composable
    fun ProjectAnalysisScreen(
        sonuc: String,
        onCreateApp: () -> Unit
    ) {

        val scrollState =
            rememberScrollState()

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        scrollState
                    )
                    .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            Text(
                text =
                    "✓",

                color =
                    Color(0xFF7F8CFF),

                fontSize =
                    42.sp,

                fontWeight =
                    FontWeight.ExtraBold
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    "PROJE ANALİZİ TAMAMLANDI",

                color =
                    Color.White,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Ana Robot projenizi analiz etti.\nŞimdi gerçek uygulamayı oluşturmaya başlayabiliriz.",

                color =
                    Color(0xFF8E95AD),

                fontSize =
                    14.sp,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            ResultCard(
                sonuc =
                    sonuc
            )

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            Surface(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                color =
                    Color(0xFF0D1223),

                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            1.dp,
                            Color(0xFF303B68)
                        )
            ) {

                Column(
                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(
                        text =
                            "🚀 SONRAKİ AŞAMA",

                        color =
                            Color(0xFF8D9AFF),

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.5.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text =
                            "AI ekibimiz şimdi bu projeyi gerçek bir Android uygulamasına dönüştürecek.",

                        color =
                            Color.White,

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Tasarım, kodlama, geliştirme, test ve kontrol işlemleri arka planda yürütülecek.",

                        color =
                            Color(0xFF8B92AA),

                        fontSize =
                            13.sp,

                        lineHeight =
                            20.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            Button(

                onClick =
                    onCreateApp,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .shadow(
                            15.dp,
                            RoundedCornerShape(20.dp)
                        ),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                Color(0xFF5865F2)
                        )
            ) {

                Text(
                    text =
                        "🚀  UYGULAMAYI OLUŞTUR",

                    color =
                        Color.White,

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    letterSpacing =
                        0.5.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )
        }
    }

    /*
     * =========================================================
     * AI ÇALIŞMA EKRANI
     * =========================================================
     */

    @Composable
    fun AIWorkingScreen(
        apkMode: Boolean
    ) {

        val infiniteTransition =
            rememberInfiniteTransition(
                label =
                    "workingScreen"
            )

        val ringRotation by
        infiniteTransition.animateFloat(

            initialValue =
                0f,

            targetValue =
                360f,

            animationSpec =
                infiniteRepeatable(

                    animation =
                        tween(
                            7000,
                            easing =
                                LinearEasing
                        ),

                    repeatMode =
                        RepeatMode.Restart
                ),

            label =
                "ringRotation"
        )

        val reverseRotation by
        infiniteTransition.animateFloat(

            initialValue =
                360f,

            targetValue =
                0f,

            animationSpec =
                infiniteRepeatable(

                    animation =
                        tween(
                            11000,
                            easing =
                                LinearEasing
                        ),

                    repeatMode =
                        RepeatMode.Restart
                ),

            label =
                "reverseRotation"
        )

        val pulse by
        infiniteTransition.animateFloat(

            initialValue =
                0.92f,

            targetValue =
                1.08f,

            animationSpec =
                infiniteRepeatable(

                    animation =
                        tween(
                            1200,
                            easing =
                                FastOutSlowInEasing
                        ),

                    repeatMode =
                        RepeatMode.Reverse
                ),

            label =
                "workingPulse"
        )

        var mesajIndex by remember {

            mutableIntStateOf(
                0
            )
        }

        val mesajlar =

            if (apkMode) {

                listOf(

                    "Kodlama AI çalışıyor...",

                    "Uygulama kaynak kodları oluşturuluyor...",

                    "Android proje yapısı hazırlanıyor...",

                    "Geliştirme işlemleri sürüyor...",

                    "Kodlar kontrol ediliyor...",

                    "Son kontroller yapılıyor..."
                )

            } else {

                listOf(

                    "İsteğiniz analiz ediliyor...",

                    "Sizin için çalışıyoruz...",

                    "En uygun yapı belirleniyor...",

                    "Tasarım hazırlanıyor...",

                    "Kod mimarisi oluşturuluyor...",

                    "Geliştirme planı hazırlanıyor...",

                    "Son kontroller yapılıyor..."
                )
            }

        LaunchedEffect(Unit) {

            while (true) {

                delay(2800)

                mesajIndex =
                    (
                            mesajIndex + 1
                            ) %
                            mesajlar.size
            }
        }

        Box(

            modifier =
                Modifier.fillMaxSize(),

            contentAlignment =
                Alignment.Center
        ) {

            Canvas(
                modifier =
                    Modifier.fillMaxSize()
            ) {

                val center =
                    Offset(

                        size.width / 2f,

                        size.height / 2f
                    )

                drawCircle(

                    brush =
                        Brush.radialGradient(

                            colors =
                                listOf(

                                    Color(0xFF5865F2)
                                        .copy(
                                            alpha =
                                                0.18f
                                        ),

                                    Color.Transparent
                                )
                        ),

                    radius =
                        size.minDimension *
                                0.48f,

                    center =
                        center
                )
            }

            Canvas(

                modifier =
                    Modifier
                        .size(360.dp)
                        .rotate(
                            ringRotation
                        )
            ) {

                val center =
                    Offset(

                        size.width / 2f,

                        size.height / 2f
                    )

                drawCircle(

                    color =
                        Color(0xFF6875FF)
                            .copy(
                                alpha =
                                    0.28f
                            ),

                    radius =
                        size.minDimension *
                                0.43f,

                    center =
                        center,

                    style =
                        Stroke(
                            width =
                                2.dp.toPx()
                        )
                )

                for (i in 0 until 8) {

                    val angle =
                        Math.toRadians(
                            (
                                    i * 45
                                    ).toDouble()
                        )

                    val radius =
                        size.minDimension *
                                0.43f

                    drawCircle(

                        color =
                            Color(0xFF9CA8FF),

                        radius =
                            3.5.dp.toPx(),

                        center =
                            Offset(

                                center.x +
                                        cos(
                                            angle
                                        ).toFloat() *
                                        radius,

                                center.y +
                                        sin(
                                            angle
                                        ).toFloat() *
                                        radius
                            )
                    )
                }
            }

            Canvas(

                modifier =
                    Modifier
                        .size(310.dp)
                        .rotate(
                            reverseRotation
                        )
            ) {

                val center =
                    Offset(

                        size.width / 2f,

                        size.height / 2f
                    )

                drawCircle(

                    color =
                        Color(0xFF4D5EFF)
                            .copy(
                                alpha =
                                    0.30f
                            ),

                    radius =
                        size.minDimension *
                                0.43f,

                    center =
                        center,

                    style =
                        Stroke(
                            width =
                                1.5.dp.toPx()
                        )
                )
            }

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Box(

                    modifier =
                        Modifier
                            .size(
                                (
                                        250 *
                                                pulse
                                        ).dp
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    PremiumRobot(
                        aktif =
                            true
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Surface(

                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),

                    color =
                        Color(0xFF0D1223)
                            .copy(
                                alpha =
                                    0.96f
                            ),

                    border =
                        androidx.compose.foundation
                            .BorderStroke(
                                1.dp,
                                Color(0xFF303B68)
                            ),

                    shadowElevation =
                        12.dp
                ) {

                    Column(

                        modifier =
                            Modifier.padding(

                                horizontal =
                                    24.dp,

                                vertical =
                                    16.dp
                            ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text =
                                if (apkMode)
                                    "⚙️  AI GELİŞTİRME EKİBİ"
                                else
                                    "🤖  ANA ROBOT",

                            color =
                                Color(0xFF8D9AFF),

                            fontSize =
                                11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                2.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    7.dp
                                )
                        )

                        Text(

                            text =
                                mesajlar[
                                    mesajIndex
                                ],

                            color =
                                Color.White,

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.SemiBold,

                            textAlign =
                                TextAlign.Center
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    6.dp
                                )
                        )

                        Text(

                            text =
                                if (apkMode)
                                    "Gerçek kaynak kodları hazırlanıyor"
                                else
                                    "AI ekibimiz sizin için çalışıyor",

                            color =
                                Color(0xFF777F98),

                            fontSize =
                                11.sp
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )

                Row(

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            7.dp
                        )
                ) {

                    listOf(

                        "🤖",
                        "🎨",
                        "💻",
                        "🧠",
                        "🛠️",
                        "🧪",
                        "🔍"

                    ).forEach { emoji ->

                        Text(

                            text =
                                emoji,

                            fontSize =
                                15.sp
                        )
                    }
                }
            }
        }
    }

    /*
     * =========================================================
     * KODLAMA AI SONUCU
     * =========================================================
     */

    @Composable
    fun KodlamaResultScreen(
        sonuc: String
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            Text(

                text =
                    "✓",

                color =
                    Color(0xFF7F8CFF),

                fontSize =
                    48.sp,

                fontWeight =
                    FontWeight.ExtraBold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(

                text =
                    "KODLAMA AI TAMAMLANDI",

                color =
                    Color.White,

                fontSize =
                    22.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(

                text =
                    "Kodlama AI gerçek Android proje dosyalarını oluşturdu.",

                color =
                    Color(0xFF8E95AD),

                fontSize =
                    14.sp,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )

            Surface(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .shadow(
                            18.dp,
                            RoundedCornerShape(
                                24.dp
                            )
                        ),

                shape =
                    RoundedCornerShape(
                        24.dp
                    ),

                color =
                    Color(0xFF0D1223),

                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            1.dp,
                            Color(0xFF303B68)
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Text(

                        text =
                            "💻 KODLAMA AI ÇIKTISI",

                        color =
                            Color(0xFF8D9AFF),

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.5.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )

                    Text(

                        text =
                            sonuc,

                        color =
                            Color(0xFFD7DBEA),

                        fontSize =
                            12.sp,

                        lineHeight =
                            19.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        24.dp
                    )
            )

            Surface(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        22.dp
                    ),

                color =
                    Color(0xFF0D1223),

                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            1.dp,
                            Color(0xFF252D52)
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Text(

                        text =
                            "🚀 SIRADAKİ AŞAMA",

                        color =
                            Color(0xFF8D9AFF),

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.5.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )

                    Text(

                        text =
                            "Şimdi bu kaynak kodlarını GitHub'a gönderip GitHub Actions ile gerçek APK oluşturacağız.",

                        color =
                            Color.White,

                        fontSize =
                            14.sp,

                        lineHeight =
                            21.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )
        }
    }

    /*
     * =========================================================
     * APK SONUÇ EKRANI
     * =========================================================
     */

    @Composable
    fun APKResultScreen() {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        35.dp
                    )
            )

            Text(

                text =
                    "✓",

                color =
                    Color(0xFF7F8CFF),

                fontSize =
                    50.sp,

                fontWeight =
                    FontWeight.ExtraBold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            Text(

                text =
                    "UYGULAMANIZ HAZIR",

                color =
                    Color.White,

                fontSize =
                    24.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            Text(

                text =
                    "AI ekibimiz uygulamanızı oluşturdu.",

                color =
                    Color(0xFF8E95AD),

                fontSize =
                    14.sp,

                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )

            Surface(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .shadow(
                            18.dp,
                            RoundedCornerShape(
                                24.dp
                            )
                        ),

                shape =
                    RoundedCornerShape(
                        24.dp
                    ),

                color =
                    Color(0xFF0D1223),

                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            1.dp,
                            Color(0xFF303B68)
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Text(

                        text =
                            "📦 APK DOSYASI",

                        color =
                            Color(0xFF8D9AFF),

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.5.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )

                    Text(

                        text =
                            "uygulamaniz.apk",

                        color =
                            Color.White,

                        fontSize =
                            17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )

                    Button(

                        onClick = {
                            // Gerçek APK indirme
                            // GitHub Actions bağlantısından
                            // sonra eklenecek.
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(
                                    56.dp
                                ),

                        shape =
                            RoundedCornerShape(
                                17.dp
                            ),

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(0xFF5865F2)
                                )
                    ) {

                        Text(

                            text =
                                "⬇  APK'YI İNDİR",

                            fontSize =
                                14.sp,

                            fontWeight =
                                FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            Surface(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(
                        22.dp
                    ),

                color =
                    Color(0xFF0D1223),

                border =
                    androidx.compose.foundation
                        .BorderStroke(
                            1.dp,
                            Color(0xFF252D52)
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Text(

                        text =
                            "📱 ANDROID CİHAZA NASIL KURULUR?",

                        color =
                            Color(0xFF8D9AFF),

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.2.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                14.dp
                            )
                    )

                    Text(

                        text =
                            "1. APK dosyasını Android cihazınıza indirin.\n\n" +
                                    "2. Dosyalar veya İndirilenler klasöründen APK dosyasına dokunun.\n\n" +
                                    "3. Gerekirse bilinmeyen kaynaklardan uygulama yükleme iznini açın.\n\n" +
                                    "4. Yükle butonuna dokunun.\n\n" +
                                    "5. Kurulum tamamlandığında uygulamayı açabilirsiniz.",

                        color =
                            Color(0xFFD0D5E5),

                        fontSize =
                            14.sp,

                        lineHeight =
                            22.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )
        }
    }

    /*
     * =========================================================
     * ROBOT
     * =========================================================
     */

    @Composable
    fun PremiumRobot(
        aktif: Boolean
    ) {

        val infiniteTransition =
            rememberInfiniteTransition(
                label =
                    "robotAnimation"
            )

        val rotation by
        infiniteTransition.animateFloat(

            initialValue =
                0f,

            targetValue =
                360f,

            animationSpec =
                infiniteRepeatable(

                    animation =
                        tween(

                            if (aktif)
                                5000
                            else
                                9000,

                            easing =
                                LinearEasing
                        ),

                    repeatMode =
                        RepeatMode.Restart
                ),

            label =
                "rotation"
        )

        val pulse by
        infiniteTransition.animateFloat(

            initialValue =
                0.85f,

            targetValue =
                1.15f,

            animationSpec =
                infiniteRepeatable(

                    animation =
                        tween(

                            if (aktif)
                                900
                            else
                                1400,

                            easing =
                                FastOutSlowInEasing
                        ),

                    repeatMode =
                        RepeatMode.Reverse
                ),

            label =
                "pulse"
        )

        Box(

            modifier =
                Modifier.size(
                    270.dp
                ),

            contentAlignment =
                Alignment.Center
        ) {

            Canvas(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .rotate(
                            rotation
                        )
            ) {

                val center =
                    Offset(

                        size.width / 2f,

                        size.height / 2f
                    )

                val radius =
                    size.minDimension *
                            0.40f

                drawCircle(

                    brush =
                        Brush.radialGradient(

                            colors =
                                listOf(

                                    Color(0xFF5865F2)
                                        .copy(
                                            alpha =
                                                0.16f
                                        ),

                                    Color.Transparent
                                )
                        ),

                    radius =
                        size.minDimension *
                                0.49f,

                    center =
                        center
                )

                drawCircle(

                    color =
                        Color(0xFF6675FF)
                            .copy(
                                alpha =
                                    0.65f
                            ),

                    radius =
                        radius,

                    center =
                        center,

                    style =
                        Stroke(
                            width =
                                2.dp.toPx()
                        )
                )

                drawCircle(

                    color =
                        Color(0xFF4D5EFF)
                            .copy(
                                alpha =
                                    0.35f
                            ),

                    radius =
                        radius *
                                1.15f,

                    center =
                        center,

                    style =
                        Stroke(
                            width =
                                1.dp.toPx()
                        )
                )

                for (i in 0 until 12) {

                    val angle =
                        Math.toRadians(
                            (
                                    i * 30
                                    ).toDouble()
                        )

                    val x =
                        center.x +
                                cos(
                                    angle
                                ).toFloat() *
                                radius

                    val y =
                        center.y +
                                sin(
                                    angle
                                ).toFloat() *
                                radius

                    drawCircle(

                        color =
                            Color(0xFF8792FF),

                        radius =
                            if (aktif)
                                3.5.dp.toPx()
                            else
                                2.dp.toPx(),

                        center =
                            Offset(
                                x,
                                y
                            )
                    )
                }
            }

            Box(

                modifier =
                    Modifier
                        .size(
                            (
                                    170 *
                                            pulse
                                    ).dp
                        )
                        .clip(
                            CircleShape
                        )
                        .background(

                            Brush.radialGradient(

                                colors =
                                    listOf(

                                        Color(0xFF27326E),

                                        Color(0xFF0E1227)
                                    )
                            )
                        )
                        .border(

                            2.dp,

                            Color(0xFF6978FF),

                            CircleShape
                        )
                        .shadow(

                            30.dp,

                            CircleShape
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Canvas(

                    modifier =
                        Modifier.fillMaxSize()
                ) {

                    val center =
                        Offset(

                            size.width / 2f,

                            size.height / 2f
                        )

                    val robotWidth =
                        size.width *
                                0.56f

                    val robotHeight =
                        size.height *
                                0.62f

                    val left =
                        center.x -
                                robotWidth /
                                2

                    val top =
                        center.y -
                                robotHeight /
                                2

                    drawRoundRect(

                        brush =
                            Brush.verticalGradient(

                                colors =
                                    listOf(

                                        Color(0xFFD9E0F0),

                                        Color(0xFF69758D),

                                        Color(0xFF30394E)
                                    )
                            ),

                        topLeft =
                            Offset(
                                left,
                                top
                            ),

                        size =
                            Size(
                                robotWidth,
                                robotHeight
                            ),

                        cornerRadius =
                            androidx.compose.ui.geometry
                                .CornerRadius(
                                    28.dp.toPx()
                                )
                    )

                    drawRoundRect(

                        color =
                            Color(0xFF101526),

                        topLeft =
                            Offset(

                                left +
                                        12.dp.toPx(),

                                top +
                                        16.dp.toPx()
                            ),

                        size =
                            Size(

                                robotWidth -
                                        24.dp.toPx(),

                                robotHeight *
                                        0.46f
                            ),

                        cornerRadius =
                            androidx.compose.ui.geometry
                                .CornerRadius(
                                    18.dp.toPx()
                                )
                    )

                    val eyeY =
                        top +
                                44.dp.toPx()

                    drawCircle(

                        color =
                            Color(0xFF8C9BFF),

                        radius =
                            6.dp.toPx(),

                        center =
                            Offset(

                                center.x -
                                        24.dp.toPx(),

                                eyeY
                            )
                    )

                    drawCircle(

                        color =
                            Color(0xFF8C9BFF),

                        radius =
                            6.dp.toPx(),

                        center =
                            Offset(

                                center.x +
                                        24.dp.toPx(),

                                eyeY
                            )
                    )

                    drawRoundRect(

                        color =
                            Color(0xFF7A89FF),

                        topLeft =
                            Offset(

                                center.x -
                                        25.dp.toPx(),

                                top +
                                        75.dp.toPx()
                            ),

                        size =
                            Size(

                                50.dp.toPx(),

                                5.dp.toPx()
                            ),

                        cornerRadius =
                            androidx.compose.ui.geometry
                                .CornerRadius(
                                    3.dp.toPx()
                                )
                    )

                    drawCircle(

                        brush =
                            Brush.radialGradient(

                                colors =
                                    listOf(

                                        Color(0xFF9CA8FF),

                                        Color(0xFF4858D8),

                                        Color.Transparent
                                    )
                            ),

                        radius =
                            17.dp.toPx(),

                        center =
                            Offset(

                                center.x,

                                top +
                                        robotHeight -
                                        28.dp.toPx()
                            )
                    )
                }

                Text(

                    text =
                        "AI",

                    color =
                        Color.White,

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.ExtraBold
                )
            }
        }
    }

    /*
     * =========================================================
     * ARKA PLAN
     * =========================================================
     */

    @Composable
    fun PremiumBackground() {

        Canvas(

            modifier =
                Modifier.fillMaxSize()
        ) {

            val points =
                listOf(

                    Offset(
                        size.width *
                                0.12f,

                        size.height *
                                0.16f
                    ),

                    Offset(
                        size.width *
                                0.88f,

                        size.height *
                                0.28f
                    ),

                    Offset(
                        size.width *
                                0.24f,

                        size.height *
                                0.72f
                    ),

                    Offset(
                        size.width *
                                0.78f,

                        size.height *
                                0.82f
                    )
                )

            points.forEach { point ->

                drawCircle(

                    color =
                        Color(0xFF4D5EFF)
                            .copy(
                                alpha =
                                    0.08f
                            ),

                    radius =
                        90.dp.toPx(),

                    center =
                        point
                )
            }
        }
    }

    /*
     * =========================================================
     * AI STATUS
     * =========================================================
     */

    @Composable
    fun AIStatusBar(
        aktif: Boolean
    ) {

        val ekip =
            listOf(

                "🤖",
                "🎨",
                "💻",
                "🧠",
                "🛠️",
                "🧪",
                "🔍"
            )

        Surface(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(
                    20.dp
                ),

            color =
                Color(0xFF0A0E1C)
                    .copy(
                        alpha =
                            0.95f
                    ),

            border =
                androidx.compose.foundation
                    .BorderStroke(
                        1.dp,
                        Color(0xFF202744)
                    )
        ) {

            Row(

                modifier =
                    Modifier.padding(

                        horizontal =
                            16.dp,

                        vertical =
                            13.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(

                    text =
                        if (aktif)
                            "⚡"
                        else
                            "✦",

                    fontSize =
                        18.sp
                )

                Spacer(
                    modifier =
                        Modifier.width(
                            10.dp
                        )
                )

                Text(

                    text =
                        if (aktif)
                            "AI ekibi arka planda çalışıyor"
                        else
                            "7 AI sistemi hazır",

                    color =
                        Color(0xFFB8BED3),

                    fontSize =
                        12.sp,

                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                Row {

                    ekip.forEach { emoji ->

                        Text(

                            text =
                                emoji,

                            fontSize =
                                14.sp,

                            modifier =
                                Modifier.padding(
                                    horizontal =
                                        2.dp
                                )
                        )
                    }
                }
            }
        }
    }

    /*
     * =========================================================
     * SONUÇ KARTI
     * =========================================================
     */

    @Composable
    fun ResultCard(
        sonuc: String
    ) {

        Surface(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        18.dp,
                        RoundedCornerShape(
                            24.dp
                        )
                    ),

            shape =
                RoundedCornerShape(
                    24.dp
                ),

            color =
                Color(0xFF0D1223),

            border =
                androidx.compose.foundation
                    .BorderStroke(
                        1.dp,
                        Color(0xFF303B68)
                    )
        ) {

            Column(

                modifier =
                    Modifier.padding(
                        20.dp
                    )
            ) {

                Row(

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(

                        modifier =
                            Modifier
                                .size(
                                    42.dp
                                )
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    Color(0xFF202B5A)
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(

                            text =
                                "🤖",

                            fontSize =
                                21.sp
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(
                                12.dp
                            )
                    )

                    Column {

                        Text(

                            text =
                                "ANA ROBOT SONUCU",

                            color =
                                Color(0xFF8D9AFF),

                            fontSize =
                                11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                1.5.sp
                        )

                        Text(

                            text =
                                "Proje analizi tamamlandı",

                            color =
                                Color.White,

                            fontSize =
                                15.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )

                HorizontalDivider(

                    color =
                        Color(0xFF252C45)
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )

                Text(

                    text =
                        sonuc,

                    color =
                        Color(0xFFD7DBEA),

                    fontSize =
                        14.sp,

                    lineHeight =
                        22.sp
                )
            }
        }
    }

    /*
     * =========================================================
     * AI İKONLARI
     * =========================================================
     */

    @Composable
    fun AIIcons() {

        val ekip =
            listOf(

                "🤖" to "Ana Robot",

                "🎨" to "Tasarım",

                "💻" to "Kodlama",

                "🧠" to "Analiz",

                "🛠️" to "Geliştirme",

                "🧪" to "Test",

                "🔍" to "Kontrol"
            )

        Column(

            modifier =
                Modifier.fillMaxWidth()
        ) {

            ekip.chunked(
                4
            ).forEach { satir ->

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceEvenly
                ) {

                    satir.forEach {
                            (emoji, isim) ->

                        Column(

                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Box(

                                modifier =
                                    Modifier
                                        .size(
                                            52.dp
                                        )
                                        .clip(
                                            CircleShape
                                        )
                                        .background(
                                            Color(0xFF10162A)
                                        )
                                        .border(
                                            1.dp,
                                            Color(0xFF252E50),
                                            CircleShape
                                        ),

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(

                                    text =
                                        emoji,

                                    fontSize =
                                        21.sp
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        5.dp
                                    )
                            )

                            Text(

                                text =
                                    isim,

                                color =
                                    Color(0xFF777F98),

                                fontSize =
                                    9.sp,

                                textAlign =
                                    TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )
            }
        }
    }

    /*
     * =========================================================
     * TEMA
     * =========================================================
     */

    @Composable
    fun PremiumTheme(
        content: @Composable () -> Unit
    ) {

        MaterialTheme(

            colorScheme =
                darkColorScheme(

                    primary =
                        Color(0xFF6875F5),

                    secondary =
                        Color(0xFF8B96FF),

                    background =
                        Color(0xFF050712),

                    surface =
                        Color(0xFF0D1122)
                ),

            content =
                content
        )
    }
}