
package hu.bratyek.darts;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextToSpeech tts;

    public class Voice {
        @JavascriptInterface
        public void say(String text) {
            runOnUiThread(() -> {
                if (tts != null) {
                    tts.speak(text,
                        TextToSpeech.QUEUE_FLUSH,
                        null, "darts");
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("hu", "HU"));
            }
        });

        WebView web = new WebView(this);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        web.addJavascriptInterface(new Voice(), "Android");
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        setContentView(web);

        web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
