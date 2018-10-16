package com.logicalwings.btapp.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicalwings.btapp.R;
import com.logicalwings.btapp.activities.ProfileImageActivity;
import com.logicalwings.btapp.utils.AppConstants;
import com.logicalwings.btapp.utils.AppUtils;
import com.logicalwings.btapp.utils.LibFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private ImageView toolSearch, toolCart, toolSave;
    private LinearLayout linearChangePass;
    private FloatingActionButton fabProfileImage;
    private TextView textToolbar;
    private AlertDialog.Builder builder;
    public static final int CHOOSE_IMAGE_CAMERA = 1;
    public static final int CHOOSE_IMAGE_GALLERY = 2;
    private Bitmap bitmap = null;
    private CircleImageView circleProfileImage;
    private EditText editName, editEmailorMobile;
    protected LibFile libFile;
    private String sapCustomerName, email, mobile;
    private FrameLayout frameLayout;
    private File mTempCameraPhotoFile;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        toolCart = getActivity().findViewById(R.id.toolbar_cart);
        toolSave = getActivity().findViewById(R.id.toolbar_save);
        toolSearch = getActivity().findViewById(R.id.toolbar_search);
        frameLayout = frameLayout = getActivity().findViewById(R.id.frame_layout_cart);
        linearChangePass = view.findViewById(R.id.linear_change_pass);
        textToolbar = (TextView) getActivity().findViewById(R.id.text_toolbar);
        fabProfileImage = view.findViewById(R.id.fab_profile_image);
        circleProfileImage = view.findViewById(R.id.circle_profile_image);
        editName = view.findViewById(R.id.edit_name);
        editEmailorMobile = view.findViewById(R.id.edit_email_mobile);
        textToolbar.setText("My Profile");

        loadState();
        initData();

        linearChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frame_layout, new ChangePasswordFragment()).commit();
            }
        });

        fabProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        circleProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileImageActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private void initData() {
        libFile = LibFile.getInstance(getContext());
        sapCustomerName = LibFile.getInstance(getActivity()).getString(AppConstants.SAP_CUSTOMER_NAME);
        email = LibFile.getInstance(getActivity()).getString(AppConstants.EMAIl);
        mobile = LibFile.getInstance(getActivity()).getString(AppConstants.MOBILE_NO);

        if (sapCustomerName != null) {
            editName.setText(sapCustomerName);
        }
        if (email != null) {
            editEmailorMobile.setText(email);
        } else {
            editEmailorMobile.setText(mobile);
        }
        AppUtils.hasInternet(getActivity());
    }

    private void loadState() {
        toolSearch.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        toolSave.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Camera", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Profile Image!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Camera")) {
                    checkPermissionCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    checkReadPermission();
                    fromGallery();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }

    /* for marshmallow permission*/
    private void checkPermissionCamera() {
        int hasStoragePermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasStoragePermission = Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.CAMERA);
        }
        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CHOOSE_IMAGE_CAMERA);
            }
        }
        takeFromCamera();
    }

    private void takeFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {

                try {
                    mTempCameraPhotoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", mTempCameraPhotoFile.getAbsolutePath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTempCameraPhotoFile));
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(takePictureIntent, CHOOSE_IMAGE_CAMERA);
            }
        }
    }

    private void checkReadPermission() {
        int hasStoragePermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasStoragePermission = Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_IMAGE_GALLERY);
            }
            return;
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void fromGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(photoPickerIntent, CHOOSE_IMAGE_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CHOOSE_IMAGE_CAMERA) {
            try {
                String filePath = mTempCameraPhotoFile.getPath();
                if (filePath != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(filePath), null, options);
                    circleProfileImage.setImageBitmap(bitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CHOOSE_IMAGE_GALLERY && data != null) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    if (selectedImage != null) {
                        cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    }
                }
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                }
                String picturePath = null;
                if (cursor != null) {
                    picturePath = cursor.getString(columnIndex);
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (picturePath != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(picturePath), null, options);
                    circleProfileImage.setImageBitmap(bitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
