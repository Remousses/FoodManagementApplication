package com.example.foodmanagementapplication;

import com.example.foodmanagementapplication.beans.Ingredient;
import com.example.foodmanagementapplication.crud.ParentCRUD;
import com.example.foodmanagementapplication.crud.FreezerCRUD;
import com.example.foodmanagementapplication.crud.FridgeCRUD;
import com.example.foodmanagementapplication.utils.DateUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmanagementapplication.utils.ErrorUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private EditText speechInput;
    private ImageView tapToSpeech;
    private Button validate;
    private TextView message;
    private RecyclerView ingredientsView;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private IngredientRecyclerViewAdapter ingredientAdapter;
    private static FridgeCRUD fridge;
    private static FreezerCRUD freezer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.speechInput = findViewById(R.id.speechInput);
        this.tapToSpeech = findViewById(R.id.tapToSpeech);
        this.tapToSpeech.setImageResource(R.drawable.microphone_off);
        this.message = findViewById(R.id.message);

        this.initIngredientsView();
        this.saveIngredient();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECORD_AUDIO }, RecordAudioRequestCode);
            }
        }

        this.initSpeechRecognizer();
    }

    private void initIngredientsView() {
        this.fridge = new FridgeCRUD(getApplicationContext());
        this.freezer = new FreezerCRUD(getApplicationContext());
        this.ingredientAdapter = new IngredientRecyclerViewAdapter(this);
        this.ingredientAdapter.setAllIngredients(this.fridge.read());
        this.ingredientsView = findViewById(R.id.ingredientsView);
        this.ingredientsView.setLayoutManager(new LinearLayoutManager(this));
        this.ingredientsView.setAdapter(this.ingredientAdapter);
    }

    private void saveIngredient() {
        this.validate = findViewById(R.id.validate);
        this.validate.setOnClickListener(v -> {
            final String[] data = speechInput.getText().toString().split(",");
            if(data.length % 2 != 0) {
                this.message.setText("Veuillez à respecter la syntaxe (aliment, date)");
                return;
            }
            try {
                final List<Ingredient> ingredientList = new ArrayList<>();
                for (int i = 0; i < data.length; i += 2) {
                    final LocalDate expirationDate = DateUtil.chooseAppropriatedDate(data[i + 1]);
                    ingredientList.add(new Ingredient(data[i], expirationDate));
                }
                this.ingredientAdapter.addItems(ingredientList);
                final ParentCRUD crud = this.getCrud();
                crud.setIngredientsList(this.ingredientAdapter.getAllIngredients());
                crud.save();

                this.message.setText("Ajout effectué");
            } catch (DateTimeParseException e) {
                this.message.setText(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                this.message.setText("Erreur au moment de la sauvegarde");
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSpeechRecognizer() {
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        System.out.println("isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        this.recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault().toString());
        this.recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        this.recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override public void onReadyForSpeech(Bundle bundle) {
                message.setText("");
                speechInput.setText("");
                speechInput.setHint("Listening...");
            }
            @Override
            public void onBeginningOfSpeech() { }
            @Override public void onRmsChanged(float v) { }
            @Override public void onBufferReceived(byte[] bytes) { }
            @Override
            public void onEndOfSpeech() { }
            @Override
            public void onError(int error) {
                message.setText("FAILED : " + ErrorUtil.getErrorText(error));
            }
            @Override
            public void onResults(Bundle bundle) {
                tapToSpeech.setImageResource(R.drawable.microphone_off);
                final List<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                speechInput.setText(data.get(0));
            }
            @Override public void onPartialResults(Bundle bundle) { }
            @Override public void onEvent(int i, Bundle bundle) { }
        });

        this.tapToSpeech.setOnTouchListener((View view, MotionEvent motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    tapToSpeech.setImageResource(R.drawable.microphone_off);
                    speechRecognizer.startListening(recognizerIntent);
                }
                return false;
        });
    }

    public ParentCRUD getCrud() {
        if(this.fridge.isActive()) {
            return this.fridge;
        }
        if(this.freezer.isActive()) {
            return this.freezer;
        }
        return null;
    }

    public void setMessage(final String sentence) {
        this.message.setText(sentence);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_fridge:
                if (!this.fridge.isActive()) {
                    this.ingredientAdapter.setAllIngredients(this.fridge.read());
                    this.freezer.setActive(false);
                }
                break;
            case R.id.navigation_freezer:
                if (!this.freezer.isActive()) {
                    this.ingredientAdapter.setAllIngredients(this.freezer.read());
                    this.fridge.setActive(false);
                }
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { }

    @Override
    public void onStop() {
        super.onStop();
        this.speechRecognizer.destroy();
        System.out.println("destroy");
    }
}