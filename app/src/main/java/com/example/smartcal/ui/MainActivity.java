package com.example.smartcal.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartcal.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            // Add the fragment programmatically (safer than <fragment/> tag)
            if (savedInstanceState == null) {
                try {
                    Fragment frag = new MainFragment();
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.main_fragment_container, frag, "MAIN_FRAGMENT");
                    t.commitNowAllowingStateLoss();
                } catch (Throwable ft) {
                    Log.e(TAG, "Failed to attach MainFragment", ft);
                    Toast.makeText(this, "Error starting app: " + ft.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "Fatal startup error", t);
            Toast.makeText(this, "Startup error: " + t.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
        }
    }
}
