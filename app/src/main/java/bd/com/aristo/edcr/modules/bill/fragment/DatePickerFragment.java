package bd.com.aristo.edcr.modules.bill.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import java.util.Calendar;

import javax.inject.Inject;

import bd.com.aristo.edcr.App;
import bd.com.aristo.edcr.R;
import bd.com.aristo.edcr.models.db.DateModel;
import bd.com.aristo.edcr.networking.APIServices;
import bd.com.aristo.edcr.utils.DateTimeUtils;
import bd.com.aristo.edcr.utils.LoadingDialog;
import bd.com.aristo.edcr.utils.ToastUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by monir.sobuj on 5/28/2018.
 */

public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;
    public DateDialogListener dateDialogListener;
    @Inject
    APIServices apiServices;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    Context context;

    public DateDialogListener getDateDialogListener() {
        return dateDialogListener;
    }

    public void setDateDialogListener(DateDialogListener dateDialogListener) {
        this.dateDialogListener = dateDialogListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public interface DateDialogListener {
        void setDate(DateModel date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        App.getComponent().inject(this);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(context)
                .inflate(R.layout.fragment_dialog_date_picker, null);
        datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        final AlertDialog builder = new AlertDialog.Builder(context)
                .setView(v)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .create();


        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button buttonPositive = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int year = datePicker.getYear();
                        int mon = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, mon, day);
                        DateModel dateModel = new DateModel(cal.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), 0, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                        getEligiblePolicy(dateModel);
                    }
                });

                Button buttonNegative = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        return builder;
    }


    public void getEligiblePolicy(DateModel date1) {
        String operationDate = DateTimeUtils.getMonthNumber(date1.getDay()) + "-" + DateTimeUtils.getMonthNumber(date1.getMonth()) + "-" + date1.getYear();
        final LoadingDialog loadingDialog = LoadingDialog.newInstance(context, "Please wait...");
        loadingDialog.show();
        compositeDisposable.add(apiServices.isEligible("BILL", operationDate)  //UserID=A00062&Designation=TM&FromDate=01-09-2020&ToDate=10-09-2020
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean s) {
                        if (s) {
                            dateDialogListener.setDate(date1);
                            dismiss();
                        } else {
                            ToastUtils.shortToast("Please Select Correct Date!!");
                        }
                    }

                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                        ToastUtils.longToast("Try again!!");
                    }
                }));
    }


}
