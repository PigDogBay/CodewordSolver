package com.pigdogbay.codewordsolver.usercontrols;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.pigdogbay.codewordsolver.R;
import com.pigdogbay.codewordsolver.model.MainModel;
import com.pigdogbay.codewordsolver.model.Square;
import com.pigdogbay.codewordsolver.model.SquareSet;

/**
 * Created by Mark on 05/12/2016.
 * Builds a dialog to pick a letter for a square
 */
public class LetterPickerDialog implements View.OnClickListener {

    private int[] buttonIds = {
            R.id.lpa,R.id.lpb,R.id.lpc,R.id.lpd,R.id.lpe,R.id.lpf,R.id.lpg,R.id.lph,R.id.lpi,
            R.id.lpj,R.id.lpk,R.id.lpl,R.id.lpm,R.id.lpn,R.id.lpo,R.id.lpp,R.id.lpq,R.id.lpr,
            R.id.lps,R.id.lpt,R.id.lpu,R.id.lpv,R.id.lpw,R.id.lpx,R.id.lpy,R.id.lpz,R.id.lpNone,
    };
    private SquareView squareView;
    private AlertDialog dialog;

    public void show(Context context, SquareView squareView, DialogInterface.OnDismissListener dismissListener, DialogInterface.OnClickListener clearAllListener){
        this.squareView = squareView;

        Square square = squareView.getSquare();
        View lettersView = View.inflate(context, R.layout.letter_picker,null);
        setUpButtons(lettersView);

        int s = R.style.AppCompatAlertDialogStyle;
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.letter_picker_dialog);
        builder.setTitle("Select Letter for "+square.getNumberString());
        builder.setView(lettersView);
        builder.setNegativeButton("Cancel", null);
        builder.setNeutralButton("CLEAR ALL",clearAllListener);
        builder.setOnDismissListener(dismissListener);
        dialog = builder.create();
        dialog.show();
    }

    private void setUpButtons(View root){
        SquareSet squareSet = MainModel.get().getSquareSet();
        //disable any letter that have already been found
        for (int i=0; i<SquareSet.ALPHABET.length; i++){
            int buttonId = buttonIds[i];
            View button = root.findViewById(buttonId);
            if (squareSet.contains(SquareSet.ALPHABET[i])){
                button.setEnabled(false);
            } else{
                button.setOnClickListener(this);
            }
        }
        //empty letter
        root.findViewById(R.id.lpNone).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.lpNone){
            squareView.getSquare().setLetter("");

        } else {
            String letter = ((Button) view).getText().toString();
            squareView.getSquare().setLetter(letter);
        }
        dialog.dismiss();
        squareView.invalidate();
    }
}
