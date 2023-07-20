package bd.com.aristo.edcr.modules.reports.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.Collections;
import java.util.List;

import bd.com.aristo.edcr.R;


/**
 * Created by monir.sobuj on 06/11/17.
 */

public class IDOTExecution implements IItem<IDOTExecution, IDOTExecution.ViewHolder> {

    @SerializedName("DoctorID")
    public String doctorID;
    @SerializedName("ed01")
    public String e1;
    @SerializedName("ed02")
    public String e2;
    @SerializedName("ed03")
    public String e3;
    @SerializedName("ed04")
    public String e4;
    @SerializedName("ed05")
    public String e5;
    @SerializedName("ed06")
    public String e6;
    @SerializedName("ed07")
    public String e7;
    @SerializedName("ed08")
    public String e8;
    @SerializedName("ed09")
    public String e9;
    @SerializedName("ed10")
    public String e10;
    @SerializedName("ed11")
    public String e11;
    @SerializedName("ed12")
    public String e12;
    @SerializedName("ed13")
    public String e13;
    @SerializedName("ed14")
    public String e14;
    @SerializedName("ed15")
    public String e15;
    @SerializedName("ed16")
    public String e16;
    @SerializedName("ed17")
    public String e17;
    @SerializedName("ed18")
    public String e18;
    @SerializedName("ed19")
    public String e19;
    @SerializedName("ed20")
    public String e20;
    @SerializedName("ed21")
    public String e21;
    @SerializedName("ed22")
    public String e22;
    @SerializedName("ed23")
    public String e23;
    @SerializedName("ed24")
    public String e24;
    @SerializedName("ed25")
    public String e25;
    @SerializedName("ed26")
    public String e26;
    @SerializedName("ed27")
    public String e27;
    @SerializedName("ed28")
    public String e28;
    @SerializedName("ed29")
    public String e29;
    @SerializedName("ed30")
    public String e30;
    @SerializedName("ed31")
    public String e31;

    @SerializedName("md01")
    public String m1;
    @SerializedName("md02")
    public String m2;
    @SerializedName("md03")
    public String m3;
    @SerializedName("md04")
    public String m4;
    @SerializedName("md05")
    public String m5;
    @SerializedName("md06")
    public String m6;
    @SerializedName("md07")
    public String m7;
    @SerializedName("md08")
    public String m8;
    @SerializedName("md09")
    public String m9;
    @SerializedName("md10")
    public String m10;
    @SerializedName("md11")
    public String m11;
    @SerializedName("md12")
    public String m12;
    @SerializedName("md13")
    public String m13;
    @SerializedName("md14")
    public String m14;
    @SerializedName("md15")
    public String m15;
    @SerializedName("md16")
    public String m16;
    @SerializedName("md17")
    public String m17;
    @SerializedName("md18")
    public String m18;
    @SerializedName("md19")
    public String m19;
    @SerializedName("md20")
    public String m20;
    @SerializedName("md21")
    public String m21;
    @SerializedName("md22")
    public String m22;
    @SerializedName("md23")
    public String m23;
    @SerializedName("md24")
    public String m24;
    @SerializedName("md25")
    public String m25;
    @SerializedName("md26")
    public String m26;
    @SerializedName("md27")
    public String m27;
    @SerializedName("md28")
    public String m28;
    @SerializedName("md29")
    public String m29;
    @SerializedName("md30")
    public String m30;
    @SerializedName("md31")
    public String m31;

    public int newCount;
    public int dotCount;
    public int executionCount;
    public int absentCount;
    public boolean isNewDoctor;
    @SerializedName("DoctorName")
    public String doctorName;

    public int serialNo;

    List<IDotModel> iDotModelList;

    long identifier;

    public List<IDotModel> getiDotModelList() {
        return iDotModelList;
    }

    public void setiDotModelList(List<IDotModel> iDotModelList) {
        this.iDotModelList = iDotModelList;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getE1() {
        return e1;
    }

    public void setE1(String e1) {
        this.e1 = e1;
    }

    public String getE2() {
        return e2;
    }

    public void setE2(String e2) {
        this.e2 = e2;
    }

    public String getE3() {
        return e3;
    }

    public void setE3(String e3) {
        this.e3 = e3;
    }

    public String getE4() {
        return e4;
    }

    public void setE4(String e4) {
        this.e4 = e4;
    }

    public String getE5() {
        return e5;
    }

    public void setE5(String e5) {
        this.e5 = e5;
    }

    public String getE6() {
        return e6;
    }

    public void setE6(String e6) {
        this.e6 = e6;
    }

    public String getE7() {
        return e7;
    }

    public void setE7(String e7) {
        this.e7 = e7;
    }

    public String getE8() {
        return e8;
    }

    public void setE8(String e8) {
        this.e8 = e8;
    }

    public String getE9() {
        return e9;
    }

    public void setE9(String e9) {
        this.e9 = e9;
    }

    public String getE10() {
        return e10;
    }

    public void setE10(String e10) {
        this.e10 = e10;
    }

    public String getE11() {
        return e11;
    }

    public void setE11(String e11) {
        this.e11 = e11;
    }

    public String getE12() {
        return e12;
    }

    public void setE12(String e12) {
        this.e12 = e12;
    }

    public String getE13() {
        return e13;
    }

    public void setE13(String e13) {
        this.e13 = e13;
    }

    public String getE14() {
        return e14;
    }

    public void setE14(String e14) {
        this.e14 = e14;
    }

    public String getE15() {
        return e15;
    }

    public void setE15(String e15) {
        this.e15 = e15;
    }

    public String getE16() {
        return e16;
    }

    public void setE16(String e16) {
        this.e16 = e16;
    }

    public String getE17() {
        return e17;
    }

    public void setE17(String e17) {
        this.e17 = e17;
    }

    public String getE18() {
        return e18;
    }

    public void setE18(String e18) {
        this.e18 = e18;
    }

    public String getE19() {
        return e19;
    }

    public void setE19(String e19) {
        this.e19 = e19;
    }

    public String getE20() {
        return e20;
    }

    public void setE20(String e20) {
        this.e20 = e20;
    }

    public String getE21() {
        return e21;
    }

    public void setE21(String e21) {
        this.e21 = e21;
    }

    public String getE22() {
        return e22;
    }

    public void setE22(String e22) {
        this.e22 = e22;
    }

    public String getE23() {
        return e23;
    }

    public void setE23(String e23) {
        this.e23 = e23;
    }

    public String getE24() {
        return e24;
    }

    public void setE24(String e24) {
        this.e24 = e24;
    }

    public String getE25() {
        return e25;
    }

    public void setE25(String e25) {
        this.e25 = e25;
    }

    public String getE26() {
        return e26;
    }

    public void setE26(String e26) {
        this.e26 = e26;
    }

    public String getE27() {
        return e27;
    }

    public void setE27(String e27) {
        this.e27 = e27;
    }

    public String getE28() {
        return e28;
    }

    public void setE28(String e28) {
        this.e28 = e28;
    }

    public String getE29() {
        return e29;
    }

    public void setE29(String e29) {
        this.e29 = e29;
    }

    public String getE30() {
        return e30;
    }

    public void setE30(String e30) {
        this.e30 = e30;
    }

    public String getE31() {
        return e31;
    }

    public void setE31(String e31) {
        this.e31 = e31;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public String getM2() {
        return m2;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }

    public String getM3() {
        return m3;
    }

    public void setM3(String m3) {
        this.m3 = m3;
    }

    public String getM4() {
        return m4;
    }

    public void setM4(String m4) {
        this.m4 = m4;
    }

    public String getM5() {
        return m5;
    }

    public void setM5(String m5) {
        this.m5 = m5;
    }

    public String getM6() {
        return m6;
    }

    public void setM6(String m6) {
        this.m6 = m6;
    }

    public String getM7() {
        return m7;
    }

    public void setM7(String m7) {
        this.m7 = m7;
    }

    public String getM8() {
        return m8;
    }

    public void setM8(String m8) {
        this.m8 = m8;
    }

    public String getM9() {
        return m9;
    }

    public void setM9(String m9) {
        this.m9 = m9;
    }

    public String getM10() {
        return m10;
    }

    public void setM10(String m10) {
        this.m10 = m10;
    }

    public String getM11() {
        return m11;
    }

    public void setM11(String m11) {
        this.m11 = m11;
    }

    public String getM12() {
        return m12;
    }

    public void setM12(String m12) {
        this.m12 = m12;
    }

    public String getM13() {
        return m13;
    }

    public void setM13(String m13) {
        this.m13 = m13;
    }

    public String getM14() {
        return m14;
    }

    public void setM14(String m14) {
        this.m14 = m14;
    }

    public String getM15() {
        return m15;
    }

    public void setM15(String m15) {
        this.m15 = m15;
    }

    public String getM16() {
        return m16;
    }

    public void setM16(String m16) {
        this.m16 = m16;
    }

    public String getM17() {
        return m17;
    }

    public void setM17(String m17) {
        this.m17 = m17;
    }

    public String getM18() {
        return m18;
    }

    public void setM18(String m18) {
        this.m18 = m18;
    }

    public String getM19() {
        return m19;
    }

    public void setM19(String m19) {
        this.m19 = m19;
    }

    public String getM20() {
        return m20;
    }

    public void setM20(String m20) {
        this.m20 = m20;
    }

    public String getM21() {
        return m21;
    }

    public void setM21(String m21) {
        this.m21 = m21;
    }

    public String getM22() {
        return m22;
    }

    public void setM22(String m22) {
        this.m22 = m22;
    }

    public String getM23() {
        return m23;
    }

    public void setM23(String m23) {
        this.m23 = m23;
    }

    public String getM24() {
        return m24;
    }

    public void setM24(String m24) {
        this.m24 = m24;
    }

    public String getM25() {
        return m25;
    }

    public void setM25(String m25) {
        this.m25 = m25;
    }

    public String getM26() {
        return m26;
    }

    public void setM26(String m26) {
        this.m26 = m26;
    }

    public String getM27() {
        return m27;
    }

    public void setM27(String m27) {
        this.m27 = m27;
    }

    public String getM28() {
        return m28;
    }

    public void setM28(String m28) {
        this.m28 = m28;
    }

    public String getM29() {
        return m29;
    }

    public void setM29(String m29) {
        this.m29 = m29;
    }

    public String getM30() {
        return m30;
    }

    public void setM30(String m30) {
        this.m30 = m30;
    }

    public String getM31() {
        return m31;
    }

    public void setM31(String m31) {
        this.m31 = m31;
    }

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public int getDotCount() {
        return dotCount;
    }

    public void setDotCount(int dotCount) {
        this.dotCount = dotCount;
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(int executionCount) {
        this.executionCount = executionCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public boolean isNewDoctor() {
        return isNewDoctor;
    }

    public void setNewDoctor(boolean newDoctor) {
        isNewDoctor = newDoctor;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    //variables needed for adapter
    protected boolean isSelected = false; // defines if the item is selected

    protected Object tag;// defines if this item is isSelectable

    protected boolean isSelectable = true;

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IDOTExecution withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public IDOTExecution withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IDOTExecution withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IDOTExecution withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.recyclerDVR;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_doctor_wise_dot;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }


    private ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        Context ctx = holder.itemView.getContext();
        String doc = "";
        if(isNewDoctor){
            doc = serialNo+". "+doctorID+" [New]";
        } else {
            doc = serialNo+". "+doctorName+" ["+doctorID+"]";
        }
        String exe = "DCR: "+ executionCount;
        String absent = "Absent: "+ absentCount;
        String newD = "New DCR: "+ newCount;
        String dot = "DOT: "+ dotCount;
        holder.txtExe.setText(exe);
        holder.txtAbsent.setText(absent);
        holder.txtNew.setText(newD);
        holder.txtDOT.setText(dot);
        holder.txtNameID.setText(doc);

        if(iDotModelList != null) {
            final FastItemAdapter<IDotModel> fastAdapter = new FastItemAdapter<>();
            fastAdapter.add(iDotModelList);
            fastAdapter.setHasStableIds(false);
            holder.rvDOTStatus.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
            holder.rvDOTStatus.setAdapter(fastAdapter);
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtExe.setText(null);
        holder.txtAbsent.setText(null);
        holder.txtNew.setText(null);
        holder.txtDOT.setText(null);
        holder.txtNameID.setText(null);
    }


    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public IDOTExecution withIdentifier(long identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public long getIdentifier() {
        return identifier;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtExe, txtAbsent, txtNew, txtDOT, txtNameID;
        RecyclerView rvDOTStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            txtNameID = itemView.findViewById(R.id.txtNameID);
            txtDOT = itemView.findViewById(R.id.txtDOT);
            txtNew = itemView.findViewById(R.id.txtNew);
            txtAbsent = itemView.findViewById(R.id.txtAbsent);
            txtExe = itemView.findViewById(R.id.txtExe);
            rvDOTStatus = itemView.findViewById(R.id.rvDOTStatus);
        }
    }
}