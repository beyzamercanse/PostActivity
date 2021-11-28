package com.example.post;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.post.databinding.ActivityMainBinding;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private Uri imageUri;


//    public static Bitmap bitmap;
//    Uri selectedImage = data.getData();
//    this.imageUri = selectedImage;
//    Bitmap bitmap = Utils.loadBitmap(this, selectedImage);
//    binding.image.setImageBitmap(bitmap);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSelectImage.setOnClickListener(v -> getPhoto());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        binding.buttonSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            requestPermissionLauncher.launch(String.valueOf(intent));
        });
        binding.buttonSave.setOnClickListener(v -> startPostActivity());
    }

    private void startPostActivity() {
        Intent intent = new Intent(this, PostActivity.class);
        String name = binding.editText1.getText().toString();
        String contents = binding.editText2.getText().toString();
        
        intent.putExtra("EXTRA_VALUE", name);
        intent.putExtra("EXTRA_CONTENT", contents);
        intent.putExtra("EXTRA_TIME", System.currentTimeMillis());
        intent.putExtra("EXTRA_IMAGE", imageUri);
        startActivity(intent);
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "권한이 설정되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "권한이 설전되지 않았습니다, 권한이 없으므로 앱을 종료합니다! ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

    private void  getPhoto()  {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        resultLauncher.launch(intent);
    }

    ActivityResultLauncher <Intent> resultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                Log.i("TEST", "data: " + data);

                if (result.getResultCode() == RESULT_OK  && null != data) {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = loadBitmap(selectedImage);
                    binding.picture.setImageBitmap(bitmap);
                    bitmap = bitmap;
                }
            });

    private Bitmap loadBitmap(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return BitmapFactory.decodeFile(picturePath);
    }


}