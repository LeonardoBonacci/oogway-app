package guru.bonacci.oogway.app;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnAsk;
    TextView tvAnswer;
    RequestQueue requestQueue;
    TextToSpeech t1;

    String url = "http://rest-service.guides.spring.io/greeting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.btnAsk = (Button) findViewById(R.id.btn_ask);
        this.tvAnswer = (TextView) findViewById(R.id.tv_answer);

        requestQueue = Volley.newRequestQueue(this);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void clearAnswer() {
        this.tvAnswer.setText("");
    }

    private void setAnswer(String id, String content) {
        this.tvAnswer.setText("\n\n\n\n\n" + id + " / " + content);

        String toSpeak = "And those who were seen dancing were thought to be insane by those who could not hear the music.";
        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void setAnswer(String str) {
        this.tvAnswer.setText(str);
    }

    private void getAnswer() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            String content = response.getString("content");
                            setAnswer(id, content);
                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setAnswer("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                });

        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(request);
    }

    public void getAnswer(View v) {
        clearAnswer();
        getAnswer();
    }
}
