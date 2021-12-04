package com.perillin.pokemon;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static com.perillin.pokemon.R.layout.item;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    URL pikaEndpoint;
    boolean catct;
    URL pikaEndpoint1;
    HttpsURLConnection myConnection;
    HttpsURLConnection myConnection1;
    RecyclerView citiesListView;
    List<Pokemon> hg = null;
    public static Pokemon pika;
    RecyclerView.LayoutManager layoutManager;
    int zg;
    CitiesAdapter citiesAdapter;
    public static final String TAG="MyAPP";
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int total;
    ProgressBar prog;
    boolean cat;boolean cat1;
@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getSupportActionBar().hide();

    setContentView(R.layout.activity_main);

    MobileAds.initialize(this, new OnInitializationCompleteListener() {

        @Override
        public void onInitializationComplete(InitializationStatus initializationStatus) {

        }
    });

        cat=false;
        cat1=false;
        hg=new ArrayList<>();
        new ProgressTask().execute();
        zg=0;
        prog =findViewById(R.id.progressBar);
        prog.setVisibility(ProgressBar.VISIBLE);

        citiesListView = findViewById(R.id.recview);
        layoutManager = new LinearLayoutManager(this);
        citiesListView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        citiesListView.addItemDecoration(itemDecoration);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        citiesListView.setItemAnimator(itemAnimator);
        citiesListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                 public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                     if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                         final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                         Log.d(TAG, citiesAdapter.getItemCount()+" "+ zg+" "+lm.findLastVisibleItemPosition());
                        if(lm.findLastVisibleItemPosition()==zg-1 && citiesAdapter.getItemCount()==zg &&cat1==false) {
                            cat1=true;
                           new ProgressTask().execute();
                        }
                     }
                 }
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {

                }
    });
    }


    public void Connection () {
                try {
                    pikaEndpoint = new URL("https://pokeapi.co/api/v2/pokemon?limit=30&offset="+zg);
                    myConnection = (HttpsURLConnection) pikaEndpoint.openConnection();
                    JsonReader reader = new JsonReader(new InputStreamReader(myConnection.getInputStream(), "UTF-8"));
                    reader.beginObject();
                    String temp;
                    while (reader.hasNext()) {
                        temp= reader.nextName();
                        if (temp.equals("results"))
                        {
                          hg.addAll(readMessage2(reader));
                        } else reader.skipValue();
                    }
                    reader.endObject();
                    reader.close();

                    zg+=30;
                }
                catch (Exception e){ }
                 finally
                {

                    myConnection.disconnect();
                }

            }


    public List<Pokemon> readMessage2(JsonReader reader) throws IOException {
        List<Pokemon> messages = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext())
        {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("url")) {
                    String hf = reader.nextString();
                    try {
                        pikaEndpoint1 = new URL(hf);
                        myConnection1 = (HttpsURLConnection) pikaEndpoint1.openConnection();
                        JsonReader reader1 = new JsonReader(new InputStreamReader(myConnection1.getInputStream(), "UTF-8"));
                        messages.add(readMessage(reader1));
                        reader1.close();
                    }finally {
                        myConnection1.disconnect();
                    }
                } else reader.skipValue();
            }
            reader.endObject();
        }
        reader.endArray();
        return messages;
    }

    public Pokemon readMessage(JsonReader reader) throws IOException {
        int weight=-1;
        int height=-1;
        int id=-1;
        int hp=-1;
        int defence=-1;
        int attack=-1;
        List<String> types=null;
        String name="";
        String temp="";
        String sprite="";
        int[] stats=new int[3];
        reader.beginObject();
        while (reader.hasNext()) {
              temp= reader.nextName();
            if (temp.equals("height")) {
                height = reader.nextInt();
            } else if (temp.equals("id")) {
                id = reader.nextInt();
            } else if (temp.equals("name")) {
                name = reader.nextString();
           } else if (temp.equals("stats")) {
              stats= readStats(reader);
              hp=stats[0];
             attack=stats[1];
             defence=stats[2];
           } else if (temp.equals("types")) {
               types=readTypes(reader);
            } else if (temp.equals("sprites")) {
                sprite=readSprites(reader);
            } else if (temp.equals("weight")) {
                weight = reader.nextInt();
            } else reader.skipValue();
        }
        reader.endObject();
        return new Pokemon(weight, height, id, hp, defence, attack, types, name, sprite);
    }
    public String readSprites (JsonReader reader) throws IOException {
        String name="";
        String gh="";
        reader.beginObject();
        while (reader.hasNext())
        {
            name = reader.nextName();
            if (name.equals("front_default")) {
                gh= reader.nextString();
            } else reader.skipValue();
        }
        reader.endObject();
        return gh;
    }
    public List<String> readTypes (JsonReader reader) throws IOException {
        List<String> temp = new ArrayList<>();
        String name = "";
        reader.beginArray();
        while (reader.hasNext())
        {
            reader.beginObject();
            while (reader.hasNext()) {
                name = reader.nextName();
                if (name.equals("type") ) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                    name = reader.nextName();
                    if (name.equals("name")) {
                        temp.add(reader.nextString());
                    }
                    else reader.skipValue();
                    }
                    reader.endObject();
                } else reader.skipValue();
            }
            reader.endObject();
        }
        reader.endArray();
        return temp;
    }
    public int[] readStats (JsonReader reader) throws IOException {
        String name = "";
        int i=0;
       int[]temp=new int[6];
        reader.beginArray();
        while (reader.hasNext()) {

            reader.beginObject();
            while (reader.hasNext()) {
                name = reader.nextName();
                if (name.equals("base_stat")) {
                   temp[i]=reader.nextInt();
                    i++;
                } else reader.skipValue();
            }
            reader.endObject();
        }
        reader.endArray();
        return temp;
    }

    private class CitiesAdapter extends RecyclerView.Adapter<CitiesViewHolder>
    {
        private List<Pokemon> cities;

        CitiesAdapter(List<Pokemon> cities) {
            this.cities = cities;
        }

        private Pokemon getItem(String name) {
            for (Pokemon s:cities)
            {
                if(s.getName()==name)
                {
                    return cities.get(cities.indexOf(s));
                }
            }
            return null;
        }
        @Override
        public CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View rowView = layoutInflater.inflate(item, parent, false);
            return new CitiesViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(CitiesViewHolder holder, int position) {
            String cityName = cities.get(position).getName();
            TextView cityNameView = holder.cityNameView;
            ImageView imageView=holder.flag;
            cityNameView.setText(cityName);
            if(citiesAdapter.getItem(cityName).getImage()!=null) {
                imageView.setImageBitmap(citiesAdapter.getItem(cityName).getImage());
            }
            else {
                try {
                    String src = cities.get(position).getSprite();

                    new AsyncTaskLoadImage(holder.flag, citiesAdapter.getItem(cityName)).execute(src);
                } catch (Exception e) {

                }
            }
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

    }
       public class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
            private ImageView imageView;
            Pokemon pita;
            public AsyncTaskLoadImage(ImageView imageView, Pokemon pita) {
                this.imageView = imageView;
                this.pita=pita;
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(params[0]);
                    bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                try {
                    int width, height;
                    height = bitmap.getHeight();
                    width = bitmap.getWidth();
                    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas c = new Canvas(bmpGrayscale);
                    Paint paint = new Paint();
                    ColorMatrix cm = new ColorMatrix();
                    cm.setSaturation(0);
                    c.drawBitmap(bitmap, 0, 0, paint);
                    imageView.setImageBitmap(bmpGrayscale);
                    pita.setImage(bmpGrayscale);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }



 class CitiesViewHolder extends RecyclerView.ViewHolder  {
    TextView cityNameView;
    ImageView flag;
    CitiesViewHolder(final View itemView){
        super(itemView);
        cityNameView= itemView.findViewById(R.id.textView3);
        cityNameView.setTypeface(Typeface.createFromAsset(getAssets(),"font.ttf"));
        flag=itemView.findViewById(R.id.imageView3);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            pika=citiesAdapter.getItem(String.valueOf(cityNameView.getText()));


                Intent intent=new Intent(MainActivity.this, pokemon_in.class);
                startActivity(intent);
            }
        });
    }
 }
    class ProgressTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... unused) {
            Connection();
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            prog.setVisibility(ProgressBar.INVISIBLE);
            if(!cat) {
                citiesAdapter = new CitiesAdapter(hg);
                citiesListView.setAdapter(citiesAdapter);
                cat = true;
            }
            else
            {
                citiesAdapter.notifyDataSetChanged();
                cat1=false;
            }
    }
    }
}