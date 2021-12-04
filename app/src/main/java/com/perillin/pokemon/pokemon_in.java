package com.perillin.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class pokemon_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pokemon_in);

        final AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-2889068957261961/9028479109")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(0x303030)).build();

                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAds(new AdRequest.Builder().build(), 3);

    Pokemon iga=MainActivity.pika;
        TextView txt=findViewById(R.id.textView);
        txt.setText("Name: "+ iga.getName());
        txt.setTypeface(Typeface.createFromAsset(getAssets(),"font.ttf"));

        TextView txt1=findViewById(R.id.textView1);
        txt1.setText("Atack: "+iga.getAttack());
        txt1.setTypeface(Typeface.createFromAsset(getAssets(),"font.ttf"));

        TextView txt2=findViewById(R.id.textView2);
        txt2.setText("Defence: "+iga.getDefence());
        txt2.setTypeface(Typeface.createFromAsset(getAssets(),"font.ttf"));

        TextView txt3=findViewById(R.id.textView3);
        txt3.setText("Height: "+iga.getHeight());
        txt3.setTypeface(Typeface.createFromAsset(getAssets(),"font.ttf"));

        TextView txt4=findViewById(R.id.textView4);
        txt4.setText("Weight: "+iga.getWeight());
        txt4.setTypeface(Typeface.createFromAsset(getAssets(),"font.ttf"));
        ImageView ing=findViewById(R.id.imageView);
        int width, height;
        height = iga.getImage().getHeight();
        width = iga.getImage().getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        c.drawBitmap(iga.getImage(), 0, 0, paint);
        ing.setImageBitmap(bmpGrayscale);
    }
}