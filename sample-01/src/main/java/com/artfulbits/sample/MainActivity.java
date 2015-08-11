package com.artfulbits.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.artfulbits.ui.binding.toolbox.BindingActivity;

public class MainActivity extends BindingActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new LoginFragment())
          .commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean result = false;

    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_settings) {

      // TODO: place reaction on settings click

      result = true;
    } else {
      result = super.onOptionsItemSelected(item);
    }

    return result;
  }

}
