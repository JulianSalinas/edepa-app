package imagisoft.rommie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ActivitySplash extends AppCompatActivity{

    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        mContentView = findViewById(R.id.fullscreen_content);

        /* Hides not important items for splash*/
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean defaultResult = super.onTouchEvent(event);
        initApplication();
        return defaultResult;
    }

    private void initApplication(){
        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(intent);
        sayWelcome();
    }

    private void sayWelcome(){
        int m = R.string.welcome;
        Context c = getApplicationContext();
        Toast toast = Toast.makeText(c, m, Toast.LENGTH_LONG);
        toast.show();
    }


}
