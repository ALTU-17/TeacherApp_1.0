package in.aceventura.evolvuschool.teacherapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;


import in.aceventura.evolvuschool.teacherapp.R;
import in.aceventura.evolvuschool.teacherapp.pojo.EventImages;

public class MainActivity extends AppCompatActivity {
Button pick;
    public static int REQUEST_CODE_GET_CAMERA_FILE_PATH = 1888;
    public static int REQUEST_CODE_GET_FILE_PATH = 1;

    LinearLayout micici_listView;
    private TextView mResultLabel;
    private ImageView mImageView;
   EventImages eventImages;
   ArrayList<EventImages> meventImagesArrayList;
    String Companyname, Imagedata, ImageName;
    int width, height, btnwidth, btnwidth_fortwo;

    public static String filePath, fileBase64Code, fileName, f64 = "";
    final Random rand = new Random();
    int diceRoll;
    Bitmap bitmap;
    static Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        btnwidth = (width / 3) - 10;
        btnwidth_fortwo = (width / 2) - 10;
       // SetUI();
       micici_listView=findViewById(R.id.micici_listView);
        pick=findViewById(R.id.pick);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        meventImagesArrayList=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent=new Intent(this,CreateTeachernote.class);
        startActivity(intent);
        finish();
        return true;
    }

    private void SetUI() {
        StringTokenizer stvalue;
        ScrollView scrollView1 = new ScrollView(this);
        RelativeLayout.LayoutParams paramscroll = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        scrollView1.setLayoutParams(paramscroll);
        LinearLayout layoutque = new LinearLayout(this);
        layoutque.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutque.setOrientation(LinearLayout.VERTICAL);

        final Button btnUpload = new Button(this);
        RelativeLayout.LayoutParams parambtnUpload = new RelativeLayout.LayoutParams(
                btnwidth / 2, btnwidth / 2);
        parambtnUpload.setMargins(10, 10, 10, 10);
        btnUpload.setGravity(Gravity.LEFT);
        btnUpload.setLayoutParams(parambtnUpload);
        LinearLayout layoutbtnatch = new LinearLayout(this);
        layoutbtnatch.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutbtnatch.setOrientation(LinearLayout.HORIZONTAL);
        layoutbtnatch.setPadding(10, 10, 10, 10);
        layoutbtnatch.addView(btnUpload);

        HorizontalScrollView scrollView = new HorizontalScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        micici_listView = new LinearLayout(this);
        micici_listView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        scrollView.setHorizontalScrollBarEnabled(true);
        scrollView.addView(micici_listView);
        layoutbtnatch.addView(scrollView);
        layoutque.addView(layoutbtnatch);

        scrollView1.addView(layoutque);
        setContentView(scrollView1);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    showCameraChooser();
                } else if (options[item].equals("Choose from Gallery")) {
                    showFileChooser();
                }

                // }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void showFileChooser() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        // intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent,
                REQUEST_CODE_GET_FILE_PATH);
    }
    protected void showCameraChooser() {
        // TODO Auto-generated method stub
        Intent cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,
                REQUEST_CODE_GET_CAMERA_FILE_PATH);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GET_FILE_PATH
                && resultCode == Activity.RESULT_OK) {

            if (data.getData() != null) {
                uri = data.getData();
                //  String  realPath = getRealPathFromURI(data.getData());
                //    Log.d("uri", "onActivityResult: "+realPath);
                InputStream stream;
                try {
                    stream = this
                            .getContentResolver().openInputStream(
                                    data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                    final int maxSize = 500;
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmap.getWidth();
                    int inHeight = bitmap.getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, outWidth,
                            outHeight, true);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    // photo = Bitmap.createScaledBitmap(photo, 300, 300, true);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream1);

                    byte[] byte_arr = stream1.toByteArray();
                    File tmpDir = new File(Environment
                            .getExternalStorageDirectory().getAbsolutePath()
                            + "/DC/");
                    tmpDir.mkdir();

                    fileBase64Code = Base64.encodeToString(byte_arr,
                            Base64.DEFAULT);

                    Log.d("testramanora", "onActivityResult: " + tmpDir.getAbsolutePath());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                diceRoll = rand.nextInt(6) + 1;

                fileName = diceRoll + ".png";

                eventImages = new EventImages();
                eventImages.setImgName(fileName);
                eventImages.setFileBase64Code(fileBase64Code);
                eventImages.setExtension(":image/png;base64");
                meventImagesArrayList.add(eventImages);
                updateList();

            }
        } else if (requestCode == REQUEST_CODE_GET_CAMERA_FILE_PATH
                && resultCode == Activity.RESULT_OK) {

            try {

                diceRoll = rand.nextInt(6) + 1;
                fileName = diceRoll + ".png";
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                final int maxSize = 500;
                int outWidth;
                int outHeight;
                int inWidth = photo.getWidth();
                int inHeight = photo.getHeight();
                if (inWidth > inHeight) {
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }
                photo = Bitmap.createScaledBitmap(photo, outWidth, outHeight,
                        true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byte_arr = stream.toByteArray();
                fileBase64Code = Base64
                        .encodeToString(byte_arr, Base64.DEFAULT);
                Log.d("testramanora", "fileBase64Code" + fileBase64Code);

                eventImages = new EventImages();
                eventImages.setImgName(fileName);
                eventImages.setFileBase64Code(fileBase64Code);
                eventImages.setExtension(":image/png;base64");
                meventImagesArrayList.add(eventImages);
                updateList();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            // }
        } else if (resultCode == RESULT_CANCELED) {
            uri = null;
            Toast.makeText(this, "Cancelled!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateList() {
      micici_listView.removeAllViews();
        if (meventImagesArrayList.size() > 0) {
            Log.d("testramanora", "updateList: eventImagesArrayList.size()  " + meventImagesArrayList.size());

            for (int i = 0; i < meventImagesArrayList.size(); i++) {
                setView(i);
            }
        }
    }
    private void setView(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.custom_horizontal_image,
                null);
        ImageView imageView_uploadedimg = baseView.findViewById(R.id.imageName);

        final ImageView imgcancel = baseView.findViewById(R.id.imgcancel);
       // imgcancel.setVisibility(View.GONE);
        String img_data = meventImagesArrayList.get(i).getFileBase64Code();
        byte[] imageAsBytes = Base64.decode(img_data, Base64.DEFAULT);
        imageView_uploadedimg.setImageBitmap(BitmapFactory.decodeByteArray(
                imageAsBytes, 0, imageAsBytes.length));
        imageView_uploadedimg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgcancel.setVisibility(View.VISIBLE);
                return true;
            }
        });

        imgcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meventImagesArrayList.remove(i);
                updateList();
            }
        });
        micici_listView.addView(baseView);
    }
}
