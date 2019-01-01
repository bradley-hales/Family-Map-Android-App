package Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Fragments.LoginFragment;
import Fragments.MapFragment;
import com.example.bradl.familymapserver.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {
    private String INTENT_KEY_LOGOUT = "logout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        Intent intent = getIntent();
        boolean logout = intent.getBooleanExtra(INTENT_KEY_LOGOUT, true);

        if (logout){
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        else{
            fragment = new MapFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

//        if (fragment == null){
//            fragment = new LoginFragment();
//            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
////            fragment = new MapFragment();
////            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
//        }
    }

    public void replaceFrag() {
        MapFragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
