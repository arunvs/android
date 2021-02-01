package org.thecyberspace.mynote;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;
@Override
    protected void onCreate(Bundle savedInstance){
    super.onCreate(savedInstance);{
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new HomeFragment()).commit();

            //bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment tempFragment = null;
                    switch(item.getItemId()){
                        case R.id.menu_search:
                            tempFragment = new SearchFragment();
                            break;
                        case R.id.menu_home:
                            tempFragment = new HomeFragment();
                            break;
                        case R.id.menu_setting:
                            tempFragment = new SettingsFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, tempFragment).commit();
                    return true;
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel =
                        new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
        }

        }
    }
}
