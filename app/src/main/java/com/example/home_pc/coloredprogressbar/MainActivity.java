package com.example.home_pc.coloredprogressbar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    PendingIntent pendingIntent;
    public static final int REQUEST_CODE = 100;
    public static final int TASK_CODE = 100;
    public final static int STATUS_FINISH = 200;
    public static final String PENDING_INTENT = "pendingIntent";
    public static final String PENDING_RESULT = "pendingResult";
    private ProgressBar progressBar;
    private Button button;
    private int result;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartLoad();
            }
        });
    }

    private void StartLoad() {
        Intent emptyIntent = new Intent();
        pendingIntent = createPendingResult(TASK_CODE, emptyIntent, 0);
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(PENDING_INTENT, pendingIntent);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == STATUS_FINISH) {
            int result = data.getIntExtra(PENDING_RESULT, 0);
            this.result = result;
            setColoredProgress(result);
        }
    }

    private void setColoredProgress(int result) {
        if (result <= 6) {
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            progressBar.setProgress(result);
        } else if (result <= 13) {
            progressBar.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            progressBar.setProgress(result);
        } else {
            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            progressBar.setProgress(result);
        }
    }

}
