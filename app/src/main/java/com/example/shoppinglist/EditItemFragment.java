package com.example.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.shoppinglist.model.MarketItems;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private String mParam2; // TODO pass quantity and set it as default value
    private EditItemDialogListener mEditItemDialogListener;

    public EditItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EditItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditItemFragment newInstance(String param1) {
        EditItemFragment fragment = new EditItemFragment();
        fragment.setItemName(param1);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Edit item");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add, (ViewGroup) getView(), false);
        alertDialogBuilder.setView(viewInflated);

        AutoCompleteTextView autoTextView = viewInflated.findViewById(R.id.new_item);
        autoTextView.setText(mParam1);
        autoTextView.setEnabled(false);

        final NumberPicker numberPickerView = viewInflated.findViewById(R.id.quantity);
        numberPickerView.setMinValue(1);
        numberPickerView.setMaxValue(10);
        numberPickerView.setValue(1);
        numberPickerView.setWrapSelectorWheel(false);

        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On success
                mEditItemDialogListener.onItemEdit(mParam1, numberPickerView.getValue());
            }
        });
        alertDialogBuilder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEditItemDialogListener.onItemDelete(mParam1);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    public void setEditItemDialogListener(EditItemDialogListener editItemDialogListener) {
        mEditItemDialogListener = editItemDialogListener;
    }

    public interface EditItemDialogListener {
        void onItemEdit(String itemName, int quantity);
        void onItemDelete(String itemName);
    }

    private void setItemName(String itemName) {
        mParam1 = itemName;
    }
}
