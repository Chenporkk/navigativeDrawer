package com.example.navigativedrawer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.String;



import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final String MYPREF = "my_prefs";
    private static final String MYKEY = "my_key";
    private int PICK_IMAGE_REQUEST = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private Object DialogInterface;
    private EditText myEditText;
    private Uri uri;
    private AlertDialog dialog;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private ListView list;
    private ArrayList<String> stringlist;
    private ArrayAdapter<String> arrayadapter;
    private String[] spinnerItems = new String[]{
            "Beer", "Coffe", "Juice"};

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        CircleImageView myButton = (CircleImageView) findViewById(R.id.imageView2);
        ImageButton mButton = (ImageButton) findViewById(R.id.imageButton);
        EditText myEditText = (EditText) findViewById(R.id.editText);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        stringlist = new ArrayList<>(Arrays.asList(spinnerItems));
        if(savedInstanceState!=null){
            if( savedInstanceState.containsKey(MYKEY)) stringlist = savedInstanceState.getStringArrayList(MYKEY);
            if(savedInstanceState.containsKey("uri")){
                try {
                    uri = Uri.parse(savedInstanceState.getString("uri"));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));

                    myButton.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.e("Main", Log.getStackTraceString(e));
                }

            }
            if(getSharedPreferences(MYPREF,MODE_PRIVATE).getBoolean("dialog_shown",false)){
                showAddCatDialog(savedInstanceState);
            }
        }
        arrayadapter = new ArrayAdapter<String>(MainActivity.this,R.layout.textviewl,stringlist );
        arrayadapter.setDropDownViewResource(R.layout.textviewl);
        spinner.setAdapter(arrayadapter);
        myButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }

        });
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCatDialog(null);
                getSharedPreferences(MYPREF,MODE_PRIVATE).edit().putBoolean("dialog_shown",true).apply();
            }
        });

    }

    private void setWindowFlag(MainActivity mainActivity, final int bits, boolean b) {
        Window win = mainActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (b) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void showAddCatDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.pop_potr, null);
        TextView mText = (TextView) mView.findViewById(R.id.textView);
        View dView = (View) mView.findViewById(R.id.divider2);
        TextView m1Text = (TextView) mView.findViewById(R.id.textView8);
        final EditText mEditText = (EditText) mView.findViewById(R.id.editText);
        TextView m2Text = (TextView) mView.findViewById(R.id.textView1);
        final EditText m1EditText = (EditText) mView.findViewById(R.id.editText1);
        Button mButtton = (Button) mView.findViewById(R.id.button3);
        final Button m1Buttton = (Button) mView.findViewById(R.id.button4);
        ConstraintLayout mConstrain = (ConstraintLayout) mView.findViewById(R.id.constraintLayout);


        mButtton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEditText.getText().toString().isEmpty() && !m1EditText.getText().toString().isEmpty()) {
                    stringlist.add(m1EditText.getText().toString());
                    arrayadapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this,
                            "Category is added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    dialog = null;
                    getSharedPreferences(MYPREF,MODE_PRIVATE).edit().putBoolean("dialog_shown",false).apply();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Please fill any empty field!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(savedInstanceState != null){
            mEditText.setText(savedInstanceState.getString("dataA",""));
            m1EditText.setText((savedInstanceState.getString("dataB", "")));
        }

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
        m1Buttton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
                getSharedPreferences(MYPREF,MODE_PRIVATE).edit().putBoolean("dialog_shown",false).apply();
            }
        });
        /*mButtton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
                getSharedPreferences(MYPREF,MODE_PRIVATE).edit().putBoolean("dialog_shown",false).apply();
            }
        });*/
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.imageView2);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(MYKEY, stringlist);
        if(uri !=null){
            outState.putString("uri",uri.toString());
        }
        if(dialog != null){
            outState.putString("dataA", ((EditText)dialog.findViewById(R.id.editText)).getText().toString());
            outState.putString("dataB", ((EditText)dialog.findViewById(R.id.editText1)).getText().toString());
            dialog.dismiss();
        }
    }

}