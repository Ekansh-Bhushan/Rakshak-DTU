package eu.ekansh.rakshakdtu

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.ekansh.rakshakdtu.data.TokenManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    tokenManager: TokenManager,
    onFinished: (destination: String) -> Unit
) {
    // ── Animatables ───────────────────────────────────────────────────────────
    val logoScale     = remember { Animatable(0.5f) }
    val logoAlpha     = remember { Animatable(0f) }
    val glowAlpha     = remember { Animatable(0f) }
    val pulseScale    = remember { Animatable(1f) }
    val pulseAlpha    = remember { Animatable(0f) }
    val textAlpha     = remember { Animatable(0f) }
    val taglineOffset = remember { Animatable(24f) }
    val bottomAlpha   = remember { Animatable(0f) }

    // Continuous glow pulse after intro
    val infiniteGlow = rememberInfiniteTransition(label = "glow")
    val glowPulse by infiniteGlow.animateFloat(
        initialValue = 0.4f, targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glowPulse"
    )

    LaunchedEffect(Unit) {
        // 1. Logo fades + pops in
        logoAlpha.animateTo(1f, tween(350))
        logoScale.animateTo(
            1f,
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow)
        )

        // 2. Glow appears
        glowAlpha.animateTo(1f, tween(300))

        // 3. Pulse ring fires outward
        pulseAlpha.animateTo(0.7f, tween(80))
        pulseScale.animateTo(2.4f, tween(650, easing = FastOutSlowInEasing))
        pulseAlpha.animateTo(0f, tween(350))

        // 4. Text fades in + tagline slides up
        textAlpha.animateTo(1f, tween(400))
        taglineOffset.animateTo(0f, tween(400, easing = FastOutSlowInEasing))

        // 5. Bottom text
        bottomAlpha.animateTo(1f, tween(400))

        delay(800)

        val destination = if (tokenManager.getToken() != null)
            Screen.DashboardScreen.route
        else
            Screen.LoginScreen.route

        onFinished(destination)
    }

    // ── Background ────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0A1628),
                        Color(0xFF060D1A),
                        Color(0xFF020509)
                    ),
                    radius = 1400f
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Ambient background glow (matches logo's blue)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x221A4A8A), Color.Transparent),
                    center = Offset(size.width / 2f, size.height * 0.42f),
                    radius = size.minDimension * 0.72f
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // ── Logo area ─────────────────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                // Glow halo behind logo
                Canvas(
                    modifier = Modifier
                        .size(200.dp)
                        .alpha(glowAlpha.value * glowPulse)
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0x661A6AC8), Color.Transparent),
                            center = center,
                            radius = size.minDimension / 2
                        )
                    )
                }

                // Pulse ring
                Canvas(
                    modifier = Modifier
                        .size(170.dp)
                        .scale(pulseScale.value)
                        .alpha(pulseAlpha.value)
                ) {
                    drawCircle(
                        color = Color(0xFF4A90D9),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                }

                // The actual logo image
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.rakshak_logo),
                    contentDescription = "Rakshak DTU Logo",
                    modifier = Modifier
                        .size(170.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── App name ──────────────────────────────────────────────────────
            Text(
                text = "RAKSHAK",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 8.sp,
                color = Color.White,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(Modifier.height(6.dp))

            // ── Accent line ───────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .width(180.dp)
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFF4A90D9),
                                Color(0xFFE8A020),
                                Color(0xFF4A90D9),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(Modifier.height(10.dp))

            // ── Tagline ───────────────────────────────────────────────────────
            Text(
                text = "Secure Campus Access",
                fontSize = 15.sp,
                color = Color(0xFF7EB3E8),
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = taglineOffset.value.dp)
            )
        }

        // ── Bottom: university name + version ─────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 52.dp)
                .alpha(bottomAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Separator dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color(0xFF2A4A6A), RoundedCornerShape(2.dp))
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Delhi Technological University",
                fontSize = 12.sp,
                color = Color(0xFF3A6A9A),
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            // "By Ekansh"
            Text(
                text = "By SKILLOP Society",
                fontSize = 11.sp,
                color = Color(0xFF4A90D9).copy(alpha = 0.7f), // Matches the blue theme
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}