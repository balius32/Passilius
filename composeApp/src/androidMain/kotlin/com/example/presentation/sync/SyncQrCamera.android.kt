package com.example.presentation.sync

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.core.designsystem.OnSurfacePrimary
import com.example.core.designsystem.SurfaceContainerLowest
import com.example.core.designsystem.VaultTypography
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import java.util.concurrent.atomic.AtomicBoolean

@Composable
actual fun SyncQrCamera(
    onResult: (String) -> Unit,
    modifier: Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted = it }

    LaunchedEffect(granted) {
        if (!granted) requestPermission.launch(Manifest.permission.CAMERA)
    }

    if (!granted) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceContainerLowest)
                .clickable { requestPermission.launch(Manifest.permission.CAMERA) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Allow camera access to scan the sync QR",
                style = VaultTypography.bodySmall,
                color = OnSurfacePrimary
            )
        }
        return
    }

    val onResultState = rememberUpdatedState(onResult)
    val delivered = remember { AtomicBoolean(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val scanner = remember { ScannerRef() }

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(20.dp)),
        factory = { viewContext ->
            DecoratedBarcodeView(viewContext).apply {
                barcodeView.decoderFactory = DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
                setStatusText("Scan the QR on your computer")
                decodeContinuous(object : BarcodeCallback {
                    override fun barcodeResult(result: BarcodeResult) {
                        val text = result.text ?: return
                        if (delivered.compareAndSet(false, true)) {
                            pause()
                            onResultState.value(text)
                        }
                    }

                    override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>) = Unit
                })
                scanner.view = this
            }
        }
    )

    DisposableEffect(lifecycleOwner) {
        val view = scanner.view
        if (view == null) {
            onDispose { }
        } else {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> view.post { view.resume() }
                    Lifecycle.Event.ON_PAUSE -> view.pause()
                    else -> Unit
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                view.post { view.resume() }
            }
            onDispose {
                view.pause()
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

private class ScannerRef {
    var view: DecoratedBarcodeView? = null
}
