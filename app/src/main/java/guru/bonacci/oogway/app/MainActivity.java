package guru.bonacci.oogway.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    Button btnGetRepos;  // This is a reference to the "Get Repos" button.
    TextView tvRepoList;  // This will reference our repo list text box.
    RequestQueue requestQueue;  // This is our requests queue to process our HTTP requests.

    String url = "http://rest-service.guides.spring.io/greeting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // This is some magic for Android to load a previously saved state for when you are switching between actvities.
        setContentView(R.layout.activity_main);  // This links our code to our layout which we defined earlier.

        this.btnGetRepos = (Button) findViewById(R.id.btn_get_repos);  // Link our clicky button.
        this.tvRepoList = (TextView) findViewById(R.id.tv_repo_list);  // Link our repository list text output box.
        this.tvRepoList.setMovementMethod(new ScrollingMovementMethod());  // This makes our text box scrollable, for those big GitHub contributors with lots of repos :)

        requestQueue = Volley.newRequestQueue(this);  // This setups up a new request queue which we will need to make HTTP requests.
    }

    private void clearRepoList() {
        // This will clear the repo list (set it as a blank string).
        this.tvRepoList.setText("");
    }

    private void addToRepoList(String repoName, String lastUpdated) {
        // This will add a new repo to our list.
        // It combines the repoName and lastUpdated strings together.
        // And then adds them followed by a new line (\n\n make two new lines).
        String strRow = repoName + " / " + lastUpdated;
        String currentText = tvRepoList.getText().toString();
        this.tvRepoList.setText(currentText + "\n\n" + strRow);
    }

    private void setRepoListText(String str) {
        // This is used for setting the text of our repo list box to a specific string.
        // We will use this to write a "No repos found" message if the user doens't have any.
        this.tvRepoList.setText(str);
    }

    private void getRepoList() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            String content = response.getString("content");
                            addToRepoList(id, content);
                        } catch (JSONException e) {
                            // If there is an error then output this to the logs.
                            Log.e("Volley", "Invalid JSON Object.");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(request);
    }

    public void getReposClicked(View v) {
        // Clear the repo list (so we have a fresh screen to add to)
        clearRepoList();
        // Call our getRepoList() function that is defined above and pass in the
        // text which has been entered into the etGitHubUser text input field.
        getRepoList();
    }
}
