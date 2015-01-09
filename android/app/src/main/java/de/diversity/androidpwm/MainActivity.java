package de.diversity.androidpwm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import de.diversity.androidpwm.tools.SHA;


public class MainActivity extends Activity {

    private static final int FILE_CHOOSER = 11;
    private String fileSelected, pass = "";
    private CheckBox chkBoxFilePath, chkBoxPasswortFile;
    private File optionF;
    private EditText passText;

    public enum lMode{
        LOAD, FILE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpen(lMode.FILE);
            }
        });

        Button load = (Button) findViewById(R.id.btnLoad);
        load.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                loadFile();
            }
        });

        passText = (EditText) findViewById(R.id.editText);

        //filecheck
        chkBoxFilePath = (CheckBox) findViewById(R.id.checkBox);
        chkBoxPasswortFile = (CheckBox) findViewById(R.id.checkBox2);

        File optionD = getDir("opt",0);
        optionF = new File(optionD+File.separator+"opt.ini");
        if(optionF.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(optionF));
                String line = br.readLine();
                while(line != null){
                    if(line.contains("O")){
                        String f[] = line.split("\\|{2}");
                        btn.setText(f[1]);
                        fileSelected = String.valueOf(btn.getText());
                        chkBoxFilePath.setChecked(true);
                    }else if(line.contains("P")){
                        passText.setText(line.split("\\|{2}")[1]);
                        chkBoxPasswortFile.setChecked(true);
                        pass = String.valueOf(passText.getText());
                    }
                    line = br.readLine();
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFile() {
        try {
            File file = new File(fileSelected);
            if (file != null && file.exists()) {
                    BufferedReader fr = new BufferedReader(new FileReader(
                            file));
                    if (fr.readLine().equalsIgnoreCase("diversITy.PWM")) {
                        String pa = fr.readLine();
                        String sss = (String) SHA.getHash(passText.getText().toString(), "Sha-512", SHA.TypeToGiveBack.HEXSTRING);
                        sss = (String) SHA.getHash(sss, "Sha-512",
                                SHA.TypeToGiveBack.HEXSTRING);
                        if (new String(Base64.decode(pa, Base64.URL_SAFE))
                                .equals(sss)) {
                            pass = passText.getText().toString();
                            doOpen(lMode.LOAD);
                        } else {
                            messageBox("Fehler", "Falsches Passwort");
                        }
                    } else {
                        messageBox("Fehler", "keine diversityPWM Datei");
                    }
            } else {
                messageBox("Fehler", "Datei nicht vorhanden");
            }
        }catch(Exception ex){
            messageBox("Fehler","Fehler in loadFile");
        }
    }

    private void doOpen(lMode mode) {
        if(mode == lMode.LOAD){
            //Toast.makeText(this, fileSelected, Toast.LENGTH_LONG).show();
            File optionD = getDir("opt",0);
            optionF = new File(optionD+File.separator+"opt.ini");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(optionF));
                if(chkBoxFilePath.isChecked()){
                    bw.write("O||"+fileSelected);
                    bw.write(System.getProperty("line.separator"));
                }
                if(chkBoxPasswortFile.isChecked()){
                    bw.write("P||"+pass);
                    bw.write(System.getProperty("line.separator"));
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, LoadingSreenActivity.class);
            ArrayList<String> extensions = new ArrayList<String>();
       extensions.add(fileSelected);
        extensions.add(pass);
        intent.putStringArrayListExtra("arguments", extensions);
            startActivity(intent);
        }else if(mode == lMode.FILE) {
            Intent intent = new Intent(this, FileChooser.class);
//        ArrayList<String> extensions = new ArrayList<String>();
//        extensions.add(".pdf");
//        extensions.add(".xls");
//        extensions.add(".xlsx");
//        intent.putStringArrayListExtra("filterFileExtension", extensions);
            startActivityForResult(intent, FILE_CHOOSER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
            fileSelected = data.getStringExtra("fileSelected");
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
            ((Button) findViewById(R.id.btn)).setText(fileSelected);
        }

    }

    //*********************************************************
//generic dialog, takes in the method name and error message
//*********************************************************
    private void messageBox(String method, String message)
    {
        Log.d("EXCEPTION: " + method, message);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(this);
        messageBox.setTitle(method);
        messageBox.setMessage(message);
        messageBox.setCancelable(false);
        messageBox.setNeutralButton("OK", null);
        messageBox.show();
    }
}
