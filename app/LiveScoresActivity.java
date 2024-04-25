import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;

import java.io.IOException;

public class LiveScoresActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_scores);

        textView = findViewById(R.id.textView);
        fetchTimezone();
    }

    public void fetchTimezone() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api-football-v1.p.rapidapi.com/v3/timezone")
                .get()
                .addHeader("X-RapidAPI-Key", "a5369540e9mshc2faf67481a9d8bp17c142jsnc7b47d788625")
                .addHeader("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the error
                e.printStackTrace();
                runOnUiThread(() -> textView.setText("Failed to fetch data"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    runOnUiThread(() -> textView.setText(myResponse));
                } else {
                    // Handle the response error
                    runOnUiThread(() -> textView.setText("Response not successful"));
                }
            }
        });
    }
}
