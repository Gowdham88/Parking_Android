package com.polsec.pyrky.activity.forgotpassword;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.HomeActivity;
import com.polsec.pyrky.activity.booking.BookingsActivity;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.adapter.DrawerItemCustomAdapter;
import com.polsec.pyrky.fragment.HomeFragment;
import com.polsec.pyrky.fragment.ProfileFragment;
import com.polsec.pyrky.pojo.DataModel;
import com.polsec.pyrky.preferences.PreferencesHelper;
import com.polsec.pyrky.utils.CircleTransformation;
import com.polsec.pyrky.utils.Utils;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForgotpasswordActivity extends AppCompatActivity {
    ImageView backBtnclose;
    RelativeLayout cancelbtnrel;
    EditText mEmail;
    TextView resetBtn,resetbutton1;
    RelativeLayout LinLay;
    TextView Txt_error;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mEmail =(EditText)findViewById(R.id.email_edit);
        cancelbtnrel=(RelativeLayout)findViewById(R.id.btncancel);
        resetBtn=(TextView)findViewById(R.id.sinin_edt);
        Txt_error = (TextView)findViewById(R.id.tx_error);
        LinLay=(RelativeLayout)findViewById(R.id.const_lay);
        backBtnclose=(ImageView)findViewById(R.id.close_img);
//
        Txt_error.setVisibility(View.GONE);
        mEmail.addTextChangedListener(mTextWatcher);
        mEmail.setInputType(mEmail.getInputType()
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | EditorInfo.TYPE_TEXT_VARIATION_FILTER);

        LinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ForgotpasswordActivity.this);
            }
        });

        cancelbtnrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotpasswordActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
                finish();
//                onBackPressed();


            }
        });

//        backBtnclose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ForgotpasswordActivity.this, SignInActivity.class);
//
//                startActivity(intent);
//                finish();
////           onBackPressed();
//
//
//            }
//        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ForgotpasswordActivity.this);
                String emailAddress = mEmail.getText().toString();
//                if (validateForm()) {
                    if (emailAddress.isEmpty() || !emailAddress.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                        Txt_error.setVisibility(View.VISIBLE);
//                    showerror("invalid email address");
//                        showalert();
//                        Toast.makeText(ForgotpasswordActivity.this, "inavalid email", Toast.LENGTH_SHORT).show();

                    } else {
                        Txt_error.setVisibility(View.GONE);
                        showProgressDialog();
                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Popup();

//                                            Toast.makeText(getContext(), "Reset Successsfully", Toast.LENGTH_SHORT).show();
//                                            Log.d(TAG, "Email sent.");
                                        } else {
//                                        showerror("Reset password failed.");
                                            Toast.makeText(ForgotpasswordActivity.this, "Reset password failed.", Toast.LENGTH_SHORT).show();
                                            hideProgressDialog();

                                        }
                                    }
                                });
//                    }
                }
            }

        });

    }

    private void showalert() {
    }

    public void showProgressDialog() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }
    private void Popup() {


        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_lay, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        Button ok = (Button)deleteDialogView.findViewById(R.id.ok_button);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotpasswordActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
                hideProgressDialog();
                alertDialog1.dismiss();
            }
        });


        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog1.show();
//        alertDialog1.getWindow().setLayout((int) Utils.convertDpToPixel(228,getActivity()),(int)Utils.convertDpToPixel(220,getActivity()));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
//        lp.height=200dp;
//        lp.width=228;
        lp.gravity = Gravity.CENTER;
//        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog1.getWindow().setAttributes(lp);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            valid = true;
        }else{
            if (TextUtils.isEmpty(email)){
                Toast.makeText(this, "Enter e-mail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                Toast.makeText(this, "Enter valid e-mail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        return valid;
    }
        private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Txt_error.setVisibility(View.GONE);
            // check Fields For Empty Values

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ForgotpasswordActivity.this,SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_righ);
        finish();
    }

    //    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItem(position);
//        }
//
//    }
//
//    private void selectItem(int position) {
//
//        Fragment fragment = null;
//
//        switch (position) {
//            case 0:
//                loadFragment(new HomeFragment());
//                toolbarText.setText("Home");
//                break;
//            case 1:
//                Intent intent=new Intent(HomeActivity.this, BookingsActivity.class);
//                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                startActivity(intent);
//                break;
//            case 2:
//                loadFragment(new ProfileFragment());
//                toolbarText.setText("Profile");
//                break;
//
//            case 3:
//                Intent intent1 = new Intent(getApplicationContext(), SignInActivity.class);
//                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent1.putExtra("EXIT", true);
////            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
//                startActivity(intent1);
//                PreferencesHelper.signOut(HomeActivity.this);
//                mAuth.signOut();
//                HomeActivity.this.finish();
//                break;
//
//            default:
//                break;
//        }
//
////        loadFragment(new HomeFragment());
////        toolbarText.setText("Home");
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
//
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(mNavigationDrawerItemTitles[position]);
//            mDrawerLayout.closeDrawer(mDrawerList);
//
//        } else {
//            Log.e("MainActivity", "Error in creating fragment");
//        }
//    }


//    mTitle = mDrawerTitle = getTitle();
//    mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
//    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//    mDrawerList = (ListView) findViewById(R.id.lst_menu_items);


//    setupToolbar();
//
//    DataModel[] drawerItem = new DataModel[4];
//
//    drawerItem[0] = new DataModel(R.drawable.ic_tab_home, "Home");
//    drawerItem[1] = new DataModel(R.drawable.ic_bookings_menu, "Bookings");
//    drawerItem[2] = new DataModel(R.drawable.ic_tab_user, "Profile");
//    drawerItem[3] = new DataModel(R.drawable.ic_logout, "Logout");
//    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//    getSupportActionBar().setHomeButtonEnabled(true);
//
//    DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(HomeActivity.this,R.layout.list_view_item_row, drawerItem);
//        mDrawerList.setAdapter(adapter);
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//    setupDrawerToggle();
//    NavigationView navigationView = findViewById(R.id.nav_view);
//    //        navigationView.setNavigationItemSelectedListener(this);
//    TextView txtProfileName = (TextView)findViewById(R.id.user_name);
//    CircleImageView profileImage = (CircleImageView)findViewById(R.id.user_image);
//    String profileImageUrl = PreferencesHelper.getPreference(HomeActivity.this,PreferencesHelper.PREFERENCE_PROFILE_PIC);
//        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.user_profile_avatar_size);
//        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
//        Picasso.with(HomeActivity.this)
//                .load(profileImageUrl)
//                .resize(avatarSize, avatarSize)
//                .centerCrop()
//                .transform(new CircleTransformation())
//                .into(profileImage);
//    }
//        txtProfileName.setText(UsrName);

}

