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
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends DialogFragment {

    public static final String TAG = "EditItem";

    private String mItemName;
    private int mQuantity;
    private EditItemDialogListener mEditItemDialogListener;

    public EditItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 The item name.
     * @param param2 The quantity.
     * @return A new instance of fragment EditItemFragment.
     */
    public static EditItemFragment newInstance(String param1, int param2) {
        EditItemFragment fragment = new EditItemFragment();
        fragment.setItemName(param1);
        fragment.setQuantity(param2);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.edit_item);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add, (ViewGroup) getView(), false);
        alertDialogBuilder.setView(viewInflated);

        AutoCompleteTextView autoTextView = viewInflated.findViewById(R.id.new_item);
        autoTextView.setText(mItemName);
        autoTextView.setEnabled(false);
        autoTextView.setAlpha(0.5f);

        final NumberPicker numberPickerView = viewInflated.findViewById(R.id.quantity);
        numberPickerView.setMinValue(1);
        numberPickerView.setMaxValue(10);
        numberPickerView.setValue(mQuantity > 0 ? mQuantity : 1);
        numberPickerView.setWrapSelectorWheel(false);

        alertDialogBuilder.setPositiveButton(R.string.save,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // On success
                mEditItemDialogListener.onItemEdit(mItemName, numberPickerView.getValue());
            }
        });
        alertDialogBuilder.setNeutralButton(R.string.delete_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mEditItemDialogListener.onItemDelete(mItemName);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
        mItemName = itemName;
    }

    private void setQuantity(int quantity) {
        mQuantity = quantity;
    }
}
