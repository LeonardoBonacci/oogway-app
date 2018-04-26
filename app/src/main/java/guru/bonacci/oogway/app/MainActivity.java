package guru.bonacci.oogway.app;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //IPv4 Address.
    private static final String url = "http://192.168.6.101:13579/hi/consult?q=hi&apikey=dear";
    private static final String US = "this is us";

    private RequestQueue requestQueue;

    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private TextToSpeech t1;

    private MemberCache memberCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        memberCache = new MemberCache();
        requestQueue = Volley.newRequestQueue(this);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void speak(String toBeSaid) {
        Toast.makeText(getApplicationContext(), toBeSaid,Toast.LENGTH_SHORT).show();
        t1.speak(toBeSaid, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void publish(Message message) {
        messageAdapter.add(message);
        messagesView.setSelection(messagesView.getCount() - 1);
    }

    private void getAnswer() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GemCarrier gem = new ObjectMapper().readValue(response.toString(), GemCarrier.class);
                            final MemberData data = memberCache.getByName(gem.getAuthor());
                            final Message message = new Message(gem.getSaying(), data, false);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    publish(message);
                                    speak(message.getText());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                    }
                });

        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(request);
    }

    public void sendMessage(View view) {
        String messageString = editText.getText().toString();
        if (messageString.length() > 0) {
            editText.getText().clear();

            final MemberData ourData = memberCache.getByName(US);
            final Message message = new Message(messageString, ourData, true);
            publish(message);

            getAnswer();
        }
    }
}

class MemberCache {
    private Map<String, MemberData> cache = new HashMap<>();

    public MemberData getByName(String name) {
        cache.putIfAbsent(name, new MemberData(name, getRandomColor()));
        return cache.get(name);
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

}

class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}

